package at.ac.tuwien.e0525580.omov.model.db4o;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.model.ISmartFolderDao;
import at.ac.tuwien.e0525580.omov.model.ISmartFolderDaoListener;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;

import com.db4o.Db4o;
import com.db4o.ObjectSet;

public class Db4oSmartFolderDao extends AbstractDb4oDao implements ISmartFolderDao {

    private static final Log LOG = LogFactory.getLog(Db4oSmartFolderDao.class);

    private final Set<ISmartFolderDaoListener> listeners = new HashSet<ISmartFolderDaoListener>();
    
    
    public Db4oSmartFolderDao(Db4oConnection connection) {
        super(connection);

        LOG.info("Setting cascade update for class: " + SmartFolder.class.getName());
        Db4o.configure().objectClass(SmartFolder.class.getName()).cascadeOnUpdate(true); 
    }

    private SmartFolder getSmartFolderById(long id) {
        ObjectSet<SmartFolder> os = this.objectContainer.get(this.newPrototypeSmartFolderId(id));
        if(os.hasNext() == false) throw new IllegalArgumentException("id: " + id + " (unkown)");
        SmartFolder folder = os.next();
        if(folder == null) throw new IllegalArgumentException("id: " + id + " (null)");
        if(os.hasNext() == true) throw new IllegalArgumentException("id: " + id + " (more than one)");
        return folder;
    }

    public List<SmartFolder> getAllSmartFolders() throws BusinessException {
        ObjectSet<SmartFolder> os = this.objectContainer.query(SmartFolder.class);
        
        return Collections.unmodifiableList(new ObjectSetTransformer<SmartFolder>().transformList(os));
    }

    public SmartFolder insertSmartFolder(SmartFolder smartFolder) {
        LOG.info("Inserting smartFolder: " + smartFolder);
        final int id = this.getNextSmartFolderId();
        final SmartFolder result = SmartFolder.byOther(id, smartFolder);
        
        this.objectContainer.set(result);
        this.notifyListeners();
        return result;
    }

    public void updateSmartFolder(SmartFolder smartFolder) throws BusinessException {
        LOG.info("updateSmartFolder(smartFolder="+smartFolder+")");
        
        SmartFolder foundSmartFolder = this.getSmartFolderById(smartFolder.getId()); // check for existence
        if(foundSmartFolder == null) {
            LOG.error("Could not update SmartFolder because it could not be found! (smartFolder="+smartFolder+")");
            throw new BusinessException("Could not update movie because it could not be found!");
        }
        this.objectContainer.delete(foundSmartFolder);
        this.objectContainer.set(smartFolder);

        if(this.isAutoCommit() == true) {
            this.commit();
        }
        
        this.notifyListeners();
    }

    public void deleteSmartFolder(SmartFolder smartFolder) throws BusinessException {
        LOG.info("deleting smartfolder: " + smartFolder + " (id="+smartFolder.getId()+")");
        
        SmartFolder found = this.getSmartFolderById(smartFolder.getId());
        this.objectContainer.delete(found);
        
        this.notifyListeners();
    }
    
    
    

    private SmartFolder newPrototypeSmartFolderId(long id) {
        // // id, String name, boolean matchAll, List<AbstractColumnCriterion> criteri
        return new SmartFolder(id, null, false, null);
    }

    
    private int getNextSmartFolderId() {
        ObjectSet<SmartFolderIdentity> os = this.objectContainer.get(SmartFolderIdentity.class);
        
        final int result;
        if(os.hasNext() == false) {
            LOG.info("initializing smartfolder identity.");
            SmartFolderIdentity identity = new SmartFolderIdentity();
            result = identity.getValueAndIncrement();
            this.objectContainer.set(identity);
        } else {
            SmartFolderIdentity identity = os.next();
            result = identity.getValueAndIncrement();
            this.objectContainer.set(identity);
        }
        
        LOG.debug("next smartfolder id: " + result);
        return result;
    }
    
    private static class SmartFolderIdentity {
        private int nextVal = 0;
        public int getValueAndIncrement() {
            return this.nextVal++;
        }
    }

    final void notifyListeners() {
        LOG.debug("Notifying "+this.listeners.size()+" listeners...");
        for (ISmartFolderDaoListener listener : this.listeners) {
            listener.smartFolderDataChanged();
        }
        LOG.debug("Notifying "+this.listeners.size()+" listeners finished.");
    }
    
    public void registerMovieDaoListener(ISmartFolderDaoListener listener) {
//        System.out.println("registering smartfolder listener");
        LOG.debug("Registering smartfolder listener: " + listener.getClass().getName());
        this.listeners.add(listener);
        
    }
    public void unregisterMovieDaoListener(ISmartFolderDaoListener listener) {
//        System.out.println("unregistering smartfolder listener");
        LOG.debug("Unregistering smartfolder listener: " + listener.getClass().getSimpleName());
        this.listeners.remove(listener);
    }
    
}
