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

import java.awt.Toolkit;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.swing.ImageIcon;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class AbstractImageFactory {

    private static final Logger LOG = Logger.getLogger(AbstractImageFactory.class);

    private static final String PATH_IMAGES = "/images/";
    

    private final Map<String, ImageIcon> iconsCache = new HashMap<String, ImageIcon>();

    
    protected AbstractImageFactory() {
        // no public instantiation
    }
    

    protected ImageIcon getImage(final String filename) {
        LOG.debug("getting image by filename '" + filename + "'.");

        final ImageIcon image;

        final ImageIcon cachedIcon = this.iconsCache.get(filename);
        if (cachedIcon != null) {
            image = cachedIcon;
        } else {
            final String imagePath = PATH_IMAGES + filename;
            LOG.info("loading and caching image for first time by path '" + imagePath + "'...");
            
            final URL imageUrl = AbstractImageFactory.class.getResource(imagePath);
            if (imageUrl == null) {
                throw new FatalException("Could not load image (filename='" + filename + "') by image path '" + imagePath + "'!");
            }
            
            LOG.debug("loaded image (" + filename + ") by url '" + imageUrl.getFile() + "'.");
            image = new ImageIcon(Toolkit.getDefaultToolkit().getImage(imageUrl));
            this.iconsCache.put(filename, image);
        }

        return image;
    }

    
}
