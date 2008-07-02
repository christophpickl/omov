/*
 * @(#) $Id:  $
 *
 * Copyright 2004/2005 by SPARDAT Sparkassen-Datendienst Ges.m.b.H.,
 * A-1110 Wien, Geiselbergstr.21-25.
 * All rights reserved.
 *
 */
package net.sourceforge.omov.app.playground;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;

import net.sourceforge.jpotpourri.util.CloseUtil;
import net.sourceforge.omov.core.BusinessException;

public class ProxyPlayground {
    public static void main(String[] args) throws BusinessException {
        SocketAddress address = new InetSocketAddress("proxy-sd.s-mxs.net", 8080);
        final Proxy proxy = new Proxy(Proxy.Type.HTTP, address);
        downloadFile("http://orf.at/foot_news.html", new File("C:/myyyyyyy.html"), proxy);
        System.out.println("FINISHED!");
    }

    static void downloadFile(String urlString, File target, Proxy proxy) throws BusinessException {
        if(target.exists() == true) {
            if(target.delete() == false) {
                throw new RuntimeException("Could not delete file '"+target.getAbsolutePath()+"'!");
            }
        }

        if(target.getParentFile().exists() == false || target.getParentFile().isDirectory() == false) {
            throw new BusinessException("Parent folder '"+target.getParent()+"' does not exist!");
        }

        URL realUrl;
        try {
            realUrl = new URL(urlString);
        } catch (MalformedURLException e) {
            throw new BusinessException("Invalid URL given '"+urlString+"'!", e);
        }
        HttpURLConnection http;
        try {
            System.out.println("Opening connection...");
            if(proxy == null) {
                http = (HttpURLConnection) realUrl.openConnection();
            } else {
                http = (HttpURLConnection) realUrl.openConnection(proxy);
            }
        } catch (IOException e) {
            throw new BusinessException("Could not open connection to server '"+realUrl.getHost()+"'!", e);
        }

        http.setUseCaches(false);
        http.setDoOutput(false);


        try {
            System.out.println("Connecting ...");
            http.connect();
        } catch (IOException e) {
            throw new BusinessException("Could not connect to http-url '"+urlString+"'!");
        }

        BufferedInputStream in = null;
        OutputStream out = null;
        try {
            System.out.println("Downloading file ...");
            in = new BufferedInputStream(http.getInputStream());
            out = new BufferedOutputStream(new FileOutputStream(target));

            byte[] buf = new byte[256];
            int n = 0;
            while ((n=in.read(buf))>=0) {
               out.write(buf, 0, n);
            }

        } catch (IOException e) {
            throw new BusinessException("Could not download file from url '"+urlString+"' to file '"+target.getAbsolutePath()+"'!", e);
        } finally {
            CloseUtil.close(in);
            CloseUtil.close(out);
        }

        System.out.println("Disconnecting ...");
        http.disconnect();
    }
}
