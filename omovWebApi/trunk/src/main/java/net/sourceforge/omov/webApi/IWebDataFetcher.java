package net.sourceforge.omov.webApi;

import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.tools.webdata.WebSearchResult;


//FEATURE websearch: maybe automatically check HOST + "/robots.txt" for permission?!

/**
* 
* @author christoph_pickl@users.sourceforge.net
*/
public interface IWebDataFetcher {
	
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
