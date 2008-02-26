package at.ac.tuwien.e0525580.omov2.bo.movie;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;



public class VersionedMovies implements Serializable {
    
    private static final long serialVersionUID = 4552162987116801481L;

    private final List<Movie> movies;
    
    private final int dataVersion = Movie.DATA_VERSION;
    
    public VersionedMovies(List<Movie> movies) {
        this.movies = Collections.unmodifiableList(movies);
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VersionedMovies[length="+movies.size() + "]");
        for (Movie movie : this.movies) {
            sb.append("\n  ").append(movie);
        }
        return sb.toString();
    }
    
    public List<Movie> getMovies() {
        return this.movies;
    }
    
    public int getDataVersion() {
        return this.dataVersion;
    }
}
