package com.object0r.tools.proxymity.datatypes;


import com.object0r.tools.proxymity.phantomjs.PhantomJsManager;

import java.sql.Connection;

/**
 * The type Collector parameters.
 */
public class CollectorParameters {
    /**
     * The Db connection.
     */
    Connection dbConnection;
    /**
     * The Use tor.
     */
    boolean useTor;
    /**
     * The Sleep between scans seconds.
     */
    int sleepBetweenScansSeconds;
    /**
     * The Phantom js manager.
     */
    PhantomJsManager phantomJsManager;

    /**
     * Gets sleep between scans seconds.
     *
     * @return the sleep between scans seconds
     */
    public int getSleepBetweenScansSeconds() {
        return sleepBetweenScansSeconds;
    }

    /**
     * Sets sleep between scans seconds.
     *
     * @param sleepBetweenScansSeconds the sleep between scans seconds
     */
    public void setSleepBetweenScansSeconds(int sleepBetweenScansSeconds) {
        this.sleepBetweenScansSeconds = sleepBetweenScansSeconds;
    }

    /**
     * Gets db connection.
     *
     * @return the db connection
     */
    public Connection getDbConnection() {
        return dbConnection;
    }

    /**
     * Sets db connection.
     *
     * @param dbConnection the db connection
     */
    public void setDbConnection(Connection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Is use tor boolean.
     *
     * @return the boolean
     */
    public boolean isUseTor() {
        return useTor;
    }

    /**
     * Sets use tor.
     *
     * @param useTor the use tor
     */
    public void setUseTor(boolean useTor) {
        this.useTor = useTor;
    }

    /**
     * Gets phantom js manager.
     *
     * @return the phantom js manager
     */
    public PhantomJsManager getPhantomJsManager() {
        return phantomJsManager;
    }

    /**
     * Sets phantom js manager.
     *
     * @param phantomJsManager the phantom js manager
     */
    public void setPhantomJsManager(PhantomJsManager phantomJsManager) {
        this.phantomJsManager = phantomJsManager;
    }
}
