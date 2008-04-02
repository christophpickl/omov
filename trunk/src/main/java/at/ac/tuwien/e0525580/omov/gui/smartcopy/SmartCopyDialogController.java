package at.ac.tuwien.e0525580.omov.gui.smartcopy;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.main.MainWindowController;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.ISmartCopyListener;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopy;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopyPreprocessResult;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

class SmartCopyDialogController implements ActionListener, ICopySwingWorkerListener, ISmartCopyListener {

    private static final Log LOG = LogFactory.getLog(SmartCopyDialogController.class);

    static final String CMD_START_COPY = "CMD_START_COPY";
    static final String CMD_START_COPY_ANYWAY = "CMD_START_COPY_ANYWAY";
    static final String CMD_ABORT_COPY_ANYWAY = "CMD_ABORT_COPY_ANYWAY";
    static final String CMD_CANCEL_COPY = "CMD_STOP_COPY";
    static final String CMD_USE_SELECTED_MOVIES = "CMD_USE_SELECTED_MOVIES";
    
    private final MainWindowController mainController;
    private final SmartCopyDialog dialog;
    private SmartCopy smartCopy = null;
    private CopySwingWorker worker = null;
//    private CopyThread worker = null;
    private Timer timer = null;
    
    public SmartCopyDialogController(SmartCopyDialog dialog, MainWindowController mainController) {
        this.dialog = dialog;
        this.mainController = mainController;
    }

    public void actionPerformed(final ActionEvent event) {
        new GuiAction() {protected void _action() {
            final String cmd = event.getActionCommand();
            LOG.debug("actionPerformed for command '"+cmd+"'.");
            
            if(cmd.equals(CMD_START_COPY)) {
                doStartCopy();
            } else if(cmd.equals(CMD_START_COPY_ANYWAY)) {
                doStartCopyAnyway();
            } else if(cmd.equals(CMD_ABORT_COPY_ANYWAY)) {
                dialog.doClose();
            } else if(cmd.equals(CMD_CANCEL_COPY)) {
                doCancel();
            } else if(cmd.equals(CMD_USE_SELECTED_MOVIES)) {
                doUseSelectedMovies();
            } else {
                throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
            }
        }}.doAction();
    }

    
    private void doStartCopy() {
        LOG.debug("doStartCopy()");
        this.dialog.getProgressBar().setValue(0);
        
        final String movieIdsString = this.dialog.getMovieIdString();
        final File targetDirectory = this.dialog.getTargetDirectory();
        assert(targetDirectory != null);
        
        if(movieIdsString.length() == 0) {
            GuiUtil.warning(this.dialog, "SmartCopy aborted", "The ID-String field is empty.");
            return;
        }
        
        this.smartCopy = new SmartCopy(movieIdsString, targetDirectory);
        final SmartCopyPreprocessResult result = this.smartCopy.preprocess();
        
        this.dialog.fillPreprocessResultTable(result);
        
        if(result.isFatalError() == true) {
            GuiUtil.error(this.dialog, "SmartCopy aborted", "A fatal error occured. See table for details.");
            return;
        }
        
        if(result.isMajorError() || result.isMinorError()) {
            GuiUtil.warning(this.dialog, "SmartCopy suspended", "Some errors occured. See table for details.");
            return;
        }
        
        this.doCopy();
    }

    private void doStartCopyAnyway() {
        LOG.info("doStartCopyAnyway()");
        this.doCopy();
    }
    
    private void doCopy() {
        LOG.info("doCopy()");
        this.dialog.enableUiForCopy(false);
        
//        this.worker = new CopyThread(this, this, this.smartCopy);
        
        this.worker = new CopySwingWorker(this, this, this.smartCopy);
        
//        this.worker.addPropertyChangeListener(new PropertyChangeListener() { --> not necessary because of workerFinished(boolean) method
//            public void propertyChange(PropertyChangeEvent event) {
//                System.out.println(event.getPropertyName() + " = " + event.getNewValue());
//            }
//        });
        
        final File targetDirectory = this.smartCopy.getTargetDirectory();
        final long copyTotal = this.smartCopy.preprocess().getTotalCopySizeInKb();
        final long kbBeforeCopying = FileUtil.getSizeRecursive(targetDirectory);
        LOG.debug("kbBeforeCopying = " + kbBeforeCopying);
        this.timer = new Timer();
        
        
        this.timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                final int progress;
                if(isTargetDirectoryWasCleanedUp == false) {
                    progress = 0;
                } else {
                    final long targetDirSize = FileUtil.getSizeRecursive(targetDirectory);
                    final double copiedAlready = targetDirSize - kbBeforeCopying;
                    progress = (int) ((copiedAlready / copyTotal) * 100);
//                    System.out.println("targetDirSize = "+targetDirSize+"; copiedAlready = "+copiedAlready+"; copyTotal="+copyTotal+"; progress="+progress+"%");
                }
                dialog.getProgressBar().setValue(progress);
            }
        }, 0, 100);
        
//        this.wasCanceled = false;
        this.isTargetDirectoryWasCleanedUp = false;
        this.createdDirectories = new LinkedList<File>();
        this.worker.execute();
//        this.worker.start();
    }
    
    private void doUseSelectedMovies() {
        final List<Movie> selectedMovies = this.mainController.getSelectedTableMovies();
        if(selectedMovies.size() == 0) {
            GuiUtil.warning(this.dialog, "SmartCopy", "There is not any movie selected.");
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("[[");
            for (int i = 0; i < selectedMovies.size(); i++) {
                if(i != 0) sb.append(",");
                sb.append(String.valueOf(selectedMovies.get(i).getId()));
            }
            sb.append("]]");
            this.dialog.setMovieIdString(sb.toString());
        }
    }
    
//    private boolean wasCanceled = false;
    private void doCancel() {
        LOG.debug("doCancel()");
        
        this.worker.cancel(true);
        
//        this.wasCanceled = true;
//        this.worker.interrupt();
    }
    

//  public void workerFinished() {
  public void workerFinished(boolean wasAborted) {
      LOG.info("Worker finished; wasAborted="+wasAborted);
      this.timer.cancel();

//        if(this.wasCanceled == true) {
        if (wasAborted == true) {
            for (File createdDirectory : this.createdDirectories) {
                if (createdDirectory.exists()) {
                    LOG.debug("Going to delete created directory at '" + createdDirectory.getAbsolutePath() + "'.");
                    try {
                        FileUtil.deleteDirectoryRecursive(createdDirectory);
                    } catch (BusinessException e) {
                        LOG.error("Could not delete directory at '" + createdDirectory.getAbsolutePath() + "' created by copy worker!");
                    }
                }
            }
            this.dialog.enableUiForCopy(true);
        } else {
            final int folderCnt = this.createdDirectories.size();
            LOG.debug("Successfully copied " + folderCnt + " movie folders.");
            GuiUtil.info(this.dialog, "SmartCopy finished", "Successfully created " + folderCnt + " movie folder"
                    + (folderCnt != 1 ? "s" : "") + " and copied " + FileUtil.formatFileSize(this.worker.getCopiedSizeInKb()) + ".");
            this.dialog.doClose();
        }
    }
    
    private boolean isTargetDirectoryWasCleanedUp = false;
    private List<File> createdDirectories;
    public void startedCopyingDirectory(File directory) {
        LOG.debug("Started copying directory '"+directory.getAbsolutePath()+"'.");
        this.createdDirectories.add(directory);
    }
    

    public void targetDirectoryWasCleanedUp() {
        LOG.debug("targetDirectoryWasCleanedUp()");
        this.isTargetDirectoryWasCleanedUp = true;
    }
}