package at.phudy.dao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;

import at.phudy.model.Catalog;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
class CatalogHibernateDao extends AbstractDefaultDao<Long, Catalog> implements ICatalogDao {

	private static final Log LOG = LogFactory.getLog(CatalogHibernateDao.class);

	
	@Inject
	public CatalogHibernateDao(SessionFactory sessionFactory) {
		super(sessionFactory, Catalog.class);
		LOG.info("Constructor invoked.");
	}

	@Override
	public Catalog getById(final Long id) {
		LOG.info("Method getById() invoked.");
		// TODO Auto-generated method stub
		return null;
	}


}
