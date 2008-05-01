package at.ac.tuwien.e0525580.omov.model;

import java.util.List;
import java.util.Set;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;

public interface IMovieDao extends IDao {
    
    Movie getMovie(long id) throws BusinessException;
    Set<Movie> getMovies() throws BusinessException;
    List<Movie> getMoviesSorted() throws BusinessException;

    List<String> getMovieTitles() throws BusinessException; // distinct
    List<String> getMovieGenres() throws BusinessException; // distinct
    List<String> getMovieStyles() throws BusinessException; // distinct
    List<String> getMovieLanguages() throws BusinessException; // distinct
    List<String> getMovieSubtitles() throws BusinessException; // distinct
    List<String> getMovieDirectors() throws BusinessException; // distinct
    List<String> getMovieActors() throws BusinessException; // distinct
    List<String> getMovieFolderPaths() throws BusinessException; // distinct
    
    
    List<Movie> getMoviesBySmartFolder(SmartFolder smartFolder) throws BusinessException;

    Movie insertMovie(Movie insertMovie) throws BusinessException;
    List<Movie> insertMovies(List<Movie> insertMovies) throws BusinessException;
    void updateMovie(Movie movie) throws BusinessException;
    void deleteMovie(Movie movie) throws BusinessException;
    
    

    // happens within insertMovie/updateMovie
//    void insertLanguage(String language) throws BusinessException;
//    void insertMovie2Language(String language) throws BusinessException;
//    ... and so forth ...
    
    
    
    
    void addMovieDaoListener(IMovieDaoListener listener);
    void unregisterMovieDaoListener(IMovieDaoListener listener);
    
    
//    void setAutocommit(boolean autoCommit) throws BusinessException;
//    void commit() throws BusinessException;
//    void rollback();
//    
//    void close() throws BusinessException;
//    boolean isConnected();
}
