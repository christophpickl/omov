package at.ac.tuwien.e0525580.omov.gui.main;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.bo.VersionedMovies;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.CommonController;
import at.ac.tuwien.e0525580.omov.gui.IPrevNextMovieProvider;
import at.ac.tuwien.e0525580.omov.gui.SmartCopyDialog;
import at.ac.tuwien.e0525580.omov.gui.export.ExporterChooserDialog;
import at.ac.tuwien.e0525580.omov.gui.movie.AddEditMovieDialog;
import at.ac.tuwien.e0525580.omov.gui.movie.EditMoviesDialog;
import at.ac.tuwien.e0525580.omov.gui.preferences.PreferencesWindow;
import at.ac.tuwien.e0525580.omov.gui.scan.ScanDialog;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.tools.remote.IRemoteDataReceiver;
import at.ac.tuwien.e0525580.omov.tools.remote.RemoteServer;
import at.ac.tuwien.e0525580.omov.util.CoverUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;


final class MainWindowController extends CommonController implements IRemoteDataReceiver {

    private static final Log LOG = LogFactory.getLog(MainWindowController.class);
    
    private static final IMovieDao DAO = BeanFactory.getInstance().getMovieDao();
    
    private final MainWindow mainWindow;
    
    public MainWindowController(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
        RemoteServer.getInstance().setMainReceiver(this);
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
                DAO.setAutoCommit(true);
            }
            for (Movie m : moviesToDelete) {
                try {
                    CoverUtil.deleteCoverFileIfNecessary(m);
                } catch (BusinessException e) {
                    GuiUtil.error(this.mainWindow, "Deleting Coverfile failed", "Could not delete Coverfile for Movie '"+m.getTitle()+"' (ID="+m.getId()+")!");
                }
            }
            LOG.debug("doDeleteMovies() finished");
        } else {
            LOG.debug("Deleting movie by user aborted.");
        }
    }
    

    // should be only invoked by menu bar
    public void doEditMovie() {
        List<Movie> selectedMovies = this.mainWindow.getSelectedMovies();
        if(selectedMovies.size() == 0) { // FEATURE gar nicht erst dazu kommen lassen! editButtons disablen, wenn nix selected ist.
            GuiUtil.warning(this.mainWindow, "Edit Movie", "Not any movie was selected.");
            return;
        }
        if(selectedMovies.size() == 1) {
            this.doEditMovie(selectedMovies.get(0), this.mainWindow.newPrevNextMovieProvider());
        } else {
            assert(selectedMovies.size() > 1);
            this.doEditMovies(selectedMovies);
        }
    }
    
    public void doEditMovie(final Movie originalMovie, IPrevNextMovieProvider prevNextProvider) {
        LOG.info("doEditMovie(" + originalMovie + ")");
        AddEditMovieDialog editDialog = AddEditMovieDialog.newEditDialog(this.mainWindow, originalMovie, prevNextProvider);
        editDialog.setVisible(true);
        
        if(editDialog.isActionConfirmed() == true) {
            Movie editMovie = null;
            try {
                editMovie = editDialog.getConfirmedObject();
                DAO.updateMovie(editMovie); // movie will stay temporary in a inconsistent state (coverFile could be something like /usr/home/pics/someFile.jpg)
                if(editDialog.isCoverChanged() == true) {
                    CoverUtil.resetCover(editMovie); // temporary inconsistency will corrected in here if necessary
                }
            } catch (BusinessException e) {
                LOG.error("Could not edit movie: " + editMovie, e);
                GuiUtil.error(this.mainWindow, "Edit failed", "Could not edit movie '"+originalMovie.getTitle()+"'!");
            }
            LOG.debug("doEditMovie finished");
        } else {
            LOG.debug("doEditMovie aborted by user");
        }
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
                    final String coverFile      = dialog.isFieldSelected(MovieField.COVER_FILE) ? confirmedMovie.getCoverFile()  : movieToEdit.getCoverFile();
                    final Set<String> genres    = dialog.isFieldSelected(MovieField.GENRES)     ? confirmedMovie.getGenres()     : movieToEdit.getGenres();
                    final Set<String> languages = dialog.isFieldSelected(MovieField.LANGUAGES)  ? confirmedMovie.getLanguages()  : movieToEdit.getLanguages();
                    final String style          = dialog.isFieldSelected(MovieField.STYLE)      ? confirmedMovie.getStyle()      : movieToEdit.getStyle();
                    final String director       = dialog.isFieldSelected(MovieField.DIRECTOR)   ? confirmedMovie.getDirector()   : movieToEdit.getDirector();
                    final Set<String> actors    = dialog.isFieldSelected(MovieField.ACTORS)     ? confirmedMovie.getActors()     : movieToEdit.getActors();
                    final int year              = dialog.isFieldSelected(MovieField.YEAR)       ? confirmedMovie.getYear()       : movieToEdit.getYear();
                    final String comment        = dialog.isFieldSelected(MovieField.COMMENT)    ? confirmedMovie.getComment()    : movieToEdit.getComment();
                    final Quality quality           = dialog.isFieldSelected(MovieField.QUALITY)    ? confirmedMovie.getQuality()    : movieToEdit.getQuality();
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
        if(selectedMovies.size() == 0) { // FEATURE gar nicht erst dazu kommen lassen! editButtons disablen, wenn nix selected ist.
            GuiUtil.warning(this.mainWindow, "Fetch Metadata", "Not any movie was selected.");
            return;
        }
        if(selectedMovies.size() == 1) {
            this.doFetchMetaData(selectedMovies.get(0));
        } else {
            GuiUtil.warning(this.mainWindow, "Fetch Metadata", "Can not fetch metadata for more than one Movie at the time.");
        }
    }
    public void doFetchMetaData(Movie movieFetchingData) {
        final Movie metadataEnhancedMovie = this._doFetchMetaData(this.mainWindow, movieFetchingData);
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
    
    public void doShowPreferences() {
        final PreferencesWindow preferencesWindow = new PreferencesWindow(this.mainWindow);
        preferencesWindow.setVisible(true);
    }
    
    public void doScan() {
        LOG.info("doScan");
        new ScanDialog(this.mainWindow).setVisible(true);

    }
    
//    public void doRemoteConnect() {
//        final RemoteConnectDialog dialog = new RemoteConnectDialog(this.mainWindow);
//        dialog.setVisible(true);
//    }
    

    // import from exported *.omo file; or XML file
    public void doImport() {
        JOptionPane.showMessageDialog(this.mainWindow, "Importing not yet implemented.", "Ups", JOptionPane.INFORMATION_MESSAGE);
    }

    public void doExport() {
        ExporterChooserDialog chooser = new ExporterChooserDialog(this.mainWindow);
        chooser.setVisible(true);
    }
    
    
    public void doShowHelp() {
        GuiUtil.info("ups", "Help not yet implemented!");
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
    }
    
    public void doSmartCopy() {
        final SmartCopyDialog dialog = new SmartCopyDialog(this.mainWindow);
        dialog.setVisible(true);
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
}
