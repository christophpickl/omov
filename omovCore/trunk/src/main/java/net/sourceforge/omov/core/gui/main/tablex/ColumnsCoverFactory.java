package net.sourceforge.omov.core.gui.main.tablex;

import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.bo.CoverFileType;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.model.IMovieDaoListener;
import net.sourceforge.omov.core.util.CoverUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * stores thumbnail images for movies.
 */
class ColumnsCoverFactory implements IMovieDaoListener {

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
