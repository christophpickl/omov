package at.ac.tuwien.e0525580.omov.tools.webdata;

import java.net.URLEncoder;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Parser;
import org.htmlparser.util.ParserException;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.MovieCreator;

public class WebImdbExtractor implements IWebExtractor {
    

    private static final Log LOG = LogFactory.getLog(WebImdbExtractor.class);
    static final String HOST = "http://imdb.com";
    static final boolean DEBUG = false;
    
    
    
    public static void main(String[] args) throws Exception {
        dummyDetail();
    }
    
    public static void dummySearch() throws Exception {
//        final String search = "independence day";
//        final String search = "matrix";
        final String search = "matrix";
        final boolean fetchCover = true;

        IWebExtractor ex = new WebImdbExtractor();
        List<WebSearchResult> result = ex.search(search);

        for (WebSearchResult r : result) {
            System.out.println("-> " + r);
            // System.out.println("Movie details found: " + ex.getDetails(r));
            System.out.println(ex.getDetails(r, fetchCover));
            break;
        }
    }
    
    public static void dummyDetail() throws Exception {
        final IWebExtractor ex = new WebImdbExtractor();
//        final String url = "http://imdb.com/title/tt0116629/"; // independence day
//        final String url = "http://imdb.com/title/tt0126029/"; // shrek

//        final String url = "http://imdb.com/title/tt0364888/"; // was some exception thrown
        final String url = "http://imdb.com/title/tt0475520/"; // was also some exception thrown (totally different website layout!)
        
        final Movie result = ex.getDetails(new WebSearchResult("", url), false);
        System.out.println(result);
    }
    
    public List<Movie> getAllDetails(final String movieTitle, boolean fetchCover) throws BusinessException {
        LOG.info("Fetching all details for movie with title '"+movieTitle+"'...");
        final List<Movie> movies = new LinkedList<Movie>();
        
        for (WebSearchResult searchResult : this.search(movieTitle)) {
            movies.add(this.getDetails(searchResult, fetchCover));
        }
        
        return movies;
    }


    public List<WebSearchResult> search(final String movieTitle) throws BusinessException{
        LOG.info("Start search for movie with title '"+movieTitle+"' ...");
        try {
            final String urlEncodedSearch = URLEncoder.encode(movieTitle, "UTF-8");
            final String fullUrlEncoded = HOST + "/find?s=all&q="+urlEncodedSearch;
            LOG.info("Fetching data form website '"+fullUrlEncoded+"'...");
            final Parser parser = new Parser(fullUrlEncoded);
            final ImdbStartPage visitor = new ImdbStartPage(movieTitle);
            parser.visitAllNodesWith(visitor);
            
            final List<WebSearchResult> result = visitor.getFoundTargetLinks();
            LOG.info("Found "+result.size()+" detail pages, for movie title '"+movieTitle+"'");
            return result;
        } catch(Exception e) {
            throw new BusinessException("Could not start search for movie '"+movieTitle+"'!", e);
        }
    }
    
    
    public Movie getDetails(WebSearchResult searchResult, boolean fetchCover) throws BusinessException {
        final String detailUrl = searchResult.getUrl();
        LOG.info("fetching details from url: '" + detailUrl + "'");
        
        try {
            final Parser parser = new Parser(detailUrl);
            final ImdbDetailPage visitor = new ImdbDetailPage(fetchCover);
            parser.visitAllNodesWith(visitor);
            
            final Movie movie = visitor.getMovie();
            LOG.debug("found movie details: " + movie);
            return movie;
        } catch(ParserException e) {
            throw new BusinessException("", e);
        }
    }

    /**
     * old movie data will be overwritten by metadata
     * 
     * fetched metadata:
     *    String title; Set<String> genres; (String coverFile)
     *    String director; String comment; int year; int duration; Set<String> actors;
     * 
     * general data:
     *    String title; boolean seen; int rating; String coverFile; Set<String> genres; Set<String> languages; String style;
     * 
     * detail data:
     *    String director; Set<String> actors; int year; String comment
     *    
     * @see ImdbMovieData#getMovie()
     */
    private static Movie enhanceMovie(Movie o, Movie m) {
        final MovieCreator c = Movie.create(o.getId()).title(m.getTitle()).genres(m.getGenres()).director(m.getDirector()).year(m.getYear()).comment(m.getComment()).actors(m.getActors()).duration(m.getDuration()).coverFile(m.getOriginalCoverFile());
        
        return c.seen(o.isSeen()).rating(o.getRating())
            .director(o.getDirector()).actors(o.getActors()).year(o.getYear()).comment(o.getComment())
            .fileSizeKb(o.getFileSizeKb()).folderPath(o.getFolderPath()).format(o.getFormat()).files(o.getFiles()).duration(o.getDuration()).resolution(o.getResolution()).subtitles(o.getSubtitles()).get();
    }
    

    /**
     * invokes this.search(movie.getTitle()) and uses first WebSearchResult to enhance the given movie.
     * 
     * @return null if nothing was found.
     */
    public Movie fetchAndEnhanceMovie(Movie originalMovie, boolean fetchCover) throws BusinessException {
        final String movieTitle = originalMovie.getTitle();
        final List<WebSearchResult> results = this.search(movieTitle);
        
        if(results.size() == 0) {
            LOG.debug("Could not find any metadata for movie with title '"+movieTitle+"'.");
            return null;
        }
        
        final WebSearchResult result = results.get(0); // get top match only
        final Movie metadataMovie = this.getDetails(result, fetchCover);
        return enhanceMovie(originalMovie, metadataMovie);
    }
    


}
