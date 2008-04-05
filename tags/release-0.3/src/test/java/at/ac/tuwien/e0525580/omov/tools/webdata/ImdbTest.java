package at.ac.tuwien.e0525580.omov.tools.webdata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebImdbExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebSearchResult;
import at.ac.tuwien.e0525580.omov.util.CollectionUtil;

public class ImdbTest extends TestCase {
    
    private static final Log LOG = LogFactory.getLog(ImdbTest.class);
    
    
    public void xtestSearchResult() throws Exception {
        final String search = "independence day";
        
        final List<String> expectedUrls = new ArrayList<String>();
        expectedUrls.add("http://imdb.com/title/tt0116629/");
        expectedUrls.add("http://imdb.com/title/tt0208227/");
        expectedUrls.add("http://imdb.com/title/tt0085724/");
        expectedUrls.add("http://imdb.com/title/tt0292014/");
        expectedUrls.add("http://imdb.com/title/tt0073165/");
        expectedUrls.add("http://imdb.com/title/tt0879821/");
        expectedUrls.add("http://imdb.com/title/tt0361872/");
        expectedUrls.add("http://imdb.com/title/tt0327949/");
        expectedUrls.add("http://imdb.com/title/tt0358533/");
        expectedUrls.add("http://imdb.com/title/tt0357791/");
        expectedUrls.add("http://imdb.com/title/tt0818604/");
        
        IWebExtractor ex = new WebImdbExtractor();
        List<WebSearchResult> result = ex.search(search);
        
        final List<String> actualUrls = new LinkedList<String>();
        for (WebSearchResult r : result) {
            actualUrls.add(r.getUrl());
        }
        
        assertEquals(expectedUrls.size(), actualUrls.size());
        for (int i = 0; i < expectedUrls.size(); i++) {
            assertEquals(expectedUrls.get(i), actualUrls.get(i));
        }
    }
    
    private void abstractTestDetail(final String detailPageUrl,
            String title, Set<String> genres, String director, String comment, int year, Set<String> actors, int duration) throws Exception {
        LOG.info("Creating expected movie instance.");
        
        final Movie expected = Movie.create(-1).title(title).genres(genres).director(director).comment(comment).year(year).actors(actors).duration(duration).get();
        
        final IWebExtractor ex = new WebImdbExtractor();
        LOG.info("Fetching details for independence day.");
        final Movie actual = ex.getDetails(new WebSearchResult("", detailPageUrl), false);
        
        LOG.info("Comparing expected with actual movie.");
        assertEquals(expected, actual);
    }
    
    public void testDetailIndependenceDay() throws Exception {
        final String comment = "The aliens are coming and their goal is to invade and destroy. Fighting superior technology, Man's best weapon is the will to survive.";
        Set<String> genres = CollectionUtil.asStringSet("Action", "Sci-Fi", "Thriller");
        Set<String> actors = CollectionUtil.asStringSet("Will Smith", "Bill Pullman", "Jeff Goldblum");
        
        this.abstractTestDetail("http://imdb.com/title/tt0116629/", "Independence Day", genres, "Roland Emmerich", comment, 1996, actors, 145);
    }
}
