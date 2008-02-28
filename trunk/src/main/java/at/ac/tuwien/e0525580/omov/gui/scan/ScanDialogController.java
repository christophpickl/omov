package at.ac.tuwien.e0525580.omov.gui.scan;

import java.io.File;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.Configuration;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.CommonController;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.DirectoryChooser.IDirectoryChooserListener;
import at.ac.tuwien.e0525580.omov.gui.movie.AddEditMovieDialog;
import at.ac.tuwien.e0525580.omov.tools.scan.IScanListener;
import at.ac.tuwien.e0525580.omov.tools.scan.IScanner;
import at.ac.tuwien.e0525580.omov.tools.scan.RepositoryPreparer;
import at.ac.tuwien.e0525580.omov.tools.scan.ScanHint;
import at.ac.tuwien.e0525580.omov.tools.scan.ScanThread;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.tools.scan.Scanner;
import at.ac.tuwien.e0525580.omov.tools.scan.RepositoryPreparer.PreparerResult;
import at.ac.tuwien.e0525580.omov.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov.util.CoverUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

class ScanDialogController extends CommonController implements IScanListener, IDirectoryChooserListener {

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
//        final File directory = GuiUtil.getDirectory(null, null);
//        if (directory == null) {
//            return;
//        }
//        
        LOG.info("doPrepareRepoistory on directory '"+directory.getAbsolutePath()+"'...");
//        final File directory = new File("/Users/phudy/Movies/Holy/"); // shortcut
        
        final RepositoryPreparer preparer = new RepositoryPreparer();
        final PreparerResult result = preparer.process(directory);
        new RepositoryPreparerResultWindow(result).setVisible(true);
    }

    public void choosenDirectory(File dir) {
        // getParent() returns null, if there is no parent directory; therefore take path of directory itself (which seems to be a top level path.
        final File parent = (dir.getParentFile() != null) ? dir.getParentFile() : dir;
        Configuration.getInstance().setRecentScanRoot(parent.getAbsolutePath()); // store back in preferences
    }
    
    public void doEditScannedMovie(ScannedMovie scannedInputMovie) {
        LOG.info("doShowScannedMovie(" + scannedInputMovie + ")");
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
            // FEATURE make scanner type configurable (plug-in)
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
    
}
