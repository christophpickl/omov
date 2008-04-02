package at.ac.tuwien.e0525580.omov.gui.export;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.tools.export.ImportProcessResult;
import at.ac.tuwien.e0525580.omov.tools.export.ImporterBackup;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

class BackupImportController {

    private static final Log LOG = LogFactory.getLog(BackupImportController.class);
    
    private final BackupImportDialog dialog;
    
    BackupImportController(BackupImportDialog dialog) {
        this.dialog = dialog;
    }
    public void doImportBackup(final File backupFile) {
        LOG.info("Importing backup file '"+backupFile.getAbsolutePath()+"'.");
        
        this.dialog.enableInterface(false);
        final ZipFile backupZipFile = this.getZipFile(backupFile);
        if(backupZipFile == null) {
            return;
        }
        

        final ImporterBackup importer = new ImporterBackup(backupFile, backupZipFile);
        final ImportSwingWorker worker = new ImportSwingWorker(this, importer);
        
        worker.execute();
    }
    
    private ZipFile getZipFile(final File backupFile) {
        try {
            return new ZipFile(backupFile);
        } catch (ZipException e) {
            GuiUtil.warning(this.dialog, "Import Failed", "The selected file '"+backupFile.getAbsolutePath()+"' is not a proper backup file!");
            return null;
        } catch (IOException e) {
            GuiUtil.error(this.dialog, "Import Failed", "The selected file '"+backupFile.getAbsolutePath()+"' is corrupted or does not exist!");
            return null;
        }
    }
    
    /**
     * @see ImportSwingWorker#done()
     */
    public void stoppedWork(ImportProcessResult processResult) {
        LOG.debug("Swing worker stopped work.");
        
        if(processResult == null) { // actually, this can never happen because worker is uninterruptable
            LOG.info("Seems as work was aborted because process result is null.");
            
        } else if(processResult.isSucceeded() == true) {
            final int cntSkippedMovies = processResult.getSkippedMovies().size();
            final int cntInsertedMovies = processResult.getInsertedMovies().size();
            
            String dialogText = "Successfully imported "+cntInsertedMovies+" movie"+(cntInsertedMovies != 1 ? "s" : "")+" stored in backup.";
            if(cntSkippedMovies > 0) {
                dialogText += "\n(Skipped "+cntSkippedMovies+" Movie"+(cntSkippedMovies != 1 ? "s" : "")+" because " +
                                 (cntSkippedMovies != 1 ? "some" : "the")+" movie folderpath"+(cntSkippedMovies != 1 ? "s are" : " is")+" already in use)";
            }
            GuiUtil.info(this.dialog, "Import Successfull", dialogText);
            
        } else { // processResult.isSucceeded() == false
            GuiUtil.error(this.dialog, "Import Failed", processResult.getErrorMessage());
        }
        
        this.dialog.dispose();
    }
}
