package at.ac.tuwien.e0525580.omov.tools.smartcopy;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;

public class SmartCopyPreprocessResult {

    private static final Log LOG = LogFactory.getLog(SmartCopyPreprocessResult.class);

    private final String idString;
    private final File targetDirectory;
    private List<Integer> ids = null;
    private List<Movie> moviesToCopy = null;
    private long totalCopySizeInKb = 0;
    
    private final List<String> fatalErrors = new LinkedList<String>(); 
    private final List<String> majorErrors = new LinkedList<String>();
    private final List<String> minorErrors = new LinkedList<String>();
    
    
    public SmartCopyPreprocessResult(final String idString, File targetDirectory) {
        this.idString = idString;
        this.targetDirectory = targetDirectory;
    }
    
    void parseIdString() {
        LOG.debug("Parsing id string '"+idString+"'.");
        
        final String input = idString.trim();
        if(input.startsWith("[[") == false) {
            LOG.debug("Invalid id input '"+input+"'! Not starting correctly.");
            this.fatalErrors.add("The ID-request must start with the characters '[['!");
            return;
        }
        if(input.endsWith("]]") == false) {
            LOG.debug("Invalid id input '"+input+"'! Not ending correctly.");
            this.fatalErrors.add("The ID-request must end with the characters ']]'!");
            return;
        }
        final String innerInput = input.substring(2, input.length()-2);
        if(innerInput.trim().length() == 0) {
            LOG.debug("Invalid id input '"+input+"'! No id given.");
            this.fatalErrors.add("The list of IDs is empty!");
            return;
        }
        
        final String[] parts = innerInput.split(",");

        this.ids = new LinkedList<Integer>();
        for (int i=0; i < parts.length; i++) {
            final String part = parts[i].trim();
            try {
                this.ids.add(Integer.parseInt(part));
            } catch(NumberFormatException e) {
                LOG.debug("Invalid id input '"+input+"'! Not starting correctly.");
                this.minorErrors.add("Found an illegal ID '"+part+"'!");
            }
        }
    }
    
    void fetchMovies() {
        LOG.debug("Fetching movies.");
        assert(this.isFatalError() == false && this.ids != null);
        
        final IMovieDao movieDao = BeanFactory.getInstance().getMovieDao();
        this.moviesToCopy = new ArrayList<Movie>(this.ids.size());
        
        for (int id : this.ids) {
            try {
                final Movie movie = movieDao.getMovie(id);
                if(movie == null) {
                    this.majorErrors.add("There is no movie with id '"+id+"' existing!");
                    continue;
                }
                if(movie.isFolderPathSet() == false) {
                    this.minorErrors.add("No folder defined for movie '"+movie.getTitle()+"' (ID="+movie.getId()+").");
                    continue;
                }
                final File movieFolder = new File(movie.getFolderPath());
                if(movieFolder.exists() == false) {
                    this.majorErrors.add("The folder '"+movie.getFolderPath()+"' for movie '"+movie.getTitle()+"' does not exist anymore!");
                    continue;
                }
                final File targetMovieFolder = new File(this.targetDirectory, movieFolder.getName());
                if(targetMovieFolder.exists()) {
                    this.majorErrors.add("The target folder '"+targetMovieFolder.getAbsolutePath()+"' for movie '"+movie.getTitle()+"' already exists!");
                }
                
                LOG.debug("Adding movie to list of movies which should be copied: " + movie);
                this.moviesToCopy.add(movie);
                this.totalCopySizeInKb += movie.getFileSizeKb();
                
            } catch (BusinessException e) {
                LOG.error("Could not get movie by id '"+id+"'!", e);
                this.majorErrors.add("Could not get movie by id '"+id+"'!");
            }
        }
    }

    List<Movie> getMoviesToCopy() {
        return this.moviesToCopy;
    }
    
    public long getTotalCopySizeInKb() {
        return this.totalCopySizeInKb;
    }
    
    public boolean isFatalError() {
        return this.fatalErrors.size() > 0;
    }
    
    public List<String> getFatalErrors() {
        return Collections.unmodifiableList(this.fatalErrors);
    }
    
    public boolean isMajorError() {
        return this.majorErrors.size() > 0;
    }
    
    public List<String> getMajorErrors() {
        return Collections.unmodifiableList(this.majorErrors);
    }
    
    public boolean isMinorError() {
        return this.minorErrors.size() > 0;
    }
    
    public List<String> getMinorErrors() {
        return Collections.unmodifiableList(this.minorErrors);
    }
}
