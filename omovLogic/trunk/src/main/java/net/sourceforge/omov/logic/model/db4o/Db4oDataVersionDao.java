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

import java.util.Set;

import net.sourceforge.omov.core.imodel.IDataVersionDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.ObjectSet;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
