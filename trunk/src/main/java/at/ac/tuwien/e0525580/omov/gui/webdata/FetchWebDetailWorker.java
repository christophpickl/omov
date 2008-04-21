package at.ac.tuwien.e0525580.omov.gui.webdata;

import javax.swing.JDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebImdbExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebSearchResult;

public class FetchWebDetailWorker extends SwingWorker<Movie, String> {

    private static final Log LOG = LogFactory.getLog(FetchWebDetailWorker.class);
    
    private final JDialog progressDialog;
    private final WebSearchResult searchResult;
    private final IFetchedWebDetail listener;
    private Movie movie = null;
    private Exception thrownException= null;
    
    public FetchWebDetailWorker(JDialog progressDialog, WebSearchResult searchResult, IFetchedWebDetail listener) {
        this.progressDialog = progressDialog;
        this.searchResult = searchResult;
        this.listener = listener;
    }
    
    @Override
    protected Movie doInBackground() throws Exception {
        LOG.debug("doInBackground() started");
        
        try {
            final IWebExtractor ex = new WebImdbExtractor();
            LOG.info("Fetching details for searchresult '"+searchResult+"'");
            this.movie = ex.getDetails(searchResult, true);
        } catch(Exception e) {
            this.thrownException = e;
        }
        
        LOG.debug("doInBackground() finished");
        return this.movie;
    }
    
    @Override
    protected void done() {
        LOG.debug("done(); isCancelled() = " + this.isCancelled());
        
        this.progressDialog.dispose();

        final Movie fetchedMovie = this.isCancelled() ? null : this.movie; // this.get() can throw exception :( therefore use own instance variable
        this.listener.didFetchedWebDetail(this.isCancelled(), this.searchResult, fetchedMovie, this.thrownException);
    }
    
    public static interface IFetchedWebDetail {
        /** movie instance, or null if user aborted work */
        void didFetchedWebDetail(boolean wasCancelled, WebSearchResult searchResult, Movie movie, Exception exception);
    }
}