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

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class StringUtil {

//    private static final Log LOG = LogFactory.getLog(StringUtil.class);
    
    private StringUtil() {
        // no instantiation
    }
    
    public static String asString(File... files) {
        if(files.length == 0) return "[]";
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (File file : files) {
            if(!first) sb.append(", ");
            sb.append(file.getAbsolutePath());
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String escapeLineFeeds(String withLineFeeds) {
        return withLineFeeds.replaceAll("\n", "\\\\n");
    }
    
    public static String enforceMaxWidth(String text, int maxWidth) {
    	if(text.length() <= maxWidth) {
    		return text;
        }
    	
    	return text.substring(0, maxWidth) + "...";
    }
    
//    private static final NeedlemanWunch NEEDLEMAN_WUNCH = new NeedlemanWunch();
//    /**
//     * @see StringSimilarity#needleman()
//     */
//    public static float similarity(String s1, String s2) {
//        final float similarity = NEEDLEMAN_WUNCH.getSimilarity(s1, s2);
//        LOG.debug("returning similarity of '"+similarity+"' for string s1 '"+s1+"' and s2 '"+s2+"'.");
//        return similarity;
//    }
    

    /**
     * @author aTunes team
     */
	public static String getString(Object... strings) {
		StringBuilder objStringBuilder = new StringBuilder();

		for (Object element : strings)
			objStringBuilder.append(element);

		return objStringBuilder.toString();
	}
}
