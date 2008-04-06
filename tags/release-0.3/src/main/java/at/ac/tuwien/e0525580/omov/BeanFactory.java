package at.ac.tuwien.e0525580.omov;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

import at.ac.tuwien.e0525580.omov.common.VersionMinorMajor;
import at.ac.tuwien.e0525580.omov.model.IDataVersionDao;
import at.ac.tuwien.e0525580.omov.model.IDatabaseConnection;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.model.ISmartFolderDao;

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