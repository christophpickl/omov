package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.io.File;

public interface IChooserListener {

    /**
     * gets invoked if user has choosen directory and approved operation.
     * @param dir is never null
     */
    void doChoosen(File dir);
    
}
