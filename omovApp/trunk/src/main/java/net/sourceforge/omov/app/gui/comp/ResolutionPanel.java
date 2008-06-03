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

package net.sourceforge.omov.app.gui.comp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.gui.inputfields.NumberField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ResolutionPanel extends JPanel {

    private static final long serialVersionUID = 1537997093262769402L;
    
    private final NumberField inpWidth;
    private final NumberField inpHeight;
    
    public ResolutionPanel(Resolution resolution) {
        this.setOpaque(false);
        final int columnSize = 4;
        this.inpWidth  = new NumberField(resolution.getWidth(), 0, 9999, columnSize);
        this.inpHeight = new NumberField(resolution.getHeight(), 0, 9999, columnSize);
        
        this.initComponents();
    }
    
    public void setFocusSelection(final boolean focusSelectionEnabled) {
        this.inpWidth.setFocusSelection(focusSelectionEnabled);
        this.inpHeight.setFocusSelection(focusSelectionEnabled);
    }
    
    private void initComponents() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);

        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridy = 0;

        c.gridx = 0;
        this.add(this.inpWidth, c);
        
        c.gridx = 1;
        this.add(new JLabel(" x "), c);
        
        c.gridx = 2;
        this.add(this.inpHeight, c);
    }
    
    public void setResolution(Resolution resolution) {
        this.inpWidth.setNumber(resolution.getWidth());
        this.inpHeight.setNumber(resolution.getHeight());
    }
    
    public Resolution getResolution() {
        return new Resolution((int) inpWidth.getNumber(), (int) inpHeight.getNumber());
    }
}
