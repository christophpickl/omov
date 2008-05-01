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

    public int getMovieDataVersion() {
        final DataVersion dataVersion = this.getDataVersion();
        if(dataVersion == null) return -1;
        return dataVersion.getMovieVersion();
    }
    
    public int getSmartfolderDataVersion() {
        final DataVersion dataVersion = this.getDataVersion();
        if(dataVersion == null) return -1;
        return dataVersion.getSmartfolderVersion();
    }
    
    private DataVersion getDataVersion() {
        final ObjectSet<DataVersion> os = this.objectContainer.get(DataVersion.class);
        final Set<DataVersion> set = new ObjectSetTransformer<DataVersion>().transformSet(os);
        if(set.size() == 0) {
            LOG.info("Not any DataVersion instance was yet stored; returning null.");
            return null;
        } else if(set.size() == 1) {
            return set.iterator().next();
        } else {
            throw new IllegalStateException("There are "+set.size()+" dataversion instances stored!");
        }
    }

    public void storeDataVersions(final int movieVersion, final int smartfolderVersion) {
        final ObjectSet<DataVersion> os = this.objectContainer.get(DataVersion.class);
        final Set<DataVersion> set = new ObjectSetTransformer<DataVersion>().transformSet(os);
        
        if(set.size() == 0) {
            LOG.debug("Storing initial movie version "+movieVersion+" and smartfolder verison "+smartfolderVersion+".");
            this.objectContainer.set(new DataVersion(movieVersion, smartfolderVersion));
        } else {
            assert(set.size() == 1);
            
            final DataVersion storedVersion = set.iterator().next();
            LOG.debug("Overwriting old movie version "+storedVersion.getMovieVersion()+" with "+movieVersion+" and old smartfolder version "+storedVersion.getSmartfolderVersion()+" with "+smartfolderVersion+".");
            storedVersion.setMovieVersion(movieVersion);
            storedVersion.setSmartfolderVersion(smartfolderVersion);
            
            this.objectContainer.set(storedVersion);
        }
    }

    private static class DataVersion {
        private int movieVersion = -1;
        private int smartfolderVersion = -1;
        public DataVersion(final int movieVersion, final int smartfolderVersion) {
            this.movieVersion = movieVersion;
            this.smartfolderVersion = smartfolderVersion;
        }
        public int getMovieVersion() {
            return this.movieVersion;
        }
        public void setMovieVersion(int movieVersion) {
            this.movieVersion = movieVersion;
        }
        public int getSmartfolderVersion() {
            return this.smartfolderVersion;
        }
        public void setSmartfolderVersion(int smartfolderVersion) {
            this.smartfolderVersion = smartfolderVersion;
        }
    }
}
