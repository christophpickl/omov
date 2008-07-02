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

import net.sourceforge.jpotpourri.gui.inputfield.NumberField;
import net.sourceforge.jpotpourri.util.Duration;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DurationPanel extends JPanel {
    
    private static final long serialVersionUID = 6476608166509749830L;

    private final NumberField inpMin;
    private final NumberField inpHours;
    
    public DurationPanel(Duration duration) {
        this.setOpaque(false);
        final int columnSize = 2;
        this.inpMin = new NumberField(duration.getMinutes(), 0, 999, columnSize);
        this.inpHours = new NumberField(duration.getHours(), 0,  99, columnSize);
        
        this.initComponents();
        
    }
    
    public void setFocusSelection(final boolean focusSelectionEnabled) {
        this.inpMin.setFocusSelectionEnabled(focusSelectionEnabled);
        this.inpHours.setFocusSelectionEnabled(focusSelectionEnabled);
    }

    
    private void initComponents() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);

        c.gridy = 0;

        c.insets = new Insets(0, 0, 0, 3); // top left bottom right
        c.gridx = 0;
        this.add(this.inpHours, c);

        c.insets = new Insets(0, 0, 0, 6); // top left bottom right
        c.gridx++;
        this.add(new JLabel("h"), c);
        

        c.insets = new Insets(0, 0, 0, 3); // top left bottom right
        c.gridx++;
        this.add(this.inpMin, c);

        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridx++;
        this.add(new JLabel("min"), c);
    }
    
    public void setDuration(Duration duration) {
        this.inpMin.setNumber(duration.getMinutes());
        this.inpHours.setNumber(duration.getHours());
    }
    
    public Duration getDuration() {
        return Duration.newByMinHour((int) this.inpMin.getNumber(), (int) this.inpHours.getNumber());
    }
}
