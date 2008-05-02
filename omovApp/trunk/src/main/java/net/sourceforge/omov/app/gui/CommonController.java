package net.sourceforge.omov.app.gui;

import java.awt.Component;
import java.util.List;

import net.sourceforge.omov.app.gui.webdata.WebSearchResultsDialog;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.tools.webdata.IWebExtractor;
import net.sourceforge.omov.core.tools.webdata.WebImdbExtractor;
import net.sourceforge.omov.core.tools.webdata.WebSearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
            GuiUtil.error(parent, "Fetching Metadata Failed", "Could not get metadta for movie '"+movieFetchingData.getTitle()+"'!\nPlease check your internet connection.");
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
