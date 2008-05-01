package net.sourceforge.omov.core.tools;

import java.io.File;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.util.FileUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class TemporaryFilesCleaner {

    private static final Log LOG = LogFactory.getLog(TemporaryFilesCleaner.class);
    
    private TemporaryFilesCleaner() {
        // no instantiation
    }
    
    public static void clean() {
        final File temporaryFolder = PreferencesDao.getInstance().getTemporaryFolder();
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
