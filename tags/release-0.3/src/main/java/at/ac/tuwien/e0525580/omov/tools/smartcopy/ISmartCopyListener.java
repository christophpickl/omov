package at.ac.tuwien.e0525580.omov.tools.smartcopy;

import java.io.File;

public interface ISmartCopyListener {
    
    void startedCopyingDirectory(File directory);
    
//    void finishedCopyingDirectory(File directory);
    
    void targetDirectoryWasCleanedUp();
}
