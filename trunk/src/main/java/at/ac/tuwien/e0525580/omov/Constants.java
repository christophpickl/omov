package at.ac.tuwien.e0525580.omov;

import java.awt.Color;
import java.awt.Dimension;

public class Constants {

    public static final int VERSION_MAJOR = 0;
    public static final int VERSION_MINOR = 2;
    public static final String VERSION_STRING = VERSION_MAJOR + "." + VERSION_MINOR;
    

    public static final int COVER_IMAGE_WIDTH = 120;
    public static final int COVER_IMAGE_HEIGHT = 160;
    
    public static final Color COLOR_WINDOW_BACKGROUND = new Color(196, 196, 196);
    
    

    
    
    private Constants() {
        // no instantiation
    }

    public static Dimension getCoverDimension() {
        return new Dimension(COVER_IMAGE_WIDTH, COVER_IMAGE_HEIGHT);
    }
    
}
