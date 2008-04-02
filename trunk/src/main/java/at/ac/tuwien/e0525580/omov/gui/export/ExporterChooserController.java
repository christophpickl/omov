package at.ac.tuwien.e0525580.omov.gui.export;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.tools.export.ExporterBackup;
import at.ac.tuwien.e0525580.omov.tools.export.ExporterHtml;
import at.ac.tuwien.e0525580.omov.tools.export.HtmlColumn;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class ExporterChooserController {

    private static final Log LOG = LogFactory.getLog(ExporterChooserController.class);
    
    private final ExporterChooserDialog dialog;
    
    public ExporterChooserController(ExporterChooserDialog dialog) {
        this.dialog = dialog;
    }

    public void doExportHtml(List<HtmlColumn> columns, final List<Movie> movies) {
        
        final boolean coversEnabled = columns.contains(HtmlColumn.COLUMN_COVER);
        final File targetFile = this.getTargetHtmlFile("html", !coversEnabled); // if covers enabled -> disable file existence check (it would check for file itself, and not necessary directory)
        if(targetFile == null) return;
        
        final ExporterHtml exporter = new ExporterHtml(columns);
        try {
            LOG.info("Exporting HTML-Report to '"+targetFile.getAbsolutePath()+"' with "+movies.size()+" movies.");
            exporter.process(movies, targetFile);
            GuiUtil.info(this.dialog, "Export finished", "The exported file was successfully saved to:\n"+exporter.getTargetFilePath());
        } catch (BusinessException e) {
            GuiUtil.error(this.dialog, "Export HTML", "Exporting Movies failed! Error message is:\n"+e.getMessage());
            LOG.error("Could not export movies to target '"+targetFile.getAbsolutePath()+"'!", e);
        }
    }
    
    public void doExportBackup(final List<Movie> movies) {
        final File targetDirectory = this.getTargetDirectory();
        if(targetDirectory == null) return;
        
        
        try {
            LOG.info("Exporting Backup to '"+targetDirectory.getAbsolutePath()+"'.");
            final File backupFile = ExporterBackup.process(movies, targetDirectory);
            GuiUtil.info(this.dialog, "Export finished", "The exported file was successfully saved to:\n"+backupFile.getAbsolutePath());
        } catch (BusinessException e) {
            GuiUtil.error(this.dialog, "Export Backup", "Generating report failed!\n"+e.getMessage());
        }
    }
    
    
    
    
    private File getTargetDirectory() {
        JFileChooser chooser = new JFileChooser(PreferencesDao.getInstance().getRecentExportDestination());
        chooser.setDialogTitle("Target Directory for Backup");
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        
        if(chooser.showOpenDialog(this.dialog) != JFileChooser.APPROVE_OPTION) {
            return null;
        }
        
        return chooser.getSelectedFile();
    }
    
    private File getTargetHtmlFile(final String desiredExtension, final boolean checkForFileExistence) {
        JFileChooser chooser = new JFileChooser(PreferencesDao.getInstance().getRecentExportDestination());
        chooser.setDialogTitle("Target HTMLFile");
        
        if (chooser.showSaveDialog(this.dialog) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File selectedFile = chooser.getSelectedFile();
        if(selectedFile.getName().endsWith("." + desiredExtension) == false) {
            LOG.debug("User did not specify file with desired extension '"+desiredExtension+"'; renaming file (file was: '"+selectedFile.getAbsolutePath()+"').");
            selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + "." + desiredExtension);
        }
        PreferencesDao.getInstance().setRecentExportDestination(selectedFile.getParent());
        
        if(checkForFileExistence == true && selectedFile.exists() == true) {
            int confirm = JOptionPane.showConfirmDialog(this.dialog, "Overwrite existing file '"+selectedFile.getName()+"'?", "Export HTML", JOptionPane.YES_NO_OPTION);
            if(confirm != JOptionPane.YES_OPTION) {
                return null;
            }
            if(selectedFile.delete() == false) {
                GuiUtil.error(this.dialog, "File Error", "Could not delete File '"+selectedFile.getAbsolutePath() + "'!");
                return null;
            }
        }
        
        return selectedFile;
    }
    
}
