package net.sourceforge.omov.core.tools.scan;

import java.io.File;
import java.util.HashSet;

import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.SelectableMovie;

public class ScannedMovie extends SelectableMovie {

    private static final long serialVersionUID = 3374310280498510658L;
    
    // MINOR scanner: add boolean:metadataFetched indicating that this scanned movie got fetched metadata? -> is this even useful?

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
    
    public static ScannedMovie clearMetadataMovie(ScannedMovie scanned) {
        final String folderName = new File(scanned.getFolderPath()).getName();
        
        final Movie metadataCleared = Movie.create(scanned.getId())
            .actors(new HashSet<String>()).comment("").coverFile("").director("").duration(0).genres(new HashSet<String>()).title(folderName).year(0)
            .get();
        return new ScannedMovie(Movie.updateByMetadataMovie(scanned, metadataCleared), scanned.isSelected());
    }
}
