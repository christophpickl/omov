package at.phudy.dao;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.AnnotationConfiguration;

import com.google.inject.AbstractModule;

public class DaoModule extends AbstractModule {

	@Override
	protected void configure() {
		bind(ICatalogDao.class).to(CatalogHibernateDao.class);
		
    	bind(SessionFactory.class).toInstance(
    			new AnnotationConfiguration().configure().buildSessionFactory());
	}

}
