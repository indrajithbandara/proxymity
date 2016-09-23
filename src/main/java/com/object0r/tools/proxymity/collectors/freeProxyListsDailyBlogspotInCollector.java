package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;

import java.util.Vector;

/**
 * The type Free proxy lists daily blogspot in collector.
 */
public class freeProxyListsDailyBlogspotInCollector extends ProxyCollector {
    /**
     * Instantiates a new Free proxy lists daily blogspot in collector.
     *
     * @param collectorParameters the collector parameters
     */
    public freeProxyListsDailyBlogspotInCollector(CollectorParameters collectorParameters) {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies() {
        try {
            genericParsingOfUrl("http://freeproxylistsdaily.blogspot.in/feeds/posts/default", ProxyInfo.PROXY_TYPES_HTTP);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getProxies();
    }

    @Override
    protected String collectorName() {
        return "freeproxylistsdaily.blogspot.in";
    }
}
