package at.phudy;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hsqldb.DatabaseManager;

import at.phudy.dao.DaoModule;
import at.phudy.dao.HsqldbShutdown;
import at.phudy.service.ServiceModule;

import com.google.inject.Guice;
import com.google.inject.Injector;

public class MyServletContextListener implements ServletContextListener {

	private static final Log LOG = LogFactory.getLog(MyServletContextListener.class);

	private HsqldbShutdown hsqldbShutdown;

	private static Injector injector;
	
	
	@Override
	public void contextInitialized(ServletContextEvent event) {
		LOG.info("contextInitialized(event="+event+")");
		
		injector = Guice.createInjector(new ServiceModule(), new DaoModule());
		
		this.hsqldbShutdown = injector.getInstance(HsqldbShutdown.class);
		
//    	Runtime.getRuntime().addShutdownHook(new Thread(injector.getInstance(HsqldbShutdownHook.class)));
	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {
		LOG.info("contextDestroyed(event="+event+")");
		
		// avoid NullPointerExceptions when undeploying the webapp
		DatabaseManager.getTimer().shutDown();
		this.hsqldbShutdown.shutdown();
	}
	
	public static void injectMembers(final Object object) {
		if(injector == null) {
			throw new IllegalStateException("Injector was not yet initialized via contextInitialized method!");
		}
		injector.injectMembers(object);
	}
}
