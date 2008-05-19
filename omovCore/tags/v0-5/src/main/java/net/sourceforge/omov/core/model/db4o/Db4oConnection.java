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

import java.io.File;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.FatalException.FatalReason;
import net.sourceforge.omov.core.model.IDatabaseConnection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.DatabaseFileLockedException;
import com.db4o.Db4o;
import com.db4o.ObjectContainer;

// http://developer.db4o.com/resources/api/db4o-java/

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oConnection implements IDatabaseConnection {

    private static final Log LOG = LogFactory.getLog(Db4oConnection.class);
    private ObjectContainer connection;
    
    private boolean autoCommit = true;
    
    public boolean isAutoCommit() {
        return this.autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Db4oConnection(String dbFileName) {
        final boolean isRunningJunitTest = System.getProperty("omovTestRunning") != null;
        
        if(isRunningJunitTest == false) {
            dbFileName = new File(PreferencesDao.getInstance().getDataFolder(), dbFileName).getAbsolutePath();
        }
        
        LOG.info("Opening database file '"+dbFileName+"'.");
        try {
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
