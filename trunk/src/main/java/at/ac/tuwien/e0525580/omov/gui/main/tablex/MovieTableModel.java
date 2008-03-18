package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.model.IMovieDaoListener;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;

public class MovieTableModel extends AbstractTableModel implements IMovieTableModel, IMovieDaoListener {

    private static final long serialVersionUID = 2139356418261910376L;
    private static final Log LOG = LogFactory.getLog(MovieTableModel.class);
    private static final IMovieDao DAO = BeanFactory.getInstance().getMovieDao();

    private List<Movie> data = new ArrayList<Movie>();
    
    abstract static class MovieColumn {
        private final MovieField field;
        private final int prefWidth;
        public MovieColumn(MovieField field, int prefWidth) {
            this.field = field;
            this.prefWidth = prefWidth;
        }
        public String getLabel() {
            return this.field.label();
        }
        public Class getColumnClass() {
            return this.field.getFieldClass();
        }
        public int getPrefWidth() {
            return this.prefWidth;
        }
        public abstract Object getValue(Movie movie);
    }

    private static final List<String> COLUMN_NAMES;
    private static final List<MovieColumn> COLUMNS;
    static {
        final List<MovieColumn> columns = new ArrayList<MovieColumn>();

        // FEATURE add coverFile as tablecolumn (mantis: 7)
        
        columns.add(new MovieColumn(MovieField.TITLE, 100)        { public Object getValue(Movie movie) { return movie.getTitle(); } });
        columns.add(new MovieColumn(MovieField.RATING, 60)        { public Object getValue(Movie movie) { return movie.getRating(); } });
        columns.add(new MovieColumn(MovieField.QUALITY, 60)       { public Object getValue(Movie movie) { return movie.getQuality(); } });
        columns.add(new MovieColumn(MovieField.DURATION, 40)      { public Object getValue(Movie movie) { return movie.getDuration(); }});
        columns.add(new MovieColumn(MovieField.GENRES, 100)       { public Object getValue(Movie movie) { return movie.getGenresString(); }
                                                                    public Class getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieColumn(MovieField.DATE_ADDED, 100)   { public Object getValue(Movie movie) { return movie.getDateAdded(); }});
        columns.add(new MovieColumn(MovieField.FILE_SIZE_KB, 60)  { public Object getValue(Movie movie) { return movie.getFileSizeFormatted(); }
                                                                    public Class getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieColumn(MovieField.FOLDER_PATH, 100)  { public Object getValue(Movie movie) { return movie.getFolderPath(); }});
        columns.add(new MovieColumn(MovieField.FORMAT, 80)        { public Object getValue(Movie movie) { return movie.getFormat(); }});
        columns.add(new MovieColumn(MovieField.RESOLUTION, 40)    { public Object getValue(Movie movie) { return movie.getResolution(); }});
        columns.add(new MovieColumn(MovieField.YEAR, 40)          { public Object getValue(Movie movie) { return movie.getYear(); }});
        columns.add(new MovieColumn(MovieField.STYLE, 80)         { public Object getValue(Movie movie) { return movie.getStyle(); }});
        columns.add(new MovieColumn(MovieField.LANGUAGES, 100)    { public Object getValue(Movie movie) { return movie.getLanguagesString(); }
                                                                    public Class getColumnClass() { return String.class;} }); // override Set.class with String.class
        
        final List<String> columnNames = new ArrayList<String>(columns.size());
        for (MovieColumn column : columns) {
            columnNames.add(column.getLabel());
        }
        
        COLUMNS = Collections.unmodifiableList(columns);
        COLUMN_NAMES = Collections.unmodifiableList(columnNames);
    }

    private SmartFolder smartFolder;
    private String searchTerm;
    
    public MovieTableModel() {
        DAO.registerMovieDaoListener(this);
        this.reloadData();
    }

    private void reloadData() {
        try {
            if(this.smartFolder == null && this.searchTerm == null) {
                LOG.debug("reloading data whole...");
                this.data = new ArrayList<Movie>(DAO.getMoviesSorted());
                
            } else if(this.smartFolder == null && this.searchTerm != null) {
                LOG.debug("reloading data by search string '"+this.searchTerm+"'...");
                this.data = naiveSearch(DAO.getMoviesSorted(), searchTerm);
                
            } else if(this.smartFolder != null && this.searchTerm == null) {
                LOG.debug("reloading data by criteria...");
                this.data = new ArrayList<Movie>(DAO.getMoviesBySmartFolder(this.smartFolder));
                
            } else {
                assert(this.smartFolder != null && this.searchTerm != null);
                LOG.debug("reloading data by criteria and search '"+this.searchTerm+"'...");
                this.data = naiveSearch(DAO.getMoviesBySmartFolder(this.smartFolder), searchTerm);
            }
            
            LOG.debug("reloaded "+this.data.size()+" movies.");
        } catch (BusinessException e) {
            throw new FatalException("Could not reload data for MovieTable", e);
        }
        
//        this.doSort(this.lastSortColumn);
        this.fireTableDataChanged();
    }

    /**
     * compares only following to attributes with given search string: title and genres
     */
    private static List<Movie> naiveSearch(List<Movie> source, String search) { // TODO somehow be more performant when searching
        List<Movie> result = new LinkedList<Movie>();
        for (Movie movie : source) {
            // TODO do not only search within title&genres but also in other (wich?) attributes
            if (movie.getTitle().toLowerCase().contains(search.toLowerCase())
             || movie.getGenresString().toLowerCase().contains(search.toLowerCase())) {
                result.add(movie);
            }
        }
        return result;
    }

    /**
     * IMovieDaoListener method
     */
    public void movieDataChanged() {
        this.reloadData();
    }

    static List<MovieColumn> getColumns() {
        return COLUMNS;
    }

    public static List<String> getColumnNames() {
        return COLUMN_NAMES;
    }
    
    /******************************************************************************************************************/
    /****   IMovieTableModel
    /******************************************************************************************************************/
    
    public void doSearch(String searchTerm) {
        this.searchTerm = searchTerm;
        this.reloadData();
    }
    
    public void setSmartFolder(SmartFolder smartFolder) {
        LOG.info("Setting smartFolder: " + smartFolder);
        this.smartFolder = smartFolder;
        this.reloadData();
    }
    
    public Movie getMovieAt(final int modelRowIndex) {
        if (modelRowIndex == -1) {
            LOG.debug("Not any row selected.");
            return null;
        }
        return this.data.get(modelRowIndex);
    }

    public List<Movie> getMoviesAt(final int[] modelRowIndices) {
        if (modelRowIndices.length == 0) {
            return new ArrayList<Movie>(0);
        }

        final List<Movie> set = new ArrayList<Movie>(modelRowIndices.length);
        for (int row : modelRowIndices) {
            set.add(this.getMovieAt(row));
        }
        return set;
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
        return false; // TODO make cells editable (mantis: 6)
    }
    
}
