package com.object0r.tools.proxymity.phantomjs;


import com.object0r.tools.proxymity.Proxymity;

import java.util.HashMap;
import java.util.Map;

/**
 * The type Phantom js manager.
 */
public class PhantomJsManager extends Thread
{
    /**
     * Instantiates a new Phantom js manager.
     *
     * @param useTor the use tor
     */
    public PhantomJsManager(boolean useTor)
    {
        this.useTor = useTor;
    }

    /**
     * The Jobs.
     */
    HashMap<String, PhantomJsJob> jobs = new HashMap<String, PhantomJsJob>();
    /**
     * The Use tor.
     */
    boolean useTor = false;

    /**
     * Add job phantom js job.
     *
     * @param url the url
     * @return the phantom js job
     */
    synchronized public PhantomJsJob addJob(String url)
    {
        if (jobs.containsKey(url))
        {
            return jobs.get(url);
        }
        else
        {
            PhantomJsJob phantomJsJob = new PhantomJsJob(url);
            jobs.put(url,  phantomJsJob);
            return phantomJsJob;
        }
    }

    /**
     * Add job phantom js job.
     *
     * @param url  the url
     * @param body the body
     * @return the phantom js job
     */
    synchronized public PhantomJsJob addJob(String url, String body)
    {
        if (jobs.containsKey(url))
        {
            return jobs.get(url);
        }
        else
        {
            PhantomJsJob phantomJsJob = new PhantomJsJob(url, body);
            jobs.put(url,  phantomJsJob);
            return phantomJsJob;
        }
    }

    public void run()
    {
        for (int i = 0; i< Proxymity.PHANTOM_JS_WORKERS_COUNT; i++)
        {
            new PhantomJsWorker(this, useTor).start();
            try { Thread.sleep(1000); } catch (Exception e) { }
        }

        while (true)
        {
            try
            {
                Thread.sleep(20000);
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    /**
     * Similar to pop.
     *
     * @return next job
     */
    synchronized PhantomJsJob getNextJob()
    {
        for (Map.Entry<String, PhantomJsJob> entry : jobs.entrySet())
        {
            String url = entry.getKey();
            PhantomJsJob phantomJsJob = entry.getValue();
            if (phantomJsJob.isPending())
            {
                phantomJsJob.setStatusProcessing();
                return phantomJsJob;
            }
        }
        return null;
    }
}
