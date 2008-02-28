package at.ac.tuwien.e0525580.omov2.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DecimalFormat;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BusinessException;

public final class FileUtil {

    private static final Log LOG = LogFactory.getLog(FileUtil.class);
    
    
    private FileUtil() {
        // no instantiation
    }

    /**
     * @return null if no extension; otherwise lowercase suffix behind last "."
     */
    public static String extractExtension(File file) {
        return extractExtension(file.getName());
    }

    /**
     * @return null if no extension; otherwise lowercase suffix behind last "."
     */
    public static String extractExtension(String fileName) {
        if(fileName.indexOf(".") > 0) {
            return fileName.substring(fileName.lastIndexOf(".")+1).toLowerCase();
        }
        return null;
    }
    
    public static void copyFile(final File source, final File target) throws BusinessException {
        LOG.debug("Copying file '"+source.getAbsolutePath()+"' to '"+target.getAbsolutePath()+"'.");
        
        if(source.exists() == false) {
            throw new BusinessException("Could not copy sourcefile '"+source.getAbsolutePath()+"' because it does not exist!");
        }
        
        InputStream input = null;
        OutputStream output = null;
        
        if(target.exists() == true) {
            LOG.info("Overwrting existing target file '"+target.getAbsolutePath()+"'.");
            if(target.delete() == false) {
                throw new BusinessException("Could not delete target file '"+target.getAbsolutePath()+"'!");
            }
        }
        try {
            input = new FileInputStream(source);
            output = new FileOutputStream(target);
            
            byte[] bytes = new byte[1024];
            while(input.read(bytes) >= 0) {
                output.write(bytes);
            }
        } catch(IOException e) {
            throw new BusinessException("Could not copy file from '"+source.getAbsolutePath()+"' to '"+target.getAbsolutePath()+"'!", e);
        } finally {
            try { if(input != null) input.close(); } catch(IOException e) { LOG.error("Could not close stream!", e);}
            try { if(output != null) output.close(); } catch(IOException e) { LOG.error("Could not close stream!", e);}
        }
    }
    
    public static void copyDirectoryRecursive(final File sourceDir, final File targetSuperDir) throws BusinessException {
        LOG.debug("Copying directory recursive '"+sourceDir.getAbsolutePath()+"' to '"+targetSuperDir.getAbsolutePath()+"'.");
        
        if(sourceDir.exists() == false) {
            throw new BusinessException("Could not copy sourcefile '"+sourceDir.getAbsolutePath()+"' because it does not exist!");
        }
        
        final File targetDir = new File(targetSuperDir, sourceDir.getName());
        if(targetDir.mkdir() == false) {
            throw new BusinessException("Could not create directory '"+targetDir.getAbsolutePath()+"'!");
        }
        
        for (File subFile : sourceDir.listFiles()) {
            if(subFile.isFile()) {
                copyFile(subFile, new File(targetDir, subFile.getName()));
            } else {
                copyDirectoryRecursive(subFile, targetDir);
            }
        }
    }
    
    private static final DecimalFormat FILE_SIZE_FORMAT = new DecimalFormat("0.0");
    public static String formatFileSize(final long inKiloByte) {
//        if(inByte < 1024) {
//            return FILE_SIZE_FORMAT.format(inByte) + "B"; 
//        }
//            
//        final long inKiloByte = inByte / 1024;
        if(inKiloByte < 1024) {
            return FILE_SIZE_FORMAT.format(inKiloByte) + " KB";
        }
            
        final long inMegaByte = inKiloByte / 1024;
        if(inMegaByte < 1024) {
            return FILE_SIZE_FORMAT.format(inKiloByte / 1024.) + " MB";
        }

        return FILE_SIZE_FORMAT.format(inMegaByte / 1024) + " GB";
    }
    
    public static String formatFileSizeGb(final long inKiloByte) {
        return FILE_SIZE_FORMAT.format(inKiloByte / 1024. / 1024.) + " GB";
    }
    
    public static double getGigaByteFromKiloByte(long kb) {
        double mb = kb / 1024.;
        double gb = mb / 1024. * 10;
        long gb10th = Math.round(gb);
        return gb10th / 10.;
    }
    
    public static void deleteDirectoryRecursive(final File directory) throws BusinessException {
        LOG.debug("Deleting directory '"+directory.getAbsolutePath()+"' recursive.");
        if(directory.exists() == false) throw new IllegalArgumentException("Directory '"+directory.getAbsolutePath()+"' does not exist!");
        if(directory.isDirectory() == false) throw new IllegalArgumentException("Given file '"+directory.getAbsolutePath()+"' is not a directory!");
        
        for (File subFile : directory.listFiles()) {
            if(subFile.isFile()) {
                if(subFile.delete() == false) {
                    throw new BusinessException("Could not delete file '"+subFile.getAbsolutePath()+"'!");
                }
            } else {
                deleteDirectoryRecursive(subFile);
            }
        }
        
        
        if(directory.delete() == false) {
            throw new BusinessException("Could not delete directory '"+directory.getAbsolutePath()+"'!");
        }
    }
    
//    public static void markMovieAdded(final Movie movie) throws IOException {
//        final File markFile = FileUtil.getMarkMovieAdd(movie);
//        
//        if(!markFile.createNewFile()) {
//            throw new IOException("could not create file '" + markFile.getAbsolutePath() + "'!");
//        }
//        FileWriter writer = null;
//        try {
//            writer = new FileWriter(markFile);
//            writer.write(String.valueOf(movie.getId()));
//        } finally {
//            if(writer != null) writer.close();
//        }
//    }
    
//    public static File getMarkMovieAdd(final Movie movie) {
//        return new File(UserConfig.getInstance().getDataRoot(), movie.getFolder().getName() + File.separator + Scanner.MOVIE_ADDED_MARK_NAME);
//    }
    
//    public static void unmarkMovieAdded(final Movie movie) throws IOException {
//        final File markFile = FileUtil.getMarkMovieAdd(movie);
//        if(!markFile.delete()) {
//            throw new IOException("could not delete file '" + markFile.getAbsolutePath() + "'!");
//        }
//    }
    
//    public static void deleteCover(final Movie movie) throws IOException {
//        if(!movie.getCoverFile().delete()) {
//            throw new IOException("could not delete file '" + movie.getCoverFile().getAbsolutePath() + "'!");
//        }
//    }
}

