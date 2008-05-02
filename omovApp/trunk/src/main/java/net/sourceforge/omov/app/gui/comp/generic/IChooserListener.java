package net.sourceforge.omov.app.gui.comp.generic;

import java.io.File;

public interface IChooserListener {

    /**
     * gets invoked if user has choosen directory and approved operation.
     * @param dir is never null
     */
    void doChoosen(File dir);
    
}
