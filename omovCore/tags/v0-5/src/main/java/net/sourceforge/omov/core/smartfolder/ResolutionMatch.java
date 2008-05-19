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

import net.sourceforge.omov.core.bo.Resolution;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class ResolutionMatch extends AbstractMatch<Resolution> {

    private static final Log LOG = LogFactory.getLog(ResolutionMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_GREATER = "greater";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_GREATER);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static ResolutionMatch newEquals(Resolution value) {
        return new ResolutionMatch(LABEL_EQUALS, new Resolution[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                Resolution equals = this.getValueAt(0);
                return query.constrain(equals);
            }
        };
    }

    /**
     * @param value inclusive
     */
    public static ResolutionMatch newGreater(Resolution value) {
        return new ResolutionMatch(LABEL_GREATER, new Resolution[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing greater for value '"+this.getValueAt(0)+"'.");
                Resolution greater = this.getValueAt(0);
                return query.descend("width").constrain(greater.getWidth()).greater().and(
                        query.descend("height").constrain(greater.getHeight()).greater());
            }
        };
    }
    
    // in range?!
    
    private ResolutionMatch(String label, Resolution[] values) {
        super(label, values);
    }
}
