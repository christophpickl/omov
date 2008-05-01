package net.sourceforge.omov.core;

import net.sourceforge.omov.core.common.VersionMinorMajor;
import net.sourceforge.omov.core.model.IDataVersionDao;
import net.sourceforge.omov.core.model.IDatabaseConnection;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.core.model.ISmartFolderDao;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

public final class BeanFactory {

//    private static final Log LOG = LogFactory.getLog(BeanFactory.class);
    
    private final String springBeans = "beans.xml";
    private static final BeanFactory instance = new BeanFactory();
    private XmlBeanFactory xbf;
    
    private BeanFactory() {
        this.xbf = new XmlBeanFactory(new ClassPathResource(this.springBeans));
    }
    
    public static BeanFactory getInstance() {
        return instance;
    }

    private Object getBean(final String bean) {
        return this.xbf.getBean(bean);
    }

    
    public IDatabaseConnection getDatabaseConnection() {
        return (IDatabaseConnection) getBean("IDatabaseConnection");
    }
    
    public IMovieDao getMovieDao() {
        return (IMovieDao) getBean("IMovieDao");
    }
    
    public ISmartFolderDao getSmartFolderDao() {
        return (ISmartFolderDao) getBean("ISmartFolderDao");
    }
    
    public IDataVersionDao getDataVersionDao() {
        return (IDataVersionDao) getBean("IDataVersionDao");
    }
    
    public VersionMinorMajor getCurrentApplicationVersion() {
        return (VersionMinorMajor) getBean("CurrentApplicationVersion");
    }
    
    @SuppressWarnings("unused")
    public String getSql(String key) {
//        LOG.debug("Getting bean with key '"+key+"'.");
//        return getBean(key).toString();
        throw new UnsupportedOperationException("OR-mapping sucks! use native OO-db instead!");
    }
}
