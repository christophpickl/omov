package at.phudy.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.google.inject.Inject;

public class HsqldbShutdownHook implements Runnable {

	private static final Log LOG = LogFactory.getLog(HsqldbShutdownHook.class);

	private final HsqldbShutdown hsqldbShutdown;
	
	
	@Inject
	public HsqldbShutdownHook(final HsqldbShutdown hsqldbShutdown) {
		LOG.info("Constructor invoked.");
		
		this.hsqldbShutdown = hsqldbShutdown;
	}
	
	@Override
	public void run() {
		LOG.info("Method run() invoked.");
		
		this.hsqldbShutdown.shutdown();
	}

}
