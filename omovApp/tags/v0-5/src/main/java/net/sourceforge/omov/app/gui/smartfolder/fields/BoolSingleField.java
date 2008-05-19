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

package net.sourceforge.omov.app.gui.smartfolder.fields;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class BoolSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 3687599339607030534L;
    
    private final JRadioButton btnTrue;
    private final JRadioButton btnFalse;
    
    BoolSingleField(boolean initValue) {
        this.btnTrue = new JRadioButton("true" , initValue == true);
        this.btnFalse = new JRadioButton("false"   , initValue == false);

        this.btnTrue.setOpaque(false);
        this.btnFalse.setOpaque(false);

        final ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(this.btnTrue);
        btnGroup.add(this.btnFalse);

        this.add(this.btnTrue);
        this.add(this.btnFalse);
    }

    @Override
    public Object[] getValues() {
        return new Boolean[] { this.btnTrue.isSelected() };
    }
    
}
