/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.app.gui.webdata;

import javax.swing.JDialog;

import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.webApi.IWebDataFetcher;
import net.sourceforge.omov.webApi.WebDataFetcherFactory;
import net.sourceforge.omov.webApi.WebSearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
            final IWebDataFetcher ex = WebDataFetcherFactory.newWebDataFetcher();
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