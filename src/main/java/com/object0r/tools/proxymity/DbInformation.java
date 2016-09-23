package com.object0r.tools.proxymity;

/**
 * The type Db information.
 */
public class DbInformation
{
    /**
     * Instantiates a new Db information.
     *
     * @param url      the url
     * @param username the username
     * @param password the password
     * @param port     the port
     * @param database the database
     */
    DbInformation(String url, String username, String password,  int port, String database)
    {
        this.username = username;
        this.password = password;
        this.database = database;
        this.url = url;
        this.port = port;
    }

    private String username;
    private String password;
    private String url;
    private int port;
    private String database;

    /**
     * Gets username.
     *
     * @return the username
     */
    public String getUsername()
    {
        return username;
    }

    /**
     * Sets username.
     *
     * @param username the username
     */
    public void setUsername(String username)
    {
        this.username = username;
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String getPassword()
    {
        return password;
    }

    /**
     * Sets password.
     *
     * @param password the password
     */
    public void setPassword(String password)
    {
        this.password = password;
    }

    /**
     * Gets url.
     *
     * @return the url
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Sets url.
     *
     * @param url the url
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * Gets port.
     *
     * @return the port
     */
    public int getPort()
    {
        return port;
    }

    /**
     * Sets port.
     *
     * @param port the port
     */
    public void setPort(int port)
    {
        this.port = port;
    }

    /**
     * Gets database.
     *
     * @return the database
     */
    public String getDatabase() {
        return database;
    }

    /**
     * Sets database.
     *
     * @param database the database
     */
    public void setDatabase(String database) {
        this.database = database;
    }
}
