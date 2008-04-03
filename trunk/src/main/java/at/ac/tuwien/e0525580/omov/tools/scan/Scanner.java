package at.ac.tuwien.e0525580.omov.tools.scan;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.MovieFolderInfo;
import at.ac.tuwien.e0525580.omov.bo.RawScannedMovie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.tools.webdata.IWebExtractor;
import at.ac.tuwien.e0525580.omov.tools.webdata.WebImdbExtractor;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.MovieFileUtil;

/**
 * scans for following information: folderPath, files, fileSizeKb, format (using a MovieFolderInfo object)
 */
public class Scanner implements IScanner {

    private static final Log LOG = LogFactory.getLog(Scanner.class);
    
    public static void main(String[] args) throws BusinessException {
        Scanner scanner = new Scanner(null, new File("/Users/phudy/Movies/Holy"), false, new WebImdbExtractor());
        for (Movie movie : scanner.process()) {
            System.out.println(movie);
        }
        System.out.println("Finished.");
    }
    
    private final File scanRoot;
    private final IWebExtractor webExtractor;
    private final boolean insertDatabase;
    
    private final List<ScannedMovie> result = new ArrayList<ScannedMovie>();
    private final List<ScanHint> hints = new LinkedList<ScanHint>();
    private boolean wasProcessed = false;
    private final IScanListener listener;
    private boolean shouldStop = false;
    
    public Scanner(IScanListener listener, File scanRoot) throws BusinessException {
        this(listener, scanRoot, false, null);
    }

    public Scanner(IScanListener listener, File scanRoot, boolean insertDatabase) throws BusinessException {
        this(listener, scanRoot, insertDatabase, null);
    }
    
    public File getScanRoot() {
        return this.scanRoot;
    }
    
    public Scanner(IScanListener listener, File scanRoot, boolean insertDatabase, IWebExtractor webExtractor) throws BusinessException {
        if(scanRoot.exists() == false) throw new BusinessException("Scandirectory '"+scanRoot.getAbsolutePath()+"' does not exist!");
        if(scanRoot.isDirectory() == false) throw new BusinessException("File at '"+scanRoot.getAbsolutePath()+"' is not a directory!");
        
        this.listener = listener;
        this.scanRoot = scanRoot;
        this.insertDatabase = insertDatabase;
        this.webExtractor = webExtractor;
    }
    
    public List<Movie> process() throws BusinessException {
        if(this.wasProcessed == true) {
            throw new IllegalStateException("Scanner already processed!");
        }
        this.wasProcessed = true;
        
        List<ScannedMovie> scannedMovies = this.scanMovies();
        if(this.shouldStop == true) return null;
        
        if(this.webExtractor != null) {
            LOG.info("Starting web extraction.");
            scannedMovies = this.enhanceWithMetaData(scannedMovies);
            if(this.shouldStop == true) return null;
        }
        
        if(this.insertDatabase == true) {
            LOG.info("Inserting scanned movies.");
            this.insertMovies(scannedMovies);
            if(this.shouldStop == true) return null;
        }
        
        if(this.shouldStop == true) return null;
        
        if(this.listener != null) {
            this.listener.doScanFinished(Collections.unmodifiableList(scannedMovies), Collections.unmodifiableList(this.hints));
        }
        
        final List<Movie> movies = new ArrayList<Movie>(scannedMovies.size());
        for (ScannedMovie scannedMovie : scannedMovies) {
            movies.add(scannedMovie.toMovie());
        }
        return movies;
    }
    
    private List<ScannedMovie> enhanceWithMetaData(List<ScannedMovie> originalMovies) throws BusinessException {
        assert(this.webExtractor != null);
        
        if(this.listener != null) {
            this.listener.doNextPhase("Fetching Metadata");
        }
        
        final List<ScannedMovie> enhancedMovies = new ArrayList<ScannedMovie>(originalMovies.size());
        
        for (ScannedMovie originalMovie : originalMovies) {
            if(this.shouldStop) return null;
            
            // FEATURE scanner: always fetch cover if scanning?
            final Movie enhancedMovie = this.webExtractor.fetchAndEnhanceMovie((Movie) originalMovie, true);
            if(enhancedMovie != null) {
                enhancedMovies.add(ScannedMovie.newByMovie(enhancedMovie, originalMovie.isSelected()));
            } else {
                enhancedMovies.add(originalMovie);
                this.hints.add(ScanHint.error("Could not fetch Metadata for movie '"+originalMovie.getTitle()+"'!"));
            }
            if(this.listener != null) {
                this.listener.doNextFinished();
            }
        }
        
        return enhancedMovies;
    }
    
    private void insertMovies(List<ScannedMovie> insertMovies) throws BusinessException {
        LOG.info("Inserting "+insertMovies.size()+" movies into database.");
        if(this.listener != null) {
            this.listener.doNextPhase("Inserting Database values");
        }
        
        final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
        dao.setAutoCommit(false);
        
        boolean committed = false;
        try {
            for (Movie movie: insertMovies) {
                if(this.shouldStop) return;
                dao.insertMovie(movie);
                if(this.listener != null) {
                    this.listener.doNextFinished();
                }
            }
            dao.commit();
            committed = true;
        } catch(BusinessException e) {
            throw e;
        } finally {
            if(committed == false) {
                dao.rollback();
            }
            dao.setAutoCommit(true);
        }
    }
    
    private int directoriesToScan;
    
    private static final IMovieDao DAO = BeanFactory.getInstance().getMovieDao();
    
    private List<ScannedMovie> scanMovies() throws BusinessException {
        LOG.info("Scanning directory: " + this.scanRoot.getAbsolutePath());
        
        final List<String> existingMovieFolders = DAO.getMovieFolderPaths();
        
        final File[] rootFiles = this.scanRoot.listFiles(new FileFilter() {
            public boolean accept(final File file) {
                if(file.isFile() == true) return false;
                if(existingMovieFolders.contains(file.getAbsolutePath()) == true) {
                    LOG.info("Rescanning already inserted movie at path '"+file.getAbsolutePath()+"'.");
                    hints.add(ScanHint.info("Skipped already imported movie '"+file.getName()+"'."));
                    return false;
                }
                return true;
        }});
        
        this.directoriesToScan = rootFiles.length;
        
        if(this.listener != null) {
            this.listener.doDirectoryCount(this.directoriesToScan);
        }
        
        if(this.listener != null) {
            this.listener.doNextPhase("Scanning Directory");
        }
        
        for(File folder : rootFiles) {
            assert(folder.isDirectory() && folder.exists());
            if(this.shouldStop) return null;
            this.result.add(this.scanMovieFolder(folder));
            if(this.listener != null) {
                this.listener.doNextFinished();
            }
        }
        
        LOG.info("Scanning returned "+this.result.size()+" movies.");
        return this.result;
    }
    
    private static String path(File file, File scanRoot) {
        if(scanRoot != null) {
            return file.getAbsolutePath().substring(scanRoot.getAbsolutePath().length() + 1);
        }
        return file.getAbsolutePath();
    }
    
    public static MovieFolderInfo scanMovieFolderInfo(File folder) {
        return scanMovieFolderInfo(folder, null, null);
    }
    
    
    public static MovieFolderInfo scanMovieFolderInfo(File folder, File scanRoot, List<ScanHint> hints) {
        LOG.info("scanning movie '"+folder.getAbsolutePath()+"'...");
        
        final boolean hintsEnabled = hints != null;
        final String folderPath = folder.getAbsolutePath();

        final List<String> files = new LinkedList<String>();
        final Set<String> formats = new HashSet<String>();
        
        final long fileSizeKb = Scanner.scanMovieFolder(folder, files, formats, folder, hints, hintsEnabled);
        
        final String format = constructExtensionString(formats);
        
        if(files.size() == 0) {
            if(hintsEnabled) hints.add(ScanHint.warning("There was not any movie file found for folder '"+scanRoot.getAbsolutePath()+"'."));
        }
        
        return new MovieFolderInfo(folderPath, files, fileSizeKb, format);
    }
    
    private static long scanMovieFolder(File folder, List<String> files, Set<String> formats, File movieFolderRoot, List<ScanHint> hints, boolean hintsEnabled) {
        LOG.debug("Scanning movie folder '"+folder.getAbsolutePath()+"'.");
        long fileSizeKb = 0;
        
        for(File file : folder.listFiles()) {
            if(file.isFile()) {
                final String fileExtension = FileUtil.extractExtension(file);
                if(FileUtil.isHiddenFile(file) == true) {
                    LOG.debug("Skipping hidden file '"+file.getAbsolutePath()+"'.");
                    continue;
                } else if(MovieFileUtil.isMovieFileExtension(fileExtension) == false) {
                    if(fileExtension != null) { // it does have a file extension
                        if(hintsEnabled) hints.add(ScanHint.info("Unkown movie format: " + path(file, movieFolderRoot)));
                    }
                    continue;
                }
                
                if(MovieFileUtil.isMovieFile(file) == false) {
                    throw new FatalException("The given file '"+file.getAbsolutePath()+"' is not a valid movie file!");
                }
                fileSizeKb += file.length() / 1024;
                files.add(path(file, movieFolderRoot)); // store something like "VIDEO_TS/something.vob"
                
                formats.add(fileExtension.toLowerCase());
                
            } else { // file.isDirectory()
                fileSizeKb += Scanner.scanMovieFolder(file, files, formats, movieFolderRoot, hints, hintsEnabled);
            }
        }
        
        return fileSizeKb;
    }
    
    private static String constructExtensionString(final Set<String> formats) {
        final String format;
        
        if(formats.size() == 0) {
            format = "";
        } else if(formats.size() == 1){
            format = formats.iterator().next();
        } else {
            final StringBuilder sb = new StringBuilder();
            final List<String> formatList = new ArrayList<String>(formats.size());
            
            for (String string : formats) {
                formatList.add(string);
            }
            Collections.sort(formatList, String.CASE_INSENSITIVE_ORDER);
            
            boolean first = true;
            for (String string : formatList) {
                if(first == true) {
                    first = false;
                } else {
                    sb.append(Movie.FORMAT_DELIMITER);
                }
                sb.append(string);
            }
            format = sb.toString();
            LOG.debug("Found multiple ("+formats.size()+") movie files; resulting string is '"+format+"'.");
        }
        
        return format;
    }
    
    private ScannedMovie scanMovieFolder(File folder) {
        final String title = FileUtil.clearFileNameDots(folder);
        final MovieFolderInfo folderInfo = scanMovieFolderInfo(folder, this.scanRoot, this.hints);
        
        return ScannedMovie.newByMovie(new RawScannedMovie(title, folderInfo.getFolderPath(), folderInfo.getFileSizeKB(), folderInfo.getFormat(), folderInfo.getFiles()).toMovie(), true);
    }
    
    
    public void shouldStop() {
        LOG.info("got shouldStop() message!");
        this.shouldStop = true;
    }

    public boolean isShouldStop() {
        return this.shouldStop;
    }
}
