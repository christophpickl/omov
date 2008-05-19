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

package net.sourceforge.omov.app.gui.doubletten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.tools.doubletten.DoublettenSet;
import net.sourceforge.omov.core.tools.doubletten.DuplicatesFinder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DuplicatesTableModel extends AbstractTableModel {

    private static final Log LOG = LogFactory.getLog(DuplicatesTableModel.class);
    private static final long serialVersionUID = -7431454679177606409L;

    private final DuplicatesFinder finder;

    private DoublettenSet data = DoublettenSet.EMPTY_SET;

//    private static final List<String> COLUMN_NAMES;
    private static final List<DuplicatesColumn> COLUMNS;
    static {
        final List<DuplicatesColumn> columns = new ArrayList<DuplicatesColumn>();
        
        columns.add(new DuplicatesColumn(MovieField.ID.label(), Integer.class, 30, 30, 50) { public Object getValue(Movie movie) { return movie.getId(); }});
        columns.add(new DuplicatesColumn(MovieField.TITLE.label(), String.class, 20, 100, -1) { public Object getValue(Movie movie) { return movie.getTitle(); }});
        columns.add(new DuplicatesColumn(MovieField.FOLDER_PATH.label(), String.class, 20, 200, -1) { public Object getValue(Movie movie) { return movie.getFolderPath(); }});
        columns.add(new DuplicatesColumn(MovieField.FILE_SIZE_KB.label(), String.class, 40, 40, 100) { public Object getValue(Movie movie) { return movie.getFileSizeFormatted(); }});
        columns.add(new DuplicatesColumn(MovieField.FILES.label(), Integer.class, 30, 30, 30) { public Object getValue(Movie movie) { return movie.getFiles().size(); }});
        
//        final List<String> columnNames = new ArrayList<String>(columns.size());
//        for (DuplicatesColumn column : columns) {
//            columnNames.add(column.getLabel());
//        }
//      COLUMN_NAMES = Collections.unmodifiableList(columnNames);
        
        COLUMNS = Collections.unmodifiableList(columns);
    }


    static List<DuplicatesColumn> getColumns() {
        return COLUMNS;
    }
    
    
    
    public DuplicatesTableModel(final DuplicatesFinder finder) {
        this.finder = finder;
        
        this.loadData();
    }

    private void loadData() {
        final DoublettenSet foundDuplicates = this.finder.getFoundDuplicates();
        
        this.data = foundDuplicates;
        
        this.fireTableDataChanged();
        LOG.debug("Loaded "+this.data.size()+" duplicate movies.");
    }
    
    public void deleteMovie(int row, Movie movie) {
        LOG.debug("Deleting movie at row "+row+": "+movie+".");
        
        this.data.remove(row, movie);
        this.fireTableDataChanged();
    }
    
    
    public Movie getMovieAtRow(int row) {
        return this.data.get(row);
    }
    
    /**
     * @return table row indices (got from doubletten set) of similar movies
     */
    public int[] selectionChanged(int row) {
        assert(row > -1);
        final Movie selectedMovie = this.data.get(row);
        return this.data.getSimilarMovieIndices(selectedMovie);
        
    }

    /******************************************************************************************************************/
    /****   JTable
    /******************************************************************************************************************/

    public Object getValueAt(int row, int col) {
        final Movie movie = this.data.get(row);
        return COLUMNS.get(col).getValue(movie);
    }
    public String getColumnName(final int col) {
        return COLUMNS.get(col).getLabel();
    }
    public Class<?> getColumnClass(int col) {
        return COLUMNS.get(col).getColumnClass();
    }

    public int getColumnCount() {
        return COLUMNS.size();
    }
    
    public int getRowCount() {
        if (this.data == null) { // necessary, because DefaultTableModel will invoke it before
            return 0;
        }
        return this.data.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
    

    

    abstract static class DuplicatesColumn {
        private final String label;
        private final Class<?> columnClass;
        private final int minWidth;
        private final int prefWidth;
        private final int maxWidth;
        public DuplicatesColumn(String label, Class<?> columnClass, int minWidth, int prefWidth, int maxWidth) {
            this.label = label;
            this.columnClass = columnClass;
            this.minWidth = minWidth;
            this.prefWidth = prefWidth;
            this.maxWidth = maxWidth;
        }
        public String getLabel() {
            return this.label;
        }
        public Class<?> getColumnClass() {
            return this.columnClass;
        }
        public int getMinWidth() {
            return this.minWidth;
        }
        public int getPrefWidth() {
            return this.prefWidth;
        }
        public int getMaxWidth() {
            return this.maxWidth;
        }
        public abstract Object getValue(Movie movie);
    }
}
