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

package net.sourceforge.omov.app.gui.main;

import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import javax.swing.JOptionPane;

import net.sourceforge.omov.app.App;
import net.sourceforge.omov.app.gui.AboutDialog;
import net.sourceforge.omov.app.gui.CommonController;
import net.sourceforge.omov.app.gui.IPrevNextMovieProvider;
import net.sourceforge.omov.app.gui.doubletten.DuplicatesFinderProgressDialog;
import net.sourceforge.omov.app.gui.export.BackupImportDialog;
import net.sourceforge.omov.app.gui.export.ExporterChooserDialog;
import net.sourceforge.omov.app.gui.movie.AddEditMovieDialog;
import net.sourceforge.omov.app.gui.movie.EditMoviesDialog;
import net.sourceforge.omov.app.gui.preferences.PreferencesWindow;
import net.sourceforge.omov.app.gui.scan.ScanDialog;
import net.sourceforge.omov.app.gui.smartcopy.SmartCopyDialog;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.MovieFolderInfo;
import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.core.bo.VersionedMovies;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.core.tools.export.ImportExportConstants;
import net.sourceforge.omov.core.tools.osx.FinderReveal;
import net.sourceforge.omov.core.tools.remote.IRemoteDataReceiver;
import net.sourceforge.omov.core.tools.remote.RemoteServer;
import net.sourceforge.omov.core.tools.scan.Scanner;
import net.sourceforge.omov.core.tools.vlc.VlcPlayerFactory;
import net.sourceforge.omov.core.util.CoverUtil;
import net.sourceforge.omov.core.util.FileUtil;
import net.sourceforge.omov.core.util.UserSniffer;
import net.sourceforge.omov.qtjApi.QtjVideoPlayerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class MainWindowController extends CommonController<Movie> implements IRemoteDataReceiver {

    private static final Log LOG = LogFactory.getLog(MainWindowController.class);
    
    private static final IMovieDao DAO = BeanFactory.getInstance().getMovieDao();
    
    private final MainWindow mainWindow;

    /** lazy initialized and stored */
    private PreferencesWindow preferencesWindow;
    
    
    
    public MainWindowController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        
//        RemoteServer.getInstance().setMainReceiver(this);
    }

    public void doAddMovie() {
        LOG.info("doAddMovie()");
        final AddEditMovieDialog addDialog = AddEditMovieDialog.newAddDialog(this.mainWindow);
        addDialog.setVisible(true);
        
        if(addDialog.isActionConfirmed()) {
            final Movie newMovie = addDialog.getConfirmedObject();
            try {
                final Movie insertedMovie = DAO.insertMovie(newMovie);
                if(addDialog.isCoverChanged() == true) {
                    CoverUtil.resetCover(insertedMovie);
                }
                this.mainWindow.didAddMovie(insertedMovie);
                
            } catch (BusinessException e) {
                LOG.error("Could not add movie '"+newMovie+"'!", e);
                GuiUtil.error(this.mainWindow, "Adding movie failed", "Could not save movie '"+newMovie.getTitle()+"'!");
            }
        }
        LOG.debug("doAddMovie() finished");
    }

    public void doDeleteMovie(final Movie movie) {
        LOG.info("doDeleteMovie("+movie+")");
        
        int confirmed = JOptionPane.showConfirmDialog(this.mainWindow, "Do you really want to delete '"+movie.getTitle()+"'?", "Delete Movie", JOptionPane.YES_NO_OPTION);
        if(confirmed == JOptionPane.YES_OPTION) {
            try {
                DAO.deleteMovie(movie);
                CoverUtil.deleteCoverFileIfNecessary(movie);
            } catch (BusinessException e) {
                LOG.error("Deleting movie failed: " + movie, e);
                GuiUtil.error(this.mainWindow, "Deleting Movie failed", "Could not delete movie '"+movie.getTitle()+"'!");
            }
            LOG.debug("doDeleteMovie("+movie+") finished");
        } else {
            LOG.debug("Deleting movie by user aborted.");
        }        
    }
    
    public void doDeleteMovies(final List<Movie> moviesToDelete) {
        LOG.debug("doDeleteMovies(moviesToDelete.size="+moviesToDelete.size()+")");
        assert(moviesToDelete.size() > 1);

        int confirmed = JOptionPane.showConfirmDialog(this.mainWindow, "Do you really want to delete "+moviesToDelete.size()+" movies at once?", "Deleting Movies", JOptionPane.YES_NO_OPTION);
        if(confirmed == JOptionPane.YES_OPTION) {
            
            final boolean wasAutoCommitEnabled = DAO.isAutoCommit();
            boolean committed = false;
            Movie movie = null;
            try {
                DAO.setAutoCommit(false);
                for (Movie m : moviesToDelete) {
                    movie = m;
                    DAO.deleteMovie(m);
                }
                
                DAO.commit();
                committed = true;
            } catch (BusinessException e) {
                LOG.error("Deleting movie failed: " + movie, e);
                GuiUtil.error(this.mainWindow, "Deleting Movie failed", "Could not delete movie '"+movie.getTitle()+"'!");
                return;
            } finally {
                if(committed == false) DAO.rollback();
                DAO.setAutoCommit(wasAutoCommitEnabled);
            }
            for (Movie m : moviesToDelete) {
                try {
                    CoverUtil.deleteCoverFileIfNecessary(m);
                } catch (BusinessException e) {
                    LOG.warn("Could not delete Coverfile for Movie '"+m.getTitle()+"' (ID="+m.getId()+")!");
                }
            }
            LOG.debug("doDeleteMovies() finished");
        } else {
            LOG.debug("Deleting movie by user aborted.");
        }
    }
    
    
    public void doQuickview(Movie movie) {

		if(movie.getFiles().size() == 0) {
			GuiUtil.info(this.mainWindow, "QuickView", "No files to play for movie '"+movie.getTitle()+"'.");
			return;
		}
		
		final File movieFile = new File(movie.getFolderPath(), movie.getFiles().get(0));
		if(movieFile.exists() == false) {
			GuiUtil.warning(this.mainWindow, "QuickView", "File does not exist: " + movieFile.getAbsolutePath());
			return;
		}
		
    	try {
    		QtjVideoPlayerFactory.newVideo(movie, movieFile, this.mainWindow).setVisible(true);
//			new MoviePlayer(movie, movieFile, this.mainWindow).setVisible(true);
		} catch (Exception e) {
			LOG.error("Could not play movie file '"+movieFile.getAbsolutePath()+"'!", e);
			GuiUtil.error(this.mainWindow, "QuickView", "Playing movie file failed!");
		}
    }
    

    // should be only invoked by menu bar
    public void doEditMovie() {
        List<Movie> selectedMovies = this.mainWindow.getSelectedMovies();
        if(selectedMovies.size() == 0) {
            // should not be possible anyway!
            assert(false);
            GuiUtil.warning(this.mainWindow, "Edit Movie", "Not any movie was selected.");
            
        } else if(selectedMovies.size() == 1) {
            this.doEditMovie(selectedMovies.get(0), this.mainWindow.newPrevNextMovieProvider());
        } else {
            assert(selectedMovies.size() > 1);
            this.doEditMovies(selectedMovies);
        }
    }

    // should be only invoked by menu bar
    public void doDeleteMovie() {
        LOG.debug("Delete method for menubar invoked.");
        final List<Movie> selectedMovies = this.mainWindow.getSelectedMovies();
        
        if(selectedMovies.size() == 0) {
            // should not be possible anyway!
            assert(false);
            GuiUtil.warning(this.mainWindow, "Delete Movie", "Not any movie was selected.");
            
        } else if(selectedMovies.size() == 1) {
            this.doDeleteMovie(selectedMovies.get(0));
            
        } else {
            assert(selectedMovies.size() > 1);
            this.doDeleteMovies(selectedMovies);
        }
    }
    
    public void doEditMovie(final Movie originalMovie, IPrevNextMovieProvider prevNextProvider) {
        LOG.info("doEditMovie(" + originalMovie + ")");
        AddEditMovieDialog editDialog = AddEditMovieDialog.newEditDialog(this.mainWindow, originalMovie, prevNextProvider);
        editDialog.setVisible(true);
        
        if(editDialog.isActionConfirmed() == false) {
            LOG.debug("doEditMovie aborted by user");
            return;
        }
            Movie editMovie = null;
            try {
                editMovie = editDialog.getConfirmedObject();
                DAO.updateMovie(editMovie); // movie will stay temporary in a inconsistent state (coverFile could be something like /usr/home/pics/someFile.jpg)
                if(editDialog.isCoverChanged() == true) {
                    CoverUtil.resetCover(editMovie); // temporary inconsistency will corrected in here if necessary
                }

                this.mainWindow.didEditMovie(editMovie);
            } catch (BusinessException e) {
                LOG.error("Could not edit movie: " + editMovie, e);
                GuiUtil.error(this.mainWindow, "Edit failed", "Could not edit movie '"+originalMovie.getTitle()+"'!");
            }
            LOG.debug("doEditMovie finished");
            // this.mainWindow.reloadTableData(); <-- delete me
    }
    
    public void doEditMovies(final List<Movie> moviesToEdit) {
        assert(moviesToEdit.size() > 1);
        EditMoviesDialog dialog = new EditMoviesDialog(this.mainWindow, moviesToEdit);
        dialog.setVisible(true);
        
        if(dialog.isActionConfirmed() == true) {
            final Movie confirmedMovie = dialog.getConfirmedObject().get(0);
            DAO.setAutoCommit(false);
            try {
                for (Movie movieToEdit : moviesToEdit) {
                    final String title          = dialog.isFieldSelected(MovieField.TITLE)      ? confirmedMovie.getTitle()      : movieToEdit.getTitle();
                    final boolean seen          = dialog.isFieldSelected(MovieField.SEEN)       ? confirmedMovie.isSeen()        : movieToEdit.isSeen();
                    final int rating            = dialog.isFieldSelected(MovieField.RATING)     ? confirmedMovie.getRating()     : movieToEdit.getRating();
                    final String coverFile      = dialog.isFieldSelected(MovieField.COVER_FILE) ? confirmedMovie.getOriginalCoverFile()  : movieToEdit.getOriginalCoverFile();
                    final Set<String> genres    = dialog.isFieldSelected(MovieField.GENRES)     ? confirmedMovie.getGenres()     : movieToEdit.getGenres();
                    final Set<String> languages = dialog.isFieldSelected(MovieField.LANGUAGES)  ? confirmedMovie.getLanguages()  : movieToEdit.getLanguages();
                    final String style          = dialog.isFieldSelected(MovieField.STYLE)      ? confirmedMovie.getStyle()      : movieToEdit.getStyle();
                    final String director       = dialog.isFieldSelected(MovieField.DIRECTOR)   ? confirmedMovie.getDirector()   : movieToEdit.getDirector();
                    final Set<String> actors    = dialog.isFieldSelected(MovieField.ACTORS)     ? confirmedMovie.getActors()     : movieToEdit.getActors();
                    final int year              = dialog.isFieldSelected(MovieField.YEAR)       ? confirmedMovie.getYear()       : movieToEdit.getYear();
                    final String comment        = dialog.isFieldSelected(MovieField.COMMENT)    ? confirmedMovie.getComment()    : movieToEdit.getComment();
                    final Quality quality       = dialog.isFieldSelected(MovieField.QUALITY)    ? confirmedMovie.getQuality()    : movieToEdit.getQuality();
                    final int duration          = dialog.isFieldSelected(MovieField.DURATION)   ? confirmedMovie.getDuration()   : movieToEdit.getDuration();
                    final Resolution resolution = dialog.isFieldSelected(MovieField.RESOLUTION) ? confirmedMovie.getResolution() : movieToEdit.getResolution();
                    final Set<String> subtitles = dialog.isFieldSelected(MovieField.SUBTITLES)  ? confirmedMovie.getSubtitles()  : movieToEdit.getSubtitles();
                    
                    final Movie updatedMovie = Movie.newByMultipleEdited(movieToEdit, title, seen, rating, coverFile, genres, languages,
                                                style, director, actors, year, comment, quality, duration, resolution, subtitles);
                    DAO.updateMovie(updatedMovie);
                    
                    if(dialog.isCoverChanged() == true) { // eigentlich gehoert das raus aus der schleife! erst wenn db-aktionen fertig, dann covers loeschen
                        CoverUtil.resetCover(updatedMovie);
                    }
                }
                
                DAO.commit();
            } catch (BusinessException e) {
                DAO.rollback();
                LOG.error("Could not edit movies: " + Arrays.toString(moviesToEdit.toArray()), e);
                GuiUtil.error(this.mainWindow, "Edit failed", "Could not edit movies!");
            } finally {
                DAO.setAutoCommit(true);
            }
        }
    }

    // should be only invoked by menu bar
    public void doFetchMetaData() {
        List<Movie> selectedMovies = this.mainWindow.getSelectedMovies();
        if(selectedMovies.size() == 0) {
            // should not be possible anyway!
            assert(false);
            GuiUtil.warning(this.mainWindow, "Fetch Metadata", "Not any movie was selected.");
            
        } else if(selectedMovies.size() == 1) {
            this.doFetchMetaData(selectedMovies.get(0));
        } else {
            // should not be possible anyway!
            assert(false);
            GuiUtil.warning(this.mainWindow, "Fetch Metadata", "Can not fetch metadata for more than one Movie at the time.");
        }
    }
    public void doFetchMetaData(Movie movieFetchingData) {
    	this._doFetchMetaData(this.mainWindow, movieFetchingData); // this method will invoke didFetchedMetaData afterwards
    }

	@Override
	protected void didFetchedMetaData(Movie movieFetchingData, Movie metadataEnhancedMovie) {
        if(metadataEnhancedMovie == null) {
            return;
        }

        final Movie updatedMovie = Movie.updateByMetadataMovie(movieFetchingData, metadataEnhancedMovie);
        try {
            DAO.updateMovie(updatedMovie);
            if (updatedMovie.isCoverFileSet() == true) {
                CoverUtil.resetCover(updatedMovie);
            }
        } catch (BusinessException e) {
            LOG.error("Could not update movie '" + updatedMovie + "' with metadata '" + metadataEnhancedMovie + "'!", e);
            GuiUtil.error(this.mainWindow, "Edit with Metadata failed", "Could not enhance movie '" + updatedMovie.getTitle()
                    + "' with given metadata!");
        }
	}
	
	
    
    public void doRevealMovie(Movie movie) {
        assert(UserSniffer.isMacOSX());
        final String folderPath = movie.getFolderPath();
        LOG.debug("Revealing folder path '"+folderPath+"' for movie with id "+movie.getId()+" and title '"+movie.getTitle()+"'.");
        if(folderPath.trim().length() == 0) {
            GuiUtil.warning("Revealing movie folder", "No folder set for movie '"+movie.getTitle()+"'!");
            return;
        }
        if(new File(folderPath).exists() == false) {
            GuiUtil.error("Revealing movie folder", "The folder at '"+movie.getFolderPath()+"' does not exist!");
            return;
        }
        
        try {
            FinderReveal.revealFile(new File(folderPath));
        } catch (BusinessException e) {
            LOG.error("Could not reveal movie "+movie+"!", e);
            GuiUtil.error("Revealing movie in Finder failed!", "Could not reveal movie folder path at '"+movie.getFolderPath()+"'!");
        }
    }

    // should be only invoked by menu bar
    public void doRevealMovie() {
        assert(UserSniffer.isMacOSX());
        List<Movie> selectedMovies = this.mainWindow.getSelectedMovies();
        if(selectedMovies.size() == 0) {
            // should not be possible anyway!
            assert(false);
            GuiUtil.warning(this.mainWindow, "Reveal Movie in Finder", "Not any movie was selected.");
        } else if(selectedMovies.size() == 1) {
            this.doRevealMovie(selectedMovies.get(0));
        } else {
            // should not be possible anyway!
            assert(false);
            GuiUtil.info(this.mainWindow, "Reveal Movie in Finder", "Revealing multiple movies in Finder is not supported.");
        }
    }
    

    public void doPlayVlc(Movie movie) {
        assert(VlcPlayerFactory.isVlcCapable());
        
        final List<String> movieFileNames = movie.getFiles();
        if(movieFileNames.isEmpty() == true) {
            GuiUtil.warning("Play in VLC", "There is not any file to play for movie '"+movie.getTitle()+"'!");
            return;
        }
        
//        final File movieFile = new File(movie.getFolderPath(), files.iterator().next());
//        if(movieFile.exists() == false) {
//            GuiUtil.error("Play in VLC", "The file at '"+movieFile.getAbsolutePath()+"' does not exist!");
//            return;
//        }
        
        File firstMovieFile = null;
        try {
        	assert(movieFileNames.size() > 0);
        	for (String movieFileName : movieFileNames) {
        		final File movieFile = new File(movie.getFolderPath(), movieFileName);
        		if(firstMovieFile == null) firstMovieFile = movieFile;
        		App.VLC_PLAYER.addFileToPlaylist(movieFile);
        	}
        	App.VLC_PLAYER.playFile(firstMovieFile);
        	
        } catch (BusinessException e) {
            LOG.error("Could not play file '"+firstMovieFile.getAbsolutePath()+"' in VLC!");
            GuiUtil.error("Play in VLC failed", "Could not play file '"+firstMovieFile.getAbsolutePath()+"' in VLC!");
        }
    }

    // should be only invoked by menu bar
    public void doPlayVlc() {
        assert(VlcPlayerFactory.isVlcCapable());
        
        final List<Movie> selectedMovies = this.mainWindow.getSelectedMovies();
        if(selectedMovies.size() == 0) {
            // should not be possible anyway!
            assert(false);
            GuiUtil.warning(this.mainWindow, "Play Movie in VLC", "Not any movie was selected.");
        } else if(selectedMovies.size() == 1) {
            this.doPlayVlc(selectedMovies.get(0));
            
        } else {
            // should not be possible anyway!
            assert(false);
            GuiUtil.info(this.mainWindow, "Play Movie in VLC", "Playing multiple movies in VLC is not supported.");
        }
    }
    
    public void doRescanFolders() {
    	LOG.info("doRescanFolders()");
    	
        final List<Movie> selectedMovies = this.mainWindow.getSelectedMovies();
        if(selectedMovies.size() == 0) {
            // should not be possible anyway!
            assert(false);
            GuiUtil.warning(this.mainWindow, "Rescan Folder(s)", "Not any movie was selected.");
            return;
        }
        
        final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
        for (Movie movie : selectedMovies) {
        	LOG.debug("Try to rescan " + movie);
        	if(movie.isFolderPathSet() == false) {
        		LOG.debug("Skipping movie because folderpath is not set.");
        		continue;
        	}
        	
        	final File folder = new File(movie.getFolderPath());
        	LOG.debug("Rescanning folder '"+folder.getAbsolutePath()+"' (exists="+folder.exists()+").");
        	final Movie rescannedMovie;
        	if(folder.exists() == false) {
        		rescannedMovie = Movie.newByOtherMovieFolderInfo(movie, null);
        	} else {
        		MovieFolderInfo folderInfo = Scanner.scanMovieFolderInfo(folder);
        		rescannedMovie = Movie.newByOtherMovieFolderInfo(movie, folderInfo);
        	}
        	if(movie.equals(rescannedMovie) == false) {
	        	try {
					dao.updateMovie(rescannedMovie);
				} catch (BusinessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
        	}
		}
    }
    
    public void doShowPreferences() {
        if(this.preferencesWindow == null) {
            this.preferencesWindow = new PreferencesWindow(this.mainWindow, this);
        }
        this.preferencesWindow.setVisible(true);
    }
    
    public void doScan() {
        LOG.info("doScan");
        new ScanDialog(this.mainWindow).setVisible(true);

    }
    
//    public void doRemoteConnect() {
//        final RemoteConnectDialog dialog = new RemoteConnectDialog(this.mainWindow);
//        dialog.setVisible(true);
//    }

    public void doFindDuplicates() {
        LOG.info("doFindDuplicates");
        new DuplicatesFinderProgressDialog(this.mainWindow).setVisible(true);
    }
    public void doImportBackup(File backupFile) {
        final String extension = FileUtil.extractExtension(backupFile);
        if(extension == null || extension.equalsIgnoreCase(ImportExportConstants.BACKUP_FILE_EXTENSION) == false) {
            GuiUtil.warning(this.mainWindow, "Import Failed", "The file '"+backupFile.getAbsolutePath()+"' is not a valid backup file!");
            return;
        }
        
        try {
            new ZipFile(backupFile); // will throw ZipException, if given file is not a backup file
            final BackupImportDialog dialog = new BackupImportDialog(this.mainWindow);
            dialog.setZipFile(backupFile);
            dialog.setVisible(true);
            
        } catch (ZipException e) {
            LOG.info("Could not open backupfile '"+backupFile.getAbsolutePath()+"'!", e);
            GuiUtil.error(this.mainWindow, "Import Failed", "The backupfile '"+backupFile.getAbsolutePath()+"' is corrupted!");
        } catch (IOException e) {
            LOG.info("Could not open backupfile '"+backupFile.getAbsolutePath()+"'!", e);
            GuiUtil.error(this.mainWindow, "Import Failed", "Could not open backupfile '"+backupFile.getAbsolutePath()+"'!");
        }
    }

    public void doImportBackup() {
        new BackupImportDialog(this.mainWindow).setVisible(true);
    }

    public void doExport() {
        ExporterChooserDialog chooser = new ExporterChooserDialog(this.mainWindow, this);
        chooser.setVisible(true);
    }
    
    public void doShowAbout() {
        final AboutDialog dialog = new AboutDialog(this.mainWindow);
        dialog.setVisible(true);
    }
    
    public void doQuit() {
        LOG.debug("doQuit() invoked");
        
        if(RemoteServer.getInstance().isRunning()) {
            try {
                RemoteServer.getInstance().shutDown();
            } catch (BusinessException e) {
                LOG.error("Could not shutdown running remoteserver!", e);
                GuiUtil.warning("Shutdown error", "Could not shutdown running remoteserver!");
            }
        }
        
        try {
            BeanFactory.getInstance().getDatabaseConnection().close();
        } catch (BusinessException e) {
            LOG.error("Could not close database connection!", e);
            GuiUtil.warning("Shutdown error", "Could not close database connection!");
        }
        
        this.mainWindow.dispose();
        System.exit(0);
    }
    
    public void doSmartCopy() {
        final SmartCopyDialog dialog = new SmartCopyDialog(this.mainWindow, this);
        dialog.setVisible(true);
    }
    
    public void doHandleFile(final String filename) {
        LOG.info("Handling file '" + filename + "'.");
        if(this.mainWindow.isActivated() == false) { // MINOR activated is also false, if osx user drops omo-file onto dock icon
            LOG.info("Can not handle file '" + filename + "' because main windows is currently not activated.");
            Toolkit.getDefaultToolkit().beep();
            return;
        }
        
        this.doImportBackup(new File(filename));
    }
    
    
    /**
     * @see IRemoteDataReceiver#acceptTransmission(String)
     */
    public boolean acceptTransmission(String hostAddress) {
        final int result = JOptionPane.showConfirmDialog(this.mainWindow, "Do you want to accept connection from ["+hostAddress+"]?", "Connection requested", JOptionPane.YES_NO_OPTION);
        final boolean accepted = result == JOptionPane.YES_OPTION;
//        waitingDataReceivingDialog.setVisible(true); T O D O   but do not block gui; make new thread
        return accepted;
    }
    
//    private JDialog waitingDataReceivingDialog = new JDialog();

    /**
     * @see IRemoteDataReceiver#dataReceived(VersionedMovies)
     */
    public void dataReceived(VersionedMovies movies) {
        JOptionPane.showMessageDialog(this.mainWindow, movies.toString(), "Data received!", JOptionPane.INFORMATION_MESSAGE);
    }
    
    public List<Movie> getVisibleTableMovies() {
        return this.mainWindow.getVisibleMovies();
    }
    public List<Movie> getSelectedTableMovies() {
        return this.mainWindow.getSelectedMovies();
    }
}
