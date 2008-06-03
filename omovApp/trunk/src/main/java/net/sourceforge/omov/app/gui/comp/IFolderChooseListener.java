package net.sourceforge.omov.app.gui.comp;

import java.io.File;

public interface IFolderChooseListener {

    void notifyFolderSelected(File folder);
    
    void notifyFolderCleared();
    
}
