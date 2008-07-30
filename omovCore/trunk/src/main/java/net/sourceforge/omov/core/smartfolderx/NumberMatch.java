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
public abstract class NumberMatch extends AbstractMatch<Long> {

    private static final Log LOG = LogFactory.getLog(NumberMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_NOT_EQUALS = "not equals";
    public static final String LABEL_GREATER = "greater";
    public static final String LABEL_LESS = "less";
    public static final String LABEL_IN_THE_RANGE = "in the range";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_NOT_EQUALS);
        tmp.add(LABEL_GREATER);
        tmp.add(LABEL_LESS);
        tmp.add(LABEL_IN_THE_RANGE);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static NumberMatch newEquals(Long value) {
        return new NumberMatch(LABEL_EQUALS, new Long[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0));
            }
        };
    }
    public static NumberMatch newNotEquals(Long value) {
        return new NumberMatch(LABEL_NOT_EQUALS, new Long[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).not();
            }
        };
    }
    public static NumberMatch newGreater(Long value) {
        return new NumberMatch(LABEL_GREATER, new Long[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).greater();
            }
        };
    }
    public static NumberMatch newLess(Long value) {
        return new NumberMatch(LABEL_LESS, new Long[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing less for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).smaller();
            }
        };
    }
    public static NumberMatch newInRange(Long lowerBound, Long upperBound) {
        return new NumberMatch(LABEL_IN_THE_RANGE, new Long[] { lowerBound, upperBound } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing in range of '"+this.getValueAt(0)+"' to '"+this.getValueAt(1)+"'.");
                final Constraint constraintLowerBound = query.constrain(this.getValueAt(0)).greater().equal();
                return query.constrain(this.getValueAt(1)).smaller().equal().and(constraintLowerBound);
            }
        };
    }
    
    private NumberMatch(String label, Long[] values) {
        super(label, values);
    }
}
