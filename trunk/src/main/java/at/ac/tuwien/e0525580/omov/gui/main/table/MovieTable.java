package at.ac.tuwien.e0525580.omov.gui.main.table;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext.TableContextMenuListener;
import at.ac.tuwien.e0525580.omov.gui.main.table.MovieTableHeader.HeaderContextMenuListener;

public final class MovieTable extends JTable implements TableContextMenuListener, HeaderContextMenuListener {

    private static final long serialVersionUID = 7800493957993940922L;
    private static final Log LOG = LogFactory.getLog(MovieTable.class);

    private static final String CMD_EDIT = "edit";
    private static final String CMD_FETCH_METADATA = "fetchMetadata";
    private static final String CMD_DELETE = "delete";
    
//    private final MovieTableModel model;
    
    private final MovieTableContextMenuListener contextMenuListener;

    private static final Color COLOR_VERTICAL_LINES = new Color(217, 217, 217);
    
    public MovieTable(MovieTableContextMenuListener contextMenuListener) {
        this.contextMenuListener = contextMenuListener;
        this.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

        this.setShowGrid(true);
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(true);
        this.setGridColor(COLOR_VERTICAL_LINES);
        
        
        List<JMenuItem> itemsSingle = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsSingle, "Get Info", CMD_EDIT);
        BodyContext.newJMenuItem(itemsSingle, "Fetch Metadata", CMD_FETCH_METADATA);
        BodyContext.newJMenuItem(itemsSingle, "Delete", CMD_DELETE);

        List<JMenuItem> itemsMultiple = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsMultiple, "Get Infos", CMD_EDIT);
        BodyContext.newJMenuItem(itemsMultiple, "Delete", CMD_DELETE);
        new BodyContext(this, itemsSingle, itemsMultiple, this);
        
        List<BoolMenuItem> itemsHeader = new ArrayList<BoolMenuItem>();
        int j = 0;
        for (String columnLabel : MovieTableModel.getAllColumnNames()) {
            newBoolMenuItem(itemsHeader, j++, columnLabel, columnLabel, MovieTableModel.initiallyVisibleColumn(columnLabel));
        }

        // Own ColumnModel
        this.setColumnModel(new MovieTableColumnModel());
        // Own TableHeader
        this.setTableHeader(new MovieTableHeader(this, itemsHeader, this));
        this.getTableHeader().setColumnModel(this.getColumnModel());
        
        
        final TableColumnModel colModel = this.getColumnModel();
        for (int i = 0, n = this.getColumnCount(); i < n; i++) {
            final TableColumn column = colModel.getColumn(i);
            column.setMinWidth(0);
        }
    }

//    private RatingRenderer ratingRenderer = new RatingRenderer();
//    
//    public TableCellRenderer getCellRenderer(int row, int column) {
//        final String colName = this.getColumnName(column);
//        System.out.println(colName);
//        if(colName.equals(MovieField.RATING.getLabel())) {
//            System.out.println("rating");
////            ratingRenderer.setBackground(Color.RED);
//            return ratingRenderer;
//        }
//        return super.getCellRenderer(row, column);
//    }
    
    public boolean doHeaderPopupItemClicked(BoolMenuItem menuItem) {
        final String cmd = menuItem.getActionCommand(); // actually, this is the column label
        final boolean isTrue = !menuItem.isTrue(); // "look into the future, this will be final value; wuhahaha!
        LOG.info("doHeaderPopupItemClicked() -> "+ (isTrue?"showing":"hiding") +" column '"+cmd+"' (index="+menuItem.getColumnIndex()+").");

        MovieTableModel model = (MovieTableModel) this.getModel();
        if(isTrue == false && model.getVisibleColumnsCount() == 1) {
            LOG.debug("Ignored hiding last visible column.");
            return false;
        }
        model.setColumnVisible(menuItem.getColumnIndex(), isTrue);
        return true;
    }
    
    public MovieTableHeader getTableHeader() {
        return (MovieTableHeader) this.tableHeader;
    }
    
        /*
         * // get information about all columns. final TableColumnModel
         * columnModel = jTable.getColumnModel();
         *  // setting column widths: columnModel.getColumn( col
         * ).setPreferredWidth( widthInPixels ); columnModel.getColumn( col
         * ).setMinWidth( widthInPixels ); columnModel.getColumn( col
         * ).setMaxWidth( widthInPixels );
         *  // adding a bit of extra space between the columns.
         * columnModel.setColumnMargin( 5 );
         */
    
    
    private static void newBoolMenuItem(List<BoolMenuItem> items, int column, String label, String actionCommand, boolean preselected) {
        BoolMenuItem item = new BoolMenuItem(column, label, preselected);
        item.setActionCommand(actionCommand);
        items.add(item);
    }

    public void contextMenuClicked(JMenuItem item, int tableRowSelected) {
        final String cmd = item.getActionCommand();
        LOG.info("contextMenuClicked(cmd="+cmd+")");
        
        if(cmd.equals(CMD_EDIT)) {
            this.contextMenuListener.doEditMovie(tableRowSelected);
        } else if(cmd.equals(CMD_FETCH_METADATA)) {
            this.contextMenuListener.doFetchMetaData(tableRowSelected);
        } else if(cmd.equals(CMD_DELETE)) {
            this.contextMenuListener.doDeleteMovie(tableRowSelected);
        } else {
            assert(false) : "Invalid menu item command '"+cmd+"'!";
        }
    }

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
    
    
    public static interface MovieTableContextMenuListener {
        void doEditMovie(int tableRowSelected);
        void doEditMovies(int[] tableRowSelected);
        void doDeleteMovie(int tableRowSelected);
        void doDeleteMovies(int[] tableRowSelected);
        
        void doFetchMetaData(int tableRowSelected);
    }
    

//    public Movie getSelectedMovie() {
//        if(this.getSelectedRow() < 0) return null;
//        return this.model.getMovieAt(this.getSelectedRow());
//    }
    
    
    
    static class BoolMenuItem extends JMenuItem {
        private static final long serialVersionUID = 9006887824583829016L;
        private final String label;
        private final String markedLabel;
        private final int columnIndex;
        private boolean isTrue;
        public BoolMenuItem(final int column, final String label, final boolean isTrue) {
            super(getMarkedString(label, isTrue));
            this.label = getMarkedString(label, false);
            this.markedLabel = getMarkedString(label, true);
            this.columnIndex = column;
            this.setTrue(isTrue);
        }
        public int getColumnIndex() {
            return this.columnIndex;
        }
        public boolean isTrue() {
            return this.isTrue;
        }
        public void setTrue(final boolean isTrue) {
            this.isTrue = isTrue;
            this.setText(isTrue ? this.markedLabel : this.label);
            
        }
        private static String getMarkedString(String label, boolean selected) {
            return selected ? "[ " + label + " ]" : label;
        }
    }

}
