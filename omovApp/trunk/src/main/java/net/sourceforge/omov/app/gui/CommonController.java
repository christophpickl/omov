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

package net.sourceforge.omov.app.gui;

import java.awt.Component;
import java.io.File;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.app.gui.webdata.IWebSearchWorkerListener;
import net.sourceforge.omov.app.gui.webdata.WebSearchProgress;
import net.sourceforge.omov.app.gui.webdata.WebSearchResultsDialog;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.gui.OmovGuiUtil;
import net.sourceforge.omov.qtjApi.QtjFactory;
import net.sourceforge.omov.webApi.WebSearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class CommonController<M extends Movie> implements IWebSearchWorkerListener<M> {	
	
    private static final Log LOG = LogFactory.getLog(CommonController.class);
    
    protected void _doFetchMetaData(JFrame owner, M movieFetchingData) {
        LOG.info("fetching metadata for movie '"+movieFetchingData+"'.");
        
        final WebSearchProgress<M> progressDialog = new WebSearchProgress<M>(owner, owner, movieFetchingData, this);
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                progressDialog.setVisible(true); // will invoke didSearchByTitle later on
            }
        });
    }
    
    public final void didSearchByTitle(boolean wasCancelled, List<WebSearchResult> result, M movieFetchingData, Exception thrownException) {
    	LOG.debug("didSearchByTitle(wasCancelled="+wasCancelled+",result.size="+(result == null ? "null" : new Integer(result.size()))+")");
    	if(wasCancelled == true) {
    		return;
    	}
    	
        final WebSearchResultsDialog dialog = new WebSearchResultsDialog(null, result);
        dialog.setVisible(true);
        
        if(dialog.isActionConfirmed()) {
            final Movie newMovie = dialog.getMovie();
            LOG.info("Confirmed new metadata for movie '"+newMovie+"'");
            this.didFetchedMetaData(movieFetchingData, newMovie);
        } else {
        	this.didFetchedMetaData(movieFetchingData, null);
        }
    }
    
    /**
     * 
     * @param movie can be null
     */
    protected abstract void didFetchedMetaData(M movieFetchingData, Movie metadataEnhancedMovie);
    

    protected final void doPlayQuickView(Component owner, Movie movie) {
    	assert(QtjFactory.isQtjAvailable() == true);
    	if(owner == null) {
    		throw new NullPointerException("owner");
    	}
    	
		if(movie.getFiles().size() == 0) {
			PtGuiUtil.info(owner, "QuickView", "No files to play for movie '"+movie.getTitle()+"'.");
			return;
		}

		// FIXME QTJ - do not select file, but let QtjVideoPlayerImpl decide which file!
		final File movieFile = new File(movie.getFolderPath(), movie.getFiles().get(0));
		if(movieFile.exists() == false) {
			PtGuiUtil.warning(owner, "QuickView", "File does not exist: " + movieFile.getAbsolutePath());
			return;
		}
		
    	try {
    		QtjFactory.newQtjVideoPlayer(movie, movieFile).setVisible(true);
//			new MoviePlayer(movie, movieFile, this.mainWindow).setVisible(true);
		} catch (BusinessException e) {
			LOG.error("Could not play movie file '"+movieFile.getAbsolutePath()+"'!", e);
			
			final String errMsg = "Playing movie file failed because of some internal error!";
			final String errTitle = "QuickView";
			if(owner instanceof JDialog) {
				OmovGuiUtil.error((JDialog) owner, errTitle, errMsg, e);
			} else if(owner instanceof JFrame) {
				OmovGuiUtil.error((JFrame) owner, errTitle, errMsg, e);
			} else {
				throw new IllegalArgumentException("Invalid owner class: " + owner.getClass().getName());
			}
			
		}
    }
}
