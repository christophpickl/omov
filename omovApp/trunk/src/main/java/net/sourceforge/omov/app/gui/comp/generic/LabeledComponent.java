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

package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class LabeledComponent extends JPanel {

    private static final long serialVersionUID = -3101237571525060234L;
    

    public LabeledComponent(final Component component, final String labelText) {
        this(component, labelText, BorderLayout.CENTER);
    }
    
    public LabeledComponent(final Component component, final String labelText, final String borderLayoutPosition) {
        super(new BorderLayout());
        
        final JLabel label = new JLabel(labelText);
        label.setFont(new Font("sans", Font.BOLD, 10));
        
        this.setOpaque(false);
        
        this.add(label, BorderLayout.NORTH);
        this.add(component, borderLayoutPosition);
    }
}
