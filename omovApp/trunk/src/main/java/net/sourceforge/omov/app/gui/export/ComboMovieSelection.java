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

package net.sourceforge.omov.app.gui.export;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class ComboMovieSelection extends JComboBox {

    private static final long serialVersionUID = -597660834469335997L;

    public ComboMovieSelection() {
        super(new ComboMovieModel());
        this.setSelectedIndex(0);
        this.setOpaque(false);
    }
    
    public MovieSelectionMode getMovieSelectionMode() {
        return ((ComboMovieModel) this.getModel()).getSelectionMode(this.getSelectedIndex());
    }
    
    enum MovieSelectionMode {
        ALL("all"), VISIBLE("visible"), SELECTED("selected");
        
        private final String label;
        private MovieSelectionMode(String label) {
            this.label = label;
        }
        public String getLabel() {
            return this.label;
        }
    }
    
    private static class ComboMovieModel extends DefaultComboBoxModel{
        private static final long serialVersionUID = -3341594987214035821L;
        
        private static final MovieSelectionMode[] DATA = new MovieSelectionMode[] { MovieSelectionMode.ALL, MovieSelectionMode.VISIBLE, MovieSelectionMode.SELECTED};
        
        public Object getElementAt(int row) {
            return DATA[row].getLabel();
        }
        
        public MovieSelectionMode getSelectionMode(int row) {
            return DATA[row];
        }

        public int getSize() {
            return 3;
        }
        
    }
}
