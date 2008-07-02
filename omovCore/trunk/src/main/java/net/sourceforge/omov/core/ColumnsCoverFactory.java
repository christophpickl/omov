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

package net.sourceforge.omov.core;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import net.sourceforge.omov.core.bo.CoverFileType;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.model.IMovieDaoListener;
import net.sourceforge.omov.core.util.CoverUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * stores thumbnail images for movies.
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ColumnsCoverFactory implements IMovieDaoListener {

    private static final Log LOG = LogFactory.getLog(ColumnsCoverFactory.class);
    private static final ColumnsCoverFactory INSTANCE = new ColumnsCoverFactory();

    private final Map<Movie, ImageIcon> movieIcons = new HashMap<Movie, ImageIcon>();
    
    
    private ColumnsCoverFactory() {
        BeanFactory.getInstance().getMovieDao().addMovieDaoListener(this);
    }
    
    public static ColumnsCoverFactory getInstance() {
        return INSTANCE;
    }
    
    public ImageIcon getImage(final Movie movie) {
        final ImageIcon storedIcon = this.movieIcons.get(movie);
        if(storedIcon != null) {
            return storedIcon;
        }
        
        if(movie.isCoverFileSet() == true) {
            final ImageIcon coverIcon = this.loadImage(movie);
            this.movieIcons.put(movie, coverIcon);
            return coverIcon;
        }
        
        return null;
    }
    
    private ImageIcon loadImage(final Movie movie) {
        if(movie.isCoverFileSet() == false) {
            return null;
        }
        
        final ImageIcon coverIcon = CoverUtil.getMovieCoverImage(movie, CoverFileType.THUMBNAIL);
        LOG.debug("Loaded cover file '"+coverIcon+"' (file='"+movie.getCoverFile(CoverFileType.THUMBNAIL)+"') for movie '"+movie.getTitle()+"' (id="+movie.getId()+").");
        return coverIcon;
    }

    public void movieDataChanged() {
        LOG.debug("Reloading coverimages for movietable columns...");
        
        // clearing the map is sufficient
        this.movieIcons.clear();
        
        
//        final Set<Movie> coverFilesToRemove = new HashSet<Movie>();
//        final Map<Movie, ImageIcon> coverFilesToAdd = new HashMap<Movie, ImageIcon>();
//        
//        for (final Movie movie : this.movieIcons.keySet()) {
//            final ImageIcon storedIcon = this.movieIcons.get(movie);
//            
//            if(movie.isCoverFileSet() == true) {
//                final ImageIcon coverIcon = this.loadImage(movie);
//                coverFilesToAdd.put(movie, coverIcon);
//                
//            } else { // (movie.isCoverFileSet() == false)
//                if(storedIcon != null) {
//                    coverFilesToRemove.add(movie);
//                }
//            }
//        }
//        
//        for (Movie coverToRemove : coverFilesToRemove) {
//            this.movieIcons.put(coverToRemove, null);
//        }
//        this.movieIcons.putAll(coverFilesToAdd);
        
    }
    
}
