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

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.sourceforge.omov.gui.CoreImageFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author http://blog.elevenworks.com/?p=10
 */
public class BrushedMetalPanel extends TiledImagePanel {

    private static final Log LOG = LogFactory.getLog(BrushedMetalPanel.class);
    private static final long serialVersionUID = -8717131766395076544L;

    private static Image image = CoreImageFactory.getInstance().getImgBrushed();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            // Do nothing...
        }

        final JFrame frame = new JFrame("Brushed Metal Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        final JPanel panel = new BrushedMetalPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public BrushedMetalPanel() {
        super(image);
    }

    private HighlightedImagePanelUI ui;
    
    protected void initializeUI() {
    	LOG.debug("initializeUI()");
    	ui = HighlightedImagePanelUI.createUI(this);
        super.setUI(ui);
    }

    public void setImage(Image aImage) {
        if (image == aImage) {
            super.setImage(aImage);
        }
    }

    public void setActive(boolean active) {
//    	LOG.debug("setActive(active="+active+")");
    	this.ui.setActive(active);
    }
}
