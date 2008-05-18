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

import java.awt.Dimension;
import java.util.List;

import net.sourceforge.omov.core.util.CollectionUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public enum CoverFileType {
    
    THUMBNAIL(40, 40), // use in HTML report
    NORMAL(120, 160);

    
    
    private final int maxWidth;
    
    private final int maxHeight;
    
    private final String filenamePart;
    
    private final String toString;
    
    
    private CoverFileType(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        
        this.filenamePart = "-" + maxWidth + "x" + maxHeight;
        this.toString = this.name() + "[maxWidth="+maxWidth+";maxHeight="+maxHeight+"]";
    }
    
    public int getMaxHeight() {
        return this.maxHeight;
    }
    
    public int getMaxWidth() {
        return this.maxWidth;
    }
    
    public Dimension getDimension() {
        return new Dimension(this.maxWidth, this.maxHeight);
    }
    
    String getFilenamePart() {
        return this.filenamePart;
    }
    
    public String toString() {
        return toString;
    }

    public static List<CoverFileType> getAllTypes() {
        return new CollectionUtil<CoverFileType>().asImmutableList(THUMBNAIL, NORMAL);
    }
}
