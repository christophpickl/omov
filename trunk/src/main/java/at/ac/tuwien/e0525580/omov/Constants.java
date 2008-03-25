package at.ac.tuwien.e0525580.omov;

import java.awt.Color;
import java.awt.Dimension;
import java.io.File;

import at.ac.tuwien.e0525580.omov.util.UserSniffer;

public class Constants {

    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 2;
    public static final String VERSION_STRING = VERSION_MAJOR + "." + VERSION_MINOR;
    

    public static final int COVER_IMAGE_WIDTH = 120;
    public static final int COVER_IMAGE_HEIGHT = 160;
    
    public static final Color COLOR_WINDOW_BACKGROUND = new Color(196, 196, 196);
    
    private static File osxApplicationSupportFolderCache = null;

    public static final String OMOV_WEBSITE_URL = "http://omov.sourceforge.net";
    
    
    private Constants() {
        // no instantiation
    }

    public static Dimension getCoverDimension() {
        return new Dimension(COVER_IMAGE_WIDTH, COVER_IMAGE_HEIGHT);
    }
    

    /**
     * @return something like "/Users/John/Library/Application Support/OurMovies v1.0"
     */
    public static File getOsxApplicationSupportFolder() {
        assert(UserSniffer.isMacOSX());
        
        if(osxApplicationSupportFolderCache == null) {
            final File userHome = new File(System.getProperty("user.home"));
            osxApplicationSupportFolderCache = new File(userHome, "Library/Application Support/OurMovies v"+Constants.VERSION_STRING);
        }
        return osxApplicationSupportFolderCache;
    }
}
