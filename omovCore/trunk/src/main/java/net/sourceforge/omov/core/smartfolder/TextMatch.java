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
 * equals, not equals, contains, not contains, starts with, ends with
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class TextMatch extends AbstractMatch<String> {

    private static final Log LOG = LogFactory.getLog(TextMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_NOT_EQUALS = "not equals";
    public static final String LABEL_CONTAINS = "contains";
    public static final String LABEL_NOT_CONTAINS = "not contains";
    public static final String LABEL_STARTS_WITH = "starts with";
    public static final String LABEL_ENDS_WITH = "ends with";
    
    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_NOT_EQUALS);
        tmp.add(LABEL_CONTAINS);
        tmp.add(LABEL_NOT_CONTAINS);
        tmp.add(LABEL_STARTS_WITH);
        tmp.add(LABEL_ENDS_WITH);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static TextMatch newEquals(String value) {
        return new TextMatch(LABEL_EQUALS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                String string = this.getValueAt(0);
//                return query.constrain(string).startsWith(false).and(
//                       query.constrain(string).endsWith(false).and(
//                       query.constrain(string).like())); // FEATURE db4o: how to compare for case-INsensitive equality using db4o?
                return query.constrain(string).equal();
            }
        };
    }
    public static TextMatch newNotEquals(String value) {
        return new TextMatch(LABEL_NOT_EQUALS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).not();
            }
        };
    }
    public static TextMatch newContains(String value) {
        return new TextMatch(LABEL_CONTAINS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing contains for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).like();
            }
        };
    }
    public static TextMatch newNotContains(String value) {
        return new TextMatch(LABEL_NOT_CONTAINS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not contains for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).like().not();
            }
        };
    }
    public static TextMatch newStartsWith(String value) {
        return new TextMatch(LABEL_STARTS_WITH, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing starts with for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).startsWith(false); // false ... case insensitive
            }
        };
    }
    public static TextMatch newEndsWith(String value) {
        return new TextMatch(LABEL_ENDS_WITH, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing ends with for value '"+this.getValueAt(0)+"'.");
                return query.constrain(this.getValueAt(0)).endsWith(false); // false ... case insensitive
            }
        };
    }
    
    private TextMatch(String label, String[] values) {
        super(label, values);
    }

}
