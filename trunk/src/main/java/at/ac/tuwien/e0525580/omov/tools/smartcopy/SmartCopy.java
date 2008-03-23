package at.ac.tuwien.e0525580.omov.tools.smartcopy;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

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
        }
        return this.preprocessResult;
    }
    
    
    public void process() throws BusinessException {
        LOG.debug("SmartCopy is going to start working.");
        if(this.preprocessResult == null) {
            throw new IllegalStateException("preprocess() was not yet invoked!");
        }
        
        boolean successfullyCopied = false;
        final List<File> createdDirectories = new LinkedList<File>();
        try {
            for (Movie movieToCopy : this.preprocessResult.getMoviesToCopy()) {
                // if (user aborted) break + cleanup   --- maybe do copy in thread an stop thread if user aborted progress
                
                final File movieFolder = new File(movieToCopy.getFolderPath());
                LOG.debug("Copying movie folder '"+movieFolder.getAbsolutePath()+"' started.");
                
                final File targetDir = new File(this.targetDirectory, movieFolder.getName());
                if(targetDir.exists() == true) {
                    LOG.debug("Deleting already existing target directory '"+targetDir.getAbsolutePath()+"'.");
                    FileUtil.deleteDirectoryRecursive(targetDir);
                }
                
                final File createdDirectory = FileUtil.copyDirectoryRecursive(movieFolder, this.targetDirectory);
                LOG.debug("Copying movie folder '"+movieFolder.getAbsolutePath()+"' finished.");
                createdDirectories.add(createdDirectory);
            }
            
            LOG.info("SmartCopy finished successfully.");
            successfullyCopied = true;
        } finally {
            if(successfullyCopied == false) {
                cleanup(createdDirectories);
            }
        }
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
