package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.awt.Color;
import java.awt.Component;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;
import org.jdesktop.swingx.table.TableColumnExt;

import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext.TableContextMenuListener;


public class MovieTableX extends JXTable implements TableContextMenuListener {

    private static final long serialVersionUID = -6558625595861308741L;
    private static final Log LOG = LogFactory.getLog(MovieTableX.class);

    private static final String CMD_EDIT = "edit";
    private static final String CMD_FETCH_METADATA = "fetchMetadata";
    private static final String CMD_DELETE = "delete";

    private static Color COLOR_EVEN = Color.WHITE;
    private static Color COLOR_ODD = new Color(241, 245, 250);
    
    private final IMovieTableContextMenuListener contextMenuListener;
    
    public MovieTableX(final IMovieTableContextMenuListener contextMenuListener, MovieTableModel model) {
        this.contextMenuListener = contextMenuListener;
        this.setModel(model);
        
        // TODO add render + if row selected => darkblue background + white foreground

        
        // JXTable features START
        this.setColumnControlVisible(true);
        this.setHighlighters(HighlighterFactory.createAlternateStriping(COLOR_EVEN, COLOR_ODD));
//        this.addHighlighter(new ColorHighlighter(Color.RED, Color.BLUE, HighlightPredicate.ROLLOVER_ROW));
        
//        this.getTableHeader().setColumnModel(columnModel);
        
        this.getColumnByField(MovieField.QUALITY).setComparator(Quality.COMPARATOR);
        this.getColumnByField(MovieField.QUALITY).setCellRenderer(new QualityRenderer());
        
//        table.getColumnModel().getColumn(2).setCellRenderer(new DurationRenderer());
//        table.getColumnModel().getColumn(2).setCellEditor(new DurationEditor());
//        table.getColumnExt(MovieField.TITLE.label()).setVisible(false);
        
        // JXTable features END
        
        this.initContextMenu();
    }
    
    private void initContextMenu() {
        final List<JMenuItem> itemsSingle = new ArrayList<JMenuItem>();
        BodyContext.newJMenuItem(itemsSingle, "Get Info", CMD_EDIT);
        BodyContext.newJMenuItem(itemsSingle, "Fetch Metadata", CMD_FETCH_METADATA);
        BodyContext.newJMenuItem(itemsSingle, "Delete", CMD_DELETE);

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
    
    private TableColumnExt getColumnByField(MovieField field) {
        return this.getColumnExt(field.label());
    }
    

    private static class QualityRenderer extends DefaultTableCellRenderer {
        private static final long serialVersionUID = 783309666699748436L;

        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            Quality quality = (Quality) value;
            
            Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            JLabel lbl = (JLabel) c;
            lbl.setText(quality.label());
            return lbl;
        }
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
}
