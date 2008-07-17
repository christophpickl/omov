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

package net.sourceforge.omov.core.tools;

import java.io.File;

import net.sourceforge.jpotpourri.util.FileUtil;
import net.sourceforge.jpotpourri.util.FileUtilException;
import net.sourceforge.omov.core.PreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class TemporaryFilesCleaner {

    private static final Log LOG = LogFactory.getLog(TemporaryFilesCleaner.class);
    
    private TemporaryFilesCleaner() {
        // no instantiation
    }
    
    public static void clean() {
        final File temporaryFolder = PreferencesDao.getInstance().getTemporaryFolder();
        LOG.info("Cleaning up temporary folder '"+temporaryFolder.getAbsolutePath()+"'.");
        
        final File[] temporaryFiles = temporaryFolder.listFiles();
        for (final File temporaryFile : temporaryFiles) {
            if(temporaryFile.isDirectory()) {
                try {
                    FileUtil.deleteDirectoryRecursive(temporaryFile);
                } catch (FileUtilException e) {
                    LOG.warn("Could not delete temporary folder '"+temporaryFile.getAbsolutePath()+"'!");
                }
            } else { // is file
                if(FileUtil.isHiddenFile(temporaryFile) == true) {
                    LOG.debug("Ignoring hidden file '"+temporaryFile.getName()+"' in temporary folder.");
                } else {
                    LOG.debug("Deleting temporary file '"+temporaryFile.getName()+"'.");
                    if(temporaryFile.delete() == false) {
                        LOG.warn("Could not delete temporary file '"+temporaryFile.getAbsolutePath()+"'!");
                    }
                }
            }
        }
    }
}
