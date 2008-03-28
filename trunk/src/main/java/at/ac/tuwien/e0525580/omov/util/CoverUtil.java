package at.ac.tuwien.e0525580.omov.util;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.Configuration;
import at.ac.tuwien.e0525580.omov.bo.CoverFileType;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ImagePanel;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;

public final class CoverUtil {

    private static final Log LOG = LogFactory.getLog(CoverUtil.class);
    private static final IMovieDao DAO = BeanFactory.getInstance().getMovieDao();
    
    
//    public static final File DEFAULT_COVER = initDefaultCover();

    private static final List<String> VALID_COVER_EXTENSIONS = new ArrayList<String>(3);
    static {
        VALID_COVER_EXTENSIONS.add("jpg");
        VALID_COVER_EXTENSIONS.add("jpeg");
        VALID_COVER_EXTENSIONS.add("png");
    }

    
    private CoverUtil() {
        
    }
    
    /**
     * Deletes existing coverfile; if coverfile attribute is set, copies the file and stores the path in database.
     * 
     * precondition: client should have checked if user has changed something
     */
    public static void resetCover(final Movie movie) throws BusinessException {
        final String coverFile = movie.getOriginalCoverFile();
        LOG.debug("Resetting coverFile to '"+coverFile+"' for movie: " + movie);
        
        CoverUtil.deleteCoverFileIfNecessary(movie);
        
        if(movie.isCoverFileSet()) {
            CoverUtil.copyAndSaveCover(movie);
        }
    }

    /**
     * Simply deletes the cover file.
     * 
     * ATTENTION: does not set Movie.coverFile to "" 
     */
    public static boolean deleteCoverFileIfNecessary(final Movie movie) throws BusinessException {
        return CoverUtil.deleteCoverFile(movie, false);
    }

//    public static boolean deleteCoverFile(final Movie movie) throws BusinessException {
//        return CoverUtil.deleteCoverFile(movie, true);
//    }
    
    /**
     * @return true if coverfile was deleted, false if it was not existing
     */
    private static boolean deleteCoverFile(final Movie movie, final boolean wasIntended) throws BusinessException {
        LOG.debug("Deleting cover file for movie '"+movie.getTitle()+"' (ID="+movie.getId()+"); wasIntended="+wasIntended);
        final File imageFolder = Configuration.getInstance().getCoversFolder();
        File[] foundCoverFiles = imageFolder.listFiles(new FileFilter() { public boolean accept(File pathname) {
                return (pathname.getName().startsWith(movie.getId() + ".") // the original image is stored in format: 
                      || pathname.getName().startsWith(movie.getId() + "-")); // resized images are stored in format: "1-40x40.jpg"
        }});
        if(foundCoverFiles == null || foundCoverFiles.length == 0) {
            // assert: movie.coverFilePath != "" 
            final String logMsg = "Did not delete anything because not any cover file existed in image folder '"+imageFolder.getAbsolutePath()+"' for movie: " + movie;
            if(wasIntended == true) {
                LOG.warn(logMsg);
            } else {
                LOG.info(logMsg);
            }
            return false;
        }
        
        
        for (File foundCoverFile : foundCoverFiles) {
            LOG.debug("Deleting cover file '"+foundCoverFile.getAbsolutePath()+"' for movie: " + movie);
            if(foundCoverFile.delete() == false) {
                throw new BusinessException("Could not delete cover file '"+foundCoverFile.getAbsolutePath()+"'!");
            }
        }
        // DAO.updateMovie(Movie.newByOtherMovieSetCoverFile(movie, "")); NO!!! do not update coverFile -> client has to do
        return true;
    }
    
    /**
     * Copies the cover file to application's CoverFold with the format: "ID"."extension" and also updates the DB entry.
     */
    private static void copyAndSaveCover(Movie movie) throws BusinessException {
        LOG.info("Saving cover file '"+movie.getOriginalCoverFile()+"' for movie "+movie+".");
        assert(movie.isCoverFileSet() == true);
        
        final String coverFile = movie.getOriginalCoverFile();
        final String extension = FileUtil.extractExtension(coverFile);
        assert(extension != null); // should have been checked by CoverSelector
        
        final String newCoverFileName = movie.getId() + "." + extension;
        
        final File targetFile = new File(Configuration.getInstance().getCoversFolder(), newCoverFileName);
        FileUtil.copyFile(new File(coverFile), targetFile);
        
        DAO.updateMovie(Movie.newByOtherMovieSetCoverFile(movie, newCoverFileName));
        
        copyCoverFileTypes(movie);
    }
    
    private static void copyCoverFileTypes(Movie movie) throws BusinessException {
        final File sourceFile;
        if(movie.getOriginalCoverFile().startsWith(Configuration.getInstance().getCoversFolder().getAbsolutePath())) {
            sourceFile = new File(Configuration.getInstance().getCoversFolder(), movie.getOriginalCoverFile());
        } else {
            sourceFile = new File(movie.getOriginalCoverFile());
        }
        _copyCoverFileTypes(movie, sourceFile);
    }
    
    public static void copyCoverFileTypesByOriginal(final Movie movie, final File originalCoverFile) throws BusinessException {
        _copyCoverFileTypes(movie, originalCoverFile);
    }
    
    private static void _copyCoverFileTypes(final Movie movie, final File sourceFile) throws BusinessException {
        LOG.debug("Copying all cover file types by sourceFile '"+sourceFile.getAbsolutePath()+"' for movie: " + movie);
        
        for (CoverFileType targetType : CoverFileType.getAllTypes()) {
            createCoverFileType(targetType, movie, sourceFile);
        }
    }
    
    private static void createCoverFileType(CoverFileType fileType, Movie movie, File coverFileSource) throws BusinessException {
        assert(coverFileSource.exists() == true);
        final String coverFileNameTarget = movie.getCoverFile(fileType);
        final File coverFileTarget = new File(Configuration.getInstance().getCoversFolder(), coverFileNameTarget);
        LOG.debug("Copying cover type '"+fileType+"' to file '"+coverFileTarget.getAbsolutePath()+"'.");
        

        final ImagePanel imagePanel = new ImagePanel(fileType.getDimension());
        final Image coverImage = ImageUtil.getResizedCoverImage(coverFileSource, imagePanel, fileType);
        final int w = coverImage.getWidth(null);
        final int h = coverImage.getHeight(null);
        
        final BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        final Graphics2D g2 = bi.createGraphics();
        g2.drawImage(coverImage, 0, 0, null);
        g2.dispose();
        
        try {
            ImageIO.write(bi, FileUtil.extractExtension(coverFileSource), coverFileTarget);
        } catch (Exception e) {
            throw new BusinessException("Could not save coverFile '"+coverFileSource.getAbsolutePath()+"' to '"+coverFileTarget.getAbsolutePath()+"'!", e);
        }
    }
    
    
    /**
     * @return true if the passed extension is equals to some of 'jpg', 'jpeg' or 'png'.
     */
    public static boolean isValidCoverExtension(final String extension) {
        return VALID_COVER_EXTENSIONS.contains(extension);
    }
    
    
    
    public static ImageIcon getMovieCoverImage(Movie movie, CoverFileType coverType) {
        final File coverFile = new File(Configuration.getInstance().getCoversFolder(), movie.getCoverFile(coverType));
        LOG.debug("Loading cover image from '"+coverFile.getAbsolutePath()+"'.");
        // !!! coverFile can be not existing, if currently invoking moviedao-listeners (see: MainWindowController.doEditMovie inconsistency)
        ImageIcon result = new ImageIcon(Toolkit.getDefaultToolkit().getImage(coverFile.getAbsolutePath()));
        return result;
    }
    
    
//    private static File initDefaultCover() {
//        File cover;
//        try {
//            cover = new ClassPathResource(Movie.COVER_FILENAME + ".jpg").getFile();
//        } catch (IOException e) {
//            LOG.error("could not get default coverfile by classpath resource!", e);
//            cover = new File(Movie.COVER_FILENAME + ".jpg");
//        }
//        LOG.debug("initializing default cover with file '" + cover.getAbsolutePath() + "'.");
//        return cover;
//    }
    
    
//    public static File getCover(final Movie movie) {
//        // T O D O   use movie.hasCover() => true, look for file, if not exist throw exception => false, get default
//        final String prefix = UserConfig.getInstance().getDataRoot().getAbsolutePath() + File.separator +
//            movie.getFolder().getName() + File.separator + 
//            Movie.COVER_FILENAME;
//        
//        for (String extension : VALID_COVER_EXTENSIONS) {
//            File cover = new File(prefix + "." + extension);
//            if(cover.exists()) {
//                return cover;
//            }
//        }
//        
//        return CoverUtil.DEFAULT_COVER;
//    }
    
//    public static void copyCover(final File oldFile, final Movie movie) throws IOException {
//        final String oldName = oldFile.getName();
//        final String extension = oldName.substring(oldName.lastIndexOf(".")+1, oldName.length());
//        final String newAbsolutePath = movie.getFolder().getAbsolutePath() + File.separator + Movie.COVER_FILENAME + "." + extension;
//        LOG.info("copying cover to '" + newAbsolutePath + "'...");
//        
//        final File newFile = new File(newAbsolutePath);
//        if(newFile.exists()) {
//            if(!newFile.delete()) {
//                throw new IOException("could not delete file '" + newFile.getAbsolutePath() + "'!");
//            }
//        }
//        
//        FileUtil.copyFile(oldFile, newFile);
//    }
}
