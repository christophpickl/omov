/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.core.model.db4o;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.core.model.IMovieDaoListener;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oMovieDao extends AbstractDb4oDao implements IMovieDao {

    private static final Log LOG = LogFactory.getLog(Db4oMovieDao.class);

    private final Set<IMovieDaoListener> listeners = new HashSet<IMovieDaoListener>();
    
    public Db4oMovieDao(Db4oConnection connection) {
        super(connection);
    }
    
    


    public Set<Movie> getMovies() {
        final ObjectSet<Movie> os = this.objectContainer.get(Movie.class);
        LOG.debug("getMovies() is returning " + os.size() + " movies");
        return new ObjectSetTransformer<Movie>().transformSet(os);
    }

    public List<Movie> getMoviesSorted() {
        final List<Movie> list = new ArrayList<Movie>(this.getMovies());
        Collections.sort(list, Movie.TITLE_COMPARATOR);
        return list;
    }

    @SuppressWarnings("serial")
    public Movie getMovie(final long id) {
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
        if(os.hasNext() == true) {
        	throw new RuntimeException("Duplicate movies found by id '"+id+"'!");
        }
        LOG.info("Found movie by id '"+id+"': " + result);
        return result;
    }

    public List<String> getMovieLanguages() {
        LOG.info("Getting languages...");
        final List<String> result = this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getLanguages();
            }});
        LOG.debug("Got "+result.size()+" languages.");
        return result;
    }

    public List<String> getMovieSubtitles() {
        LOG.info("Getting subtitles...");
        final List<String> result = this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getSubtitles();
            }});
        LOG.debug("Got "+result.size()+" subtitles.");
        return result;
    }

    public List<String> getMovieDirectors() {
        LOG.info("Getting directors...");
        final List<String> result = this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getDirector();
            }});
        LOG.debug("Got "+result.size()+" directors.");
        return result;
    }

    public List<String> getMovieActors() {
        LOG.info("Getting actors...");
        final List<String> result = this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getActors();
            }});
        LOG.debug("Got "+result.size()+" directors.");
        return result;
    }

    public List<String> getMovieGenres() {
        LOG.info("Getting genres...");
        return this.getMovieStrings(new MovieStringsExtractor() {
            public Collection<String> getStrings(Movie movie) {
                return movie.getGenres();
            }});
    }
    
    public List<String> getMovieTitles() {
        LOG.info("Getting titles...");
        return this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getTitle();
            }});
    }

    public List<String> getMovieStyles() {
        LOG.info("Getting styles...");
        return this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getStyle();
            }});
    }

    public List<String> getMovieFolderPaths() {
        LOG.info("Getting folderpaths...");
        return this.getMovieString(new MovieStringExtractor() {
            public String getString(Movie movie) {
                return movie.getFolderPath();
            }});
    }
    


    public Movie insertMovie(Movie insertMovie) {
        LOG.info("Inserting movie: " + insertMovie);
        
        final int id = this.getNextMovieId();
        final Date dateAdded = new Date();
        LOG.debug("New id will be " + id + " and date added " + dateAdded);
        final Movie result = Movie.newByOtherMovieSetDateAdded(id, dateAdded, insertMovie);
        
        this.objectContainer.set(result);

        if(this.isAutoCommit() == true) {
            this.commit(); // necessary?
        }
        
        this.notifyListeners();
        return result;
    }



    public List<Movie> insertMovies(List<Movie> insertMovies) {
        final boolean wasAutoComitEnabled = this.isAutoCommit();
        
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
            if(commited == false) {
                this.rollback();
            }
            this.setAutoCommit(wasAutoComitEnabled);
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
    

    
    public void deleteMovie(Movie deletingMovie) {
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
    public List<Movie> getMoviesBySmartFolder(SmartFolder smartFolder) {
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
    
    public void addMovieDaoListener(IMovieDaoListener listener) {
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
    

    private List<String> getMovieString(MovieStringExtractor extractor) {
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
    
    private List<String> getMovieStrings(MovieStringsExtractor extractor) {
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
        private int nextVal = 1;
        public int getValueAndIncrement() {
            return this.nextVal++;
        }
    }



}
