package net.sourceforge.omov.logic;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.SocketAddress;
import java.net.URL;
import java.net.URLConnection;

import net.sourceforge.omov.logic.prefs.PreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * mantis 0000053
 */
public final class ProxyEnabledConnectionFactory {

    private static final Log LOG = LogFactory.getLog(ProxyEnabledConnectionFactory.class);
    
    
    private static Proxy proxy;
    
    private static String proxyHostInUse;
    
    private static int proxyPortInUse;
    
    
    private ProxyEnabledConnectionFactory() {
    	// no instantiation
    }
    

    
    public static URLConnection openConnection(URL url) throws IOException {
    	LOG.debug("Connecting to url: " + url);
    	if(PreferencesDao.getInstance().isProxyEnabled()) {
    		checkProxyInstance();
    		LOG.info("Returning connection with proxy '"+proxyHostInUse+":"+proxyPortInUse+"'.");
    		return url.openConnection(proxy);
    	}
    	
    	LOG.info("Returning connection without proxy.");
    	return url.openConnection();
    }
    
    private static void checkProxyInstance() {
    	final String proxyHost = PreferencesDao.getInstance().getProxyHost();
		final int proxyPort = PreferencesDao.getInstance().getProxyPort();
		
		if(proxy != null && proxyHost.equals(proxyHostInUse) && proxyPort == proxyPortInUse) {
			// nothing to do, because proxy object is up2date
			return;
		}
		
		LOG.debug("Creating new proxy object to url '"+proxyHost+":"+proxyPort+"'.");
		SocketAddress address = new InetSocketAddress(proxyHost, proxyPort);
        proxy = new Proxy(Proxy.Type.HTTP, address);
        proxyHostInUse = proxyHost;
        proxyPortInUse = proxyPort;
    }
    
}
