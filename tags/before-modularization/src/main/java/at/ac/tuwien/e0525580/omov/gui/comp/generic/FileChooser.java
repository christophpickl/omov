package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import at.ac.tuwien.e0525580.omov.util.CollectionUtil;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

public class FileChooser extends AbstractFileDirectoryChooser {

//    private static final Log LOG = LogFactory.getLog(DirectoryChooser.class);
    
    private static final long serialVersionUID = 4870536517103469362L;

    private final FileFilter fileFilter;
    
    
    /** simplest constructor */
    public FileChooser(String dialogTitle, String... validExtensions) {
        this(dialogTitle, AbstractFileDirectoryChooser.DEFAULT_BUTTON_LABEL, null, AbstractFileDirectoryChooser.DEFAULT_BUTTON_POSITION, validExtensions);
    }
    
    /** adds button position */
    public FileChooser(String dialogTitle, ButtonPosition position, String... validExtensions) {
        this(dialogTitle, AbstractFileDirectoryChooser.DEFAULT_BUTTON_LABEL, null, position, validExtensions);
    }
    
    /** adds default path */
    public FileChooser(String dialogTitle, File defaultPath, ButtonPosition position, String... validExtensions) {
        this(dialogTitle, AbstractFileDirectoryChooser.DEFAULT_BUTTON_LABEL, defaultPath, position, validExtensions);
    }
    
    /** finally: adds button label*/
    public FileChooser(String dialogTitle, String buttonLabel, File defaultPath, ButtonPosition position, String... validExtensions) {
        super(dialogTitle, defaultPath, position, buttonLabel);
        
        final Set<String> validExtensionsSet = new LinkedHashSet<String>(Arrays.asList(validExtensions));
        final String validExtensionsString = CollectionUtil.toString(validExtensionsSet);
        
        this.fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.isDirectory() == true) return true;
                
                final String extension = FileUtil.extractExtension(file);
                if(extension == null) {
                    return false;
                }
                
                return validExtensionsSet.contains(extension);
            }
            @Override
            public String getDescription() {
                return validExtensionsString;
            }
        };
    }

    /**
     * @return can be null
     */
    public File getSelectedFile() {
        return this.getFileOrDir();
    }

    @Override
    int getSelectionMode() {
        return JFileChooser.FILES_ONLY;
    }

    @Override
    FileFilter getFileFilter() {
        return this.fileFilter;
    }

    @Override
    boolean getIsRightFileOrDir(final File file) {
        return file.isFile();
    }
    
    public final void setFile(final File file) {
        this.setFileOrDir(file);
    }
}
