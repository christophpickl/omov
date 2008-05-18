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
public abstract class RatingMatch extends AbstractMatch<Integer> {

    private static final Log LOG = LogFactory.getLog(RatingMatch.class);

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
    
    public static RatingMatch newEquals(Integer value) {
        return new RatingMatch(LABEL_EQUALS, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0));
            }
        };
    }
    public static RatingMatch newNotEquals(Integer value) {
        return new RatingMatch(LABEL_NOT_EQUALS, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).not();
            }
        };
    }
    public static RatingMatch newGreater(Integer value) {
        return new RatingMatch(LABEL_GREATER, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).greater();
            }
        };
    }
    public static RatingMatch newLess(Integer value) {
        return new RatingMatch(LABEL_LESS, new Integer[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing less for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).smaller();
            }
        };
    }
    public static RatingMatch newInRange(Integer lowerBound, Integer upperBound) {
        return new RatingMatch(LABEL_IN_THE_RANGE, new Integer[] { lowerBound, upperBound } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing in range of '"+this.getValueAt(0)+"' to '"+this.getValueAt(1)+"'.");
                return query.constrain(this.getValueAt(1)).smaller().equal().and(
                       query.constrain(this.getValueAt(0)).greater().equal());
            }
        };
    }
    
    private RatingMatch(String label, Integer[] values) {
        super(label, values);
    }

}
