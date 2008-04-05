package at.ac.tuwien.e0525580.omov.tools.export;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import at.ac.tuwien.e0525580.omov.bo.Movie;

public class ImportProcessResult {

    private boolean succeeded = false;
    
    private String errorMessage = "";
    
    /** movies which were not imported because movie folderpath is already in use */
    private final List<Movie> skippedMovies = new LinkedList<Movie>();
    
    private final List<Movie> insertedMovies = new LinkedList<Movie>();
    
    
    ImportProcessResult() {
        
    }
    
    void succeeded() {
        this.succeeded = true;
    }
    
    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
    
    void addSkippedMovie(final Movie movie) {
        this.skippedMovies.add(movie);
    }
    
    void setInsertedMovie(final List<Movie> insertedMovies) {
        if(this.insertedMovies == null) throw new NullPointerException("insertedMovies");
        this.insertedMovies.addAll(insertedMovies);
    }
    
    /**
     * @return movies which were not imported because movie folderpath is already in use.
     */
    public List<Movie> getSkippedMovies() {
        return Collections.unmodifiableList(this.skippedMovies);
    }
    public List<Movie> getInsertedMovies() {
        return Collections.unmodifiableList(this.insertedMovies);
    }
    
    public boolean isSucceeded() {
        return this.succeeded;
    }
    
    public String getErrorMessage() {
        return this.errorMessage;
    }
}
