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

import java.io.File;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

import net.sourceforge.omov.core.tools.scan.Scanner;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class FileUtil extends at.ac.tuwien.e0525580.jlib.util.FileUtil {

    private static final Log LOG = LogFactory.getLog(FileUtil.class);

    
    

    private static final Set<String> DOT_PREFIXES;
    static {
        final Set<String> tmp = new TreeSet<String>();
        tmp.add("dr");
        tmp.add("mr");
        DOT_PREFIXES = Collections.unmodifiableSet(tmp);
    }
    
    
    

    /**
     * [Mantis 0000037] Removes additional dots if file is something like "The.Great.Adventure.of.Someone.avi"
     * @param fileWithDots file or folder, does not matter
     * @return given string "The.Great.Adventure.of.Someone.avi" returns "The Great Adventure of Someone.avi"
     * @see Scanner#scanMovieFolderInfo(File)
     * {@link http://omov.sourceforge.net/mantis/view.php?id=37}
     */
    public static String clearFileNameDots(final File fileWithDots) {
        final String nameWithDots = fileWithDots.getName();
        final int cntDots = nameWithDots.split("\\.").length - 1;

        if(cntDots <= (fileWithDots.isFile() ? 1 : 0)) {
            LOG.debug("No dots to clear for "+(fileWithDots.isFile()?"file":"directory")+" '"+fileWithDots.getAbsolutePath()+"'.");
            return nameWithDots;
        }
        
        final String nameToClear;
        final String extensionToUse;
        if(fileWithDots.isFile()) {
            final String extension = nameWithDots.substring(nameWithDots.lastIndexOf(".")+1); // extension can not be null, because dot count is > 1
            nameToClear = nameWithDots.substring(0, nameWithDots.length() - (extension.length() + 1));
            extensionToUse = "." + extension;
        } else {
            nameToClear = nameWithDots;
            extensionToUse = "";
        }
//        System.out.println("nameToClear: '"+nameToClear+"'");
        
        final StringBuilder sb = new StringBuilder();
        int lastDotIdx = -1;
        int dotIdx = nameToClear.indexOf(".");
        
        do {
            final String part = nameToClear.substring(lastDotIdx+1, dotIdx);
//            System.out.println("part ["+part+"]");
            
            sb.append(part);
            if(isDotPrefix(part)) {
                sb.append(". "); // this is a valid dot; append whitespace
            } else {
                sb.append(" "); // replace of dot
            }
            lastDotIdx = dotIdx;
            
        } while( (dotIdx = nameToClear.indexOf(".", dotIdx+1)) != -1);
//        System.out.println("last part ["+nameToClear.substring(lastDotIdx+1)+"]");
        sb.append(nameToClear.substring(lastDotIdx+1)); // add last part
        
        final String result = sb.toString();
        return result.trim() + extensionToUse;
//        return nameToClear.replaceAll("\\.", " ") + "." + extensionToUse;
    }
    
    private static boolean isDotPrefix(String s) {
        final int lastWhitespace = s.lastIndexOf(" ");
        if(lastWhitespace != -1) {
            s = s.substring(lastWhitespace + 1);
        }
        return DOT_PREFIXES.contains(s.toLowerCase());
    }
}

