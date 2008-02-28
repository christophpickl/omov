package at.ac.tuwien.e0525580.omov.gui;

import java.awt.Component;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.webdata.WebSearchResultsDialog;
import at.ac.tuwien.e0525580.omov.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebImdbExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebSearchResult;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public abstract class CommonController {

    private static final Log LOG = LogFactory.getLog(CommonController.class);
    
    protected Movie _doFetchMetaData(Component parent, Movie movieFetchingData) {
        LOG.info("fetching metadata for movie '"+movieFetchingData+"'.");
        IWebExtractor ex = new WebImdbExtractor();
        List<WebSearchResult> result;
        try {
            result = ex.search(movieFetchingData.getTitle());
        } catch (BusinessException e) {
            LOG.error("Could not fetch movie details for movie '"+movieFetchingData+"'!", e);
            GuiUtil.error(parent, "Fetching Metadata failed", "Could not get metadta for movie '"+movieFetchingData.getTitle()+"'!");
            return null;
        }
        
        final WebSearchResultsDialog dialog = new WebSearchResultsDialog(null, result);
        dialog.setVisible(true);
        
        if(dialog.isActionConfirmed()) {
            final Movie newMovie = dialog.getMovie();
            LOG.info("Confirmed new metadata for movie '"+newMovie+"'");
            return newMovie;
        }
        return null;
    }
}
