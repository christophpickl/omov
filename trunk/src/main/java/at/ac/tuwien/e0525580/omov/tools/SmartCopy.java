package at.ac.tuwien.e0525580.omov.tools;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class SmartCopy {
    
    private static final Log LOG = LogFactory.getLog(SmartCopy.class);
    
    public SmartCopy() {
        
    }
    
    // TODO use progress bar
    public void copy(final int ids[], final File targetDirectory, Component owner) throws BusinessException {

        if(targetDirectory.exists() && targetDirectory.isFile()) {
            throw new BusinessException("Target is an already existing file!");
        }
        
        if(targetDirectory.exists() == true) {
            boolean confirmed = GuiUtil.getYesNoAnswer(owner, "SmartCopy", "The target directory at '"+targetDirectory.getAbsolutePath()+"' already exists.\nDo you want to reuse it?");
            if(confirmed == false) {
                return;
            }
        } else {
            final boolean dirsCreated = targetDirectory.mkdirs();
            if(dirsCreated == false) {
                throw new BusinessException("Could not create target directory '"+targetDirectory.getAbsolutePath()+"'!");
            }
        }
        
        
        final List<Movie> movies = new ArrayList<Movie>(ids.length);
        final IMovieDao movieDao = BeanFactory.getInstance().getMovieDao();
        
        for (int id : ids) {
            final Movie movie = movieDao.getMovie(id);
            if(movie == null) {
                throw new BusinessException("Could not find Movie with id "+id+"!");
            }
            movies.add(movie);
        }
        
        for (Movie movie : movies) {
            // if (user aborted) break + cleanup;
            
            if(movie.getFolderPath().length() == 0) {
                // TODO return warning to user; no folder path was set!
                continue;
            }
            final File movieFolder = new File(movie.getFolderPath());
            if(movieFolder.exists() == false) {
                LOG.warn("The movie folder at '"+movieFolder.getAbsolutePath()+"' does not exist anymore!");
                // TODO return warning to user
                continue;
            }
            
            FileUtil.copyDirectoryRecursive(movieFolder, targetDirectory);
        }
        // copying complete
    }
}
