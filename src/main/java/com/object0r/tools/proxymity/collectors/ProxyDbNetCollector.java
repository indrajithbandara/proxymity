package com.object0r.tools.proxymity.collectors;

import com.object0r.tools.proxymity.ProxyCollector;
import com.object0r.tools.proxymity.datatypes.CollectorParameters;
import com.object0r.tools.proxymity.datatypes.ProxyInfo;
import com.object0r.toortools.Utilities;

import java.util.StringTokenizer;
import java.util.Vector;

/**
 * The type Proxy db net collector.
 */
public class ProxyDbNetCollector extends ProxyCollector {
    /**
     * Instantiates a new Proxy db net collector.
     *
     * @param collectorParameters the collector parameters
     */
    public ProxyDbNetCollector(CollectorParameters collectorParameters) {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies() {
        try {
            Thread t = new Thread() {
                public void run() {
                    processCategory("http://proxydb.net/?protocol=http&minavail=0&maxtime=0&limit=15&ip_filter=&port_filter=&host_filter=&country_filter=&isp_filter=&via_filter=&offset=", ProxyInfo.PROXY_TYPES_HTTP);
                }
            };
            t.start();

            Thread t2 = new Thread() {
                public void run() {
                    processCategory("http://proxydb.net/?protocol=socks5&minavail=0&maxtime=0&limit=15&ip_filter=&port_filter=&host_filter=&country_filter=&isp_filter=&via_filter=&offset=", ProxyInfo.PROXY_TYPES_SOCKS5);
                }
            };
            t2.start();

            Thread t3 = new Thread() {
                public void run() {
                    processCategory("http://proxydb.net/?protocol=https&minavail=0&maxtime=0&limit=15&ip_filter=&port_filter=&host_filter=&country_filter=&isp_filter=&via_filter=&offset=", ProxyInfo.PROXY_TYPES_HTTPS);
                }
            };
            t3.start();

            t.join();
            ;
            t2.join();
            t3.join();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return getProxies();
    }

    @Override
    protected String collectorName() {
        return "proxydb.net";
    }

    /**
     * Process category.
     *
     * @param url  the url
     * @param type the type
     */
    public void processCategory(String url, String type) {
        try {
            for (int i = 0; i < 50; i++) {
                Vector<String> lines = extractTableRows(url + (i * 15), true);
                for (String line : lines) {
                    if (line.contains("<td>")) {
                        try {
                            line = Utilities.cut("<a href=\"/", "\"", line);
                            StringTokenizer st = new StringTokenizer(line, "/");
                            String ip = st.nextToken();
                            String port = st.nextToken();
                            Integer.parseInt(port);
                            ProxyInfo proxyInfo = new ProxyInfo();
                            proxyInfo.setHost(ip);
                            proxyInfo.setPort(port);
                            proxyInfo.setType(type);
                            addProxy(proxyInfo);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
