package com.object0r.tools.proxymity;

import com.object0r.tools.proxymity.datatypes.ProxyInfo;
import com.object0r.toortools.Utilities;

import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;

/**
 * The type Proxy checker.
 */
public class ProxyChecker implements Runnable
{
    /**
     * The constant PROXY_STATUS_PENDING.
     */
    public final static String PROXY_STATUS_PENDING = "pending";
    /**
     * The constant PROXY_STATUS_INACTIVE.
     */
    public final static String PROXY_STATUS_INACTIVE = "inactive";
    /**
     * The constant PROXY_STATUS_ACTIVE.
     */
    public final static String PROXY_STATUS_ACTIVE = "active";
    /**
     * The constant PROXY_STATUS_DEAD.
     */
    public static final String PROXY_STATUS_DEAD = "dead";
    /**
     * The My ip.
     */
    static String myIp;
    /**
     * The Proxy info.
     */
    ProxyInfo proxyInfo;
    /**
     * The Db connection.
     */
    Connection dbConnection;

    /**
     * Instantiates a new Proxy checker.
     *
     * @param proxyInfo    the proxy info
     * @param dbConnection the db connection
     */
    ProxyChecker(ProxyInfo proxyInfo, Connection dbConnection)
    {
        this.proxyInfo = proxyInfo;
        this.dbConnection = dbConnection;
    }

    /**
     * Sets my ip.
     */
    public void setMyIp()
    {
        try
        {
            myIp = Utilities.getIp();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.exit(0);
        }
    }

    public void run()
    {
        try
        {
            Thread.sleep(new Random().nextInt(3000));

            Proxy proxy = getProxyFromProxyInfo(proxyInfo);
            String ip = Utilities.getIp(proxy, 3, Proxymity.TIMEOUT_MS/1000, Proxymity.TIMEOUT_MS/1000);

            if (ip.equals(myIp))
            {
                markProxyNoGood(proxyInfo);
                //System.out.println("Proxy Not Anonymous.");
                //Never seen that
            }
            else
            {
                //System.out.println("My Ip/Remote "+myIp+"/"+ip);
                markProxyAsGood(proxyInfo);
                setProxyRemoteIp(proxyInfo, ip);
                try
                {
                    //Try https.
                    try
                    {
                        HttpURLConnection.setFollowRedirects(false);

                        HttpURLConnection con =
                                (HttpURLConnection) new URL("https://"+Proxymity.HTTPS_CHECK_URL.replace("https://","")).openConnection(proxy);

                        con.setConnectTimeout(Proxymity.TIMEOUT_MS);
                        con.setReadTimeout(Proxymity.TIMEOUT_MS);
                        Scanner sc2 = new Scanner(con.getInputStream());
                        StringBuffer sb2 = new StringBuffer();

                        while (sc2.hasNext())
                        {
                            sb2.append(sc2.nextLine()+"\n");
                        }
                        if (sb2.toString().contains(Proxymity.HTTPS_CHECK_STRING)) {
                            markProxyAsHttps(proxyInfo);
                        } else {
                            markProxyAsNotHttps(proxyInfo);
                        }
                        /*if (con.getResponseCode() ==  HttpURLConnection.HTTP_OK)
                        {
                            markProxyAsHttps(proxyInfo);
                        }
                        else
                        {
                            //System.out.println("Code is: "+con.getResponseCode());
                            markProxyAsNotHttps(proxyInfo);
                        }*/

                        con.getInputStream().close();
                    }
                    catch (Exception e)
                    {
                        //System.out.println("Https error: "+e.toString());
                        if (proxyInfo.getType().equals(ProxyInfo.PROXY_TYPES_SOCKS4) || proxyInfo.getType().equals( ProxyInfo.PROXY_TYPES_SOCKS4))
                        {
                            //WE ARE NEVER HERE, RESEARCH WHY/
                            System.out.println("Socks Error HTTPS: "+e.toString());
                        }

                        markProxyAsNotHttps(proxyInfo);
                    }
                }
                catch (Exception e)
                {
                    System.out.println("Failed to verify Security");
                    System.out.println(e);
                }

                //System.out.println("SuMy ip is: "+ip);
            }
        }
        catch (Exception e)
        {
            markProxyNoGood(proxyInfo);
            //System.out.print("e");
            //e.printStackTrace();
        }
    }

    private void markProxyAnonymous(ProxyInfo proxyInfo)
    {
        try
        {
            Statement st = dbConnection.createStatement();
            String id = sanitizeDatabaseInput(proxyInfo.getId());

            st.executeUpdate("UPDATE "+Proxymity.TABLE_NAME+" SET fullanonymous = 'yes' WHERE id = '"+id+"'");

            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void markProxyNoGood(ProxyInfo proxyInfo)
    {
        try
        {
            if (proxyInfo.isCheckOnlyOnce())
            {
                setProxyStatus(proxyInfo, ProxyChecker.PROXY_STATUS_DEAD);
            }
            else
            {
                setProxyStatus(proxyInfo, ProxyChecker.PROXY_STATUS_INACTIVE);
            }

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void markProxyAsGood(ProxyInfo proxyInfo)
    {
        try
        {
            setProxyStatus(proxyInfo, ProxyChecker.PROXY_STATUS_ACTIVE);

        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void markProxyAsHttps(ProxyInfo proxyInfo)
    {
        setProxyHttpsStatus(proxyInfo, true);
    }

    private void markProxyAsNotHttps(ProxyInfo proxyInfo)
    {
        setProxyHttpsStatus(proxyInfo, false);
    }

    private void setProxyHttpsStatus(ProxyInfo proxyInfo, boolean https)
    {
        try
        {
            String httpsStatus;
            if (https)
            {
                httpsStatus = "yes";
            }
            else
            {
                httpsStatus = "no";
            }
            String id = proxyInfo.getId();
            id = sanitizeDatabaseInput(id);
            Statement st = dbConnection.createStatement();
            st.executeUpdate("UPDATE "+Proxymity.TABLE_NAME+" SET https = '"+httpsStatus+"'WHERE id = '"+id+"'");
            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void setProxyStatus(ProxyInfo proxyInfo, String proxyStatus)
    {
        try
        {
            String id = proxyInfo.getId();
            id = sanitizeDatabaseInput(id);

            Statement st = dbConnection.createStatement();
            st.executeUpdate("UPDATE "+Proxymity.TABLE_NAME+" SET status = '"+proxyStatus+"', lastchecked = NOW() WHERE id = '"+id+"'");
            if (proxyStatus.equals(ProxyChecker.PROXY_STATUS_ACTIVE))
            {
                st.executeUpdate("UPDATE "+Proxymity.TABLE_NAME+" SET lastactive  = NOW() WHERE id = '"+id+"'");
            }
            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    /**
     * Sanitize database input string.
     *
     * @param value the value
     * @return the string
     */
    public static String sanitizeDatabaseInput(String value)
    {
        while (value.contains("''")) {
            value = value.replace("''","'");
        }
        return value.replace("'","''");
    }

    private static Proxy getProxyFromProxyInfo(ProxyInfo proxyInfo)
    {
        Proxy.Type type = null;

        if (proxyInfo.getType().equals( ProxyInfo.PROXY_TYPES_SOCKS4 ) )
        {
            type = Proxy.Type.SOCKS;
        }
        else if (proxyInfo.getType().equals( ProxyInfo.PROXY_TYPES_SOCKS5 ) )
        {
            type = Proxy.Type.SOCKS;
        }
        else if (proxyInfo.getType().equals( ProxyInfo.PROXY_TYPES_HTTP ) )
        {
            type = Proxy.Type.HTTP;
        }
        else if (proxyInfo.getType().equals( ProxyInfo.PROXY_TYPES_HTTPS ) )
        {
            type = Proxy.Type.HTTP;
        }
        return new Proxy(type,  new InetSocketAddress(proxyInfo.getHost(), Integer.parseInt(proxyInfo.getPort()))) ;
    }

    /**
     * Sets proxy remote ip.
     *
     * @param proxyInfo     the proxy info
     * @param proxyRemoteIp the proxy remote ip
     */
    public void setProxyRemoteIp(ProxyInfo proxyInfo, String proxyRemoteIp)
    {
        try
        {
            Statement st = dbConnection.createStatement();
            String id = sanitizeDatabaseInput(proxyInfo.getId());

            st.executeUpdate("UPDATE "+Proxymity.TABLE_NAME+" SET remoteIp = '"+proxyRemoteIp+"' WHERE id = '"+id+"'");
            st.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
