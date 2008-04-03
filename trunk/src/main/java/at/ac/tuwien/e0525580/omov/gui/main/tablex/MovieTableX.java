package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.awt.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JMenuItem;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.bo.CoverFileType;
import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory.Icon16x16;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ITableSelectionListener;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.TableContextMenuListener;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.MovieTableColumns.IMovieTableColumn;
import at.ac.tuwien.e0525580.omov.tools.osx.FinderReveal;
import at.ac.tuwien.e0525580.omov.tools.osx.VlcPlayDelegator;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;


public class MovieTableX extends JXTable implements TableContextMenuListener {

    private static final long serialVersionUID = -6558625595861308741L;
    private static final Log LOG = LogFactory.getLog(MovieTableX.class);

    private static final String CMD_EDIT = "edit";
    private static final String CMD_FETCH_METADATA = "fetchMetadata";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_REVEAL = "reveal"; // OSX only
    private static final String CMD_PLAY_VLC = "playVlc"; // OSX only

    private static final int COVER_COLUMN_GAP = 8;
    static Color COLOR_SELECTED_BG = new Color(61, 128, 223);
    static Color COLOR_SELECTED_FG = Color.WHITE;
    
    private final IMovieTableContextMenuListener contextMenuListener;
    
    private final int defaultRowHeight;
    
    public MovieTableX(final IMovieTableContextMenuListener contextMenuListener, MovieTableModel model) {
        this.contextMenuListener = contextMenuListener;
        this.setModel(model);
        
        // JXTable features START
        this.setColumnControlVisible(true);
        GuiUtil.setAlternatingBgColor(this);
//        this.addHighlighter(new ColorHighlighter(Color.RED, Color.BLUE, HighlightPredicate.ROLLOVER_ROW));
        
//        this.getTableHeader().setColumnModel(columnModel);
        
        this.getColumnByField(MovieField.QUALITY).setComparator(Quality.COMPARATOR);
        TableRenderers.updateRenderers(this);

        assert(model.getColumnCount() == this.getColumnCount());
        defaultRowHeight = this.getRowHeight();
        for(IMovieTableColumn movieColumn : MovieTableColumns.getColumns()) {
            final TableColumnExt column = this.getColumnExt(movieColumn.getLabel());
            column.setPreferredWidth(movieColumn.getPrefWidth());
            
            final Boolean visible = PreferencesDao.getInstance().isMovieColumnVisible(movieColumn.getLabel());
            LOG.debug("Setting column '"+column.getTitle()+"' to visible '"+visible+"'.");
            column.setVisible(visible);
            
            if(movieColumn.getLabel().equals(MovieTableColumns.COVER_COLUMN_LABEL) && visible == true) {
                this.setRowHeight(CoverFileType.THUMBNAIL.getMaxHeight() + COVER_COLUMN_GAP);
                column.setMaxWidth(CoverFileType.THUMBNAIL.getMaxWidth() + COVER_COLUMN_GAP);
                column.setResizable(false);
            }
        }
        
        this.getColumnModel().addColumnModelListener(new TableColumnModelListener() {
            public void columnAdded(TableColumnModelEvent e) {
                updatePrefColumnVisibility();
            }
            public void columnRemoved(TableColumnModelEvent e) {
                updatePrefColumnVisibility();
            }
            public void columnMarginChanged(ChangeEvent e) { /* ignore */ }
            public void columnMoved(TableColumnModelEvent e) { /* ignore */ }
            public void columnSelectionChanged(ListSelectionEvent e) { /* ignore */ }
        });

        this.packAll();
        // JXTable features END
        
        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if(event.getValueIsAdjusting() == true) {
                    return;
                }
                final int selectedRows = MovieTableX.this.getSelectedRowCount();
                if(selectedRows == 0) {
                    MovieTableX.this.notifyChangedEmpty();
                } else if(selectedRows == 1) {
                    MovieTableX.this.notifyChangedSingle(MovieTableX.this.getSelectedRow());
                } else { // selected rows > 1
                    MovieTableX.this.notifyChangedMultiple(MovieTableX.this.getSelectedRows());
                }
            }
        });
        
        this.initContextMenu();
        
        // FEATURE hitting backspace when row(s) selected should delete (first confirm) movie
    }
    
    private void updatePrefColumnVisibility() {
        final Map<String, Boolean> columns = new HashMap<String, Boolean>();
        
        boolean setRowHeightForCover = false;
        for(IMovieTableColumn movieColumn : MovieTableColumns.getColumns()) {
            final TableColumnExt column = this.getColumnExt(movieColumn.getLabel());
            columns.put(movieColumn.getLabel(), column.isVisible());

            if(movieColumn.getLabel().equals(MovieTableColumns.COVER_COLUMN_LABEL) && column.isVisible() == true) {
                setRowHeightForCover = true;
            }
        }
        this.setRowHeight((setRowHeightForCover) ? CoverFileType.THUMBNAIL.getMaxHeight() + COVER_COLUMN_GAP : this.defaultRowHeight);
        
        PreferencesDao.getInstance().setMovieColumnVisibility(columns);
    }
    
    private void initContextMenu() {
        final List<JMenuItem> itemsSingle = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsSingle, "Get Info", CMD_EDIT, ImageFactory.getInstance().getIcon(Icon16x16.INFORMATION));
        BodyContext.newJMenuItem(itemsSingle, "Delete", CMD_DELETE, ImageFactory.getInstance().getIcon(Icon16x16.DELETE));
        BodyContext.newJMenuSeparator(itemsSingle);
        BodyContext.newJMenuItem(itemsSingle, "Fetch Metadata", CMD_FETCH_METADATA, ImageFactory.getInstance().getIcon(Icon16x16.FETCH_METADATA));
        FinderReveal.addRevealJMenuItem(itemsSingle, CMD_REVEAL);
        VlcPlayDelegator.addVlcPlayJMenuItem(itemsSingle, CMD_PLAY_VLC);
        // TODO en-/disable menuitems if e.g.: movie got no moviefiles, disable play in vlc 

        final List<JMenuItem> itemsMultiple = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsMultiple, "Get Infos", CMD_EDIT);
        BodyContext.newJMenuItem(itemsMultiple, "Delete", CMD_DELETE);
        new BodyContext(this, itemsSingle, itemsMultiple, this);
    }
    
    public int getSelectedModelRow() {
        final int tableRow = this.getSelectedRow();
        if(tableRow == -1) return -1;
        return this.convertRowIndexToModel(tableRow);
    }
    
    public int[] convertRowIndicesToModel(final int[] tableRows) {
        final int[] modelRows = new int[tableRows.length];
        for (int i = 0; i < modelRows.length; i++) {
            modelRows[i] = this.convertRowIndexToModel(tableRows[i]);
        }
        return modelRows;
    }

    TableColumnExt getColumnByField(MovieField field) {
        return this.getColumnExt(field.label());
    }
    TableColumnExt getColumnByLabel(String label) {
        return this.getColumnExt(label);
    }
    
    
    

    /**
     * TableContextMenuListener method
     */
    public void contextMenuClicked(JMenuItem item, int tableRowSelected) {
        final String cmd = item.getActionCommand();
        LOG.info("contextMenuClicked(cmd="+cmd+", tableRowSelected="+tableRowSelected+")");
        
        if(cmd.equals(CMD_EDIT)) {
            this.contextMenuListener.doEditMovie(tableRowSelected);
        } else if(cmd.equals(CMD_FETCH_METADATA)) {
            this.contextMenuListener.doFetchMetaData(tableRowSelected);
        } else if(cmd.equals(CMD_DELETE)) {
            this.contextMenuListener.doDeleteMovie(tableRowSelected);
        } else if(cmd.equals(CMD_REVEAL)) {
            this.contextMenuListener.doRevealMovie(tableRowSelected);
        } else if(cmd.equals(CMD_PLAY_VLC)) {
            this.contextMenuListener.doPlayVlc(tableRowSelected);
        } else {
            assert(false) : "Invalid menu item command '"+cmd+"'!";
        }
    }

    /**
     * TableContextMenuListener method
     */
    public void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected) {
        final String cmd = item.getActionCommand();
        if(cmd.equals(CMD_EDIT)) {
            this.contextMenuListener.doEditMovies(tableRowsSelected);
        } else if(cmd.equals(CMD_DELETE)) {
            this.contextMenuListener.doDeleteMovies(tableRowsSelected);
        } else {
            assert(false) : "Invalid menu item command '"+cmd+"'!";
        }
    }
    
    
    
    private final Set<ITableSelectionListener> selectionListeners = new HashSet<ITableSelectionListener>();
    public void addTableSelectionListener(ITableSelectionListener listener) {
        LOG.info("adding selection listener: " + listener);
        this.selectionListeners.add(listener);
    }
    public void removeTableSelectionListener(ITableSelectionListener listener) {
        final boolean removed = this.selectionListeners.remove(listener);
        LOG.info("removed selection listener (removed="+removed+"): " + listener);
    }
    private void notifyChangedEmpty() {
        for (ITableSelectionListener listener : this.selectionListeners) {
            listener.selectionEmptyChanged();
        }
    }
    private void notifyChangedSingle(int row) {
        for (ITableSelectionListener listener : this.selectionListeners) {
            listener.selectionSingleChanged(row);
        }
    }
    private void notifyChangedMultiple(int[] rows) {
        for (ITableSelectionListener listener : this.selectionListeners) {
            listener.selectionMultipleChanged(rows);
        }
    }
}
