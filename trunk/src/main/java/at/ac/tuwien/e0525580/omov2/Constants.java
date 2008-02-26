package at.ac.tuwien.e0525580.omov2;

import java.awt.Dimension;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import at.ac.tuwien.e0525580.omov2.util.CollectionUtil;
import at.ac.tuwien.e0525580.omov2.util.FileUtil;

public class Constants {


    public static final int COVER_IMAGE_WIDTH = 120;
    public static final int COVER_IMAGE_HEIGHT = 160;
    
    
    
    private static final Set<String> HIDDEN_FILE_NAMES = CollectionUtil.immutableSet(
            // apple system files
            ".DS_Store", "Icon\r",
            // windows system files
            "Thumbs.db");
    
    private static final Set<String> KNOWN_MOVIE_FILE_EXTENSIONS = CollectionUtil.immutableSet(
            "mpg", "mpeg", "mp4", "avi", "ogm", "mkv", "divx", "wmv", "flv",
            "bin", "cue", 
            "mdf", "mds", 
            "bup, ifo, vob"); // von DVDs

    
    
    private Constants() {
        // no instantiation
    }

    public static Dimension getCoverDimension() {
        return new Dimension(COVER_IMAGE_WIDTH, COVER_IMAGE_HEIGHT);
    }
    
    
    public static boolean isHiddenFile(File file) {
        return HIDDEN_FILE_NAMES.contains(file.getName());
    }
    
    public static boolean isMovieFile(File file) {
        return Constants.isMovieFileExtension(FileUtil.extractExtension(file));
    }
    
    public static boolean isMovieFileExtension(String extension) {
        return KNOWN_MOVIE_FILE_EXTENSIONS.contains(extension);
    }
    
    private static final Map<Integer, String> QUALITY_MAPPING;
    static {
        Map<Integer, String> tmp = new HashMap<Integer, String>();
        tmp.put(0, "Unrated");
        tmp.put(1, "Ugly");
        tmp.put(2, "Normal");
        tmp.put(3, "Good");
        tmp.put(4, "DVD best");
        QUALITY_MAPPING = Collections.unmodifiableMap(tmp);
    }
    
    public static String mapQuality(int i) {
        if(i < 0 || i > 4) throw new IllegalArgumentException("quality must be between 0-4 but was: " + i);
        return QUALITY_MAPPING.get(i);
    }
}
