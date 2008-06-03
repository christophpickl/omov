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

import java.awt.Color;
import java.awt.Font;
import java.io.File;

import net.sourceforge.omov.core.util.UserSniffer;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Constants {

    /** default window background */
    private static final Color COLOR_WINDOW_BACKGROUND = new Color(196, 196, 196);
    
    /** misc purpose (eg used in doubletten dialog as row background*/
    private static final Color COLOR_LIGHT_GRAY = new Color(192, 192, 192);
    
//    /**  */
//    private static final Color COLOR_VERY_LIGHT_GRAY = new Color(230, 230, 230);
    
    private static final Color COLOR_DARK_GRAY = new Color(100, 100, 100);

    /** table background for selected rows */
    private static final Color COLOR_SELECTED_BG = new Color(61, 128, 223);

    /** table background for selected rows without focus */
    private static final Color COLOR_SELECTED_NOFOCUS_BG = new Color(212, 212, 212);

    /** table foreground for selected rows */
    private static final Color COLOR_SELECTED_FG = Color.WHITE;

    
    private static final Color COLOR_ROW_BACKGROUND_EVEN = Color.WHITE;
    
    private static final Color COLOR_ROW_BACKGROUND_ODD = new Color(241, 245, 250);
    
    private static final String OMOV_WEBSITE_URL = "http://omov.sourceforge.net";

    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    private static final Font FONT_HEADER_1 = new Font(null, Font.BOLD, 14);
    private static final Font FONT_LABELED_COMPONENT = new Font("sans", Font.BOLD, 10);
    private static final Font FONT_SMALL = new Font(null, Font.PLAIN, 10);
    
    

    private static File osxApplicationSupportFolderCache = null;
    
    
    private Constants() {
        // no instantiation
    }

    /**
     * @return something like "/Users/John/Library/Application Support/OurMovies v1.0"
     */
    public static File getOsxApplicationSupportFolder() {
        assert(UserSniffer.isMacOSX());
        
        if(osxApplicationSupportFolderCache == null) {
            final File userHome = new File(System.getProperty("user.home"));
            // not seperating between different versions, means that they will use the same files (e.g.: coverfolder/db-file will not be deleted) ... hm... :(
            osxApplicationSupportFolderCache = new File(userHome, "Library/Application Support/OurMovies"); 
        }
        return osxApplicationSupportFolderCache;
    }
    
    public static String getWebUrl() {
        return OMOV_WEBSITE_URL;
    }
    
    public static Color getColorWindowBackground() {
        return COLOR_WINDOW_BACKGROUND;
    }
    
    public static Color getColorLightGray() {
        return COLOR_LIGHT_GRAY;
    }
    
//    public static Color getColorVeryLightGray() {
//        return COLOR_VERY_LIGHT_GRAY;
//    }
    
    public static Color getColorDarkGray() {
        return COLOR_DARK_GRAY;
    }
    
    public static Color getColorSelectedBackground() {
        return COLOR_SELECTED_BG;
    }
    
    public static Color getColorSelectedBackgroundNoFocus() {
        return COLOR_SELECTED_NOFOCUS_BG;
    }
    
    public static Color getColorSelectedForeground() {
        return COLOR_SELECTED_FG;
    }

    public static Color getColorRowBackgroundEven() {
        return COLOR_ROW_BACKGROUND_EVEN;
    }

    public static Color getColorRowBackgroundOdd() {
        return COLOR_ROW_BACKGROUND_ODD;
    }


    public static Font getFontHeader1() {
    	return FONT_HEADER_1;
    }

    public static Font getFontLabeledComponent() {
    	return FONT_LABELED_COMPONENT;
    }

    public static Font getFontSmall() {
    	return FONT_SMALL;
    }
    
}
