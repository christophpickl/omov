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
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.swing.JMenuItem;
import javax.swing.ListSelectionModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableColumnModelEvent;
import javax.swing.event.TableColumnModelListener;
import javax.swing.table.TableColumn;

import net.sourceforge.omov.app.gui.comp.generic.BodyContext;
import net.sourceforge.omov.app.gui.comp.generic.ITableSelectionListener;
import net.sourceforge.omov.app.gui.comp.generic.MacLikeTable;
import net.sourceforge.omov.app.gui.comp.generic.TableContextMenuListener;
import net.sourceforge.omov.core.Icon16x16;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.core.MovieTableColumns;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.MovieTableColumns.IMovieTableColumn;
import net.sourceforge.omov.core.bo.CoverFileType;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.tools.vlc.VlcPlayerFactory;
import net.sourceforge.omov.core.util.UserSniffer;
import net.sourceforge.omov.core.util.UserSniffer.OS;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieTableX extends MacLikeTable implements TableContextMenuListener {

    private static final long serialVersionUID = -6558625595861308741L;
    private static final Log LOG = LogFactory.getLog(MovieTableX.class);

    private static final String CMD_EDIT = "edit";
    private static final String CMD_FETCH_METADATA = "fetchMetadata";
    private static final String CMD_DELETE = "delete";
    private static final String CMD_REVEAL = "reveal"; // OSX only
    private static final String CMD_PLAY_VLC = "playVlc"; // OSX only

    private static final int COVER_COLUMN_GAP = 8;

    // private static final Set<MovieField> TOOLTIP_FIELDS;
    private static final Set<String> TOOLTIP_COLUMN_HEADERS;

    private static final Map<String, MovieField> TOOLTIP_FIELDS_MAP;
    static {
        final Set<MovieField> fields = new HashSet<MovieField>();
        fields.add(MovieField.TITLE);
        fields.add(MovieField.FOLDER_PATH);
        fields.add(MovieField.LANGUAGES);
        fields.add(MovieField.STYLE);
        fields.add(MovieField.DIRECTOR);
        fields.add(MovieField.GENRES);
        fields.add(MovieField.ACTORS);
        fields.add(MovieField.FORMAT);
        fields.add(MovieField.FILES);
        fields.add(MovieField.SUBTITLES);
        // TOOLTIP_FIELDS = Collections.unmodifiableSet(fields);

        final Set<String> headers = new TreeSet<String>();
        final Map<String, MovieField> map = new HashMap<String, MovieField>(fields.size());
        for (MovieField field : fields) {
            headers.add(field.label());
            map.put(field.label(), field);
        }
        TOOLTIP_COLUMN_HEADERS = headers;
        TOOLTIP_FIELDS_MAP = map;
    }


    private final Set<ITableSelectionListener> selectionListeners = new HashSet<ITableSelectionListener>();
    private final IMovieTableContextMenuListener contextMenuListener;

    private final int defaultRowHeight;

    private JMenuItem itemPlayVlc = null; // can be null all the time
    private JMenuItem itemRevealFinder = null; // can be null all the time


    public MovieTableX(final IMovieTableContextMenuListener contextMenuListener, MovieTableModel model) {
        super(model);
        this.contextMenuListener = contextMenuListener;

//        this.setRowSelectionAllowed(true);
//        this.setCellSelectionEnabled(false);
        this.setColumnSelectionAllowed(false);
        this.setRowSelectionAllowed(true);

//        this.setIntercellSpacing(new Dimension());
//        this.setShowGrid(false);
//        this.setBackground(Color.WHITE);

        // JXTable features START
        this.setColumnControlVisible(true);
//        GuiUtil.setAlternatingBgColor(this);

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
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        this.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if(event.getValueIsAdjusting() == true) {
                    return;
                }
                final int selectedRows = MovieTableX.this.getSelectedRowCount();
                if(selectedRows == 0) {
                    MovieTableX.this.notifyChangedEmpty();
                } else if(selectedRows == 1) {
                    MovieTableX.this.notifyChangedSingle();
                } else { // selected rows > 1
                    MovieTableX.this.notifyChangedMultiple();
                }
            }
        });

        this.initContextMenu();
    }

    public String getToolTipText(MouseEvent event) {
        final Point point = event.getPoint();
        final int rowIndex = this.rowAtPoint(point);
        int colIndex = this.columnAtPoint(point);
        final TableColumn column = this.getColumnModel().getColumn(colIndex);

        final String tip;
        if (rowIndex != -1 && TOOLTIP_COLUMN_HEADERS.contains(column.getHeaderValue())) {

            int modelRowIndex = this.convertRowIndexToModel(rowIndex);
            final Movie movie = ((MovieTableModel) this.getModel()).getMovieAt(modelRowIndex);
            assert(movie != null);

            final MovieField field = TOOLTIP_FIELDS_MAP.get(column.getHeaderValue());
            if(field == MovieField.LANGUAGES) {
                tip = movie.getLanguagesString();
            } else if(field == MovieField.SUBTITLES) {
                tip = movie.getSubtitlesString();
            } else if(field == MovieField.ACTORS) {
                tip = movie.getActorsString();
            } else if(field == MovieField.FILES) {
                tip = movie.getFilesString();
            } else if(field == MovieField.GENRES) {
                tip = movie.getGenresString();
            } else {
                tip = field.getValue(movie).toString();
            }
        } else {
//            tip = super.getToolTipText(event);
            tip = null;
        }
        if(tip == null || tip.length() == 0) return null;
        return tip;
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
        if(UserSniffer.isOS(OS.MAC) == true) {
        	this.itemRevealFinder = BodyContext.newJMenuItem(itemsSingle, "Reveal in Finder", CMD_REVEAL, ImageFactory.getInstance().getIcon(Icon16x16.REVEAL_FINDER));
        } else {
        	this.itemRevealFinder = null;
        }
        
        if(VlcPlayerFactory.isVlcCapable()) {
        	this.itemPlayVlc = BodyContext.newJMenuItem(itemsSingle, "Play in VLC", CMD_PLAY_VLC, ImageFactory.getInstance().getIcon(Icon16x16.VLC));//MacVlcPlayer.addVlcPlayJMenuItem(itemsSingle, CMD_PLAY_VLC);
        }

        final List<JMenuItem> itemsMultiple = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsMultiple, "Get Infos", CMD_EDIT, ImageFactory.getInstance().getIcon(Icon16x16.INFORMATION));
        BodyContext.newJMenuItem(itemsMultiple, "Delete", CMD_DELETE, ImageFactory.getInstance().getIcon(Icon16x16.DELETE));
        new BodyContext(this, itemsSingle, itemsMultiple, this);
    }

    public int getSelectedModelRow() {
        final int tableRow = this.getSelectedRow();
        if(tableRow == -1) return -1;
        return this.convertRowIndexToModel(tableRow);
    }

    public int[] getSelectedModelRows() {
        final int[] tableRows = this.getSelectedRows();
        assert(tableRows.length > 1);
        final int[] modelRows = new int[tableRows.length];
        for (int i = 0; i < modelRows.length; i++) {
            modelRows[i] = this.convertRowIndexToModel(tableRows[i]);
        }
        return modelRows;
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

    private void notifyChangedSingle() {
        final Movie newSelectedMovie = ((MovieTableModel) this.getModel()).getMovieAt(this.getSelectedModelRow());

        if(this.itemPlayVlc != null) {
            final boolean enabled = newSelectedMovie.isFolderPathSet() && newSelectedMovie.getFiles().size() > 0;
            this.itemPlayVlc.setEnabled(enabled);
        }
        if(this.itemRevealFinder != null) {
            final boolean enabled = newSelectedMovie.isFolderPathSet();
            this.itemRevealFinder.setEnabled(enabled);
        }

        for (ITableSelectionListener listener : this.selectionListeners) {
            listener.selectionSingleChanged(newSelectedMovie);
        }
    }

    private void notifyChangedMultiple() {
        final MovieTableModel model = ((MovieTableModel) this.getModel());
        final int[] rows = this.getSelectedModelRows();
        final List<Movie> newSelectedMovies = new ArrayList<Movie>(rows.length);
        for (int i = 0; i < rows.length; i++) {
            newSelectedMovies.add(model.getMovieAt(rows[i]));
        }

        for (ITableSelectionListener listener : this.selectionListeners) {
            listener.selectionMultipleChanged(newSelectedMovies);
        }
    }
}
