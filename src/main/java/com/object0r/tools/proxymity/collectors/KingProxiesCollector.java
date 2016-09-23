package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;
import com.object0r.toortools.Utilities;

import java.util.Vector;

/**
 * The type King proxies collector.
 */
public class KingProxiesCollector extends ProxyCollector {
    /**
     * Instantiates a new King proxies collector.
     *
     * @param collectorParameters the collector parameters
     */
    public KingProxiesCollector(CollectorParameters collectorParameters) {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies() {
        try {

            String page = Utilities.readUrl("https://kingproxies.com/api/v2/proxies.txt?key=freesample&alive=1&protocols=&type=&country_code=");


            this.genericParsingOfText(page, ProxyInfo.PROXY_TYPES_HTTP);
            this.genericParsingOfText(page, ProxyInfo.PROXY_TYPES_SOCKS5);

        } catch (Exception e) {
            e.printStackTrace();
        }
        return getProxies();
    }

    @Override
    protected String collectorName() {
        return "kingproxies.com";
    }
}
