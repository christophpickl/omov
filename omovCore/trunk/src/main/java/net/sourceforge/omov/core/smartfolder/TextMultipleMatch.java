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
public abstract class TextMultipleMatch extends AbstractMatch<String> {

    private static final Log LOG = LogFactory.getLog(DurationMatch.class);

    public static final String LABEL_CONTAINS = "contains";
    public static final String LABEL_NOT_CONTAINS = "not contains";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_CONTAINS);
        tmp.add(LABEL_NOT_CONTAINS);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static TextMultipleMatch newContains(String value) {
        return new TextMultipleMatch(LABEL_CONTAINS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final String contains = this.getValueAt(0);
                LOG.debug("Preparing contains for value '"+contains+"'.");

                if(contains.length() == 0) {
                    return query.constrain(contains).equal();
                }
                return query.constrain(contains).like();
                
            }
        };
    }
    
    public static TextMultipleMatch newNotContains(String value) {
        return new TextMultipleMatch(LABEL_NOT_CONTAINS, new String[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final String notContains = this.getValueAt(0);
                LOG.debug("Preparing contains for value '"+notContains+"'.");

                if(notContains.length() == 0) {
                    return query.constrain(notContains).not().equal();
                }
                return query.constrain(notContains).not().like();
                
            }
        };
    }
    
    private TextMultipleMatch(String label, String[] values) {
        super(label, values);
    }

}
