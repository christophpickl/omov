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

import java.util.Date;

import javax.swing.JLabel;

import net.sourceforge.omov.app.gui.comp.generic.DateField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DateRangeField extends AbstractCriterionField {

    private static final long serialVersionUID = 6543547462167817291L;

    private final DateField dateFrom;
    private final DateField dateTo;
    
    DateRangeField(Date initFromValue, Date initToValue, int size) {
        this.dateFrom = new DateField(initFromValue, size);
        this.dateTo = new DateField(initToValue, size);
        
        this.add(dateFrom);
        this.add(new JLabel("to"));
        this.add(dateTo);
    }

    @Override
    public Object[] getValues() {
        final Date from = this.dateFrom.getDate();
        final Date to = this.dateTo.getDate();
        if(from == null || to == null) return null;
        return new Date[] { from, to };
    }
    
}
