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

package net.sourceforge.omov.gui.brushed;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;

/**
 * 
 * @author http://blog.elevenworks.com/?p=10
 * @author christoph_pickl@users.sourceforge.net
 */
public class HighlightedImagePanelUI extends TiledImagePanelUI {
    
	private boolean active = true;
	
    public static HighlightedImagePanelUI createUI(JComponent c) {
        return new HighlightedImagePanelUI();
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        
        final Dimension vSize = c.getSize();
        final Graphics2D g2d = (Graphics2D) g;
        
        // Create the paint for the second layer of the button
        final Color vGradientStartColor = active ? Color.GRAY : Color.WHITE;
        final Color vGradientEndColor = Color.WHITE;
        
        final int vHorizontalCenter = vSize.width / 2;
        final int vOffset = (int) (vSize.width * .1);
        
        final Composite vPreviousComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
        g2d.setColor(Color.WHITE);
        g2d.fillRect(vHorizontalCenter - vOffset, 0, vOffset * 2, vSize.height);
        
        final Paint vPaint = new GradientPaint(0, 0, vGradientStartColor, vHorizontalCenter - vOffset, 0, vGradientEndColor, false);
        g2d.setPaint(vPaint);
        g2d.fillRect(0, 0, vHorizontalCenter - vOffset, vSize.height);
        
        final Paint vPaint2 = new GradientPaint(vSize.width, 0, vGradientStartColor, vHorizontalCenter + vOffset, 0, vGradientEndColor, false);
        g2d.setPaint(vPaint2);
        g2d.fillRect(vHorizontalCenter + vOffset, 0, vHorizontalCenter - vOffset, vSize.height);
        g2d.setComposite(vPreviousComposite);
    }
    

    public void setActive(boolean active) {
    	this.active = active;
    }
}

