package net.sourceforge.omov.core;

import java.awt.Color;
import java.io.File;

import net.sourceforge.omov.core.util.UserSniffer;

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

    /** table foreground for selected rows */
    private static final Color COLOR_SELECTED_FG = Color.WHITE;

    
    private static final Color COLOR_ROW_BACKGROUND_EVEN = Color.WHITE;
    
    private static final Color COLOR_ROW_BACKGROUND_ODD = new Color(241, 245, 250);
    
    private static final String OMOV_WEBSITE_URL = "http://omov.sourceforge.net";

    public static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");
    

    

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
    
    public static Color getColorSelectedForeground() {
        return COLOR_SELECTED_FG;
    }

    public static Color getColorRowBackgroundEven() {
        return COLOR_ROW_BACKGROUND_EVEN;
    }

    public static Color getColorRowBackgroundOdd() {
        return COLOR_ROW_BACKGROUND_ODD;
    }
    
}