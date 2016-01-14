package com.tools.proxymity;

import com.tools.proxymity.datatypes.ProxyInfo;
import com.toortools.Utilities;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.Statement;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ProxyChecker implements Runnable
{
    public final static String PROXY_STATUS_PENDING = "pending";
    public final static String PROXY_STATUS_INACTIVE = "inactive";
    public final static String PROXY_STATUS_ACTIVE = "active";
    public static final String PROXY_STATUS_DEAD = "dead";
    static String myIp;
    ProxyInfo proxyInfo;
    Connection dbConnection;

    ProxyChecker(ProxyInfo proxyInfo, Connection dbConnection)
    {
        this.proxyInfo = proxyInfo;
        this.dbConnection = dbConnection;
    }

    public void setMyIp()
    {
        try
        {
            myIp = Utilities.readUrl("http://cpanel.com/showip.shtml");
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

            URLConnection conn = new URL("http://cpanel.com/showip.shtml").openConnection(proxy);
            conn.setConnectTimeout(10000);
            conn.setReadTimeout(10000);
            Scanner sc = new Scanner(conn.getInputStream());
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext())
            {
                sb.append(sc.nextLine());
            }
            conn.getInputStream().close();

            String ip = sb.toString().trim();
            Pattern p = Pattern.compile("^\\d+\\.\\d+\\.\\d+\\.\\d+$");
            Matcher m = p.matcher(ip);

            if (ip.length() > 13 || ip.length() < 5 || !m.find())
            {
                markProxyNoGood(proxyInfo);
                throw new Exception("Invalid Ip returned.");
            }

            if (ip.equals(myIp))
            {
                markProxyNoGood(proxyInfo);
                System.out.println("Proxy Not Anonymous.");
                //Never seen that
            }
            else
            {
                markProxyAsGood(proxyInfo);
                setProxyRemoteIp(proxyInfo, ip);
                try
                {
                    String page = Utilities.readUrl("http://ip.cc/anonymity-test.php");
                    if (page.contains("high-anonymous (elit"))
                    {
                        markProxyAnonymous(proxyInfo);
                        page = Utilities.readUrl("http://www.iprivacytools.com/proxy-checker-anonymity-test/");
                        if (!page.contains("Proxy detected? <span style='background-color:#ffff99;'>NO</span"))
                        {
                            System.out.println("Anon error");
                        }
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
            setProxyStatus(proxyInfo, ProxyChecker.PROXY_STATUS_INACTIVE);

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
