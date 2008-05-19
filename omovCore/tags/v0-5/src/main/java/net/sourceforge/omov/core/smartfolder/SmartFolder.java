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
import java.util.Comparator;
import java.util.List;

import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SmartFolder {

    private static final Log LOG = LogFactory.getLog(SmartFolder.class);

    public static final int DATA_VERSION = 1;
    
    public static final Comparator<SmartFolder> COMPARATOR_NAME = new Comparator<SmartFolder>() {
        public int compare(final SmartFolder o1, final SmartFolder o2) {
            return o1.getName().compareTo(o2.getName());
        }
    };
    
    private final long id;
    private final String name;
    private final boolean matchAll;
    private final List<AbstractColumnCriterion<?>> criteria;

    /**
     * used to get from db4o by prototype
     */
    public SmartFolder(long id) {
        this.id = id;
        this.name = null;
        this.matchAll = false;
        this.criteria = null;
    }
    public SmartFolder(long id, String name, boolean matchAll, List<AbstractColumnCriterion<?>> criteria) {
        this.id = id;
        this.name = name;
        this.matchAll = matchAll;
        this.criteria = new ArrayList<AbstractColumnCriterion<?>>(criteria);
    }
    
    public static SmartFolder byOther(long id, SmartFolder that) {
        return new SmartFolder(id, that.name, that.matchAll, new ArrayList<AbstractColumnCriterion<?>>(that.criteria));
    }
    
    public void pepareQuery(Query query) {
        assert(this.criteria.size() > 0);
        LOG.info("Preparing query with smartfolder '"+this.getName()+"' ("+this.criteria.size()+" criteria).");
        
        query.constrain(Movie.class);
        
        List<Constraint> constraints = new ArrayList<Constraint>(this.criteria.size());
        
        for (AbstractColumnCriterion<?> criterion: this.criteria) {
            constraints.add(criterion.getDb4oConstraint(query));
        }
        
        for (int i = 1; i < constraints.size(); i++) {
            Constraint c1 = constraints.get(i-1);
            Constraint c2 = constraints.get(i);
            if(this.matchAll) c1.and(c2);
            else c1.or(c2);
        }
    }
    
    public List<AbstractColumnCriterion<?>> getCriteria() {
        return Collections.unmodifiableList(this.criteria);
    }
    
    public long getId() {
        return this.id;
    }
    
    public String getName() {
        return this.name;
    }
    
    public boolean isMatchAll() {
        return this.matchAll;
    }
    
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("[");
        sb.append("id=").append(id).append(";");
        sb.append("name=").append(name).append(";");
        sb.append("matchAll=").append(matchAll).append(";");
        sb.append("criteria.length=").append(criteria.size()).append(";");
        boolean first = true;
        for (AbstractColumnCriterion<?> criterion : this.criteria) {
            if(first == true) first = false;
            else sb.append(";");
            sb.append(criterion.toString());
        }
        sb.append("]");
        return sb.toString();
    }
}
