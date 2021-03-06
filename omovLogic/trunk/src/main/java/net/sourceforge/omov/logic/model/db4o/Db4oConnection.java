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

package net.sourceforge.omov.logic.model.db4o;

import java.io.File;
import java.io.IOException;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.FatalException.FatalReason;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.imodel.IDatabaseConnection;
import net.sourceforge.omov.core.smartfolder.SmartFolder;
import net.sourceforge.omov.logic.prefs.PreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.DatabaseFileLockedException;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.config.Configuration;
import com.db4o.defragment.Defragment;
import com.db4o.defragment.DefragmentConfig;

// http://developer.db4o.com/resources/api/db4o-java/

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oConnection implements IDatabaseConnection {

    private static final Log LOG = LogFactory.getLog(Db4oConnection.class);
    
    private static final String SYSPROPERTY_TEST_RUNNING = "omovTestRunning";
    
    private ObjectContainer connection;
    
    private boolean autoCommit = true;
    
    public boolean isAutoCommit() {
        return this.autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }
    
    private static void defragment(String dbFileName) throws IOException {
    	if(new File(dbFileName).exists() == false) {
    		LOG.info("Skip defragmentation because the DB-file '"+dbFileName+"' does not exist.");
    		return;
    	}
    	LOG.info("Running defragment operation ...");
    	
    	final DefragmentConfig config = new DefragmentConfig(dbFileName);
    	config.forceBackupDelete(true); // delete old backup file
    	
        Defragment.defrag(config);
        
        LOG.info("Defragmentation complete.");
    }

    public Db4oConnection(final String maybeDbFileName) {
        final boolean isRunningJunitTest = System.getProperty(SYSPROPERTY_TEST_RUNNING) != null; // TODO some make better test capability
        
        final String dbFileName;
        if(isRunningJunitTest == false) {
            dbFileName = new File(PreferencesDao.getInstance().getDataFolder(), maybeDbFileName).getAbsolutePath();
        } else {
        	dbFileName = maybeDbFileName;
        }
        LOG.info("Opening database file '"+dbFileName+"'.");
        try {

        	defragment(dbFileName);
        	
        	final Configuration config = Db4o.configure();
        	
            LOG.info("Setting cascade update for class: " + Movie.class.getName());
            config.objectClass(Movie.class).cascadeOnUpdate(true);

            LOG.info("Setting cascade update for class: " + SmartFolder.class.getName());
            config.objectClass(SmartFolder.class).cascadeOnUpdate(true); 

            // would also delete unused Resolution objects in database...
            // but have to make sure, not any other object is referencing the cascading deleted object
//            Db4o.configure().objectClass(Movie.class).cascadeOnDelete(true); 
            
            // deactivate callbacks because not used by omov; increases performance
            config.callbacks(false);
            
            // default is true anyway; detects class changes at startup (added/removed fields)
            // config.detectSchemaChanges(true);
            
            // if not storable (e.g.: object to store references ObjectContainer) -> throw a ObjectNotStorableException instead of silently ignoring
            config.exceptionsOnNotStorable(true);
            
        	this.connection = Db4o.openFile(dbFileName);
        } catch(DatabaseFileLockedException e) {
        	throw new FatalException("Database file '"+dbFileName+"' already in use!", e, FatalReason.DB_LOCKED);
        } catch(Exception e) {
        	throw new FatalException("Could not open db4o file '"+dbFileName+"'!", e);
        }
    }

    public void close() throws BusinessException {
        LOG.info("Closing connection.");
        try {
            if(this.connection.close() == false) {
                throw new BusinessException("Could not close DB connection!");
            }
        } finally {
            this.connection = null;
        }
    }
    
    public ObjectContainer getObjectContainer() {
        return this.connection;
    }

    public boolean isConnected() {
        return this.connection != null;
    }
}
