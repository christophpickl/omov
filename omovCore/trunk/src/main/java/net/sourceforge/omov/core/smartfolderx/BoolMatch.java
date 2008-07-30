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

package net.sourceforge.omov.core.smartfolderx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class BoolMatch extends AbstractMatch<Boolean> {

    private static final Log LOG = LogFactory.getLog(BoolMatch.class);

    public static final String LABEL_IS = "is";
    public static final String LABEL_IS_NOT = "is not";
    
    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_IS);
        tmp.add(LABEL_IS_NOT);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static BoolMatch newEquals(Boolean value) {
        return new BoolMatch(LABEL_IS, new Boolean[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                Boolean equals = this.getValueAt(0);
                return query.constrain(equals);
            }
        };
    }
    public static BoolMatch newNotEquals(Boolean value) {
        return new BoolMatch(LABEL_IS_NOT, new Boolean[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                Boolean notEquals = this.getValueAt(0);
                return query.constrain(notEquals).not();
            }
        };
    }
    
    private BoolMatch(String label, Boolean[] values) {
        super(label, values);
    }
}
