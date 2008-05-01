package at.ac.tuwien.e0525580.omov.tools.export;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.util.CoverUtil;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.ZipUtil;

import com.thoughtworks.xstream.XStream;

public class ImporterBackup implements ImportExportConstants {

    private static final Log LOG = LogFactory.getLog(ImporterBackup.class);
    private static final DateFormat TARGET_DIRECTORY_FORMAT = new SimpleDateFormat("'backupImport-'yyyyMMdd_HHmmssSSS");

    private final File backupFile;
    private final ZipFile backupZipFile;
    
    public ImporterBackup(File backupFile, ZipFile backupZipFile) {
        assert(backupFile.exists() && backupFile.isFile());
        assert(backupZipFile != null);
        this.backupFile = backupFile;
        this.backupZipFile = backupZipFile;
    }
    
    public ImportProcessResult process() {
        LOG.info("Importing backup file '"+this.backupFile.getAbsolutePath()+"'.");
        ImportProcessResult result = new ImportProcessResult();
        
        final File targetDirectory = new File(PreferencesDao.getInstance().getTemporaryFolder(), TARGET_DIRECTORY_FORMAT.format(new Date()));
        
        try {
            try {
                ZipUtil.unzip(this.backupFile, this.backupZipFile, targetDirectory);
            } catch (BusinessException e) {
                result.setErrorMessage("Could not unpack backup file '"+this.backupFile.getAbsolutePath()+"'!");
                return result;
            }
            assert(targetDirectory.exists() && targetDirectory.isDirectory());
            
            int storedDataVersion;
            try {
                storedDataVersion = readDataVersion(new File(targetDirectory, FILE_DATA_VERSION));
            } catch (BusinessException e) {
                result.setErrorMessage("Getting data version of backup file '"+this.backupFile.getAbsolutePath()+"' failed!");
                return result;
            }
            if(storedDataVersion != Movie.DATA_VERSION) {
                result.setErrorMessage("The backup can not be read by this application version!\n" +
                                        "Try using a more recent version of OurMovies.\n" +
                                        "Backup's version: "+storedDataVersion+"; Version in use: " + Movie.DATA_VERSION);
                return result;
            }

            List<Movie> backedUpMovies;
            try {
                backedUpMovies = parseXmlMovies(targetDirectory);
            } catch (BusinessException e) {
                result.setErrorMessage("The XML contents of the backup file '"+this.backupFile.getAbsolutePath()+"' are corrupted!");
                return result;
            }
            final List<Movie> insertedMovies = new ArrayList<Movie>(backedUpMovies.size());
            final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
            final boolean wasAutoCommitEnabled = dao.isAutoCommit();
            boolean copyingSucceeded = false;
            try {
                dao.setAutoCommit(false);
                LOG.info("Trying to insert "+backedUpMovies.size()+" movies into database.");
                final List<String> movieFolderPaths = dao.getMovieFolderPaths();
                for (Movie movie : backedUpMovies) {
                    // check path; if exsists, skip + add user info
                    if(movieFolderPaths.contains(movie.getFolderPath())) {
                        LOG.debug("Skipping movie because its folder path '"+movie.getFolderPath()+"' is already in use (movie="+movie+").");
                        result.addSkippedMovie(movie);
                        continue;
                    }
                    
                    // insert movie into dao and add to new list
                    LOG.debug("Inserting movie: " + movie);
                    final Movie insertedMovie = dao.insertMovie(movie);
                    insertedMovies.add(insertedMovie);
                }

                this.copyCoverFiles(targetDirectory, insertedMovies);
                
                dao.commit();
                copyingSucceeded = true;
            } catch(BusinessException e) {
                result.setErrorMessage("Some internal database error occured\nduring import of backup file '"+this.backupFile.getAbsolutePath()+"'!");
                return result;
            } finally {
                if(copyingSucceeded == false) {
                    dao.rollback();
                }
                dao.setAutoCommit(wasAutoCommitEnabled);
            }
            
            result.setInsertedMovie(insertedMovies);
            result.succeeded();
            return result;
            
        } finally {
            // cleanup
            try {
                if(targetDirectory.exists()) {
                    FileUtil.deleteDirectoryRecursive(targetDirectory);
                }
            } catch(Exception e) {
                LOG.error("Could not delete temporary import directory '"+targetDirectory.getAbsolutePath()+"'.", e);
            }
        }
    }
    
    private void copyCoverFiles(final File targetDirectory, final List<Movie> insertedMovies) {
        final File coversFolder = new File(targetDirectory, FOLDER_COVERS);
        LOG.info("Copying covers from coversFolder: " + coversFolder.getAbsolutePath());
        for (Movie movie : insertedMovies) {
            if(movie.isCoverFileSet() == false) {
                continue;
            }
            
            final File originalCoverFile = new File(coversFolder, movie.getOriginalCoverFile());
            assert(originalCoverFile.exists());
            try {
                final String extension = FileUtil.extractExtension(originalCoverFile);
                assert(extension != null);
                final String newCoverFileName = movie.getId() + "." + extension;
                
                final File targetOriginalMovie = new File(PreferencesDao.getInstance().getCoversFolder(), newCoverFileName);
                LOG.debug("Copying cover files from original file '"+originalCoverFile.getAbsolutePath()+"' to '"+targetOriginalMovie.getAbsolutePath()+"' for movie: " + movie);
                FileUtil.copyFile(originalCoverFile, targetOriginalMovie);
                CoverUtil.copyCoverFileTypesByOriginal(movie, originalCoverFile);
                
                BeanFactory.getInstance().getMovieDao().updateMovie(Movie.newByOtherMovieSetCoverFile(movie, newCoverFileName));
                
            } catch(BusinessException e) {
                LOG.error("Could not copy cover file for movie: " + movie, e);
                try {
                    LOG.debug("Clearing coverfile attribute (was '"+movie.getOriginalCoverFile()+"').");
                    BeanFactory.getInstance().getMovieDao().updateMovie(Movie.newByOtherMovieSetCoverFile(movie, "")); // set coverfile attribute to no set
                } catch(Exception e1) {
                    LOG.error("Could not clear cover file attribute for movie: " + movie, e1);
                }
                try {
                    CoverUtil.deleteCoverFileIfNecessary(movie);
                } catch(Exception e1) {
                    LOG.error("Could not clean up cover files for movie: " + movie, e1);
                }
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    private static List<Movie> parseXmlMovies(File targetDirectory) throws BusinessException {
        final File xmlFile = new File(targetDirectory, FILE_MOVIES_XML);
        final XStream xstream = new XStream();
        try {
            return (List<Movie>) xstream.fromXML(new FileReader(xmlFile));
        } catch (FileNotFoundException e) {
            throw new BusinessException("Could not read movies by xml '"+xmlFile.getAbsolutePath()+"' because it does not exist!", e);
        }
    }
    
    
    private static int readDataVersion(File dataVersionFile) throws BusinessException {
        LOG.debug("Reading data version from backupfile '"+dataVersionFile.getAbsolutePath()+"'.");
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(dataVersionFile));
            final int result = Integer.parseInt(reader.readLine().trim());
            LOG.debug("Read data version '"+result+"' from backupfile '"+dataVersionFile.getAbsolutePath()+"'.");
            return result;
        } catch(IOException e) {
            throw new BusinessException("Could not get contents of data versionfile '"+dataVersionFile.getAbsolutePath()+"'.", e);
        } finally {
            FileUtil.closeCloseable(reader);
        }
    }
    
    
    public static void main(String[] args) {
//        final File f = new File("/zip/omov_backup-2008_03_25.omo"); // DATA VERISON 1
        final File f = new File("/zip/omov_backup-2008_03_28.omo"); // DATA VERISON 2
        
        ZipFile zf;
        try {
            zf = new ZipFile(f);
        } catch (ZipException e) {
            System.out.println("ZipException: " + e.getMessage());
            return;
        } catch (IOException e) {
            System.out.println("IOException: " + e.getMessage());
            return;
        }
        new ImporterBackup(f, zf).process();
    }
    
}
