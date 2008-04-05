package at.ac.tuwien.e0525580.omov.util;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;

public final class FileUtil {

    private static final Log LOG = LogFactory.getLog(FileUtil.class);

    
    private static final Set<String> HIDDEN_FILE_NAMES = CollectionUtil.immutableSet(
            // apple system files
            ".DS_Store", "Icon\r",
            // windows system files
            "Thumbs.db");
    
    
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
    
    private static long MB5 = 1024 * 1024 * 5;
    public static void copyFile(File sourceFile, File targetFile) throws BusinessException {
        if(sourceFile.exists() == false) {
            throw new IllegalArgumentException("Could not copy given file '"+sourceFile.getAbsolutePath()+"' becaus it does not exist!");
        }
        
        if(sourceFile.length() > MB5) {
            copyBigFile(sourceFile, targetFile);
        } else {
            copySmallFile(sourceFile, targetFile);
        }
    }
    
    private static void copyBigFile(final File sourceFile, final File targetFile) throws BusinessException {
        LOG.debug("Copying BIG file '"+sourceFile.getAbsolutePath()+"' to '"+targetFile.getAbsolutePath()+"'.");
        
        if(sourceFile.exists() == false) {
            throw new BusinessException("Could not copy sourcefile '"+sourceFile.getAbsolutePath()+"' because it does not exist!");
        }
        
        InputStream input = null;
        OutputStream output = null;
        
        if(targetFile.exists() == true) {
            LOG.info("Overwrting existing target file '"+targetFile.getAbsolutePath()+"'.");
            if(targetFile.delete() == false) {
                throw new BusinessException("Could not delete target file '"+targetFile.getAbsolutePath()+"'!");
            }
        }
        try {
            input = new FileInputStream(sourceFile);
            output = new FileOutputStream(targetFile);
            
            byte[] bytes = new byte[1024];
            while(input.read(bytes) >= 0) {
                output.write(bytes);
            }
        } catch(IOException e) {
            throw new BusinessException("Could not copy file from '"+sourceFile.getAbsolutePath()+"' to '"+targetFile.getAbsolutePath()+"'!", e);
        } finally {
            try { if(input != null) input.close(); } catch(IOException e) { LOG.error("Could not close stream!", e);}
            try { if(output != null) output.close(); } catch(IOException e) { LOG.error("Could not close stream!", e);}
        }
    }
    
    /**
     * @see {@link http://www.rgagnon.com/javadetails/java-0064.html}
     */
    private static void copySmallFile(File sourceFile, File targetFile) throws BusinessException {
        LOG.debug("Copying SMALL file '"+sourceFile.getAbsolutePath()+"' to '"+targetFile.getAbsolutePath()+"'.");
        
        FileChannel inChannel = null;
        FileChannel outChannel = null;
        try {
            inChannel = new FileInputStream(sourceFile).getChannel();
            outChannel = new FileOutputStream(targetFile).getChannel();
            inChannel.transferTo(0, inChannel.size(), outChannel);
            
        } catch (IOException e) {
            throw new BusinessException("Could not copy file from '"+sourceFile.getAbsolutePath()+"' to '"+targetFile.getAbsolutePath()+"'!", e);
        } finally {
            try { if(inChannel != null) inChannel.close(); } catch(IOException e) { LOG.error("Could not close input stream!", e);}
            try { if(outChannel != null) outChannel.close(); } catch(IOException e) { LOG.error("Could not close output stream!", e);}
        }
    }
    
    public static File copyDirectoryRecursive(final File sourceDir, final File targetSuperDir) throws BusinessException {
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
        
        return targetDir;
    }
    
    private static final DecimalFormat FILE_SIZE_FORMAT = new DecimalFormat("0.0");
    /**
     * @return something like "13.3 KB" or "3.1 GB"
     */
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
                LOG.debug("Deleting file '"+subFile.getAbsolutePath()+"'.");
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
    
    /**
     * @param fileWithDots file or folder, does not matter 
     */
    public static String clearFileNameDots(final File fileWithDots) {
        // FEATURE fileutil: ignore dots with leading "mr" or "dr" (not case-sensitive)
        return fileWithDots.isFile() ? clearFileDots(fileWithDots) : clearDirectoryDots(fileWithDots);
    }

    private static String clearDirectoryDots(final File directoryWithDots) {
        final String nameWithDots = directoryWithDots.getName();
        final int cntDots = nameWithDots.split("\\.").length;

        if(cntDots <= 1) {
            LOG.debug("No dots to clear for file '"+directoryWithDots.getAbsolutePath()+"'.");
            return nameWithDots;
        }
        
        return nameWithDots.replaceAll("\\.", " ");
    }
    
    private static String clearFileDots(final File fileWithDots) {
        final String nameWithDots = fileWithDots.getName();
        final int cntDots = nameWithDots.split("\\.").length;

        if(cntDots <= 2) {
            LOG.debug("No dots to clear for file '"+fileWithDots.getAbsolutePath()+"'.");
            return nameWithDots;
        }
        final String extension = nameWithDots.substring(nameWithDots.lastIndexOf(".")+1);
        
        String result = nameWithDots.substring(0, nameWithDots.length() - (extension.length() + 1));
        result = result.replaceAll("\\.", " ");
        return result + "." + extension;
    }

    public static boolean isHiddenFile(File file) {
        return HIDDEN_FILE_NAMES.contains(file.getName());
    }
    
//    public static String getFileContents(final File sourceFile, int initialCapacityOfStringBuilder) throws BusinessException {
//        final StringBuilder sb = new StringBuilder(initialCapacityOfStringBuilder);
//        BufferedReader reader = null;
//        try {
//            reader = new BufferedReader(new FileReader(sourceFile));
//            String line = null;
//            while ( (line = reader.readLine()) != null) {
//                sb.append(line).append("\n");
//            }
//            
//            return sb.toString();
//        } catch (IOException e) {
//            throw new BusinessException("Could not get contents of file '"+sourceFile.getAbsolutePath()+"'!", e);
//        } finally {
//            try { if(reader != null) reader.close(); } catch(IOException e) { LOG.error("Could not close input stream!", e);}
//        }
//    }
    
    public static String getFileContentsFromJar(final String jarFile, int initialCapacityOfStringBuilder) throws BusinessException {
        LOG.debug("Getting contents of file '"+jarFile+"' from jar.");
        final StringBuilder sb = new StringBuilder(initialCapacityOfStringBuilder);
        InputStream input = null;
        BufferedReader reader = null;
        try {
            input = FileUtil.class.getResourceAsStream(jarFile);
            reader = new BufferedReader(new InputStreamReader(input));
            String line = null;
            while ( (line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            
            return sb.toString();
        } catch (IOException e) {
            throw new BusinessException("Could not get contents of resource '"+jarFile+"'!", e);
        } finally {
            try { if(reader != null) reader.close(); } catch(IOException e) { LOG.error("Could not close input stream!", e);}
        }
    }
    
    public static String extractLastFolderName(final String path) {
        final int index = path.lastIndexOf(File.separator);
        if(index == -1) {
            LOG.warn("Could not get last folder name of path '"+path+"'!");
            return null;
        }
        return path.substring(index + 1);
    }
    
    public static void closeCloseable(Closeable closeable) {
        if(closeable != null) {
            try {
                closeable.close();
            } catch(IOException e) {
                LOG.warn("Could not close closeable.");
            }
        }
    }
    
    public static long getSizeRecursive(File file) {
        if(file.isFile()) {
            return file.length() / 1024;
        }
        
        long size = 0;
        for (File subFile : file.listFiles()) {
            size += getSizeRecursive(subFile);
        }
        return size;
    }
    
    public static File getParentByPath(final File file) {
        final String path = file.getAbsolutePath();
        
        return new File(path.substring(0, path.lastIndexOf(File.separator)));
    }
    
    
    public static void main(String[] args) {
        System.out.println(extractLastFolderName("/folder3/folder2/folder1"));
    }
}

