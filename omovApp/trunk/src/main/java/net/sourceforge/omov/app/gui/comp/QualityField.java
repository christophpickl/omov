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

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import net.sourceforge.omov.core.bo.Quality;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QualityField extends JComboBox {

    private static final long serialVersionUID = 6284832762280153422L;


    public QualityField(Quality initValue) {
        this.setOpaque(false);
        
        this.setModel(new DefaultComboBoxModel() {
            private static final long serialVersionUID = -6244895415596156603L;
            public Object getElementAt(int row) {
                return Quality.getQualityById(row).label();
            }
            public int getSize() {
                return 5;
            }
        });
        this.setSelectedIndex(initValue.getId());
    }
    
    public void setQuality(Quality quality) {
        this.setSelectedIndex(quality.getId());
    }
    
    public Quality getQuality() {
        return Quality.getQualityById(this.getSelectedIndex());
    }
}
