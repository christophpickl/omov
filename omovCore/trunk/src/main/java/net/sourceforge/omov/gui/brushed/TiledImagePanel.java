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

import javax.swing.JPanel;
import javax.swing.plaf.PanelUI;

/**
 * 
 * @author http://blog.elevenworks.com/?p=10
 */
public class TiledImagePanel extends JPanel {
    
    private static final long serialVersionUID = -3588314729894479207L;

    private Image image;

    public TiledImagePanel() {
        initializeUI();
    }

    protected void initializeUI() {
        super.setUI(TiledImagePanelUI.createUI(this));
    }

    public TiledImagePanel(Image aImage) {
        this();
        setImage(aImage);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image aImage) {
        Image oldImage = image;
        image = aImage;
        firePropertyChange("image", oldImage, image);
        repaint();
    }

    public void setUI(PanelUI ui) {
        if (ui instanceof TiledImagePanelUI) {
            super.setUI(ui);
        }
    }

}
