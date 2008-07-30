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

import net.sourceforge.omov.core.bo.Movie.MovieField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class AbstractColumnCriterion<M extends AbstractMatch<?>> {

    private static final Log LOG = LogFactory.getLog(AbstractColumnCriterion.class);
    
    /** eg: TextMatch, NumberMatch, ... */
    private final M match;
    
    /** attribute name (for SQL column or object attribute); eg: title, year, dateAdded, ... */
    private final String column;
    
    /** displayed column label */
    private final String columnLabel;
    

    static final List<String> BOOL_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.SEEN.label());
        BOOL_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> NUMBER_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.YEAR.label());
        NUMBER_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> DATE_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.DATE_ADDED.label());
        DATE_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> TEXT_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.TITLE.label());
        tmp.add(MovieField.STYLE.label());
        tmp.add(MovieField.DIRECTOR.label());
        tmp.add(MovieField.FORMAT.label());
        tmp.add(MovieField.COMMENT.label());
        tmp.add(MovieField.FOLDER_PATH.label());
        TEXT_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> TEXT_MULTIPLE_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.SUBTITLES.label());
        tmp.add(MovieField.ACTORS.label());
        tmp.add(MovieField.GENRES.label());
        tmp.add(MovieField.LANGUAGES.label());
        TEXT_MULTIPLE_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> RATING_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.RATING.label());
        RATING_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> QUALITY_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.QUALITY.label());
        QUALITY_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> FILE_SIZE_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.FILE_SIZE_KB.label());
        FILE_SIZE_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> DURATION_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.DURATION.label());
        DURATION_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }

    static final List<String> RESOLUTION_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(MovieField.RESOLUTION.label());
        RESOLUTION_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }
    
    static final List<String> ALL_COLUMN_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.addAll(BOOL_COLUMN_LABELS);
        tmp.addAll(NUMBER_COLUMN_LABELS);
        tmp.addAll(DATE_COLUMN_LABELS);
        tmp.addAll(TEXT_COLUMN_LABELS);
        tmp.addAll(TEXT_MULTIPLE_COLUMN_LABELS);
        tmp.addAll(RATING_COLUMN_LABELS);
        tmp.addAll(QUALITY_COLUMN_LABELS);
        tmp.addAll(FILE_SIZE_COLUMN_LABELS);
        tmp.addAll(DURATION_COLUMN_LABELS);
        tmp.addAll(RESOLUTION_COLUMN_LABELS);
        ALL_COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public AbstractColumnCriterion(M match, MovieField field) {
        this.match = match;
        this.column = field.column();
        this.columnLabel = field.label();
    }
    
    final String getColumn() {
        return this.column;
    }
    
    final M getMatch() {
        return this.match;
    }
    
    public String getColumnLabel() {
        return this.columnLabel;
    }
    
    public String getMatchLabel() {
        return this.match.getLabel();
    }
    
    public Object[] getValues() {
        Object[] values = new Object[this.match.getValueCount()];
        for (int i = 0; i < values.length; i++) {
            values[i] = this.match.getValueAt(i);
        }
        return values;
    }
    
    /**
     * sets the column name and delegates to match
     */
    final Constraint getDb4oConstraint(final Query queryIn) {
        LOG.debug("Preparing query for column '"+this.getColumn()+"'.");
        final Query queryColumned = queryIn.descend(this.getColumn());
        
        final Query queryFinished;
        if(this.getColumn().equals(MovieField.QUALITY.column())) {
        	queryFinished = queryColumned.descend("id"); // compare quality's ID, and not the object itself
        } else {
        	queryFinished = queryColumned;
        }
        return this.getMatch().prepareDb4oQuery(queryFinished);
    }
    
    @Override
	public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName()).append("[");
        sb.append("column=").append(column).append(";");
        sb.append("match=").append(match).append(";");
        sb.append("]");
        return sb.toString();
    }
}
