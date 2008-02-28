package at.ac.tuwien.e0525580.omov.model.db4o;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.model.IMovieDaoListener;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;

import com.db4o.Db4o;
import com.db4o.ObjectSet;
import com.db4o.query.Predicate;
import com.db4o.query.Query;

/*
// delete all!
ObjectSet result=db.get(new Movie(null,0)); 
while(result.hasNext()) { 
    db.delete(result.next()); 
} 


constraint("").orderAscending(); 


 */
public class Db4oMovieDao extends AbstractDb4oDao implements IMovieDao {

    private static final Log LOG = LogFactory.getLog(Db4oMovieDao.class);

    private final Set<IMovieDaoListener> listeners = new HashSet<IMovieDaoListener>();
    
    public Db4oMovieDao(Db4oConnection connection) {
        super(connection);
        
        LOG.info("Setting cascade update for class: " + Movie.class.getName());
        Db4o.configure().objectClass(Movie.class.getName()).cascadeOnUpdate(true); 
    }
    
    


    public Set<Movie> getMovies() throws BusinessException {
        final ObjectSet<Movie> os = this.objectContainer.get(Movie.class);
        LOG.debug("getMovies() is returning " + os.size() + " movies");
        return new ObjectSetTransformer<Movie>().transformSet(os);
    }

    public List<Movie> getMoviesSorted() throws BusinessException {
        final List<Movie> list = new ArrayList<Movie>(this.getMovies());
        Collections.sort(list, Movie.COMPARATOR);
        return list;
    }

    @SuppressWarnings("serial")
    public Movie getMovie(final int id) throws BusinessException {
//        ObjectSet<Movie> os = this.connection.get(newPrototypeMovieId(id));
        ObjectSet<Movie> os = this.objectContainer.query(new Predicate<Movie>() {
            public boolean match(Movie movie) {
                return movie.getId() == id;
        }});
        if(os.hasNext() == false) {
            LOG.info("Could not find movie by given id "+id+"!");
            return null;
        }
        
        Movie result = os.next();
        if(os.hasNext() == true) throw new RuntimeException("Duplicate movies found by id '"+id+"'!");
        LOG.info("Found movie by id '"+id+"': " + result);
        return result;
    }

    public List<String> getMovieLanguages() throws BusinessException {
        LOG.info("Getting languages...");
        final List<String> result = this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getLanguages();
            }});
        LOG.debug("Got "+result.size()+" languages.");
        return result;
    }

    public List<String> getMovieSubtitles() throws BusinessException {
        LOG.info("Getting subtitles...");
        final List<String> result = this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getSubtitles();
            }});
        LOG.debug("Got "+result.size()+" subtitles.");
        return result;
    }

    public List<String> getMovieDirectors() throws BusinessException {
        LOG.info("Getting directors...");
        final List<String> result = this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getDirector();
            }});
        LOG.debug("Got "+result.size()+" directors.");
        return result;
    }

    public List<String> getMovieActors() throws BusinessException {
        LOG.info("Getting actors...");
        final List<String> result = this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getActors();
            }});
        LOG.debug("Got "+result.size()+" directors.");
        return result;
    }

    public List<String> getMovieGenres() throws BusinessException {
        LOG.info("Getting genres...");
        return this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getGenres();
            }});
    }
    
    public List<String> getMovieTitles() throws BusinessException {
        LOG.info("Getting titles...");
        return this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getTitle();
            }});
    }

    public List<String> getMovieStyles() throws BusinessException {
        LOG.info("Getting styles...");
        return this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getStyle();
            }});
    }

    public List<String> getMovieFolderPaths() throws BusinessException {
        LOG.info("Getting folderpaths...");
        return this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getFolderPath();
            }});
    }
    


    public Movie insertMovie(Movie insertMovie) throws BusinessException {
        LOG.info("Inserting movie: " + insertMovie);
        
        final int id = this.getNextMovieId();
        final Movie result = Movie.newByOtherMovieSetDateAdded(id, new Date(), insertMovie);
        
        this.objectContainer.set(result);

        if(this.isAutoCommit() == true) {
            this.commit();
        }
        
        this.notifyListeners();
        return result;
    }



    public List<Movie> insertMovies(List<Movie> insertMovies) throws BusinessException {
        this.setAutoCommit(false);
        boolean commited = false;
        final List<Movie> result = new ArrayList<Movie>(insertMovies.size());
        try {
            for (Movie insertMovie : insertMovies) {
                result.add(this.insertMovie(insertMovie));
            }
            this.commit();
            commited = true;
        } finally {
            if(commited == false) this.rollback();
            this.setAutoCommit(true);
        }
        return result;
    }


    public void updateMovie(Movie movie) throws BusinessException {
        LOG.info("updateMovie(movie="+movie+")");
        
        Movie foundMovie = this.getMovie(movie.getId()); // check for existence
        if(foundMovie == null) {
            LOG.error("Could not update movie because it could not be found! (Movie="+movie+")");
            throw new BusinessException("Could not update movie because it could not be found!");
        }
        this.objectContainer.delete(foundMovie);
        this.objectContainer.set(movie);

        if(this.isAutoCommit() == true) {
            this.commit();
        }
        
        this.notifyListeners();
    }
    

    
    public void deleteMovie(Movie deletingMovie) throws BusinessException {
        LOG.info("deleteMovie("+deletingMovie+")");
        
        final Movie found = this.getMovie(deletingMovie.getId());
        this.objectContainer.delete(found);
        
        if(this.isAutoCommit() == true) {
            this.commit();
        }
        
        this.notifyListeners();
    }
    
    
    /*
List <Cat> cats = db.query(new Predicate<Cat>() {
   public boolean match(Cat cat) {
      return cat.getName().equals("Occam");
   }
});
     */
    



    @SuppressWarnings("unchecked")
    public List<Movie> getMoviesBySmartFolder(SmartFolder smartFolder) throws BusinessException {
        LOG.info("getting movies by smartFolder: " + smartFolder);
        final Query query = this.objectContainer.query();
        query.constrain(Movie.class);
        
        smartFolder.pepareQuery(query);
        
        ObjectSet<Movie> os = query.execute();
        return new ObjectSetTransformer<Movie>().transformList(os);
    }

    

    final void notifyListeners() {
        LOG.debug("Notifying "+this.listeners.size()+" listeners...");
        for (IMovieDaoListener listener : this.listeners) {
            listener.movieDataChanged();
        }
        LOG.debug("Notifying "+this.listeners.size()+" listeners finished.");
    }
    
    public void registerMovieDaoListener(IMovieDaoListener listener) {
        LOG.debug("Registering movie listener: " + listener);
        this.listeners.add(listener);

        LOG.debug("There are "+this.listeners.size()+" registered listeners after registering.");
    }
    
    
    public void unregisterMovieDaoListener(IMovieDaoListener listener) {
        LOG.debug("Unregistering movie listener: " + listener);
        boolean removed = this.listeners.remove(listener);
        if(removed == false) {
            LOG.error("!!!!!! listener '"+listener+"' was not removed !!!!!!");
        }

        LOG.debug("There are "+this.listeners.size()+" registered listeners after unregistering.");
    }
    
    
    

//    private Movie newPrototypeMovieId(int id) {
//        return new Movie(id, null, null, null);
//    }

    private static interface MovieStringExtractor {
        String getString(Movie movie);
    }
    private static interface MovieStringsExtractor {
        Collection<String> getStrings(Movie movie);
    }
    

    private List<String> getMovieString(MovieStringExtractor extractor) throws BusinessException {
        final Set<String> strings = new HashSet<String>();
        for (Movie movie : this.getMovies()) {
            String s = extractor.getString(movie);
            if(s != null) strings.add(s);
        }
        
        final List<String> result = new ArrayList<String>(strings.size());
        result.addAll(strings);
        Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
        return Collections.unmodifiableList(result);
    }
    
    private List<String> getMovieStrings(MovieStringsExtractor extractor) throws BusinessException {
        final Set<String> strings = new HashSet<String>();
        for (Movie movie : this.getMovies()) {
            strings.addAll(extractor.getStrings(movie));
        }
        
        final List<String> result = new ArrayList<String>(strings.size());
        result.addAll(strings);
        Collections.sort(result, String.CASE_INSENSITIVE_ORDER);
        return Collections.unmodifiableList(result);
    }
    
    private int getNextMovieId() {
        ObjectSet<MovieIdentity> os = this.objectContainer.get(MovieIdentity.class);
        
        final int result;
        if(os.hasNext() == false) {
            LOG.info("initializing movie identity.");
            MovieIdentity identity = new MovieIdentity();
            result = identity.getValueAndIncrement();
            this.objectContainer.set(identity);
        } else {
            MovieIdentity identity = os.next();
            result = identity.getValueAndIncrement();
            this.objectContainer.set(identity);
        }
        
        LOG.debug("next movie id: " + result);
        return result;
    }
    
    private static class MovieIdentity {
        private int nextVal = 0;
        public int getValueAndIncrement() {
            return this.nextVal++;
        }
    }



}
