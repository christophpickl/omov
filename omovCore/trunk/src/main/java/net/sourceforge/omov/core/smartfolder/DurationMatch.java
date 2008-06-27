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

import at.ac.tuwien.e0525580.jlib.util.Duration;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class DurationMatch extends AbstractMatch<Duration> {

    private static final Log LOG = LogFactory.getLog(DurationMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_LONGER = "longer";
    public static final String LABEL_SHORTER = "shorter";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_LONGER);
        tmp.add(LABEL_SHORTER);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static DurationMatch newEquals(Duration value) {
        return new DurationMatch(LABEL_EQUALS, new Duration[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Duration equals = this.getValueAt(0);
                LOG.debug("Preparing equals for value '"+equals+"'.");
                
                return query.constrain(equals.getTotalInMinutes()).equal();
            }
        };
    }
    
    public static DurationMatch newLonger(Duration value) {
        return new DurationMatch(LABEL_LONGER, new Duration[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Duration longer = this.getValueAt(0);
                LOG.debug("Preparing longer for value '"+longer+"'.");
                
                return query.constrain(longer.getTotalInMinutes()).greater();
            }
        };
    }
    
    public static DurationMatch newShorter(Duration value) {
        return new DurationMatch(LABEL_SHORTER, new Duration[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Duration shorter = this.getValueAt(0);
                LOG.debug("Preparing shorter for value '"+shorter+"'.");
                
                return query.constrain(shorter.getTotalInMinutes()).smaller();
            }
        };
    }
    
    private DurationMatch(String label, Duration[] values) {
        super(label, values);
    }

}
