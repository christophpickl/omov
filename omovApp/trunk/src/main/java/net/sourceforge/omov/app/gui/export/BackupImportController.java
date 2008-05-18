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

package net.sourceforge.omov.app.gui.export;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.tools.export.ImportProcessResult;
import net.sourceforge.omov.core.tools.export.ImporterBackup;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
                // FEATURE backup import: maybe if movies skipped count > 1, display text in JTextArea (so text can be copied) which movies where skipped (title, folderpath)
            }
            GuiUtil.info(this.dialog, "Import Successfull", dialogText);
            
        } else { // processResult.isSucceeded() == false
            GuiUtil.error(this.dialog, "Import Failed", processResult.getErrorMessage());
        }
        
        this.dialog.dispose();
    }
}
