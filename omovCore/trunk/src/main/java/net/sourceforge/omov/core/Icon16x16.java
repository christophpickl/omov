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

package net.sourceforge.omov.core;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public enum Icon16x16 {

    NEW_MOVIE("new_movie.png"),
    HELP("help.png"),
    VLC("vlc.png"),
    QUICKVIEW("quickview.png"),
    PREFERENCES("preferences.png"),
    INFORMATION("information.png"),
    SCAN("scan.png"),
    DELETE("delete.png"),
    FETCH_METADATA("fetch_metadata.png"),
    IMPORT("import.png"),
    EXPORT("export.png"),
    REVEAL_FINDER("reveal_finder.png"),
    SEVERITY_INFO("severity_info.gif"),
    SEVERITY_WARNING("severity_warning.gif"),
    SEVERITY_ERROR("severity_error.gif");
    
//    WEB("web_globe.png");
    
    private final String fileName;
    
    private Icon16x16(String fileName) {
        this.fileName = fileName;
    }
    
    public String getFileName() {
    	return this.fileName;
    }
}
