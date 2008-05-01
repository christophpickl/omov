package at.ac.tuwien.e0525580.omov.smartfolder;

import java.util.Date;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Resolution;

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
    

    /******************************************************************************************************************/
    /**    T E X T   M A T C H E S
    /******************************************************************************************************************/
    
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
    

    /******************************************************************************************************************/
    /**    N U M B E R   M A T C H E S
    /******************************************************************************************************************/
    
    public void testIsYear() throws Exception {
        final NumberCriterion criterion = NumberCriterion.newYear(NumberMatch.newEquals(2008L));
        final Movie movie = this.checkOneExisting(criterion);
        
        assertEquals(MOVIE_INDEPENDENCE_DAY.getYear(), movie.getYear());
    }
    
    public void testIsNotYear() throws Exception {
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newNotEquals(42L)), MOVIE_TEST_DATA.size());
    }

    public void testGreaterYear() throws Exception {
        this.checkNoneExisting(NumberCriterion.newYear(NumberMatch.newGreater(2008L)));
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newGreater(0L)), MOVIE_TEST_DATA.size());
    }
    public void testLessYear() throws Exception {
        this.checkNoneExisting(NumberCriterion.newYear(NumberMatch.newLess(0L)));
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newLess(9999L)), MOVIE_TEST_DATA.size());
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newLess(2000L)), 3); // star wars 1-3
    }
    public void testInRangeYear() throws Exception {
        this.checkNoneExisting(NumberCriterion.newYear(NumberMatch.newInRange(0L, 100L)));
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(0L, 9999L)), MOVIE_TEST_DATA.size());
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1985L, 1985L)), 1); // star wars 1
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1985L, 1987L)), 2); // star wars 1 & 2
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1986L, 1987L)), 1); // star wars 2
        this.checkSomeExisting(NumberCriterion.newYear(NumberMatch.newInRange(1985L, 1989L)), 3); // star wars 1-3
    }
    

    /******************************************************************************************************************/
    /**    B O O L   M A T C H E S
    /******************************************************************************************************************/

    public void testBoolean() throws Exception {
        this.checkOneExisting(BoolCriterion.newSeen(BoolMatch.newEquals(Boolean.FALSE))); // zero not seen
        this.checkSomeExisting(BoolCriterion.newSeen(BoolMatch.newNotEquals(Boolean.FALSE)), 4); // zero not seen
        this.checkOneExisting(BoolCriterion.newSeen(BoolMatch.newNotEquals(Boolean.TRUE))); // zero not seen
        this.checkSomeExisting(BoolCriterion.newSeen(BoolMatch.newEquals(Boolean.TRUE)), 4); // zero not seen
    }

    /******************************************************************************************************************/
    /**    D A T E   M A T C H E S
    /******************************************************************************************************************/

    public void testDate() throws Exception {
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newEquals(MOVIE_INDEPENDENCE_DAY.getDateAdded())));
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newEquals(newDate("2008-02-14 00:00:00")))); // 2008/02/14 18:00:21
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newEquals(newDate("2008-02-14 23:59:57")))); // 2008/02/14 18:00:21
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newNotEquals(newDate("9000-00-00 00:00:00"))), MOVIE_TEST_DATA.size());
        this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newAfter(newDate("2008-02-14 00:00:00"))));
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newBefore(newDate("2007-12-31 23:59:59"))), 3); // star wars 1-3
        
        // range
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newInRange(newDate("2007-12-24 12:00:01"), newDate("2007-12-24 12:00:03"))), 3); // star wars 1-3
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newInRange(newDate("2007-12-24 12:00:02"), newDate("2007-12-24 12:00:03"))), 2); // star wars 2-3
        this.checkNoneExisting(DateCriterion.newDateAdded(DateMatch.newInRange(newDate("2008-02-14 18:00:22"), newDate("2999-12-24 01:00:00")))); // max date got "zero": "2008/02/14 18:00:21"
    }
    
    public void testDateInLast() throws Exception {
        this.checkSomeExisting(DateCriterion.newDateAdded(DateMatch.newInTheLast(999999)), MOVIE_TEST_DATA.size());
        
        
        final Movie recentlyAddedMovie = Movie.create(-1).title("Recently Added Movie").dateAdded(new Date()).get();
        
        try {
            assertEquals(MOVIE_TEST_DATA.size(), this.fetchAllMovies().size());
            objectContainer.set(recentlyAddedMovie);
            assertEquals(MOVIE_TEST_DATA.size()+1, this.fetchAllMovies().size());
            
            this.checkOneExisting(DateCriterion.newDateAdded(DateMatch.newInTheLast(1)));
        } finally {
            objectContainer.delete(recentlyAddedMovie);
            assertEquals(MOVIE_TEST_DATA.size(), this.fetchAllMovies().size());
        }
    }

    /******************************************************************************************************************/
    /**    R E S O L U T I O N   M A T C H E S
    /******************************************************************************************************************/

    public void testResolution() throws Exception {
//        System.out.println("Movies existing in DB:");
//        for (Movie m : this.fetchAllMovies()) {
//            System.out.println("- " + m);
//        }
        
        this.checkOneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(new Resolution(1999, 1999))));
        this.checkNoneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(R2000x2000)));
        this.checkNoneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(new Resolution(2001, 2000))));
        this.checkNoneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newGreater(new Resolution(2000, 2001))));
        this.checkOneExisting(ResolutionCriterion.newResolution(ResolutionMatch.newEquals(Resolution.R0x0)));
    }

    /******************************************************************************************************************/
    /**    T E X T   M U L T I P L E   M A T C H E S
    /******************************************************************************************************************/

    public void testTextMultiple() throws Exception {
        this.checkSomeExisting(TextMultipleCriterion.newActors(TextMultipleMatch.newContains("John")), 2);
        this.checkSomeExisting(TextMultipleCriterion.newActors(TextMultipleMatch.newContains("joh")),  2);
        this.checkSomeExisting(TextMultipleCriterion.newActors(TextMultipleMatch.newContains("luke")), 3);
        this.checkSomeExisting(TextMultipleCriterion.newActors(TextMultipleMatch.newContains("Luke")), 3);

        this.checkSomeExisting(TextMultipleCriterion.newLanguages(TextMultipleMatch.newContains("DE")), 1);
        this.checkSomeExisting(TextMultipleCriterion.newLanguages(TextMultipleMatch.newContains("de")), 1);
        this.checkSomeExisting(TextMultipleCriterion.newLanguages(TextMultipleMatch.newContains("EN")), 4);
        this.checkSomeExisting(TextMultipleCriterion.newLanguages(TextMultipleMatch.newContains("")),   0);
    }

    /******************************************************************************************************************/
    /**    F I L E   S I Z E   M A T C H E S
    /******************************************************************************************************************/

    public void testFileSize() throws Exception {
        this.checkSomeExisting(FileSizeCriterion.newFileSize(FileSizeMatch.newEquals    (0L)), 1);
        this.checkSomeExisting(FileSizeCriterion.newFileSize(FileSizeMatch.newEquals (4000L)), 1);
        this.checkSomeExisting(FileSizeCriterion.newFileSize(FileSizeMatch.newEquals  (100L)), 3);
        this.checkSomeExisting(FileSizeCriterion.newFileSize(FileSizeMatch.newLess    (101L)), 4);
        this.checkOneExisting (FileSizeCriterion.newFileSize(FileSizeMatch.newGreater (3000L)));
        this.checkNoneExisting(FileSizeCriterion.newFileSize(FileSizeMatch.newGreater (4000L)));
    }

    /******************************************************************************************************************/
    /**    Q U A L I T Y   M A T C H E S
    /******************************************************************************************************************/

    public void testQuality() throws Exception {
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newEquals(Quality.UNRATED)), 1);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newEquals(Quality.UGLY)),    0);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newEquals(Quality.NORMAL)),  2);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newEquals(Quality.GOOD)),    1);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newEquals(Quality.BEST)),    1);

        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newBetter(Quality.UNRATED)), 4);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newBetter(Quality.UGLY)),    4);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newBetter(Quality.NORMAL)),  2);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newBetter(Quality.GOOD)),    1);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newBetter(Quality.BEST)),    0);

        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newWorse(Quality.UNRATED)), 0);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newWorse(Quality.UGLY)),    1);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newWorse(Quality.NORMAL)),  1);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newWorse(Quality.GOOD)),    3);
        this.checkSomeExisting(QualityCriterion.newQuality(QualityMatch.newWorse(Quality.BEST)),    4);
    }

    /******************************************************************************************************************/
    /**    D U R A T I O N   M A T C H E S
    /******************************************************************************************************************/

    public void testDuration() throws Exception {
        // MINOR tests: implement duration tests
    }

    /******************************************************************************************************************/
    /**    R A T I N G   M A T C H E S
    /******************************************************************************************************************/

    public void testRating() throws Exception {
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newEquals(0)), 1);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newEquals(1)), 0);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newEquals(2)), 1);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newEquals(3)), 0);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newEquals(4)), 2);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newEquals(5)), 1);

        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newGreater(0)), 4);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newGreater(1)), 4);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newGreater(2)), 3);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newGreater(3)), 3);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newGreater(4)), 1);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newGreater(5)), 0);

        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newLess(0)), 0);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newLess(1)), 1);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newLess(2)), 1);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newLess(3)), 2);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newLess(4)), 2);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newLess(5)), 4);

        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newNotEquals(0)), 4);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newNotEquals(1)), 5);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newNotEquals(2)), 4);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newNotEquals(3)), 5);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newNotEquals(4)), 3);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newNotEquals(5)), 4);

        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(0, 5)), 5);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(0, 4)), 4);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(0, 3)), 2);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(0, 2)), 2);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(0, 1)), 1);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(0, 0)), 1);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(1, 5)), 4);
        this.checkSomeExisting(RatingCriterion.newRating(RatingMatch.newInRange(5, 5)), 1);
    }
}
