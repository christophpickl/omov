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

package net.sourceforge.omov.core.bo;

import java.util.List;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieFolderInfo {
    
    private final String folderPath;
    
    /** filename (with extension) relative to folderPath */
    private final List<String> files;
    
    private final long fileSizeKB;
    
    private final String format;
    
    public MovieFolderInfo(final String folderPath, final List<String> files, final long fileSizeKb, final String format) {
        // assert files are all valid movieFiles
        this.folderPath = folderPath;
        this.files = files;
        this.fileSizeKB = fileSizeKb;
        this.format = format;
    }
    
    public List<String> getFiles() {
        return this.files;
    }
    
    public String getFormat() {
        return this.format;
    }
    
    public String getFolderPath() {
        return this.folderPath;
    }
    
    public long getFileSizeKB() {
        return this.fileSizeKB;
    }
}
