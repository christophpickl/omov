package net.sourceforge.omov.core.gui.scan;

import java.io.File;
import java.util.List;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.gui.CommonController;
import net.sourceforge.omov.core.gui.movie.AddEditMovieDialog;
import net.sourceforge.omov.core.tools.scan.IScanListener;
import net.sourceforge.omov.core.tools.scan.IScanner;
import net.sourceforge.omov.core.tools.scan.RepositoryPreparer;
import net.sourceforge.omov.core.tools.scan.ScanHint;
import net.sourceforge.omov.core.tools.scan.ScanThread;
import net.sourceforge.omov.core.tools.scan.ScannedMovie;
import net.sourceforge.omov.core.tools.scan.Scanner;
import net.sourceforge.omov.core.tools.scan.RepositoryPreparer.PreparerResult;
import net.sourceforge.omov.core.tools.webdata.IWebExtractor;
import net.sourceforge.omov.core.util.CoverUtil;
import net.sourceforge.omov.core.util.GuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ScanDialogController extends CommonController implements IScanListener {

    private static final Log LOG = LogFactory.getLog(ScanDialogController.class);
    
    private final ScanDialog dialog;
    
    private boolean scanInProgress = false;
    private ScanThread scanThread;
    private int dirCount = 1;
    private int dirCountMax;
    private String currentPhase;
    
    public ScanDialogController(ScanDialog dialog) {
        this.dialog = dialog;
    }
    

    public void doPrepareRepository(final File directory) {
        LOG.info("doPrepareRepoistory on directory '"+directory.getAbsolutePath()+"'...");
//        final File directory = GuiUtil.getDirectory(null, null);
//        if (directory == null) {
//            return;
//        }
        
        if(GuiUtil.getYesNoAnswer(this.dialog, "Preparing Repository "+directory.getAbsolutePath(),
                "<html>" +
                    "Preparing a repository means creating a directory for each movie file.<br>" +
                    "This action is <b>not undoable</b> and can leave your folderstructure<br>" +
                    "in a state you probably personally do not like that much.<br><br>" +
                    "Are you sure to want this?" +
                "</html>") == false) {
            LOG.info("Preparing repository aborted by user.");
            return;
        }
        
        
        final RepositoryPreparer preparer = new RepositoryPreparer();
        final PreparerResult result = preparer.process(directory);
        new RepositoryPreparerResultWindow(this.dialog, result).setVisible(true);
    }
    
    public void doEditScannedMovie(ScannedMovie scannedInputMovie) {
        LOG.info("doEditScannedMovie(" + scannedInputMovie + ")");
        AddEditMovieDialog editDialog = AddEditMovieDialog.newEditScanMovieDialog(this.dialog.getOwner(), scannedInputMovie);
        editDialog.setVisible(true);
        
        if(editDialog.isActionConfirmed()) {
            final Movie confirmedMovie = editDialog.getConfirmedObject();
            
            final ScannedMovie confirmedScannedMovie = ScannedMovie.updateByMovie(confirmedMovie, scannedInputMovie);
            this.dialog.updateScannedMovie(confirmedScannedMovie);
        }
    }

    public void doClose() {
        if(this.scanInProgress == true) {
            assert(this.scanThread != null);
            
            if(GuiUtil.getYesNoAnswer(this.dialog, "Really close", "A scan is currently in progress.\nDo you really want to abort it?") == false) {
                return;
            }
            LOG.debug("User choosen to close window although scan is in progress.");
            this.scanThread.shouldStop();
            LOG.debug("Waiting for scan thread to die...");
            try {
                this.scanThread.join();
            } catch (InterruptedException e) {
                throw new FatalException("Interrupted while waiting for scan thread to die!", e);
            }
            
            LOG.debug("Scanthread died.");
        }
        LOG.debug("Disposing scan dialog.");
        this.dialog.dispose();
    }

    public void doDirectoryCount(int count) {
        LOG.info("Got directory count "+count+".");
        this.dirCountMax = count;
        this.dialog.getProgressBar().setMaximum(this.dirCountMax);
    }
    

    public void doNextFinished() {
        if(this.dirCount < this.dirCountMax) this.dirCount++;
        this.dialog.getProgressBar().setValue(this.dirCount);
        this.setProgressString();
    }

    public void doNextPhase(String phaseName) {
        LOG.info("Next phase '"+phaseName+"'.");
        this.dirCount = 1;
        this.dialog.getProgressBar().setValue(this.dirCount);
        this.currentPhase = phaseName;
        this.setProgressString();
    }
    
    private void setProgressString() {
        this.dialog.getProgressBar().setString(this.currentPhase + " "  + this.dirCount + "/" + this.dirCountMax);
    }

    
    public void doScanFinished(List<ScannedMovie> scannedMovies, List<ScanHint> hints) {
        LOG.info("Scanning finished!");
        this.scanInProgress = false;
        
        this.dialog.doScanCompleted(scannedMovies, hints);
    }
    

    
    public void doImport() {
        LOG.info("importing scanned movies.");
        List<Movie> movies = this.dialog.getSelectedMovies();
        
        this.dialog.dispose();
        
        if(movies.size() == 0) {
            GuiUtil.warning(this.dialog, "Nothing imported", "There was not any selected movie to import!");
            return;
        }
        
        try {
            final List<Movie> insertedMovies = BeanFactory.getInstance().getMovieDao().insertMovies(movies);

            // they got an id now!
            for (Movie movie : insertedMovies) {
                if(movie.isCoverFileSet() == true) {
                    CoverUtil.resetCover(movie);
                }
            }
            
            GuiUtil.info(this.dialog, "Movies imported", "Successfully imported "+movies.size()+" scanned movie"+(movies.size()==1?"":"s")+"!");
        } catch (BusinessException e) {
            LOG.error("Importing scanned movies failed!", e);
            GuiUtil.info(this.dialog, "Movies not imported", "Importing of scanned movies failed!");
        }
        
    }
    
    public void doScan(final File scanRoot, final IWebExtractor webExtractor) {
        if(scanRoot == null || scanRoot.exists() == false || scanRoot.isDirectory() == false) {
            GuiUtil.warning("Scan not started", "Please first choose a valid scan directory!");
            return;
        }
        LOG.info("Scanning directory '"+scanRoot.getAbsolutePath()+"'...");

        final boolean insertDatabase = false;
        
        try {
            // MANTIS [8] scanner: make scanner type configurable as plug-in
            final IScanner scanner = new Scanner(this, scanRoot, insertDatabase, webExtractor);
            
            this.scanThread = new ScanThread(scanner);
            this.scanInProgress = true;
            
            this.dialog.getProgressBar().setString("");
            this.scanThread.start();
            
        } catch (Exception e) {
            LOG.error("Scanning  failed! insertDatabase='"+insertDatabase+"'; webExtractor='"+webExtractor+"'; scanRoot='"+scanRoot.getAbsolutePath()+"'", e);
            GuiUtil.error(this.dialog, "Scan failed", "Performing scan on folder '"+scanRoot.getName()+"' failed!");
        }
    }
    
    public void doFetchMetaData(ScannedMovie movieFetchingData) {
        final Movie metadataEnhancedMovie = this._doFetchMetaData(this.dialog, movieFetchingData);
        if(metadataEnhancedMovie == null) {
            return;
        }
        
        final ScannedMovie confirmedScannedMovie = ScannedMovie.updateByMetadataMovie(movieFetchingData, metadataEnhancedMovie);
        this.dialog.updateScannedMovie(confirmedScannedMovie);
    }
    
    public void doRemoveMetaData(ScannedMovie scannedMovie) {
        final ScannedMovie confirmedScannedMovie = ScannedMovie.clearMetadataMovie(scannedMovie);
        this.dialog.updateScannedMovie(confirmedScannedMovie);
    }
}
