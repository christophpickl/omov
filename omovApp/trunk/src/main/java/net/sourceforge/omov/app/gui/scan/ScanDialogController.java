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

package net.sourceforge.omov.app.gui.scan;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.sourceforge.omov.app.gui.CommonController;
import net.sourceforge.omov.app.gui.movie.AddEditMovieDialog;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.tools.scan.IScanListener;
import net.sourceforge.omov.core.tools.scan.IScanner;
import net.sourceforge.omov.core.tools.scan.IScannerStopped;
import net.sourceforge.omov.core.tools.scan.RepositoryPreparer;
import net.sourceforge.omov.core.tools.scan.ScanHint;
import net.sourceforge.omov.core.tools.scan.ScanThread;
import net.sourceforge.omov.core.tools.scan.ScannedMovie;
import net.sourceforge.omov.core.tools.scan.Scanner;
import net.sourceforge.omov.core.tools.scan.RepositoryPreparer.PreparerResult;
import net.sourceforge.omov.core.util.CoverUtil;
import net.sourceforge.omov.webApi.IWebDataFetcher;
import net.sourceforge.omov.webApi.WebDataFetcherFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class ScanDialogController extends CommonController<ScannedMovie> implements IScanListener {

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
        AddEditMovieDialog editDialog = AddEditMovieDialog.newEditScanMovieDialog(
        		this.dialog.getOwner(), scannedInputMovie);
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
            
            if(GuiUtil.getYesNoAnswer(this.dialog, "Really close",
            		"A scan is currently in progress.\nDo you really want to abort it?") == false) {
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
            final String msg = "Successfully imported "+movies.size()+" scanned movie"+(movies.size()==1?"":"s")+"!";
            GuiUtil.info(this.dialog, "Movies imported", msg);
        } catch (BusinessException e) {
            LOG.error("Importing scanned movies failed!", e);
            GuiUtil.info(this.dialog, "Movies not imported", "Importing of scanned movies failed!");
        }
        
    }
    
    public void doScan(final File scanRoot, final boolean useWebExtractor) {
        if(scanRoot == null || scanRoot.exists() == false || scanRoot.isDirectory() == false) {
            GuiUtil.warning("Scan not started", "Please first choose a valid scan directory!");
            return;
        }
        LOG.info("Scanning directory '"+scanRoot.getAbsolutePath()+"'...");

        final boolean insertDatabase = false;
        
        try {
            // MANTIS [8] scanner: make scanner type configurable as plug-in
            final IScanner scanner = new Scanner(this, scanRoot, insertDatabase, useWebExtractor);
            
            this.scanThread = new ScanThread(scanner);
            this.scanInProgress = true;
            
            this.dialog.getProgressBar().setString("");
            this.scanThread.start();
            
        } catch (Exception e) {
            LOG.error("Scanning  failed! insertDatabase='"+insertDatabase+"'; " +
            		"useWebExtractor='"+useWebExtractor+"'; scanRoot='"+scanRoot.getAbsolutePath()+"'", e);
            GuiUtil.error(this.dialog, "Scan failed", "Performing scan on folder '"+scanRoot.getName()+"' failed!");
        }
    }
    
    public void doFetchMetaData(ScannedMovie movieFetchingData) {
    	// this method will invoke didFetchedMetaData afterwards
    	this._doFetchMetaData(this.dialog.getOwner(), movieFetchingData);
    }
    
	@Override
	protected void didFetchedMetaData(ScannedMovie movieFetchingData, Movie metadataEnhancedMovie) {
        if(metadataEnhancedMovie == null) {
            return;
        }
        
        ScannedMovie confirmedScannedMovie = ScannedMovie.updateByMetadataMovie(movieFetchingData, metadataEnhancedMovie);
        this.dialog.updateScannedMovie(confirmedScannedMovie);
	}
    
	
    public void doRemoveMetaData(ScannedMovie scannedMovie) {
        final ScannedMovie confirmedScannedMovie = ScannedMovie.clearMetadataMovie(scannedMovie);
        this.dialog.updateScannedMovie(confirmedScannedMovie);
    }


	public List<ScannedMovie> doEnhanceWithMetaData(List<ScannedMovie> originalMovies, List<ScanHint> hints,
			IScannerStopped stopped, IScanListener listener) throws BusinessException {
		// assert(Scanner.useWebExtractor == true)
		
    	final IWebDataFetcher fetcher = WebDataFetcherFactory.newWebDataFetcher();    	
        final List<ScannedMovie> enhancedMovies = new ArrayList<ScannedMovie>(originalMovies.size());
        
        for (ScannedMovie originalMovie : originalMovies) {
            if(stopped.isShouldStop() == true) return null;
            
            // FEATURE scanner: always fetch cover if scanning?
            
            final Movie enhancedMovie = fetcher.fetchAndEnhanceMovie(originalMovie, true);
            
            if(enhancedMovie != null) {
                enhancedMovies.add(ScannedMovie.newByMovie(enhancedMovie, originalMovie.isSelected()));
            } else {
                enhancedMovies.add(originalMovie);
                hints.add(ScanHint.error("Could not fetch Metadata for movie '"+originalMovie.getTitle()+"'!"));
            }
            if(listener != null) {
                listener.doNextFinished();
            }
        }
        
        return enhancedMovies;
	}


}
