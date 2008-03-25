package at.ac.tuwien.e0525580.omov.tools.export;

import java.io.File;

public class ImporterBackup {

    private final File backupFile;
    
    public ImporterBackup(File backupFile) {
        assert(backupFile.exists() && backupFile.isFile());
        this.backupFile = backupFile;
    }
    
    // FIXME implement ImporterBackup
    // check data version
    // transaction:
    // - import movies (if path already exists -> skip + return info
    // - get most recent ID and update
    // - copy cover files with proper ID
}
