package at.ac.tuwien.e0525580.omov.tools;

import java.awt.Component;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

public class SmartCopy {

    private static final Log LOG = LogFactory.getLog(SmartCopy.class);

    public SmartCopy() {

    }

    // TODO use progress bar
    public void copy(final int ids[], final File targetDirectory, Component owner) throws BusinessException {

        if (targetDirectory.exists() && targetDirectory.isFile()) {
            throw new BusinessException("Target is an already existing file!");
        }
        
        final List<Movie> movies = this.getMoviesById(ids);

        //      if(targetDirectory.exists() == true) {
        //          boolean confirmed = GuiUtil.getYesNoAnswer(owner, "SmartCopy", "The target directory at '"+targetDirectory.getAbsolutePath()+"' already exists.\nDo you want to reuse it?");
        //          if(confirmed == false) {
        //              return;
        //          }
        //      } else {
        final boolean dirsCreated = targetDirectory.mkdirs();
        if (dirsCreated == false) {
            throw new BusinessException("Could not create target directory '" + targetDirectory.getAbsolutePath() + "'!");
        }
        //      }

        final List<File> createdDirectories = new LinkedList<File>();
        boolean successfullyCopied = false;
        try {
            for (Movie movie : movies) {
                // if (user aborted) break + cleanup;

                if (movie.getFolderPath().length() == 0) {
                    // TODO return warning to user; no folder path was set!
                    continue;
                }
                final File movieFolder = new File(movie.getFolderPath());
                if (movieFolder.exists() == false) {
                    LOG.warn("The movie folder at '" + movieFolder.getAbsolutePath() + "' does not exist anymore!");
                    // TODO return warning to user
                    continue;
                }

                final File createdDirectory = FileUtil.copyDirectoryRecursive(movieFolder, targetDirectory);
                createdDirectories.add(createdDirectory);
            }
            successfullyCopied = true;
            
        } finally {
            if(successfullyCopied == false) {
                cleanup(createdDirectories, targetDirectory);
            }
        }
        // copying complete
    }
    
    private List<Movie> getMoviesById(final int ids[]) throws BusinessException {
        final List<Movie> movies = new ArrayList<Movie>(ids.length);
        final IMovieDao movieDao = BeanFactory.getInstance().getMovieDao();

        for (int id : ids) {
            final Movie movie = movieDao.getMovie(id);
            if (movie == null) {
                throw new BusinessException("Could not find Movie with id " + id + "!");
            }
            movies.add(movie);
        }
        
        return movies;
    }
    
    private static void cleanup(List<File> createdDirectories, File targetDirectory) {
        LOG.debug("Cleaning up SmartCopy folders.");
        for (File createdDirectory : createdDirectories) {
            try {
                FileUtil.deleteDirectoryRecursive(createdDirectory);
            } catch(Exception e) {
                LOG.error("Could not cleanup SmartCopy created directory '"+createdDirectory.getAbsolutePath()+"'!", e);
            }
        }
        if(targetDirectory.listFiles().length == 0) {
            try {
                FileUtil.deleteDirectoryRecursive(targetDirectory);
            } catch(Exception e) {
                LOG.error("Could not delete SmartCopy created target directory '"+targetDirectory.getAbsolutePath()+"'!", e);
            }
        }
    }
}
