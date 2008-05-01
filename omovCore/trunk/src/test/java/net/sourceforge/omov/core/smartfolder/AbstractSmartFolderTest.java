package net.sourceforge.omov.core.smartfolder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.omov.core.AbstractTestCase;
import net.sourceforge.omov.core.Db4oUtil;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.core.model.db4o.ObjectSetTransformer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

abstract class AbstractSmartFolderTest extends AbstractTestCase {

    private static final Log LOG = LogFactory.getLog(AbstractSmartFolderTest.class);

    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static final Resolution R2000x2000 = new Resolution(2000, 2000);
    public static final Resolution R1024x768  = new Resolution(1024, 768);
    public static final Resolution R800x800   = new Resolution(800, 800);
    public static final Resolution R0x0   = Resolution.R0x0;
    
    static final List<Movie> MOVIE_TEST_DATA = new ArrayList<Movie>();
    
    private static int currentId = 0;
    
    /* testdata constraints:
     * - titles are unqiue
     * - only 3 movies.year < 2000 ("star wars 1-3")
     * - no movie.year == 42 && no movie.year > 2008
     * - only 1 movie.seen == false ("zero"); 4 seen
     * - max movie.dateAdded = 2008/02/14 18:00:21
     * - "zero" got max resolution 2000x2000
     */
    
    static final Movie MOVIE_INDEPENDENCE_DAY = Movie.create(currentId++)
                .title("Independence Day")
                .year(2008)
                .seen(true)
                .dateAdded(newDate("2008-01-13 13:14:15"))
                .resolution(R0x0)
                .fileSizeKb(4000)
                .actors("Will", "John")
                .languages("EN", "DE")
                .quality(Quality.BEST)
                .rating(2)
                .comment("Comment")
                .director("John Doe")
                .get();
    static final Movie MOVIE_ZERO = Movie.create(currentId++)
                .title("Zero")
                .year(2000)
                .seen(false)
                .dateAdded(newDate("2008-02-14 18:00:21"))
                .resolution(R2000x2000)
                .fileSizeKb(0)
                .actors("John")
                .quality(Quality.UNRATED)
                // no language
                .rating(0)
                .comment("COMMENT")
                .get();
    static final Movie MOVIE_STAR_WARS1 = Movie.create(currentId++)
                .title("Star Wars 1")
                .year(1985)
                .seen(true)
                .dateAdded(newDate("2007-12-24 12:00:01"))
                .resolution(R1024x768)
                .fileSizeKb(100)
                .actors("Luke", "Harry")
                .languages("EN")
                .quality(Quality.NORMAL)
                .rating(4)
                .comment("NO COMMENT")
                .get();
    static final Movie MOVIE_STAR_WARS2 = Movie.create(currentId++)
                .title("Star Wars 2")
                .year(1987)
                .seen(true)
                .dateAdded(newDate("2007-12-24 12:00:02"))
                .resolution(R1024x768)
                .fileSizeKb(100)
                .actors("Luke")
                .languages("EN")
                .quality(Quality.NORMAL)
                .rating(5)
                .comment("COMMENTOS")
                .get();
    static final Movie MOVIE_STAR_WARS3 = Movie.create(currentId++)
                .title("Star Wars 3")
                .year(1989)
                .seen(true)
                .dateAdded(newDate("2007-12-24 12:00:03"))
                .resolution(R800x800)
                .fileSizeKb(100)
                .actors("Luke")
                .languages("EN")
                .quality(Quality.GOOD)
                .rating(4)
                .comment("ramramram")
                .get();
    
    static {
        MOVIE_TEST_DATA.add(MOVIE_INDEPENDENCE_DAY);
        MOVIE_TEST_DATA.add(MOVIE_ZERO);
        MOVIE_TEST_DATA.add(MOVIE_STAR_WARS1);
        MOVIE_TEST_DATA.add(MOVIE_STAR_WARS2);
        MOVIE_TEST_DATA.add(MOVIE_STAR_WARS3);
    }

    protected static final ObjectContainer objectContainer = Db4oUtil.getDbConnection("AbstractSmartFolderTest.db4");

    
    static Date newDate(String s) {
        try {
            return DATE_FORMAT.parse(s);
        } catch (ParseException e) {
            throw new IllegalArgumentException("invalid date string '"+s+"'!");
        }
    }
    
    
    static Movie createTestMovie(String title, int year, boolean seen, Date dateAdded, Resolution resolution) {
        final Movie movie = Movie.create(currentId++).title(title).year(year).seen(seen).dateAdded(dateAdded).resolution(resolution).get();
        MOVIE_TEST_DATA.add(movie);
        return movie;
    }

    

    
    protected void setUp() throws Exception {
        super.setUp();
        
        this.insertTestData();
    }
    
    private void insertTestData() {
        LOG.info("inserting test data ("+MOVIE_TEST_DATA.size()+" movies).");
        for (Movie movie : MOVIE_TEST_DATA) {
            objectContainer.set(movie);
            LOG.debug(movie);
        }
    }

    
    List<Movie> fetchMovies(boolean matchAll, List<AbstractColumnCriterion<?>> criteria) {
        final SmartFolder smartFolder = new SmartFolder(-1, "SmartFolder("+this.getName()+")", matchAll, criteria);
        final List<Movie> movies = this.executeSmartFolder(smartFolder);
        return movies;
    }
    
    List<Movie> checkSomeExisting(AbstractColumnCriterion<?> criterion, int amountExisting) {
        final boolean matchAll = true;
        final List<AbstractColumnCriterion<?>> criteria = new LinkedList<AbstractColumnCriterion<?>>();
        criteria.add(criterion);
        
        final List<Movie> movies = this.fetchMovies(matchAll, criteria);
        
        assertEquals(amountExisting, movies.size());
        return movies;
    }
    
    Movie checkOneExisting(AbstractColumnCriterion<?> criterion) {
        final boolean matchAll = true;
        final List<AbstractColumnCriterion<?>> criteria = new LinkedList<AbstractColumnCriterion<?>>();
        criteria.add(criterion);
        
        final List<Movie> movies = this.fetchMovies(matchAll, criteria);
        
        assertEquals(1, movies.size());
        return movies.get(0);
    }
    
    void checkNoneExisting(AbstractColumnCriterion<?> criterion) {
        final boolean matchAll = true;
        final List<AbstractColumnCriterion<?>> criteria = new LinkedList<AbstractColumnCriterion<?>>();
        criteria.add(criterion);
        
        final List<Movie> movies = this.fetchMovies(matchAll, criteria);
        assertEquals("Movies found: " + Arrays.toString(movies.toArray()), 0, movies.size());
    }
    
    @SuppressWarnings("unchecked")
    List<Movie> executeSmartFolder(SmartFolder smartFolder) {
        final Query query = objectContainer.query();
        smartFolder.pepareQuery(query);
        
        final ObjectSet<Movie> os = query.execute();
        final List<Movie> movies = new ObjectSetTransformer<Movie>().transformList(os);
        return movies;
    }
    
    @SuppressWarnings("unchecked")
    List<Movie> fetchAllMovies() {
        final Query query = objectContainer.query();
        query.constrain(Movie.class);
        final ObjectSet<Movie> os = query.execute();
        final List<Movie> movies = new ObjectSetTransformer<Movie>().transformList(os);
        return movies;
    }
    
}
