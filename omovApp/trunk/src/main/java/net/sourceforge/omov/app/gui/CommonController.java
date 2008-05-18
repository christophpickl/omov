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

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
