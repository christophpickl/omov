package at.ac.tuwien.e0525580.omov2.gui;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

import at.ac.tuwien.e0525580.omov2.FatalException;

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
        return this.getImage("folder_73x72.png").getImage();
    }
    public ImageIcon getIconFolder() {
        return this.getImage("folder_73x72.png");
    }
    public Image getImgBrushed() {
        return this.getImage("brushed.gif").getImage();
    }
}
