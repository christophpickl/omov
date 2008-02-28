package at.ac.tuwien.e0525580.omov.tools.scan;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

public class RepositoryPreparer {

    private static final Log LOG = LogFactory.getLog(RepositoryPreparer.class);
    
    private final List<PreparerHint> hints = new LinkedList<PreparerHint>();
    
    public static void main(String[] args) {

//        final File directory = GuiUtil.getDirectory(null, null);
//        if(directory == null) {
//            return;
//        }
        final File directory = new File("/Users/phudy/Movies/Holy/");
        
        RepositoryPreparer p = new RepositoryPreparer();
        PreparerResult r = p.process(directory);
        System.out.println("moved files: " + r.getCntMovedFiles());
        for (PreparerHint hint : r.getHints()) {
            System.out.println(hint);
        }
    }
    
    public RepositoryPreparer() {
    }

    private void success(String msg) {
        this.hints.add(PreparerHint.success(msg));
    }
    private void info(String msg) {
        this.hints.add(PreparerHint.info(msg));
    }
    private void warning(String msg) {
        this.hints.add(PreparerHint.warning(msg));
    }
    private void error(String msg) {
        this.hints.add(PreparerHint.error(msg));
    }
    
    public PreparerResult process(final File directory) {
        this.hints.clear();
        int cntMovedFiles = 0;
        int cntIgnoredDirs = 0;
        int cntIgnoredFiles = 0;
        
        for (File file : directory.listFiles()) {
            LOG.debug("Processing file/directory '"+file.getAbsolutePath()+"' ...");
            if(file.getName().equals("affe1.jpg")) {
                System.out.println("tut");
            }
            if(file.isDirectory() == true) {
                final String msg = "Ignoring existing directory: " + file.getAbsolutePath();
                LOG.debug(msg);
                this.info(msg);
                cntIgnoredDirs++;
                continue;
            }
            
            if(Constants.isMovieFile(file) == false) {
                if(Constants.isHiddenFile(file) == true) {
                    LOG.debug("Ignoring hidden file '"+file.getName()+"'.");
                } else {
                    this.info("Ignoring non movie file: " + file.getAbsolutePath());
                    cntIgnoredFiles++;
                }
                continue;
            }
            final String fileName = file.getName();
            final String targetDirName = fileName.substring(0, fileName.length() - FileUtil.extractExtension(file).length() - 1); // + 1 is the "."-dot
            final File targetDir = new File(file.getParentFile(), targetDirName);
            final String targetDirPath = targetDir.getAbsolutePath();
            LOG.info("Creating target directory '"+targetDirPath+"' for movie file '"+fileName+"'.");
            if(targetDir.exists() == true) {
                final String msg = "Directory '"+targetDirPath+"' already exists; leaving file '"+fileName+"' where it is.";
                LOG.info(msg);
                this.warning(msg);
                continue;
            }
            if(targetDir.mkdir() == false) {
                final String msg = "Could not create directory '"+targetDirPath+"'!";
                LOG.error(msg);
                this.error(msg);
                continue;
            }
            
            
            final boolean couldMoveFile = file.renameTo(new File(targetDir, fileName));
            if (couldMoveFile == false) {
                final String msg = "Could not move file '"+fileName+"' to directory '"+targetDirPath+"'!";
                LOG.error(msg);
                this.error(msg);
                continue;
            }
            
            final String msg = "Moved movie file '"+fileName+"' to directory '"+targetDirName+"'.";
            LOG.info(msg);
            this.success(msg);
            cntMovedFiles++;
        } // for each file
        
        return new PreparerResult(this.hints, cntMovedFiles, cntIgnoredDirs, cntIgnoredFiles);
    }
    
    
    
    

    
    public static class PreparerResult {
        private final List<PreparerHint> hints;
        private final int cntMovedFiles;
        private final int cntIgnoredDirs;
        private final int cntIgnoredFiles;
        private PreparerResult(final List<PreparerHint> hints, final int cntMovedFiles, int cntIgnoredDirs, int cntIgnoredFiles) {
            this.hints = hints;
            this.cntMovedFiles = cntMovedFiles;
            this.cntIgnoredDirs = cntIgnoredDirs;
            this.cntIgnoredFiles = cntIgnoredFiles;
        }
        public List<PreparerHint> getHints() {
            return this.hints;
        }
        public int getCntMovedFiles() {
            return this.cntMovedFiles;
        }
        public int getCntIgnoredDirs() {
            return this.cntIgnoredDirs;
        }
        public int getCntIgnoredFiles() {
            return this.cntIgnoredFiles;
        }
    }
}
