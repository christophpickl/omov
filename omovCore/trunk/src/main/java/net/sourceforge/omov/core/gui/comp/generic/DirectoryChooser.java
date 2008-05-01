package net.sourceforge.omov.core.gui.comp.generic;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

public class DirectoryChooser extends AbstractFileDirectoryChooser {

//    private static final Log LOG = LogFactory.getLog(DirectoryChooser.class);
    
    private static final long serialVersionUID = -3831195688364368872L;
    
    

    public DirectoryChooser(String dialogTitle) {
        super(dialogTitle);
    }
    
    public DirectoryChooser(String dialogTitle, ButtonPosition position) {
        super(dialogTitle, position);
    }
    
    public DirectoryChooser(String dialogTitle, File defaultPath, ButtonPosition position) {
        super(dialogTitle, defaultPath, position);
    }
    
    public DirectoryChooser(String dialogTitle, File defaultPath, ButtonPosition position, String buttonLabel) {
        super(dialogTitle, defaultPath, position, buttonLabel);
    }


    /**
     * @return can be null
     */
    public File getSelectedDirectory() {
        return this.getFileOrDir();
    }
    
    @Override
    int getSelectionMode() {
        return JFileChooser.DIRECTORIES_ONLY;
    }

    @Override
    FileFilter getFileFilter() {
        return null;
    }

    @Override
    boolean getIsRightFileOrDir(final File file) {
        return file.isDirectory();
    }
    
    public final void setDirectory(final File directory) {
        this.setFileOrDir(directory);
    }
}
