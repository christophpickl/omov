package at.ac.tuwien.e0525580.omov.gui.doubletten;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.tools.doubletten.DuplicatesFinder;

public class DuplicatesTableModel extends AbstractTableModel {

    private static final Log LOG = LogFactory.getLog(DuplicatesTableModel.class);
    private static final long serialVersionUID = -7431454679177606409L;

    private final DuplicatesFinder finder;

    private List<Movie> data = new ArrayList<Movie>();

//    private static final List<String> COLUMN_NAMES;
    private static final List<DuplicatesColumn> COLUMNS;
    static {
        final List<DuplicatesColumn> columns = new ArrayList<DuplicatesColumn>();
        
        columns.add(new DuplicatesColumn(MovieField.ID.label(), Integer.class, 10) { public Object getValue(Movie movie) { return movie.getId(); }});
        columns.add(new DuplicatesColumn(MovieField.TITLE.label(), String.class, 100) { public Object getValue(Movie movie) { return movie.getTitle(); }});
        columns.add(new DuplicatesColumn(MovieField.FOLDER_PATH.label(), String.class, 200) { public Object getValue(Movie movie) { return movie.getFolderPath(); }});
        columns.add(new DuplicatesColumn(MovieField.FILE_SIZE_KB.label(), String.class, 40) { public Object getValue(Movie movie) { return movie.getFileSizeFormatted(); }});
        columns.add(new DuplicatesColumn(MovieField.FILES.label(), Integer.class, 10) { public Object getValue(Movie movie) { return movie.getFiles().size(); }});
        
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
        final Set<Movie> foundDuplicates = this.finder.getFoundDuplicates();
        
        this.data = new ArrayList<Movie>();
        for (Movie duplicate : foundDuplicates) {
            this.data.add(duplicate);
        }
        this.fireTableDataChanged();
        LOG.debug("Loaded "+this.data.size()+" duplicate movies.");
    }
    
    public void deleteMovie(Movie movie) {
        LOG.debug("Deleting movie "+movie+".");
        
        this.data.remove(movie);
        this.fireTableDataChanged();
    }
    
    
    public Movie getMovieAtRow(int row) {
        return this.data.get(row);
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
        private final int prefWidth;
        public DuplicatesColumn(String label, Class columnClass, int prefWidth) {
            this.label = label;
            this.columnClass = columnClass;
            this.prefWidth = prefWidth;
        }
        public String getLabel() {
            return this.label;
        }
        public Class getColumnClass() {
            return this.columnClass;
        }
        public int getPrefWidth() {
            return this.prefWidth;
        }
        public abstract Object getValue(Movie movie);
    }
}
