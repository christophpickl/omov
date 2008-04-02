package at.ac.tuwien.e0525580.omov;

import java.awt.Color;
import java.io.File;

import at.ac.tuwien.e0525580.omov.util.UserSniffer;

public class Constants {

    public static final Color COLOR_WINDOW_BACKGROUND = new Color(196, 196, 196);

    public static final String OMOV_WEBSITE_URL = "http://omov.sourceforge.net";
    
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
            osxApplicationSupportFolderCache = new File(userHome, "Library/Application Support/OurMovies"); // TODO not seperating between here means they might use the same files (e.g.: coverfolder/db-file will not be deleted) ... hm... :(
        }
        return osxApplicationSupportFolderCache;
    }
}
