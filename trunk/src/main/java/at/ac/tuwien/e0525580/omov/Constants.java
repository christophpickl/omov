package at.ac.tuwien.e0525580.omov;

import java.awt.Color;
import java.io.File;

import at.ac.tuwien.e0525580.omov.util.UserSniffer;

public class Constants {

    /** default window background */
    private static final Color COLOR_WINDOW_BACKGROUND = new Color(196, 196, 196);
    
    /** misc purpose (eg used in doubletten dialog as row background*/
    private static final Color COLOR_LIGHT_GRAY = new Color(192, 192, 192);

    /** table background for selected rows */
    private static final Color COLOR_SELECTED_BG = new Color(61, 128, 223);

    /** table foreground for selected rows */
    private static final Color COLOR_SELECTED_FG = Color.WHITE;

    
    public static final String OMOV_WEBSITE_URL = "http://omov.sourceforge.net";

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
    
    public static Color getColorWindowBackground() {
        return COLOR_WINDOW_BACKGROUND;
    }
    
    public static Color getColorLightGrad() {
        return COLOR_LIGHT_GRAY;
    }
    
    public static Color getColorSelectedBackground() {
        return COLOR_SELECTED_BG;
    }
    
    public static Color getColorSelectedForeground() {
        return COLOR_SELECTED_FG;
    }
    
    // ... do same with other public attributes 
}
