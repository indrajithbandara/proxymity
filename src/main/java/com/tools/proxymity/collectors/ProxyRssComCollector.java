package com.tools.proxymity.collectors;

import com.tools.proxymity.ProxyCollector;
import com.tools.proxymity.datatypes.CollectorParameters;
import com.tools.proxymity.datatypes.ProxyInfo;
import com.toortools.Utilities;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Vector;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ProxyRssComCollector extends ProxyCollector
{
    public ProxyRssComCollector(CollectorParameters collectorParameters)
    {
        super(collectorParameters);
    }

    public Vector<ProxyInfo> collectProxies()
    {
        try
        {
            String gzFile = "tmp/proxyrss.com.gz";
            String outFile ="tmp/proxyrss.com.txt";
            Utilities.downloadFile("http://www.proxyrss.com/proxylists/all.gz", gzFile);

            FileInputStream fis = new FileInputStream(gzFile);
            GZIPInputStream gis = new GZIPInputStream(fis);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buffer = new byte[1024];
            int len;
            StringBuffer sb = new StringBuffer();
            while((len = gis.read(buffer)) != -1){
                //sb.append(buffer);
                fos.write(buffer, 0, len);
            }

            gis.close();
            fos.close();
            genericParsingOfText(Utilities.readFile(outFile), ProxyInfo.PROXY_TYPES_HTTP);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return getProxies();
    }
}
