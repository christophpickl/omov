package at.ac.tuwien.e0525580.omov.tools.export;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.Configuration;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.ZipUtil;

import com.thoughtworks.xstream.XStream;

public class ExporterBackup implements ImportExportConstants {

    private static final Log LOG = LogFactory.getLog(ExporterBackup.class);
    
    public static void main(String[] args) throws BusinessException {
//        List<Movie> m = new LinkedList<Movie>();
//        m.add(Movie.getDummy());
//        new ExporterXml(m, new OutputStreamWriter(System.out)).process();
        
//        for (Movie m : ExporterBackup.encodeMovies(new File("/movie.xml"))) {
//            System.out.println(m);
//        }
        
        List<Movie> movies = BeanFactory.getInstance().getMovieDao().getMoviesSorted();
        ExporterBackup.process(movies, new File("/data.omo"));
    }
    
    
    private static DateFormat ZIP_FILE_NAME = new SimpleDateFormat("yyyy_MM_dd");
    private static DateFormat TEMP_FOLDER_NAME = new SimpleDateFormat("yyyyMMddHHmmssSSS");
    
    private ExporterBackup() {
        
    }

    public static File process(List<Movie> movies, File targetDir) throws BusinessException {
        assert(targetDir.exists() && targetDir.isDirectory());
        boolean successfullyProccessed = false;
        
        final File backupTempDir = new File(Configuration.getInstance().getTemporaryFolder(), "exportBackup_" + TEMP_FOLDER_NAME.format(new Date()));
        try {
            if(backupTempDir.mkdirs() == false) {
                throw new BusinessException("Could not create folder '"+backupTempDir.getAbsolutePath()+"'!");
            }
            
            writeXml(movies, new File(backupTempDir, FILE_MOVIES_XML));
            
            copyCoverFiles(backupTempDir, movies);
            
            writeDataVersion(backupTempDir, Movie.DATA_VERSION);
            
            final File backupFile = zipContents(backupTempDir, targetDir);
            
            successfullyProccessed = true;
            return backupFile;
        } finally {
            if(successfullyProccessed == false) {
                
            }
            
            // cleanup
            if(backupTempDir.exists() == true) {
                try {
                    FileUtil.deleteDirectoryRecursive(backupTempDir);
                } catch(BusinessException e) {
                    LOG.error("Could not clean up temporary directory at '"+backupTempDir.getAbsolutePath()+"'!", e);
                }
            }
        }
    }
    
    private static File zipContents(File backupTempDir, File targetDir) throws BusinessException {
        LOG.debug("Zipping contents to single file.");
        
        final File targetZipFile = getAvailableZipFile(targetDir);
        final File temporaryZipFile = new File(Configuration.getInstance().getTemporaryFolder(), backupTempDir.getName() + ".zip");
        
        ZipUtil.zipDirectory(backupTempDir, temporaryZipFile);
        
        LOG.debug("Moving zip file '"+temporaryZipFile.getAbsolutePath()+"' to '"+targetZipFile.getAbsolutePath()+"'.");
        temporaryZipFile.renameTo(targetZipFile);
        return targetZipFile;
        
    }
    
    /**
     * copies only original cover file (when importing, resizing all images to all types and save them).
     */
    private static void copyCoverFiles(File backupTempDir, List<Movie> movies) throws BusinessException {
        LOG.debug("Copying cover files");
        final File coverDir = new File(backupTempDir, FOLDER_COVERS);
        if(coverDir.mkdir() == false) {
            throw new BusinessException("Could not create folder '"+coverDir.getAbsolutePath()+"'!");
        }
        for (final Movie movie : movies) {
            if(movie.isCoverFileSet()) {
                final File coverFile = new File(Configuration.getInstance().getCoversFolder(), movie.getOriginalCoverFile());
                if(coverFile.exists()) {
                    FileUtil.copyFile(coverFile, new File(coverDir, coverFile.getName()));
                } else {
                    LOG.warn("Could not copy cover for movie '"+movie.getTitle()+"' (ID="+movie.getId()+") because its cover file at '"+coverFile.getAbsolutePath()+"' does not exist anymore!");
                }
            }
        }
    }
    
    private static void writeDataVersion(File directory, int version) throws BusinessException {
        BufferedWriter writer = null;
        
        final File versionFile = new File(directory, FILE_DATA_VERSION);
        try {
            writer = new BufferedWriter(new FileWriter(versionFile));
            writer.write(String.valueOf(version));
        } catch(IOException e) {
            throw new BusinessException("Could not write movie data version to file '"+versionFile.getAbsolutePath()+"'!", e);
        } finally {
            if(writer != null) try { writer.close(); } catch(IOException e) { LOG.warn("Could not close buffered writer for file '"+versionFile.getAbsolutePath()+"'", e); } 
        }
    }
    
    private static File getAvailableZipFile(File directory) {
        File file = null;
        int i = 0;
        do {
            final String fileName;
            if(i == 0) {
                fileName = "omov_backup-" + ZIP_FILE_NAME.format(new Date()) + "." + BACKUP_FILE_EXTENSION;
            } else {
                fileName = "omov_backup-" + ZIP_FILE_NAME.format(new Date()) + "-" + i + "." + BACKUP_FILE_EXTENSION;
            }
            file = new File(directory, fileName);
            i++;
        } while(file.exists() == true);
        return file;
    }
    
    private static void writeXml(List<Movie> movies, File xmlFile) throws BusinessException {
        LOG.debug("Writing movie data as XML file to '"+xmlFile.getAbsolutePath()+"'.");
        XStream xstream = new XStream();
        String xml = xstream.toXML(movies);
        
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(xmlFile));
            writer.write(xml);
        } catch(IOException e) {
            throw new BusinessException("Could not generate HTML report!", e);
        } finally {
            if(writer != null) try { writer.close(); } catch(IOException e) {};
        }
    }
    
    @SuppressWarnings("unchecked")
    public static List<Movie> encodeMovies(File xmlFile) throws BusinessException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(xmlFile));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while( (line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            XStream xstream = new XStream();
            return (List<Movie>) xstream.fromXML(sb.toString());
        } catch(IOException e) {
            throw new BusinessException("Could not encode movies from: " + xmlFile.getAbsolutePath(), e);
        } finally {
            if(reader != null) try { reader.close(); } catch(IOException e) {};
        }
    }

}
