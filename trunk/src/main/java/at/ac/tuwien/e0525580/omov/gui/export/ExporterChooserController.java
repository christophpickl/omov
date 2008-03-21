package at.ac.tuwien.e0525580.omov.gui.export;

import java.io.File;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.Configuration;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.tools.export.ExporterHtml;
import at.ac.tuwien.e0525580.omov.tools.export.ExporterXml;
import at.ac.tuwien.e0525580.omov.tools.export.HtmlColumn;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class ExporterChooserController {

    private static final Log LOG = LogFactory.getLog(ExporterChooserController.class);
    
    private final ExporterChooserDialog dialog;
    
    private static IMovieDao DAO = BeanFactory.getInstance().getMovieDao();
    
    public ExporterChooserController(ExporterChooserDialog dialog) {
        this.dialog = dialog;
    }
    // FEATURE do not export all movies, but let user only export selected rows in movieTable (if desired  -JCheckBox- so)
    private String format;
    public void doExportHtml(List<HtmlColumn> columns) {
        this.format = "HTML";
        
        final boolean coversEnabled = columns.contains(HtmlColumn.COLUMN_COVER);
        final File targetFile = this.getTargetFile("html", !coversEnabled); // if covers enabled -> disable file existence check (it would check for file itself, and not necessary directory)
        if(targetFile == null) return;
        
        final ExporterHtml exporter = new ExporterHtml(columns);
        try {
            LOG.info("Exporting "+format+"-Report to '"+targetFile.getAbsolutePath()+"'.");
            final List<Movie> movies = DAO.getMoviesSorted();
            exporter.process(movies, targetFile);
            GuiUtil.info(this.dialog, "Export finished", "The exported file was successfully saved to:\n"+exporter.getTargetFilePath());
        } catch (BusinessException e) {
            JOptionPane.showMessageDialog(this.dialog, "Exporting Movies failed! Error message is:\n"+e.getMessage(), "Export "+format, JOptionPane.ERROR_MESSAGE);
            LOG.error("Could not export movies!", e);
        }
    }
    
    public void doExportXml() {
        this.format = "XML";

        final File targetFile = this.getTargetFile("xml");
        if(targetFile == null) return;
        
        try {
            LOG.info("Exporting "+format+"-Report to '"+targetFile.getAbsolutePath()+"'.");
            List<Movie> movies = DAO.getMoviesSorted();
            ExporterXml exporter = new ExporterXml();
            exporter.process(movies, targetFile);
        } catch (BusinessException e) {
            GuiUtil.error(this.dialog, "Generating report failed!\n"+e.getMessage(), "Export "+format);
        }
        GuiUtil.info(this.dialog, "Export finished", "The exported file was successfully saved to:\n"+targetFile.getAbsolutePath()+"."); 
    }

    private File getTargetFile(final String desiredExtension) {
        return this.getTargetFile(desiredExtension, true);
    }
    private File getTargetFile(final String desiredExtension, final boolean checkForFileExistence) {
        JFileChooser chooser = new JFileChooser(Configuration.getInstance().getRecentExportDestination());
        chooser.setDialogTitle("Target "+this.format+"-File");
        
        if (chooser.showSaveDialog(this.dialog) != JFileChooser.APPROVE_OPTION) {
            return null;
        }

        File selectedFile = chooser.getSelectedFile();
        if(selectedFile.getName().endsWith("." + desiredExtension) == false) {
            LOG.debug("User did not specify file with desired extension '"+desiredExtension+"'; renaming file (file was: '"+selectedFile.getAbsolutePath()+"').");
            selectedFile = new File(selectedFile.getParentFile(), selectedFile.getName() + "." + desiredExtension);
        }
        Configuration.getInstance().setRecentExportDestination(selectedFile.getParent());
        
        if(checkForFileExistence == true && selectedFile.exists() == true) {
            int confirm = JOptionPane.showConfirmDialog(this.dialog, "Overwrite existing file '"+selectedFile.getName()+"'?", "Export "+format, JOptionPane.YES_NO_OPTION);
            if(confirm != JOptionPane.YES_OPTION) {
                return null;
            }
            if(selectedFile.delete() == false) {
                JOptionPane.showMessageDialog(this.dialog, "Could not delete File '"+selectedFile.getAbsolutePath() + "'!", "File Error", JOptionPane.ERROR_MESSAGE);
                return null;
            }
        }
        
        return selectedFile;
    }
    
}
