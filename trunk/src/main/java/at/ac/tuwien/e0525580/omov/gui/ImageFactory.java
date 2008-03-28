package at.ac.tuwien.e0525580.omov.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import at.ac.tuwien.e0525580.omov.FatalException;

public class ImageFactory {

    private static final Logger LOG = Logger.getLogger(ImageFactory.class);

    private static final ImageFactory INSTANCE = new ImageFactory();

    private final Map<String, ImageIcon> icons = new HashMap<String, ImageIcon>();

    private ImageFactory() {
        // no instantiation
    }

    public static ImageFactory getInstance() {
        return INSTANCE;
    }
    
    private static final String PATH_IMAGES = "/images/";

    private ImageIcon getImage(final String filename) {
        LOG.debug("getting image by filename '" + filename + "'.");

        final ImageIcon image;

        if (this.icons.get(filename) != null) {
            image = this.icons.get(filename);
        } else {
            final String imagePath = PATH_IMAGES + filename;
            LOG.info("loading and caching image for first time by path '" + imagePath + "'...");
            
            final URL imageUrl = ImageFactory.class.getResource(imagePath);
            if (imageUrl == null) {
                throw new FatalException("Could not load image (filename='" + filename + "') by image path '" + imagePath + "'!");
            }
            
            LOG.debug("loaded image (" + filename + ") by url '" + imageUrl.getFile() + "'.");
            image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageUrl));
            this.icons.put(filename, image);
        }

        return image;
    }

    public Image getImgFolder() {
        return this.getIconFolder().getImage();
    }
    public ImageIcon getIconFolder() {
        return this.getImage("folder.png");
    }
    public Image getImgBrushed() {
        return this.getImage("brushed.gif").getImage();
    }
    public ImageIcon getSplashScreenLogo() {
        return this.getImage("logo_splashscreen.png");
    }
    public ImageIcon getAboutLogo() {
        return this.getImage("logo_about.png");
    }
    
    public ImageIcon getIcon(Icon16x16 iconEnum) {
        return this.getImage("icons/" + iconEnum.fileName);
    }

    public ImageIcon getHelp() {
        return this.getImage("help.png");
    }

    public ImageIcon getSetupWizardBanner() {
        return this.getImage("setup_wizard_banner.png");
    }
    public Image getFrameTitleIcon() {
        return this.getImage("logo_frame_title.png").getImage();
    }
    
    


    public enum Icon16x16 {
        NEW_MOVIE("new_movie.png"),
        HELP("help.png"),
        VLC("vlc.png"),
        PREFERENCES("preferences.png"),
        INFORMATION("information.png"),
        SCAN("scan.png"),
        DELETE("delete.png"),
        FETCH_METADATA("fetch_metadata.png"),
        IMPORT("import.png"),
        EXPORT("export.png"),
        REVEAL_FINDER("reveal_finder.png"),
        SEVERITY_INFO("severity_info.gif"),
        SEVERITY_WARNING("severity_warning.gif"),
        SEVERITY_ERROR("severity_error.gif");
        
        final String fileName;
        private Icon16x16(String fileName) {
            this.fileName = fileName;
        }
    }
    
}
