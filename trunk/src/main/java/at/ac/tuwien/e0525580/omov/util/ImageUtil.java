package at.ac.tuwien.e0525580.omov.util;

import java.awt.Component;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.image.FilteredImageSource;
import java.awt.image.ImageFilter;
import java.awt.image.ImageProducer;
import java.awt.image.ReplicateScaleFilter;
import java.io.File;
import java.util.Date;

import org.apache.log4j.Logger;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.ImagePanel;

public class ImageUtil {

    private static final Logger LOG = Logger.getLogger(ImageUtil.class);
    
    private ImageUtil() {
        
    }
    
    // FEATURE wenn neue cover set auf movie -> file nicht 1:1 uebernehmen, sondern in verschiedene resized formate uebernehmen
    
    public static WidthHeight recalcMaxWidthHeight(final int oldWidth, final int oldHeight, final int maxWidth, final int maxHeight) {
        final boolean DEBUG = false;

        final int newWidth;
        final int newHeight;
        
        final boolean widthOversize = oldWidth > maxWidth;
        final boolean heightOversize = oldHeight > maxHeight;
        
        if(DEBUG) System.out.println("old "+oldWidth+"/"+oldHeight+"; max "+maxWidth+"/"+maxHeight+"-- widthOversize="+widthOversize+", heightOversize="+heightOversize);
        if(widthOversize && heightOversize) {
            if(DEBUG) System.out.println("ImageUtil: width+height oversize");
            if(oldWidth < oldHeight) {
                if(DEBUG) System.out.println("ImageUtil: height oversize dominates"); // or is equal
                newHeight = maxHeight;
                newWidth = (int) ((double)oldWidth * ((double) maxHeight/oldHeight));
            } else {
                if(DEBUG) System.out.println("ImageUtil: width oversize dominates");
                newWidth = maxWidth;
                newHeight = (int) ((double)oldHeight * ((double) maxWidth/oldWidth));
            }
        } else if(heightOversize){
            if(DEBUG) System.out.println("ImageUtil: only height oversize");
            newHeight = maxHeight;
            newWidth = (int) ((double)oldWidth * ((double) maxHeight/oldHeight));
        } else if(widthOversize) {
            if(DEBUG) System.out.println("ImageUtil: only width oversize");
            newWidth = maxWidth;
            newHeight = (int) ((double)oldHeight * ((double) maxWidth/oldWidth));
        } else { // FEATURE maybe check also for too small?!
            if(DEBUG) System.out.println("ImageUtil: image to small; reusing old width and height");
            newHeight = oldHeight;
            newWidth = oldWidth;
        }

        if(DEBUG) System.out.println("ImageUtil: new "+newWidth+"/"+newHeight);
        return new WidthHeight(newWidth, newHeight);
    }
    
    public static WidthHeight recalcMaxWidthHeight(final File coverFile, final int maxWidth, final int maxHeight) {
        LOG.debug("Recalcing width/height for cover file at '"+coverFile.getAbsolutePath()+"'.");
        final ImagePanel imagePanel = new ImagePanel(maxWidth, maxHeight);
        final MediaTracker media = new MediaTracker(imagePanel);
        final Image source = Toolkit.getDefaultToolkit().getImage(coverFile.getAbsolutePath());
        media.addImage(source,0);
        
        try {
            media.waitForID(0);
            final int oldWidth = source.getWidth(imagePanel);
            final int oldHeight = source.getHeight(imagePanel);
            
            return recalcMaxWidthHeight(oldWidth, oldHeight, maxWidth, maxHeight);
        } catch(InterruptedException e) {
            LOG.error("interrupted while creating resized cover image!", e);
            throw new RuntimeException("interrupted while creating resized cover image!");
        }
    }
    
    public static Image getResizedCoverImage(final File coverFile, final Component component, final int maxWidth, final int maxHeight) {
        LOG.info("resizing cover image '" + coverFile.getAbsolutePath() + "' to maxWidth '"+maxWidth+"' and maxHeight "+maxHeight+".");
        Date resizeActionStart = new Date();
        final MediaTracker media = new MediaTracker(component);
        Image source = Toolkit.getDefaultToolkit().getImage(coverFile.getAbsolutePath());
        Image resizedImage;
        media.addImage(source,0);
        try {
            media.waitForID(0);
            final int oldWidth = source.getWidth(component);
            final int oldHeight = source.getHeight(component);
            
            final WidthHeight newDimension = recalcMaxWidthHeight(oldWidth, oldHeight, maxWidth, maxHeight);
            final int newWidth = newDimension.getWidth();
            final int newHeight = newDimension.getHeight();

            
            ImageFilter replicate = new ReplicateScaleFilter(newWidth, newHeight);
            ImageProducer prod = new FilteredImageSource(source.getSource(),replicate);
            resizedImage = Toolkit.getDefaultToolkit().createImage(prod);
            media.addImage(resizedImage,1);
            media.waitForID(1);
        } catch(InterruptedException e) {
            LOG.error("interrupted while creating resized cover image!", e);
            throw new RuntimeException("interrupted while creating resized cover image!");
        }
        LOG.info("resizing image took " + ((double)(new Date(new Date().getTime() - resizeActionStart.getTime())).getTime()/1000) + " seconds.");
        return resizedImage;
    }
    

    public static class WidthHeight {
        private final int width;
        private final int height;
        public WidthHeight(final int width, final int height) {
            super();
            this.width = width;
            this.height = height;
        }
        public int getHeight() {
            return this.height;
        }
        public int getWidth() {
            return this.width;
        }
        
    }
}
