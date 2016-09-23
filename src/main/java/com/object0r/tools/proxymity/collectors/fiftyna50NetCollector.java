package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;
import com.object0r.toortools.Utilities;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Fiftyna 50 net collector.
 */
public class fiftyna50NetCollector extends ProxyCollector
{
    /**
     * Instantiates a new Fiftyna 50 net collector.
     *
     * @param collectorParameters the collector parameters
     */
    public fiftyna50NetCollector(CollectorParameters collectorParameters)
    {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies()
    {
        try
        {
            String page = Utilities.readUrl("http://proxy50-50.blogspot.in/");
            Pattern p = Pattern.compile("<tr >.*?</tr>",Pattern.DOTALL);
            Matcher m = p.matcher(page);
            while (m.find())
            {
                String line = m.group();
                //System.out.println(line);
                if (line.contains("confirmation=http://hideip.me/ip"))
                {
                    String ip = Utilities.cut("&host=","&", line);
                    String port = Utilities.cut("&port=","&", line);
                    Integer.parseInt(port);
                    ProxyInfo proxyInfo = new ProxyInfo();

                    proxyInfo.setHost(ip);
                    proxyInfo.setPort(port);
                    proxyInfo.setType(ProxyInfo.PROXY_TYPES_HTTP);
                    addProxy(proxyInfo);
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return getProxies();
    }

    @Override
    protected String collectorName()
    {
        return "proxy50-50.blogspot.in";
    }
}
