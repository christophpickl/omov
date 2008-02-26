package at.ac.tuwien.e0525580.omov2.model.db4o;

import com.db4o.ObjectContainer;

public class AbstractDb4oDao {
    
    final ObjectContainer objectContainer;
    private final Db4oConnection connection;
    
    public AbstractDb4oDao(Db4oConnection connection) {
        this.connection = connection;
        this.objectContainer = connection.getObjectContainer();
    }

    public void commit() {
        this.objectContainer.commit();
    }

    public void rollback() {
        this.objectContainer.rollback();
    }
    
    public boolean isAutoCommit() {
        return this.connection.isAutoCommit();
    }
    
    public void setAutoCommit(boolean autoCommit) {
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
