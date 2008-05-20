package net.sourceforge.omov.app.gui.comp;

import java.io.File;

public interface IButtonFolderListener {

    void notifyFolderSelected(File folder);
    
    void notifyFolderCleared();
    
}
