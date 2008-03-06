package at.ac.tuwien.e0525580.omov;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.util.FileUtil;

public final class TemporaryFilesCleaner {

    private static final Log LOG = LogFactory.getLog(TemporaryFilesCleaner.class);
    
    private TemporaryFilesCleaner() {
        // no instantiation
    }
    
    public static void clean() {
        final File temporaryFolder = Configuration.getInstance().getTemporaryFolder();
        LOG.info("Cleaning up temporary folder '"+temporaryFolder.getAbsolutePath()+"'.");
        
        final File[] temporaryFiles = temporaryFolder.listFiles();
        for (final File temporaryFile : temporaryFiles) {
            if(temporaryFile.isDirectory()) {
                try {
                    FileUtil.deleteDirectoryRecursive(temporaryFile);
                } catch (BusinessException e) {
                    LOG.warn("Could not delete temporary folder '"+temporaryFile.getAbsolutePath()+"'!");
                }
            } else { // is file
                if(FileUtil.isHiddenFile(temporaryFile) == true) {
                    LOG.debug("Ignoring hidden file '"+temporaryFile.getName()+"' in temporary folder.");
                } else {
                    LOG.debug("Deleting temporary file '"+temporaryFile.getName()+"'.");
                    if(temporaryFile.delete() == false) {
                        LOG.warn("Could not delete temporary file '"+temporaryFile.getAbsolutePath()+"'!");
                    }
                }
            }
        }
    }
}
