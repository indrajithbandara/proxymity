package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;
import com.object0r.toortools.Utilities;

import java.io.FileNotFoundException;
import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Mr hinky dink collector.
 */
public class MrHinkyDinkCollector extends ProxyCollector
{
    /**
     * Instantiates a new Mr hinky dink collector.
     *
     * @param collectorParameters the collector parameters
     */
    public MrHinkyDinkCollector(CollectorParameters collectorParameters)
    {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies()
    {
        try
        {
            try
            {
                genericParsingOfUrl("http://www.blackhatunderground.net/forum/buy-sell-trade-blackhat-marketplace/50-000-new-proxies-every-day!-worldwide-usa-elite-socks-%2850kproxies-com%29/90/", ProxyInfo.PROXY_TYPES_HTTP);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            for (int i = 1; i < 50; i++)
            {
                String url = "http://www.mrhinkydink.com/proxies" + i + ".htm";
                if (i == 1)
                {
                    url = "http://www.mrhinkydink.com/proxies.htm";
                }
                String page = Utilities.readUrl(url);
                Pattern p = Pattern.compile("<tr bgcolor=\"#[^\"]*\" class=\"text\" height=10>.*?</tr>", Pattern.DOTALL);
                Matcher m = p.matcher(page);

                while (m.find())
                {
                    String line = m.group();
                    Pattern pp = Pattern.compile("<td>.*?</td>", Pattern.DOTALL);
                    Matcher mm = pp.matcher(line);
                    if (mm.find())
                    {
                        String ip = Utilities.cut("<td>", "<", mm.group());
                        if (mm.find())
                        {
                            String port = Utilities.cut("<td>", "<", mm.group());

                            Integer.parseInt(port);
                            ProxyInfo proxyInfo = new ProxyInfo();
                            proxyInfo.setHost(ip);
                            proxyInfo.setPort(port);
                            proxyInfo.setType(ProxyInfo.PROXY_TYPES_HTTP);
                            addProxy(proxyInfo);
                        }

                    }
                }
            }
        }
        catch (FileNotFoundException e)
        {
            return getProxies();
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
        return "mrhinkydink.com";
    }
}
