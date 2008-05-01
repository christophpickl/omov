package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;

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
    
    private JXTable table;
    public void setTable(JXTable table) {
        this.table = table;
    }

    private void reloadData() {

        final long[] selectedMovieIds;
        if(this.table != null) {
            final int rowCount = this.table.getSelectedRowCount();
            if(rowCount == 0) {
                selectedMovieIds = new long[] {};
            } else if(rowCount == 1) {
                selectedMovieIds = new long[] { getMovieAt(table.convertRowIndexToModel(table.getSelectedRow())).getId() };
            } else {
                int[] selectedRows = this.table.getSelectedRows();
                selectedMovieIds = new long[selectedRows.length];
                for (int i = 0; i < selectedRows.length; i++) {
                    selectedMovieIds[i] = getMovieAt(table.convertRowIndexToModel(selectedRows[i])).getId();
                }
            }
//            System.out.println("reloading data (before firing event; selected movie ids: " + Arrays.toString(selectedMovieIds) + ")");
        } else {
            selectedMovieIds = null;
        }
        
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

        if(this.table != null) {
            this.setSelectedRowsByMovieId(false, selectedMovieIds);
        }
    }
    
    public void setSelectedRowsByMovieId(boolean scrollingEnabled, long... selectedMovieIds) {
        LOG.debug("Setting selected rows by movie IDs: " + Arrays.toString(selectedMovieIds));
        this.table.clearSelection();
        
        for (long movieId : selectedMovieIds) {
            for (int row=0; row < this.getRowCount(); row++) {
                final long rowMovieId = getMovieAt(table.convertRowIndexToModel(row)).getId();
                if(movieId == rowMovieId) {
                    LOG.debug("Preselecting movie with id " + movieId + " at row "+row+".");
                    this.table.addRowSelectionInterval(row, row);
                    break;
                }
            }
        }
        
        if(scrollingEnabled && selectedMovieIds.length == 1) { // scroll to selected movie if its only one
            final long id = selectedMovieIds[0];

            final int col = 0;
            
            int tableRow = -1;
            for (int modelRow = 0; modelRow < this.data.size(); modelRow++) {
                if(this.data.get(modelRow).getId() == id) {
                    tableRow = this.table.convertRowIndexToView(modelRow);
                    LOG.debug("Preselecting table row " + tableRow + " (model row="+modelRow+")");
                    break;
                }
            }
            if(tableRow == -1) {
                throw new FatalException("Could not preselect movie in main window table with id "+id+"!");
            }
            
            final Rectangle rectangle = this.table.getCellRect(tableRow, col, true);
            final Point movieRowPoint = new Point(rectangle.x,rectangle.y);
            final JViewport viewport = ((JViewport) this.table.getParent());
            
            final double visibleYPosition = viewport.getViewPosition().getY();
            final double desiredYPosition = movieRowPoint.getY();
            LOG.debug("visibleYPosition = "+visibleYPosition+"; desiredYPosition = " + desiredYPosition);
            
            // FIXME GUI - scrolling to selected row does not work properly
            if(Math.abs(visibleYPosition - desiredYPosition) > 250.) { // only scroll if distance is big enough
                LOG.debug("Setting view position of table to: " + movieRowPoint);
                viewport.setViewPosition(movieRowPoint);
            }
        }
    }

    /**
     * compares only following to attributes with given search string: title and genres
     */
    private static List<Movie> naiveSearch(List<Movie> source, String search) {
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
            LOG.debug("Not any row selected (modelRowIndex == -1).");
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
        return false; // MANTIS [6] gui: make cells editable
    }
    
}
