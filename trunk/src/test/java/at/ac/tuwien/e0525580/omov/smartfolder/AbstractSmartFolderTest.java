package at.ac.tuwien.e0525580.omov.smartfolder;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.model.db4o.ObjectSetTransformer;
import at.ac.tuwien.e0525580.omov.smartfolder.AbstractColumnCriterion;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.query.Query;

abstract class AbstractSmartFolderTest extends TestCase {

    private static final Log LOG = LogFactory.getLog(AbstractSmartFolderTest.class);

    private static final String DB_FILE_NAME = "junit_test.db4";

    static final ObjectContainer DB;
    
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    
    public static final Resolution R2000x2000 = new Resolution(2000, 2000);
    public static final Resolution R1024x768 = new Resolution(1024, 768);
    
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
    static final Movie MOVIE_INDEPENDENCE_DAY = createTestMovie("Independence Day", 2008, true, newDate("2008-01-13 13:14:15"), Resolution.R0x0);
    static final Movie MOVIE_ZERO             = createTestMovie("Zero",        2000, false, newDate("2008-02-14 18:00:21"), R2000x2000);
    static final Movie MOVIE_STAR_WARS1       = createTestMovie("Star Wars 1", 1985, true, newDate("2007-12-24 12:00:01"), R1024x768);
    static final Movie MOVIE_STAR_WARS2       = createTestMovie("Star Wars 2", 1987, true, newDate("2007-12-24 12:00:02"), R1024x768);
    static final Movie MOVIE_STAR_WARS3       = createTestMovie("Star Wars 3", 1989, true, newDate("2007-12-24 12:00:03"), R1024x768);


    static {
        final boolean insertData = (new File(DB_FILE_NAME).exists() == false);
        LOG.info("Opening connection to file '"+DB_FILE_NAME+"'.");
        DB = Db4o.openFile(DB_FILE_NAME);

        if(insertData) insertTestData();
    }
    
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
        LOG.info("### " + this.getName() + " START");
    }
    protected void tearDown() throws Exception {
        super.tearDown();
        LOG.info("### " + this.getName() + " END");
    }
    
    
    
    private static void insertTestData() {
        LOG.info("inserting test data ("+MOVIE_TEST_DATA.size()+" movies).");
        for (Movie movie : MOVIE_TEST_DATA) {
            DB.set(movie);
            LOG.debug(movie);
        }
    }

    
    List<Movie> fetchMovies(boolean matchAll, List<AbstractColumnCriterion> criteria) {
        final SmartFolder smartFolder = new SmartFolder(-1, "SmartFolder("+this.getName()+")", matchAll, criteria);
        final List<Movie> movies = this.executeSmartFolder(smartFolder);
        return movies;
    }
    
    List<Movie> checkSomeExisting(AbstractColumnCriterion criterion, int amountExisting) {
        final boolean matchAll = true;
        final List<AbstractColumnCriterion> criteria = new LinkedList<AbstractColumnCriterion>();
        criteria.add(criterion);
        
        final List<Movie> movies = this.fetchMovies(matchAll, criteria);
        
        assertEquals(amountExisting, movies.size());
        return movies;
    }
    
    Movie checkOneExisting(AbstractColumnCriterion criterion) {
        final boolean matchAll = true;
        final List<AbstractColumnCriterion> criteria = new LinkedList<AbstractColumnCriterion>();
        criteria.add(criterion);
        
        final List<Movie> movies = this.fetchMovies(matchAll, criteria);
        
        assertEquals(1, movies.size());
        return movies.get(0);
    }
    
    void checkNoneExisting(AbstractColumnCriterion criterion) {
        final boolean matchAll = true;
        final List<AbstractColumnCriterion> criteria = new LinkedList<AbstractColumnCriterion>();
        criteria.add(criterion);
        
        final List<Movie> movies = this.fetchMovies(matchAll, criteria);
        assertEquals("Movies found: " + Arrays.toString(movies.toArray()), 0, movies.size());
    }
    
    @SuppressWarnings("unchecked")
    List<Movie> executeSmartFolder(SmartFolder smartFolder) {
        final Query query = DB.query();
        smartFolder.pepareQuery(query);
        
        final ObjectSet<Movie> os = query.execute();
        final List<Movie> movies = new ObjectSetTransformer<Movie>().transformList(os);
        return movies;
    }
    
    @SuppressWarnings("unchecked")
    List<Movie> fetchAllMovies() {
        final Query query = DB.query();
        query.constrain(Movie.class);
        final ObjectSet<Movie> os = query.execute();
        final List<Movie> movies = new ObjectSetTransformer<Movie>().transformList(os);
        return movies;
    }
    
}
