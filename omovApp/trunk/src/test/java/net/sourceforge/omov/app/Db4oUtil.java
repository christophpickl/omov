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

package net.sourceforge.omov.app;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oUtil {

    private static final Log LOG = LogFactory.getLog(Db4oUtil.class);
    
    public static ObjectContainer getDbConnection(final String dbFileName) {
        final File existingDbFile = new File(dbFileName);
        
        if(existingDbFile.exists()) {
            LOG.debug("Deleting existing DB file at '"+existingDbFile.getAbsolutePath()+"'.");
            if(existingDbFile.delete() == false) {
                throw new RuntimeException("Could not delete DB file '"+existingDbFile.getAbsolutePath()+"'!");
            }
        }
        
        LOG.info("Opening connection to file '"+dbFileName+"'.");
        return Db4o.openFile(dbFileName);
    }
}
