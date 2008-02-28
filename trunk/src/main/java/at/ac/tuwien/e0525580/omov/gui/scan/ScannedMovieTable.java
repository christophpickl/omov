package at.ac.tuwien.e0525580.omov.gui.scan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumnModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.SelectableMovie;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.table.WidthedTableColumn;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.util.FileUtil;

class ScannedMovieTable extends JTable {

    private static final long serialVersionUID = 6546071254953307095L;
    
    public ScannedMovieTable(final ScannedMovieTableModel model) {
        super(model);
        
//        this.getColumnModel().getColumn(0).setCellRenderer(new CheckBoxRenderer());
        JCheckBox checkbox = new JCheckBox();
        checkbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                model.changeSelectedRow(getSelectedRow());
        }});
        DefaultCellEditor cbEditor = new DefaultCellEditor(checkbox);
        this.getColumnModel().getColumn(0).setCellEditor(cbEditor);
        
        
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        // this.setPreferredScrollableViewportSize(table.getPreferredSize());
//        this.getColumnModel().getColumn(0).setCellEditor(new DefaultCellEditor(new JCheckBox())); // ???
//        model.adjustColumns(this.getColumnModel());
    }
}


//class CheckBoxRenderer extends JCheckBox implements TableCellRenderer {
//
//    private static final long serialVersionUID = -5947305831514840661L;
//
//    CheckBoxRenderer() {
//        setHorizontalAlignment(JLabel.CENTER);
//    }
//
//    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//        System.out.println("getTableCellRendererComponent() isSelected: " + isSelected);
//        if (isSelected) {
//            setForeground(table.getSelectionForeground());
//            // super.setBackground(table.getSelectionBackground());
//            setBackground(table.getSelectionBackground());
//        } else {
//            setForeground(table.getForeground());
//            setBackground(table.getBackground());
//        }
//        setSelected((value != null && ((Boolean) value).booleanValue()));
//        return this;
//    }
//}

class ScannedMovieTableModel extends AbstractTableModel {

    private static abstract class ScannedMovieColumn extends WidthedTableColumn<ScannedMovie> {
        public ScannedMovieColumn(String label, int widthMax, int widthPref, int widthMin) {
            super(label, widthMax, widthPref, widthMin);
        }
    }
    
    private static final List<ScannedMovieColumn> ALL_COLUMNS;
    private static final List<String> ALL_COLUMN_NAMES;
    private static final Map<String, ScannedMovieColumn> ALL_COLUMN_NAMES_MAP;
    static {
        final List<ScannedMovieColumn> columns = new ArrayList<ScannedMovieColumn>();
        
        columns.add(new ScannedMovieColumn("", 70, 70, 70) { // selected JCheckBox
            public Object getValue(ScannedMovie movie) {  return movie.isSelected();  }
            public Class<?> getValueClass() {  return Boolean.class;  }});
        
        columns.add(new ScannedMovieColumn(MovieField.TITLE.label(), 800, 120, 30) {
            public Object getValue(ScannedMovie movie) {  return movie.getTitle();  }
            public Class<?> getValueClass() {  return String.class;  }});
        
        columns.add(new ScannedMovieColumn(MovieField.FOLDER_PATH.label(), 800, 120, 30) {
            public Object getValue(ScannedMovie movie) {  return movie.getFolderPath();  }
            public Class<?> getValueClass() {  return String.class;  }});
        
        columns.add(new ScannedMovieColumn(MovieField.FILES.label(), 800, 120, 30) {
            public Object getValue(ScannedMovie movie) {  return movie.getFilesFormatted();  }
            public Class<?> getValueClass() {  return String.class;  }});
        
        columns.add(new ScannedMovieColumn(MovieField.FORMAT.label(), 800, 60, 60) {
            public Object getValue(ScannedMovie movie) {  return movie.getFormat();  }
            public Class<?> getValueClass() {  return String.class;  }});

        columns.add(new ScannedMovieColumn(MovieField.FILE_SIZE_KB.label(), 60, 60, 60) {
            public Object getValue(ScannedMovie movie) {  return FileUtil.formatFileSizeGb(movie.getFileSizeKb());  }
            public Class<?> getValueClass() {  return String.class;  }});

        // columns relevant to fetched metadata

        columns.add(new ScannedMovieColumn(MovieField.GENRES.label(), 1200, 120, 60) {
            public Object getValue(ScannedMovie movie) {  return movie.getGenresString();  }
            public Class<?> getValueClass() {  return String.class;  }});
        
        
        ALL_COLUMNS = Collections.unmodifiableList(columns);
        
        ALL_COLUMN_NAMES = WidthedTableColumn.getColumnLabels(ALL_COLUMNS);
        ALL_COLUMN_NAMES_MAP = WidthedTableColumn.<ScannedMovieColumn>getColumnLabelsMap(ALL_COLUMNS);
    }


    private TableColumnModel columnModel;
    public void setColumnModel(TableColumnModel columnModel) {
        this.columnModel = columnModel;
        this.prepareColumns();
        this.fireTableDataChanged();
    }
    private void prepareColumns() {
        WidthedTableColumn.prepareColumns(this.columnModel, ALL_COLUMN_NAMES_MAP);
    }
    
    private static final Log LOG = LogFactory.getLog(ScannedMovieTableModel.class);
    private static final long serialVersionUID = 5067582542184884221L;
    

    public Object getValueAt(int row, int col) {
        final ScannedMovie movie = this.movies.get(row);
        return ALL_COLUMNS.get(col).getValue(movie);
    }
    
    public String getColumnName(final int col) {
        return ALL_COLUMNS.get(col).getLabel();
    }
    
    public Class<?> getColumnClass(int col) {
        return ALL_COLUMN_NAMES_MAP.get(ALL_COLUMN_NAMES.get(col)).getValueClass();
    }
    
//    private static final List<String> ALL_COLUMN_NAMES = CollectionUtil.immutableList("", "Title", "Year", "Genre");
//    private static final int IND_SELECTED = 0, IND_TITLE = 1, IND_YEAR = 2, IND_GENRE = 3;


//    public Object getValueAt(int row, int col) {
//        final SelectableMovie movie = this.movies.get(row);
//        switch(col) {
//            case IND_SELECTED: return movie.isSelected();
//            case IND_TITLE: return movie.getTitle();
//            case IND_YEAR: return movie.getYear();
//            case IND_GENRE: return movie.getGenresString();
//            default: throw new IllegalArgumentException("unhandled column: " + col);
//        }
//    }

//    @Override
//    public Class<?> getColumnClass(int column) {
//        switch(column) {
//            case IND_SELECTED: return Boolean.class;
//            case IND_YEAR: return Integer.class;
//            
//            case IND_TITLE:
//            case IND_GENRE: return String.class;
//            default: throw new IllegalArgumentException("unhandled column: " + column);
//        }
//    }
    
    private List<ScannedMovie> movies = new ArrayList<ScannedMovie>(0);

    private int getIndexByMovieFolderPath(String folderPath) { // folder path is unique
        for (int i = 0; i < this.movies.size(); i++) {
            if(this.movies.get(i).getFolderPath().equals(folderPath)) {
                return i;
            }
        }
        throw new IllegalArgumentException("Could not find movie by folderPath '"+folderPath+"'!");
    }
    
    void updateMovieByFolderPath(ScannedMovie movie) {
        this.updateMovieAt(movie, this.getIndexByMovieFolderPath(movie.getFolderPath()));
    }
    
    public void updateMovieAt(ScannedMovie movie, int row) {
        LOG.info("updating movie at row "+row+"; movie: " + movie);
        this.movies.set(row, movie);
        this.fireTableDataChanged();
    }
    
    public ScannedMovie getMovieAt(int row) {
        return this.movies.get(row);
    }

//    void adjustColumns(TableColumnModel model) {
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            COLUMNS[i].setWidths(model.getColumn(i));
//        }
//    }
    public ScannedMovieTableModel() {
//        this.addTableModelListener(this); // TableModelListener
        
    }
    
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return columnIndex == 0; // only first col editable (checkbox)
    }
    
    List<Movie> getSelectedMovies() {
        final List<Movie> result = new ArrayList<Movie>(this.movies.size());
        
        for (SelectableMovie movie : this.movies) {
            if(movie.isSelected()) {
                result.add(movie.toMovie());
            }
        }
        
        return result;
    }

    void changeSelectedRow(int row) {
        assert(row >= 0);
        SelectableMovie movie = this.movies.get(row);
        movie.setSelected(!movie.isSelected());
        this.fireTableDataChanged();
    }
    
    public void setData(List<ScannedMovie> movies) {
        LOG.info("setting data to movies.size="+movies.size());
        
        this.movies = new ArrayList<ScannedMovie>(movies.size());
        for (int i = 0; i < movies.size(); i++) {
//            this.movies.add(SelectableMovie.byMovie(movies.get(i), true));
            this.movies.add(movies.get(i));
        }
        
        this.fireTableDataChanged();
    }
    public int getColumnCount() {
        return ALL_COLUMN_NAMES.size();
    }

    public int getRowCount() {
        return this.movies.size();
    }


}
