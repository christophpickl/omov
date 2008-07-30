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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class VersionMajorMinor {

    private static final Log LOG = LogFactory.getLog(VersionMajorMinor.class);
    
    private final int major;
    
    private final int minor;
    
    private final String versionString;
    
    
    
    public VersionMajorMinor(final int major, final int minor) {
        this.major = major;
        this.minor = minor;
        this.versionString = major + "." + minor;
    }
    
    /**
     * @param versionString something like "0.1"
     */
    public VersionMajorMinor(String versionString) {
        LOG.debug("Creating version instance by string '"+versionString+"'.");
        String parts[] = versionString.split("\\.");
        
        this.major = Integer.parseInt(parts[0]);
        this.minor = Integer.parseInt(parts[1]);
        this.versionString = major + "." + minor;
    }

    
    public int getMajor() {
        return this.major;
    }
    
    public int getMinor() {
        return this.minor;
    }
    
    @Override
    public boolean equals(final Object object) {
        if( (object instanceof VersionMajorMinor) == false) {
            return false;
        }
        final VersionMajorMinor that = (VersionMajorMinor) object;
        return (this.major == that.major && this.minor == that.minor);
    }
    
    @Override
    public int hashCode() {
        return this.major * 3 + this.minor * 17;
    }
    
    /**
     * @return something like "0.6"
     */
    public String getVersionString() {
        return this.versionString;
    }
    
    @Override
    public String toString() {
        return this.versionString;
    }
}