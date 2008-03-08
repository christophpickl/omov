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
    
    private abstract static class MovieColumn {
        private final MovieField field;
        public MovieColumn(MovieField field) {
            this.field = field;
        }
        public String getLabel() {
            return this.field.label();
        }
        public Class getColumnClass() {
            return this.field.getFieldClass();
        }
        public abstract Object getValue(Movie movie);
    }

//    private static final Map<String, MovieColumn> COLUMN_NAMES;
    private static final List<MovieColumn> COLUMNS;
    static {
        final List<MovieColumn> columns = new ArrayList<MovieColumn>();
//        final Map<String, MovieColumn> columnNames = new HashMap<String, MovieColumn>();

        columns.add(new MovieColumn(MovieField.TITLE) {
            public Object getValue(Movie movie) { return movie.getTitle(); }
        });
        columns.add(new MovieColumn(MovieField.RATING) {
            public Object getValue(Movie movie) { return movie.getRating(); }
        });
        columns.add(new MovieColumn(MovieField.QUALITY) {
            public Object getValue(Movie movie) { return movie.getQuality(); }
        });
        
//        for (MovieColumn column : columns) {
//            columnNames.put(column.getLabel(), column);
//        }
        
        COLUMNS = Collections.unmodifiableList(columns);
//        COLUMN_NAMES = Collections.unmodifiableMap(columnNames);
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

    private static List<Movie> naiveSearch(List<Movie> source, String search) { // TODO somehow be more performant when searching
        List<Movie> result = new LinkedList<Movie>();
        for (Movie movie : source) {
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
        return false; // TODO make editable
    }
    
}
