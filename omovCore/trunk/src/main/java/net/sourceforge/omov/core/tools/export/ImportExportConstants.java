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

package net.sourceforge.omov.core.tools.export;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.sourceforge.omov.core.util.FileUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public interface ImportExportConstants {

    public static final String BACKUP_FILE_EXTENSION = "omo";
    
    public static final FileFilter BACKUP_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(final File file) {
            if(file.isDirectory() == true) return true;
            
            final String extension = FileUtil.extractExtension(file);
            if(extension == null) return false;
            
            return (extension.equalsIgnoreCase(ImportExportConstants.BACKUP_FILE_EXTENSION));
        }
        @Override
        public String getDescription() {
            return "*." + ImportExportConstants.BACKUP_FILE_EXTENSION;
        }
    };
    
    static final String FILE_DATA_VERSION = "movie_data.version";
    
    static final String FILE_MOVIES_XML = "movies.xml";
    
    static final String FOLDER_COVERS = "covers";
    
}
