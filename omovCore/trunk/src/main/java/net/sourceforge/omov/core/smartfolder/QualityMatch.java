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

import net.sourceforge.omov.core.bo.Quality;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class QualityMatch extends AbstractMatch<Quality> {

    private static final Log LOG = LogFactory.getLog(DurationMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_BETTER = "better";
    public static final String LABEL_WORSE = "worse";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_BETTER);
        tmp.add(LABEL_WORSE);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static QualityMatch newEquals(Quality value) {
        return new QualityMatch(LABEL_EQUALS, new Quality[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Quality value = this.getValueAt(0);
                LOG.debug("Preparing equals for value '"+value+"'.");
                
                return query.constrain(value.getId()).equal();
            }
        };
    }
    
    public static QualityMatch newBetter(Quality value) {
        return new QualityMatch(LABEL_BETTER, new Quality[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Quality value = this.getValueAt(0);
                LOG.debug("Preparing better for value '"+value+"'.");
                
                return query.constrain(value.getId()).greater();
            }
        };
    }
    
    public static QualityMatch newWorse(Quality value) {
        return new QualityMatch(LABEL_WORSE, new Quality[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final Quality value = this.getValueAt(0);
                LOG.debug("Preparing worse for value '"+value+"'.");
                
                return query.constrain(value.getId()).smaller();
            }
        };
    }
    
    private QualityMatch(String label, Quality[] values) {
        super(label, values);
    }

//    public static void main(String[] args) {
//        ObjectContainer DB = Db4o.openFile("junit_test.db4");
//        final Query query = DB.query();
//        query.constrain(Movie.class);
//        
//        query.descend("quality").descend("id").constrain(Quality.UNRATED.getId()).greater();
//        
//        
//        final ObjectSet<Movie> os = query.execute();
//        final List<Movie> movies = new ObjectSetTransformer<Movie>().transformList(os);
//        System.out.println("movies.size = " + movies.size());
//    }
}
