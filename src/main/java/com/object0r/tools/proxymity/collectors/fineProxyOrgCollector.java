package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;

import java.util.Vector;

/**
 * The type Fine proxy org collector.
 */
public class fineProxyOrgCollector extends ProxyCollector
{
    /**
     * Instantiates a new Fine proxy org collector.
     *
     * @param collectorParameters the collector parameters
     */
    public fineProxyOrgCollector(CollectorParameters collectorParameters)
    {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies()
    {
        try
        {
            genericParsingOfUrl("http://eng.fineproxy.org/freshproxy/", ProxyInfo.PROXY_TYPES_HTTP);
            genericParsingOfUrl("http://eng.fineproxy.org/freshproxy/", ProxyInfo.PROXY_TYPES_SOCKS5);
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
        return "fineproxy.org";
    }
}
