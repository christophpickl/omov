package at.ac.tuwien.e0525580.omov.tools.scan;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.SelectableMovie;

public class ScannedMovie extends SelectableMovie {

    private static final long serialVersionUID = 3374310280498510658L;
    
    // TODO private final boolean metadataFetched; ... oder so halt.

    private ScannedMovie(Movie movie, boolean selected) {
        super(movie, selected);
    }


    public static ScannedMovie newByMovie(Movie movie, boolean selected) {
        return new ScannedMovie(movie, selected);
    }

    public static ScannedMovie updateByMovie(Movie movie, ScannedMovie scanned) {
        return new ScannedMovie(movie, scanned.isSelected());
    }
    
    public static ScannedMovie updateByMetadataMovie(ScannedMovie scanned, Movie metadata) {            
        return new ScannedMovie(Movie.updateByMetadataMovie(scanned, metadata), scanned.isSelected());
    }
}
