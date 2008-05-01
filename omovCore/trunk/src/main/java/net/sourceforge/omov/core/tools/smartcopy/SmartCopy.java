package net.sourceforge.omov.core.tools.smartcopy;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.util.FileUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class SmartCopy {

    private static final Log LOG = LogFactory.getLog(SmartCopy.class);

    private final String idString;
    private final File targetDirectory;
    private SmartCopyPreprocessResult preprocessResult = null;
    
    public SmartCopy(final String idString, File targetDirectory) {
        assert(targetDirectory.exists() && targetDirectory.isDirectory());
        this.idString = idString;
        this.targetDirectory = targetDirectory;
    }
    
    public SmartCopyPreprocessResult preprocess() {
        if(this.preprocessResult == null) {
            LOG.info("preprocessing...");
            this.preprocessResult = new SmartCopyPreprocessResult(this.idString, this.targetDirectory);
            this.preprocessResult.parseIdString();
            if(this.preprocessResult.isFatalError() == false) {
                this.preprocessResult.fetchMovies();
            }
        } else {
            LOG.debug("Returning stored preprocess result.");
        }
        return this.preprocessResult;
    }
    
    
    public void process(ISmartCopyListener listener) throws BusinessException {
        LOG.debug("SmartCopy is going to start working.");
        if(this.preprocessResult == null) {
            throw new IllegalStateException("preprocess() was not yet invoked!");
        }
        
        boolean successfullyCopied = false;
        final List<File> createdDirectories = new LinkedList<File>();
        try {
            for (Movie movieToCopy : this.preprocessResult.getMoviesToCopy()) {
                final File movieFolder = new File(movieToCopy.getFolderPath());
                final File targetDir = new File(this.targetDirectory, movieFolder.getName());
                if(targetDir.exists() == true) {
                    LOG.debug("Deleting already existing target directory '"+targetDir.getAbsolutePath()+"'.");
                    FileUtil.deleteDirectoryRecursive(targetDir);
                }
            }
            listener.targetDirectoryWasCleanedUp();
            for (Movie movieToCopy : this.preprocessResult.getMoviesToCopy()) {
                // if (user aborted) break + cleanup   --- maybe do copy in thread an stop thread if user aborted progress
                
                final File movieFolder = new File(movieToCopy.getFolderPath());
                LOG.debug("Copying movie folder '"+movieFolder.getAbsolutePath()+"' started.");
                listener.startedCopyingDirectory(new File(this.targetDirectory, movieFolder.getName()));
                final File createdDirectory = FileUtil.copyDirectoryRecursive(movieFolder, this.targetDirectory);
                createdDirectories.add(createdDirectory);
                LOG.debug("Copying movie folder '"+movieFolder.getAbsolutePath()+"' finished.");
//                listener.finishedCopyingDirectory(createdDirectory);
            }
            
            LOG.info("SmartCopy finished successfully.");
            successfullyCopied = true;
        } finally {
            if(successfullyCopied == false) {
                cleanup(createdDirectories);
            }
        }
    }
    
    public File getTargetDirectory() {
        return this.targetDirectory;
    }
    
    private static void cleanup(List<File> createdDirectories) {
        LOG.debug("Cleaning up SmartCopy folders.");
        
        for (File createdDirectory : createdDirectories) {
            try {
                FileUtil.deleteDirectoryRecursive(createdDirectory);
            } catch(Exception e) {
                LOG.error("Could not cleanup SmartCopy created directory '"+createdDirectory.getAbsolutePath()+"'!", e);
            }
        }
    }
    
}
