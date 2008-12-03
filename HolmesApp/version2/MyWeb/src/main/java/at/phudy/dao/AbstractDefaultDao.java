package at.phudy.dao;

import java.io.Serializable;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

abstract class AbstractDefaultDao<I extends Serializable, E> implements IAbstractDao<I, E> {

	private static final Log LOG = LogFactory.getLog(AbstractDefaultDao.class);

	private final SessionFactory sessionFactory;

	private final Class<E> clazz;

	public AbstractDefaultDao(final SessionFactory sessionFactory, final Class<E> clazz) {
		this.sessionFactory = sessionFactory;
		this.clazz = clazz;
	}
	
	protected final SessionFactory getSessionFactory() {
		return this.sessionFactory;
	}


	@SuppressWarnings("unchecked")
	public final List<E> getAll() {
		LOG.debug(this.getClass().getSimpleName() + ".getAll()");
		
		final Session session = this.getSessionFactory().openSession();
    	
		try {
			final Query query = session.createQuery("from " + this.clazz.getName());
	    	final List<E> result = query.list();
	    	return result;
		} finally {
			session.close();
		}
	}
	
	
	public final void saveOrUpdate(final E entity) {
		LOG.debug(this.getClass().getSimpleName() + ".saveOrUpdate()");
		
		final Session session = this.sessionFactory.openSession();
    	Transaction transaction = null;
    	try {
    		transaction = session.beginTransaction();
    		session.saveOrUpdate(entity);
    		transaction.commit();
    	} catch(HibernateException e) {
    		if(transaction != null) {
    			transaction.rollback();
    		}
    		throw e;
    	} finally {
    		session.close();
    	}
	}
	

	public final void delete(final E entity) {
		LOG.debug(this.getClass().getSimpleName() + ".delete()");
		
		final Session session = this.sessionFactory.openSession();
        Transaction transaction = null;
        try {
            transaction = session.beginTransaction();
            session.delete(entity);
            transaction.commit();
        } catch(HibernateException e) {
            if(transaction != null) {
                transaction.rollback();
            }
    		throw e;
        } finally {
            session.close();
        } 
	}
	
}
