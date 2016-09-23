package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;

import java.util.Vector;

/**
 * The type Maxiproxies com collector.
 */
public class MaxiproxiesComCollector extends ProxyCollector
{
    /**
     * Instantiates a new Maxiproxies com collector.
     *
     * @param collectorParameters the collector parameters
     */
    public MaxiproxiesComCollector(CollectorParameters collectorParameters)
    {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies()
    {
        try
        {
            genericParsingOfUrl("http://maxiproxies.com/feed/atom/", ProxyInfo.PROXY_TYPES_HTTP);
            genericParsingOfUrl("http://maxiproxies.com/proxy-lists/feed/", ProxyInfo.PROXY_TYPES_HTTP);
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
        return "maxiproxies.com";
    }
}
