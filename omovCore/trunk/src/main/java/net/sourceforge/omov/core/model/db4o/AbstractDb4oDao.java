package net.sourceforge.omov.core.model.db4o;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.ObjectContainer;

public class AbstractDb4oDao {

    private static final Log LOG = LogFactory.getLog(AbstractDb4oDao.class);
    
    final ObjectContainer objectContainer;
    
    private final Db4oConnection connection;
    
    
    public AbstractDb4oDao(Db4oConnection connection) {
        this.connection = connection;
        this.objectContainer = connection.getObjectContainer();
    }

    public void commit() {
        LOG.debug("Transaction commit.");
        this.objectContainer.commit();
    }

    public void rollback() {
        LOG.debug("Transaction rollback.");
        this.objectContainer.rollback();
    }
    
    public boolean isAutoCommit() {
        return this.connection.isAutoCommit();
    }
    
    public void setAutoCommit(boolean autoCommit) {
        LOG.debug("Setting auto commit to "+autoCommit+".");
        this.connection.setAutoCommit(autoCommit);
    }

    
//    static final class StringWrapperTransformer<T extends StringWrapper> {
//        public List<String> transformList(ObjectSet<T> os) {
//            final List<String> result = new LinkedList<String>();
//            while(os.hasNext()) {
//                result.add(os.next().getString());
//            }
//            return Collections.unmodifiableList(result);
//        }
//    }
//
//    
//    static interface StringWrapper {
//        String getString();
//    }
}
