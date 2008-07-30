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

package net.sourceforge.omov.core.smartfolder;

import java.util.Arrays;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class AbstractMatch<V> {

    /** eg: equals, not equals, lower, greater, in range, ... */
    private final String label;
    
    private final V[] values;
    
    
    AbstractMatch(String label, V[] values) {
        this.label = label;
        this.values = values;
    }
    
    abstract Constraint prepareDb4oQuery(Query query);
    
    final V getValueAt(int index) {
        return this.values[index];
    }
    
    final int getValueCount() {
        return this.values.length;
    }
    
    public final String getLabel() {
        return this.label;
    }

    @Override
	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("[");
        sb.append("label=").append(label).append(";");
        sb.append("values=").append(Arrays.toString(values)).append(";");
        sb.append("]");
        return sb.toString();
    }
}
