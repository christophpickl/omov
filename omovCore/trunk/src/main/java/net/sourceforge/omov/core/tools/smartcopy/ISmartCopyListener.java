package net.sourceforge.omov.core.tools.smartcopy;

import java.io.File;

public interface ISmartCopyListener {
    
    void startedCopyingDirectory(File directory);
    
//    void finishedCopyingDirectory(File directory);
    
    void targetDirectoryWasCleanedUp();
}
