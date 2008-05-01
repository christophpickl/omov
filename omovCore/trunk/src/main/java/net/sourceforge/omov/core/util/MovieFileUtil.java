package net.sourceforge.omov.core.util;

import java.io.File;
import java.util.Set;

public class MovieFileUtil {

    private static final Set<String> KNOWN_MOVIE_FILE_EXTENSIONS = CollectionUtil.immutableSet(
            "mpg", "mpeg", "mp4", "avi", "ogm", "mkv", "divx", "wmv", "flv", "mov",
            "bin", "cue", 
            "mdf", "mds", 
            "bup", "ifo", "vob"); // DVDs
    
    public static boolean isMovieFileExtension(String extension) {
        return KNOWN_MOVIE_FILE_EXTENSIONS.contains(extension);
    }

    public static boolean isMovieFile(File file) {
        return MovieFileUtil.isMovieFileExtension(FileUtil.extractExtension(file));
    }
    
}
