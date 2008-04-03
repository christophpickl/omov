package at.ac.tuwien.e0525580.omov.tools.webdata;

import java.util.List;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;

// FEATURE websearch: maybe automatically check HOST + "/robots.txt" for permission?!
public interface IWebExtractor {
    List<WebSearchResult> search(String movieTitle) throws BusinessException;
    
    Movie getDetails(WebSearchResult searchResult, boolean fetchCover) throws BusinessException;
    
//    Movie enhanceMovie(Movie originalMovie, Movie metadataMovie);
    
    /**
     * invokes this.search(movie.getTitle()) and uses first WebSearchResult to enhance the given movie.
     * 
     * @return null if nothing was found.
     */
    Movie fetchAndEnhanceMovie(Movie originalMovie, boolean fetchCover) throws BusinessException;
    
}

