package net.sourceforge.omov.app.gui.webdata;

import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.guicore.OmovGuiUtil;
import net.sourceforge.omov.webApi.IWebDataFetcher;
import net.sourceforge.omov.webApi.WebDataFetcherFactory;
import net.sourceforge.omov.webApi.WebSearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

public class WebSearchWorker<O extends Movie> extends SwingWorker<List<WebSearchResult>, String> {

    private static final Log LOG = LogFactory.getLog(WebSearchWorker.class);
    
    private final JDialog progressDialog;
	private List<WebSearchResult> result;
	private final O movieFetchingData;
	private final JFrame parent;
	private final IWebSearchWorkerListener<O> listener;
    private Exception thrownException= null;
    
    public WebSearchWorker(JDialog progressDialog, IWebSearchWorkerListener<O> listener, O movieFetchingData, JFrame parent) {
		this.progressDialog = progressDialog;
		this.movieFetchingData = movieFetchingData;
		this.parent = parent;
		this.listener = listener;
	}
	
	@Override
	protected List<WebSearchResult> doInBackground() {
		LOG.debug("doInBackground() started");

        try {
            final IWebDataFetcher dataFetcher = WebDataFetcherFactory.newWebDataFetcher();
            
            this.result = dataFetcher.search(this.movieFetchingData.getTitle());
        } catch (BusinessException e) {
            LOG.error("Could not fetch movie details for movie '"+this.movieFetchingData+"'!", e);
            OmovGuiUtil.error(this.parent, "Fetching Metadata Failed", "Could not get metadta for movie '"+movieFetchingData.getTitle()+"'!\nPlease check your internet connection.");
            this.result = null;
            // thrownException = e; TODO either handle exception here, or pass it to client???
        }
        
        LOG.debug("doInBackground() finished");
        return this.result;
	}

    @Override
    protected void done() {
        LOG.debug("done(); isCancelled() = " + this.isCancelled());
        
        this.progressDialog.dispose();

        this.listener.didSearchByTitle(this.isCancelled(), this.result, this.movieFetchingData, this.thrownException);
    }
    
}
