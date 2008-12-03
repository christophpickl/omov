package at.phudy.service;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.phudy.MyServletContextListener;
import at.phudy.dao.ICatalogDao;
import at.phudy.model.Catalog;

import com.google.inject.Inject;

public class SomeService implements ISomeService {

	private static final Log LOG = LogFactory.getLog(SomeService.class);

	@Inject
	private ICatalogDao catalogDao;
	
	private boolean injected = false;
	
	public SomeService() {
		// BlazeDS services must provide a public no-arg constructor
	}
	
	
	@Override
	public List<Catalog> getCatalogs() {
		LOG.info("Method getCatalogs() invoked.");
		
		if(this.injected == false) {
			this.injected = true;
			MyServletContextListener.injectMembers(this);
		}
		
		try {
			return this.catalogDao.getAll();
			
		} catch(RuntimeException e) {
			LOG.error("Could not get catalogs!", e);
			throw e;
		}
	}


}
