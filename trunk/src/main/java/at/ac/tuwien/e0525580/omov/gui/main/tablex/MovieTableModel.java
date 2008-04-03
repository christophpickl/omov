package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.model.IMovieDaoListener;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;

public class MovieTableModel extends AbstractTableModel implements IMovieTableModel, IMovieDaoListener {

    private static final long serialVersionUID = 2139356418261910376L;
    private static final Log LOG = LogFactory.getLog(MovieTableModel.class);

    private List<Movie> data = new ArrayList<Movie>();
    


    private SmartFolder smartFolder;
    private String searchTerm;
    
    
    public MovieTableModel() {
        BeanFactory.getInstance().getMovieDao().addMovieDaoListener(this);
        this.reloadData();
    }

    private void reloadData() {
        try {
            final IMovieDao movieDao = BeanFactory.getInstance().getMovieDao();
            
            if(this.smartFolder == null && this.searchTerm == null) {
                LOG.debug("reloading data whole...");
                this.data = new ArrayList<Movie>(movieDao.getMoviesSorted());
                
            } else if(this.smartFolder == null && this.searchTerm != null) {
                LOG.debug("reloading data by search string '"+this.searchTerm+"'...");
                this.data = naiveSearch(movieDao.getMoviesSorted(), searchTerm);
                
            } else if(this.smartFolder != null && this.searchTerm == null) {
                LOG.debug("reloading data by criteria...");
                this.data = new ArrayList<Movie>(movieDao.getMoviesBySmartFolder(this.smartFolder));
                
            } else {
                assert(this.smartFolder != null && this.searchTerm != null);
                LOG.debug("reloading data by criteria and search '"+this.searchTerm+"'...");
                this.data = naiveSearch(movieDao.getMoviesBySmartFolder(this.smartFolder), searchTerm);
            }
            
            LOG.debug("reloaded "+this.data.size()+" movies.");
        } catch (BusinessException e) {
            throw new FatalException("Could not reload data for MovieTable", e);
        }
        
        this.fireTableDataChanged();
    }

    /**
     * compares only following to attributes with given search string: title and genres
     */
    private static List<Movie> naiveSearch(List<Movie> source, String search) { // TODO somehow be more performant when searching
        List<Movie> result = new LinkedList<Movie>();
        for (Movie movie : source) {
            // FEATURE do not only search within title&genres but also in other (wich?) attributes
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
        return MovieTableColumns.getColumns().get(col).getValue(movie);
    }
    public String getColumnName(final int col) {
        return MovieTableColumns.getColumns().get(col).getLabel();
    }
    public Class<?> getColumnClass(int col) {
        return MovieTableColumns.getColumns().get(col).getColumnClass();
    }

    public int getColumnCount() {
        return MovieTableColumns.getColumns().size();
    }
    
    public int getRowCount() {
        if (this.data == null) { // necessary, because DefaultTableModel will invoke it before
            return 0;
        }
        return this.data.size();
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false; // FEATURE make cells editable (mantis: 6)
    }
    
}
