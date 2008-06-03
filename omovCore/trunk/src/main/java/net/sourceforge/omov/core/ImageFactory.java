/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.core;

import java.awt.Image;
import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import net.sourceforge.omov.core.common.Severity;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
    
    public ImageIcon getSeverityIcon(Severity severity) {
    	final Icon16x16 icon;
    	if(severity == Severity.INFO) {
    		icon = Icon16x16.SEVERITY_INFO;
    	} else if(severity == Severity.WARNING) {
    		icon = Icon16x16.SEVERITY_WARNING;
    	} else if(severity == Severity.ERROR) {
    		icon = Icon16x16.SEVERITY_ERROR;
    	} else {
    		throw new IllegalArgumentException("Unhandled severity "+severity+"!");
    	}
        return this.getIcon(icon);
    }

    public ImageIcon getHelp() {
        return this.getImage("help.png");
    }

    public ImageIcon getDialogError() {
        return this.getImage("dialog/error.png");
    }

    public ImageIcon getSetupWizardBanner() {
        return this.getImage("setup_wizard_banner.png");
    }
    public Image getFrameTitleIcon() {
        return this.getImage("logo_frame_title.png").getImage();
    }

    public Image getBigScanImage() {
        return this.getImage("scan-90x90_alpha18.png").getImage();
    }

    

    public ImageIcon getIcon(IconQuickView iconEnum) {
        return this.getImage("quickview/" + iconEnum.fileName);
    }

    public ImageIcon getContextMenuButton() {
        return this.getImage("ContextMenuButton.png");
    }
    
    
    public enum IconQuickView {
    	BUTTON_PLAY("button_play.png"),
    	BUTTON_BACK("button_back.png"),
    	BUTTON_PAUSE("button_pause.png"),
    	BUTTON_FULLSCREEN("button_fullscreen.png"),
    	BUTTON_CLOSE_MINI("button_close_mini.png");
    	
        final String fileName;
        private IconQuickView(String fileName) {
            this.fileName = fileName;
        }
    }

    
}
