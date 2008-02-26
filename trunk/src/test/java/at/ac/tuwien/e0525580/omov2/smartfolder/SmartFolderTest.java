package at.ac.tuwien.e0525580.omov2.smartfolder;

import java.util.Date;

import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;
import at.ac.tuwien.e0525580.omov2.bo.movie.Resolution;

public class SmartFolderTest extends AbstractSmartFolderTest {

//    private static final Log LOG = LogFactory.getLog(SmartFolderTest.class);
 
//    private void test_simple() {
//        final Query query = this.connection.query();
//        
//        query.constrain(Movie.class);
//
//        query.descend("title").constrain("gesucht").equal();
//        
//        ObjectSet<Movie> os = query.execute();
//        List<Movie> movies = new ObjectSetTransformer<Movie>().transformList(os);
//        System.out.println("found: " + movies.size());
//        System.out.println(Arrays.toString(movies.toArray()));
//    }
    
//    public void readAll() throws Exception {
//        final ObjectSet<Movie> os = this.connection.get(Movie.class);
//        
//        Set<Movie> movies = new ObjectSetTransformer<Movie>().transformSet(os);
//        System.out.println("readAll found "+movies.size()+" movies.");
//        for (Movie movie : movies) {
//            System.out.println("- " + movie);
//        }
//    }
    
    // TextMatches
    
    public void testEqualsTitle() throws Exception {
        final String expectedTitle = "IndePEndence day";
        final TextCriterion criterion= TextCriterion.newTitle(TextMatch.newEquals(expectedTitle));
        final Movie movie = this.checkOneExisting(criterion);
        
        assertEquals(MOVIE_INDEPENDENCE_DAY.getTitle(), movie.getTitle());
    }
    
    public void testContainsTitle() throws Exception {
        final TextCriterion criterion = TextCriterion.newTitle(TextMatch.newContains("penDEnce da"));
        final Movie movie = this.checkOneExisting(criterion);
        
        assertEquals(MOVIE_INDEPENDENCE_DAY.getTitle(), movie.getTitle());
    }
    
    public void testStartsWithTitle() throws Exception {
        final TextCriterion criterion = TextCriterion.newTitle(TextMatch.newStartsWith("indepENdence d")); // case insensetive
        final Movie movie = this.checkOneExisting(criterion);
        
        assertEquals(MOVIE_INDEPENDENCE_DAY.getTitle(), movie.getTitle());
    }
    
    public void testEndsWithTitle() throws Exception {
        final TextCriterion criterion = TextCriterion.newTitle(TextMatch.newEndsWith("denCe day")); // case insensetive
        final Movie movie = this.checkOneExisting(criterion);
        
        assertEquals(MOVIE_INDEPENDENCE_DAY.getTitle(), movie.getTitle());
    }
    

    // NumberMatches
    
    public void testIsYear() throws Exception {
        final NumberCriterion criterion = NumberCriterion.newYear(NumberMatch.newEquals(2008));
        final Movie movie = this.checkOneExisting(criterion);
        
        assertEquals(MOVIE_INDEPENDENCE_DAY.getYear(), movie.getYear());
    }
    
    public void testIsNotYear() throws Exception {
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newNotEquals(42)), MOVIE_TEST_DATA.size());
    }

    public void testGreaterYear() throws Exception {
        this.checkNoneExisting(NumberCriterion.newYear(NumberMatch.newGreater(2008)));
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newGreater(0)), MOVIE_TEST_DATA.size());
    }
    public void testLessYear() throws Exception {
        this.checkNoneExisting(NumberCriterion.newYear(NumberMatch.newLess(0)));
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newLess(9999)), MOVIE_TEST_DATA.size());
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newLess(2000)), 3); // star wars 1-3
    }
    public void testInRangeYear() throws Exception {
        this.checkNoneExisting(NumberCriterion.newYear(NumberMatch.newInRange(0, 100)));
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(0, 9999)), MOVIE_TEST_DATA.size());
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1985, 1985)), 1); // star wars 1
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1985, 1987)), 2); // star wars 1 & 2
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1986, 1987)), 1); // star wars 2
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1985, 1989)), 3); // star wars 1-3
    }
    

    // BoolMatches

    public void testBoolean() throws Exception {
        this.checkOneExisting(BoolCriterion.newSeen(BoolMatch.newEquals(Boolean.FALSE))); // zero not seen
        this.checkSomeExisting(BoolCriterion.newSeen(BoolMatch.newNotEquals(Boolean.FALSE)), 4); // zero not seen
        this.checkOneExisting(BoolCriterion.newSeen(BoolMatch.newNotEquals(Boolean.TRUE))); // zero not seen
        this.checkSomeExisting(BoolCriterion.newSeen(BoolMatch.newEquals(Boolean.TRUE)), 4); // zero not seen
    }
    
    // DateMatches

    public void testDate() throws Exception {
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newEquals(MOVIE_INDEPENDENCE_DAY.getDateAdded())));
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newEquals(newDate("2008/02/14 00:00:00")))); // 2008/02/14 18:00:21
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newEquals(newDate("2008/02/14 23:59:57")))); // 2008/02/14 18:00:21
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newNotEquals(newDate("9000/00/00 00:00:00"))), MOVIE_TEST_DATA.size());
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newAfter(newDate("2008/02/14 00:00:00"))));
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newBefore(newDate("2007/12/31 23:59:59"))), 3); // star wars 1-3
        
        // range
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newInRange(newDate("2007/12/24 12:00:01"), newDate("2007/12/24 12:00:03"))), 3); // star wars 1-3
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newInRange(newDate("2007/12/24 12:00:02"), newDate("2007/12/24 12:00:03"))), 2); // star wars 2-3
        this.checkNoneExisting(DateCriterion.newDateAdded(DateMatch.newInRange(newDate("2008/02/14 18:00:22"), newDate("2999/12/24 01:00:00")))); // max date got "zero": "2008/02/14 18:00:21"
    }
    
    public void testDateInLast() throws Exception {
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newInTheLast(999999)), MOVIE_TEST_DATA.size());
        
        
        final Movie recentlyAddedMovie = Movie.create(-1).title("Recently Added Movie").dateAdded(new Date()).get();
        
        try {
            assertEquals(MOVIE_TEST_DATA.size(), this.fetchAllMovies().size());
            DB.set(recentlyAddedMovie);
            assertEquals(MOVIE_TEST_DATA.size()+1, this.fetchAllMovies().size());
            
            this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newInTheLast(1)));
        } finally {
            DB.delete(recentlyAddedMovie);
            assertEquals(MOVIE_TEST_DATA.size(), this.fetchAllMovies().size());
        }
    }
    
    // ResolutionMatches

    public void testResolution() throws Exception {
        System.out.println("Movies existing in DB:");
        for (Movie m : this.fetchAllMovies()) {
            System.out.println("- " + m);
        }
        
        this.checkOneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(new Resolution(1999, 1999))));
        this.checkOneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(R2000x2000)));
        this.checkNoneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(new Resolution(2001, 2000))));
        this.checkNoneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(new Resolution(2000, 2001))));
        this.checkOneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newEquals(Resolution.R0x0)));
    }
}
