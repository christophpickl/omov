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

package net.sourceforge.omov.app.gui.main.tablex;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.swing.JViewport;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.ContinuousFilter;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.MovieTableColumns;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.core.model.IMovieDaoListener;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieTableModel extends AbstractTableModel implements IMovieTableModel, IMovieDaoListener {

    private static final long serialVersionUID = 2139356418261910376L;
    private static final Log LOG = LogFactory.getLog(MovieTableModel.class);

    private List<Movie> data = new ArrayList<Movie>();
    


    private SmartFolder smartFolder;
    private ContinuousFilter continuousFilter;
    
    
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
            
            if(this.smartFolder == null && this.continuousFilter == null) {
                LOG.debug("reloading whole data (neither smartfolder nor continuous filter set) ...");
                this.data = new ArrayList<Movie>(movieDao.getMoviesSorted());
                
            } else if(this.smartFolder == null && this.continuousFilter != null) {
                LOG.debug("reloading data by continuous filter '"+this.continuousFilter+"'...");
                this.data = naiveSearch(movieDao.getMoviesSorted(), this.continuousFilter);
                
            } else if(this.smartFolder != null && this.continuousFilter == null) {
                LOG.debug("reloading data by smartfolder...");
                this.data = new ArrayList<Movie>(movieDao.getMoviesBySmartFolder(this.smartFolder));
                
            } else {
                assert(this.smartFolder != null && this.continuousFilter != null);
                LOG.debug("reloading data by smartfolder and continuous filter '"+this.continuousFilter+"'...");
                this.data = naiveSearch(movieDao.getMoviesBySmartFolder(this.smartFolder), this.continuousFilter);
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
    private static List<Movie> naiveSearch(List<Movie> source, ContinuousFilter continuousFilter) {
        List<Movie> result = new ArrayList<Movie>();
        
        for (Movie movie : source) {
        	if(continuousFilter.isMatching(movie)) {
        		result.add(movie);
        	}
            
        }
        return Collections.unmodifiableList(result);
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
    
    public void doSearch(ContinuousFilter filter) {
        this.continuousFilter = filter;
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
