package com.object0r.tools.proxymity.datatypes;

/**
 * The type Proxy info.
 */
public class ProxyInfo {
    /**
     * The constant PROXY_TYPES_SOCKS4.
     */
    final static public String PROXY_TYPES_SOCKS4 = "socks4";
    /**
     * The constant PROXY_TYPES_SOCKS5.
     */
    final static public String PROXY_TYPES_SOCKS5 = "socks5";
    /**
     * The constant PROXY_TYPES_HTTP.
     */
    final static public String PROXY_TYPES_HTTP = "http";
    /**
     * The constant PROXY_TYPES_HTTPS.
     */
    final static public String PROXY_TYPES_HTTPS = "https";

    private String id;
    private String host;
    private String port;
    private String type;
    private boolean checkOnlyOnce = false;
    private int priority = 0;

    /**
     * Gets priority.
     *
     * @return the priority
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Sets priority.
     *
     * @param priority the priority
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Gets id.
     *
     * @return the id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param host the host
     */
    public void setHost(String host) {
        this.host = host;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public String getPort() {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(String port) {
        this.port = port;
    }

    /**
     * Gets type.
     *
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * Sets type.
     *
     * @param type the type
     * @throws Exception the exception
     */
    public void setType(String type) throws Exception {
        if (
                !type.equals(ProxyInfo.PROXY_TYPES_HTTP) &&
                        !type.equals(ProxyInfo.PROXY_TYPES_HTTPS) &&
                        !type.equals(ProxyInfo.PROXY_TYPES_SOCKS4) &&
                        !type.equals(ProxyInfo.PROXY_TYPES_SOCKS5)
                ) {
            throw new Exception("Unknown Proxy Type");
        }
        this.type = type;
    }

    /**
     * Unset check only once.
     */
    public void unsetCheckOnlyOnce() {
        this.checkOnlyOnce = true;
    }

    /**
     * Sets check only once.
     */
    public void setCheckOnlyOnce() {
        this.checkOnlyOnce = true;
    }

    /**
     * Is check only once boolean.
     *
     * @return the boolean
     */
    public boolean isCheckOnlyOnce() {
        return checkOnlyOnce;
    }

    public String toString() {
        return host + ":" + port + "/" + type;
    }
}
