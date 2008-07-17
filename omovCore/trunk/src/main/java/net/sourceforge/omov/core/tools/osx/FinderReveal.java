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

package net.sourceforge.omov.core.tools.osx;

import java.io.File;

import net.sourceforge.jpotpourri.tools.PtUserSniffer;
import net.sourceforge.omov.core.BusinessException;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class FinderReveal {

    private static final Logger LOG = Logger.getLogger(FinderReveal.class);
    
    private static final String SCRIPT =
        "set theFile to \"{0}\"\n" +
        "tell application \"Finder\"\n" +
            "reveal theFile\n" +
            "activate\n" + 
        "end tell";
    
    
    public static void revealFile(final File file) throws BusinessException {
        assert(PtUserSniffer.isMacOSX());
        
        final String osaScript = FinderReveal.createOsaScript(file);
        AppleScriptNativeExecuter.executeAppleScript(osaScript);
    }
    
    private static String createOsaScript(final File file) throws BusinessException {
        final String macFilePath = AppleScriptNativeExecuter.convertUnix2ApplePath(file);
        LOG.debug("Creating osa script for mac file path '"+macFilePath+"'.");
        String script = SCRIPT;
        script = script.replaceAll("\\{0\\}", macFilePath);
        return script;
    }
    
    public static void main(String[] args) throws BusinessException {
//        final String s = "/Volumes/MEGADISK/Movies_Holy/300";
        final String s = "/Users/phudy/Movies/_duplicate/Juno";
        final File f = new File(s);
        revealFile(f);
    }
}
