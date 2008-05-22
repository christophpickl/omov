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

package net.sourceforge.omov.gui;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JComponent;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ImagePanel extends JComponent {
    
    private static final long serialVersionUID = -9004123909937374280L;
    private Image image;
    

    public ImagePanel(int width, int height) {
        this(new Dimension(width, height));
    }

    public ImagePanel(Dimension dimension) {
    	this.setPreferredSize(dimension);
    	this.setMaximumSize(dimension);
    	this.setMinimumSize(dimension);
    }
//    public ImagePanel(String img) {
//      this(new ImageIcon(img).getImage());
//    }
    
    public ImagePanel(Image image) {
        this.setImage(image);
        this.setLayout(null);
    }
    
    public void setImage(Image image) {
        this.image = image;
        
        if(image != null) {
            final Dimension size = new Dimension(image.getWidth(null), image.getHeight(null));
            this.setPreferredSize(size);
            this.setMinimumSize(size);
            this.setMaximumSize(size);
            this.setSize(size);
        }

        this.repaint();
    }
    
    public void paintComponent(Graphics g) {
        if(this.image != null) {
            g.drawImage(this.image, 0, 0, null);
        }
    }
    
}
