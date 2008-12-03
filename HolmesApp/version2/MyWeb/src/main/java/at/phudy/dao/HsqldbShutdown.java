package at.phudy.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

import com.google.inject.Inject;

public class HsqldbShutdown {

	private static final Log LOG = LogFactory.getLog(HsqldbShutdown.class);

	private final SessionFactory sessionFactory;

	
	@Inject
	public HsqldbShutdown(final SessionFactory sessionFactory) {
		LOG.info("Constructor invoked.");
		
		this.sessionFactory = sessionFactory;
	}
	
	
	public void shutdown() {

		final Session session = this.sessionFactory.openSession();
    	try {
    		session.createSQLQuery("SHUTDOWN").executeUpdate();
    	} finally {
    		session.close();
    	}
    	
	}
}
