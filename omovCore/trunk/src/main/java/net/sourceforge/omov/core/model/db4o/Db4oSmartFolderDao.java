/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.core.model.db4o;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.model.ISmartFolderDao;
import net.sourceforge.omov.core.model.ISmartFolderDaoListener;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.ObjectSet;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oSmartFolderDao extends AbstractDb4oDao implements ISmartFolderDao {

    private static final Log LOG = LogFactory.getLog(Db4oSmartFolderDao.class);

    private final Set<ISmartFolderDaoListener> listeners = new HashSet<ISmartFolderDaoListener>();
    
    
    public Db4oSmartFolderDao(Db4oConnection connection) {
        super(connection);
    }

    private SmartFolder getSmartFolderById(long id) {
        final ObjectSet<SmartFolder> os = this.objectContainer.get(this.newPrototypeSmartFolderId(id));
        if(os.hasNext() == false) throw new IllegalArgumentException("id: " + id + " (unkown)");
        
        final SmartFolder folder = os.next();
        
        if(folder == null) throw new IllegalArgumentException("id: " + id + " (null)");
        if(os.hasNext() == true) throw new IllegalArgumentException("id: " + id + " (existing instances with this id "+os.size()+")");
        
        return folder;
    }

    public List<SmartFolder> getAllSmartFolders() throws BusinessException {
        ObjectSet<SmartFolder> os = this.objectContainer.query(SmartFolder.class);
        
        return Collections.unmodifiableList(new ObjectSetTransformer<SmartFolder>().transformList(os));
    }

    public List<SmartFolder> getAllSmartFoldersSorted() throws BusinessException {
        final ObjectSet<SmartFolder> os = this.objectContainer.query(SmartFolder.class);
        final List<SmartFolder> list = new ObjectSetTransformer<SmartFolder>().transformMutableList(os);
        Collections.sort(list, SmartFolder.COMPARATOR_NAME);
        return Collections.unmodifiableList(list);
    }

    public SmartFolder insertSmartFolder(SmartFolder smartFolder) {
        final int id = this.getNextSmartFolderId();
        final SmartFolder result = SmartFolder.byOther(id, smartFolder);
        
        LOG.info("Inserting smartFolder: " + result);
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
        return new SmartFolder(id);
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
        private int nextVal = 1;
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
