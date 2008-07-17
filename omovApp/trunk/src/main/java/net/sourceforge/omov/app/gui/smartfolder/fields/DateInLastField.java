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

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;

import net.sourceforge.jpotpourri.jpotface.inputfield.PtNumberField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DateInLastField extends AbstractCriterionField {

    private static final long serialVersionUID = -3495069799227565507L;
    
    private final PtNumberField numberField;
    private final JComboBox comboRangeType = new JComboBox(new String[] { RangeType.DAYS.name().toLowerCase(),
                                                                          RangeType.MONTHS.name().toLowerCase(),
                                                                          RangeType.WEEKS.name().toLowerCase() } );
    
    DateInLastField(int days, RangeType preselectedRangeType) {
        final int initValue = days / preselectedRangeType.getDayAmount();
        final int maxValue = 999;
        
        this.comboRangeType.setOpaque(false);
        this.comboRangeType.setSelectedItem(preselectedRangeType.name().toLowerCase());
        this.numberField = new PtNumberField(initValue, 0, maxValue, 4);
        

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);

        c.insets = new Insets(4, 4, 0, 0); // top left bottom right
        c.gridy = 0;
        c.gridx = 0;
        this.add(this.numberField, c);
        
        c.insets = new Insets(4, 6, 0, 0); // top left bottom right
        c.gridx = 1;
        this.add(this.comboRangeType, c);
    }

    @Override
    public Object[] getValues() {
        int number = (int) this.numberField.getNumber();
        int value = number * RangeType.getByString((String) this.comboRangeType.getSelectedItem()).getDayAmount();
        return new Integer[] { value };
    }
    
}
