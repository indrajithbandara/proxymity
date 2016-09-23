package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;

import java.util.Vector;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * The type Free pass ru collector.
 */
public class FreePassRuCollector extends ProxyCollector
{
    /**
     * Instantiates a new Free pass ru collector.
     *
     * @param collectorParameters the collector parameters
     */
    public FreePassRuCollector(CollectorParameters collectorParameters)
    {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies()
    {
        try
        {
            String page = persistentAnonReadUrl("http://free-pass.ru/forum/79", 50);

            Pattern p = Pattern.compile("/forum/79-\\d+-\\d+");
            Matcher m = p.matcher(page);

            while (m.find())
            {

                String url = "http://free-pass.ru" + m.group();
                String page2 = persistentAnonReadUrl(url, 50);
                genericParsingOfText(page2, ProxyInfo.PROXY_TYPES_HTTP);
                genericParsingOfText(page2, ProxyInfo.PROXY_TYPES_HTTPS);
                genericParsingOfText(page2, ProxyInfo.PROXY_TYPES_SOCKS4);
                genericParsingOfText(page2, ProxyInfo.PROXY_TYPES_SOCKS5);
                Thread.sleep(30000);
            }

        }
        catch (Exception e)
        {
            if (e.toString().contains("IOException: Server returned HTTP response code: 500 "))
            {
                System.out.println(e);
            }
        }
        return getProxies();
    }

    @Override
    protected String collectorName()
    {
        return "free-pass.ru";
    }

    /*private String myRead(String url) throws Exception
    {
        while (true)
        {
            try
            {
                return anonReadUrl(url);
            }
            catch (Exception e)
            {
                if (e.toString().contains("IOException: Server returned HTTP response code: 500"))
                {
                    return anonReadUrl(url);
                }
            }
        }
    }*/
}
