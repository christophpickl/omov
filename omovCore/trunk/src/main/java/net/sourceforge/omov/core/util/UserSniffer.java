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

package net.sourceforge.omov.core.util;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class UserSniffer {

    /** class' own logger using log4j */
    private static final Logger LOG = Logger.getLogger(UserSniffer.class);

    /**  */
    public enum OS { WIN, MAC, LINUX, UNIX, SOLARIS, OS2, UNKOWN }

    /**  */
    private static OS os;

    static {
        final String osname = System.getProperty("os.name");

        if (osname.contains("Mac")) {
            UserSniffer.os = UserSniffer.OS.MAC;
        } else if (osname.contains("Windows")) {
            UserSniffer.os = UserSniffer.OS.WIN;
        } else if (osname.contains("Linux")) {
            UserSniffer.os = UserSniffer.OS.LINUX;
        } else if (osname.contains("Unix") || osname.contains("UNIX")  || osname.contains("HP-UX")
                || osname.contains("AIX") || osname.contains("BSD") || osname.contains("Irix")) {
            UserSniffer.os = UserSniffer.OS.UNIX;
        } else if (osname.contains("SunOS") || osname.contains("Solaris")) {
            UserSniffer.os = UserSniffer.OS.SOLARIS;
        } else if (osname.contains("OS/2")) {
            UserSniffer.os = UserSniffer.OS.OS2;
        } else {
            UserSniffer.LOG.warn("could not determine operating system by name [" + osname + "]");
            UserSniffer.os = UserSniffer.OS.UNKOWN;
        }
        LOG.debug("seems as user is running '" + UserSniffer.os + "'.");
    }

    
    
    private UserSniffer() {
    	// no instantiation
    }
    
    /**
     *
     * @return
     */
    public static OS getOS() {
        return UserSniffer.os;
    }

    /**
     *
     * @param os
     * @return
     */
    public static boolean isOS(final OS isOs) {
        return UserSniffer.os.equals(isOs);
    }

    public static boolean isMacOSX() {
        return UserSniffer.os.equals(OS.MAC);
    }

    public static boolean isWindows() {
        return UserSniffer.os.equals(OS.WIN);
    }

}
