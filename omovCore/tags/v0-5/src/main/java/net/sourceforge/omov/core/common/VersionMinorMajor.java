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

package net.sourceforge.omov.core.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class VersionMinorMajor {

    private static final Log LOG = LogFactory.getLog(VersionMinorMajor.class);
    
    private final int minor;
    
    private final int major;
    
    private final String versionString;
    
    
    
    public VersionMinorMajor(final int minor, final int major) {
        this.minor = minor;
        this.major = major;
        this.versionString = minor + "." + major;
    }
    
    /**
     * @param versionString something like "0.1"
     */
    public VersionMinorMajor(String versionString) {
        LOG.debug("Creating version instance by string '"+versionString+"'.");
        String parts[] = versionString.split("\\.");
        
        this.minor = Integer.parseInt(parts[0]);
        this.major = Integer.parseInt(parts[1]);
        this.versionString = minor + "." + major;
    }
    
    
    public int getMajor() {
        return this.major;
    }
    
    public int getMinor() {
        return this.minor;
    }
    
    @Override
    public boolean equals(final Object object) {
        if( (object instanceof VersionMinorMajor) == false) {
            return false;
        }
        final VersionMinorMajor that = (VersionMinorMajor) object;
        return (this.minor == that.minor && this.major == that.major);
    }
    
    @Override
    public int hashCode() {
        return this.minor * 3 + this.major * 17;
    }
    
    public String getVersionString() {
        return this.versionString;
    }
    
    @Override
    public String toString() {
        return this.versionString;
    }
}