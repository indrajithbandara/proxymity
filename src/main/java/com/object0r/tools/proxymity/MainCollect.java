package com.object0r.tools.proxymity;


import com.object0r.toortools.os.RecurringProcessHelper;

import java.util.Properties;

/**
 * The type Main collect.
 */
public class MainCollect {

    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        RecurringProcessHelper.checkAndRun("proxymity");

        Properties properties = Main.readProperties();

        try {
            if (properties.getProperty("exitAfterMinutes") != null) {
                RecurringProcessHelper.exitAfterSeconds(Integer.parseInt(properties.getProperty("exitAfterMinutes")) * 60);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        Proxymity proxymity = new Proxymity(properties);

        //proxymity.useTor();

        //proxymity.startCheckers();
        proxymity.startCollectors();
    }


}
