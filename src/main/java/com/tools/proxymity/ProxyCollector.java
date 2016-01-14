package com.tools.proxymity;

import com.tools.proxymity.datatypes.CollectorParameters;
import com.tools.proxymity.datatypes.ProxyInfo;
import com.toortools.os.OsHelper;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Random;
import java.util.Vector;
import java.util.logging.Level;

abstract public class ProxyCollector extends  Thread
{
    String imageMagickPath = "bin\\imageMagick\\convert.exe";
    private Vector<ProxyInfo> proxies = new Vector<ProxyInfo>();

    protected Connection dbConnection;
    protected PhantomJSDriver driver;
    protected boolean useTor = false;
    public ProxyCollector(CollectorParameters collectorParameters)
    {
        //this.collectorParameters = collectorParameters;
        this.dbConnection = collectorParameters.getDbConnection();
        this.useTor = collectorParameters.isUseTor();
        if (!new File("tmp/").isDirectory())
        {
            new File("tmp").mkdir();
        }

        if (OsHelper.isWindows() && ! new File(imageMagickPath).exists())
        {
            System.out.println("Image magick is not installed, please install or update installation path. (http://www.imagemagick.org/download/binaries/ImageMagick-6.9.3-0-Q16-x64-dll.exe)");
        }
    }

    public ProxyCollector() throws Exception
    {
        throw new Exception("Default controller not allowed");
    }
    public abstract Vector<ProxyInfo> collectProxies();

    int SLEEP_SECONDS_BETWEEN_SCANS = 30;


    public void setSleepSecondsBetweenScans(int minutes)
    {
        this.SLEEP_SECONDS_BETWEEN_SCANS = minutes;
    }

    public void run()
    {
        while (true)
        {

            try
            {
                Thread.sleep(new Random().nextInt(5000));
                initProxies();
                Vector<ProxyInfo> proxyInfos = collectProxies();

                writeProxyInfoToDatabase(proxyInfos);

                Thread.sleep(SLEEP_SECONDS_BETWEEN_SCANS * 1000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    private synchronized void initProxies()
    {
        proxies  = new Vector<ProxyInfo>();
    }

    protected synchronized void addProxy(ProxyInfo proxyInfo)
    {
        proxies.add(proxyInfo);
    }

    protected Vector<ProxyInfo> getProxies()
    {
        return proxies;
    }

    public synchronized void writeProxyInfoToDatabase()
    {
        this.writeProxyInfoToDatabase(this.proxies);
    }

    private void writeProxyInfoToDatabase(Vector<ProxyInfo> proxyInfos)
    {
        try
        {
            for (ProxyInfo proxyInfo : proxyInfos)
            {
                if (
                        proxyInfo.getHost() == null
                        || proxyInfo.getPort() == null
                        || proxyInfo.getType() == null

                        )
                {
                    continue;
                }
                String query = "INSERT INTO `proxies`.`"+Proxymity.TABLE_NAME+"` (" +
                        "`id`, " +
                        "`host`, " +
                        "`port`, " +
                        "`type`, " +
                        "`inserted`, " +
                        "`lastchecked`, " +
                        "`status`, " +
                        "`fullanonymous` " +

                        ") VALUES  (" +
                        "0, " +
                        "'"+sanitizeDatabaseInput(proxyInfo.getHost())+"', " +
                        "'"+sanitizeDatabaseInput(proxyInfo.getPort())+"', " +
                        "'"+sanitizeDatabaseInput(proxyInfo.getType())+"', " +
                        "NOW(), " +
                        "NULL, " +
                        "'pending', " +
                        "'no')";
                //System.out.println(query);

                Statement st = dbConnection.createStatement();
                try
                {
                    st.executeUpdate(query);
                }
                catch (Exception MySQLIntegrityConstraintViolationException)
                {

                }
                st.close();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    String sanitizeDatabaseInput(String value)
    {
        while (value.contains("''")) {
            value = value.replace("''","'");
        }
        return value.replace("'","''");
    }

    public void initializePhantom()
    {
        java.util.logging.Logger.getLogger("com.gargoylesoftware").setLevel(Level.OFF);
        System.setProperty("org.apache.commons.logging.Log", "org.apache.commons.logging.impl.NoOpLog");

        //Utilities.readUrl("http://proxylist.hidemyass.com/2#listable");

        Capabilities caps = new DesiredCapabilities();
        String[] phantomArgs = new String[] {

        };
        if (!useTor)
        {
            phantomArgs = new  String[] {
                    "--webdriver-loglevel=NONE"
            };
        }
        else
        {
            phantomArgs = new  String[] {
                    "--webdriver-loglevel=NONE",
                    "--proxy=127.0.0.1:9050",
                    "--proxy-type=socks5"
            };
        }



        if (OsHelper.isWindows())
        {
            ((DesiredCapabilities) caps).setJavascriptEnabled(true);
            ((DesiredCapabilities) caps).setJavascriptEnabled(true);
            ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);
            ((DesiredCapabilities) caps).setCapability(
                    PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                    "bin\\phantomjs.exe"
            );
            ((DesiredCapabilities) caps).setCapability(
                    PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs
            );
        }
        else
        {
            ((DesiredCapabilities) caps).setJavascriptEnabled(true);
            ((DesiredCapabilities) caps).setCapability("takesScreenshot", true);
            ((DesiredCapabilities) caps).setCapability(
                    PhantomJSDriverService.PHANTOMJS_CLI_ARGS, phantomArgs
            );
        }
        driver = new PhantomJSDriver(caps);
    }

    protected Proxy getRandomProxy() throws Exception
    {
        Proxy proxy = null ;
        try
        {
            Statement st = dbConnection.createStatement();
            ResultSet rs = st.executeQuery("SELECT host,port,type FROM " + Proxymity.TABLE_NAME + " WHERE status = 'active' ORDER BY RAND() LIMIT 1");
            if (rs.next())
            {

                String host = rs.getString(1);
                String port = rs.getString(2);
                String proxyType = rs.getString(3);
                st.close();

                Proxy.Type type = null;

                if (proxyType.equals(ProxyInfo.PROXY_TYPES_SOCKS4)) {
                    type = Proxy.Type.SOCKS;
                } else if (proxyType.equals(ProxyInfo.PROXY_TYPES_SOCKS5)) {
                    type = Proxy.Type.SOCKS;
                } else if (proxyType.equals(ProxyInfo.PROXY_TYPES_HTTP)) {
                    type = Proxy.Type.HTTP;
                } else if (proxyType.equals(ProxyInfo.PROXY_TYPES_HTTPS)) {
                    type = Proxy.Type.HTTP;
                }
                return new Proxy(type, new InetSocketAddress(host, Integer.parseInt(port) ));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        return null;
    }

    public String ocrImage(String filename)
    {
        String text = "";
        try
        {
            String gocrPath = "gocr";
            String command;
            if (OsHelper.isWindows())
            {
                gocrPath = "bin/gocr049.exe";
            }
            command = gocrPath + " -C \"0123456789\" " + filename;
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;
            StringBuffer sb = new StringBuffer();
            while((line=input.readLine()) != null)
            {
                sb.append(line);
            }
            return sb.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return text;
    }

    public void convertImageToPnm(String inputFilename, String outputFilename )
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            String command;
            String convertPath = "convert";
            if (OsHelper.isWindows())
            {
                convertPath = imageMagickPath;
            }

            command = convertPath+ " " +  inputFilename + " " +outputFilename+"";
            Process pr = rt.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            input = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }
            //System.out.println(pr.waitFor());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void convertImageToPnmDark(String inputFilename, String outputFilename )
    {
        try
        {
            Runtime rt = Runtime.getRuntime();
            String command;
            String convertPath = "convert";
            if (OsHelper.isWindows())
            {
                convertPath = imageMagickPath;
            }

            command = convertPath+ " -type Grayscale -depth 8 -black-threshold 87% -density 300 " +  inputFilename + " " +outputFilename+"";
            Process pr = rt.exec(command);
            BufferedReader input = new BufferedReader(new InputStreamReader(pr.getInputStream()));

            String line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }

            input = new BufferedReader(new InputStreamReader(pr.getErrorStream()));

            line=null;

            while((line=input.readLine()) != null) {
                System.out.println(line);
            }
            //System.out.println(pr.waitFor());
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
