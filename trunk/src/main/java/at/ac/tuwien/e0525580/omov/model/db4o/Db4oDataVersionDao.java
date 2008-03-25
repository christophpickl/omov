package at.ac.tuwien.e0525580.omov.model.db4o;

import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.model.IDataVersionDao;

import com.db4o.ObjectSet;

public class Db4oDataVersionDao extends AbstractDb4oDao implements IDataVersionDao {

    private static final Log LOG = LogFactory.getLog(Db4oDataVersionDao.class);
    
    
    public Db4oDataVersionDao(Db4oConnection connection) {
        super(connection);
    }
    
    public int getDataVersion() {
        final ObjectSet<DataVersion> os = this.objectContainer.get(DataVersion.class);
        final Set<DataVersion> set = new ObjectSetTransformer<DataVersion>().transformSet(os);
        if(set.size() == 0) {
            LOG.info("No dataversion yet stored; returning -1.");
            return -1;
        } else if(set.size() == 1) {
            return set.iterator().next().getVersion();
        } else {
            throw new IllegalStateException("There are "+set.size()+" dataversion instances stored!");
        }
    }

    public void storeDataVersion(int version) {
        final ObjectSet<DataVersion> os = this.objectContainer.get(DataVersion.class);
        final Set<DataVersion> set = new ObjectSetTransformer<DataVersion>().transformSet(os);
        
        if(set.size() == 0) {
            LOG.debug("Storing initial version of "+version+".");
            this.objectContainer.set(new DataVersion(version));
        } else {
            assert(set.size() == 1);
            DataVersion storedVersion = set.iterator().next();
            LOG.debug("Overwriting old version "+storedVersion.getVersion()+" with new "+version+".");
            storedVersion.setVersion(version);
            this.objectContainer.set(storedVersion);
        }
    }

    private static class DataVersion {
        private int version;
        public DataVersion(final int version) {
            this.version = version;
        }
        public int getVersion() {
            return this.version;
        }
        public void setVersion(final int version) {
            this.version = version;
        }
    }
}
