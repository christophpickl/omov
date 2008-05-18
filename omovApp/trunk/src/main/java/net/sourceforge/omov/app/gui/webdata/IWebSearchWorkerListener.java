package net.sourceforge.omov.app.gui.webdata;

import java.util.List;

import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.webApi.WebSearchResult;

public interface IWebSearchWorkerListener<P extends Movie> {
	
    /** movie instance, or null if user aborted work */
    void didSearchByTitle(boolean wasCancelled, List<WebSearchResult> result, P movieFetchingData, Exception thrownException);
    
}
