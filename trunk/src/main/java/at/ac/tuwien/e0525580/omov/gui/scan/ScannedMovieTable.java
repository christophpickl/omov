package at.ac.tuwien.e0525580.omov.gui.scan;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableCellRenderer;

import org.jdesktop.swingx.JXTable;

import at.ac.tuwien.e0525580.omov.gui.OmovListCellRenderer;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

class ScannedMovieTable extends JXTable {

    private static final long serialVersionUID = 6546071254953307095L;
    
    public ScannedMovieTable(final ScannedMovieTableModel model) {
        super(model);
        
        final JCheckBox checkbox = new JCheckBox();
        checkbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                
                final int selectedRow = getSelectedRow();
                
                model.changeSelectedRow(convertRowIndexToModel(selectedRow));
                getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
                
        }});   
        final DefaultCellEditor cbEditor = new DefaultCellEditor(checkbox) {
            private static final long serialVersionUID = -2514315517496249577L;
            @Override
            public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
                final Component comp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
                final JCheckBox checkbox = (JCheckBox) comp;
                checkbox.setHorizontalAlignment(JLabel.CENTER);
                checkbox.setOpaque(false);
                OmovListCellRenderer.prepareComponent(checkbox, table, row);
                // FIXME gui: table cell bg color from checkbox editor in scan dialog is incorrect
                return comp;
            }
        };
        this.getColumnModel().getColumn(0).setCellEditor(cbEditor);
        
        
        GuiUtil.setAlternatingBgColor(this);
        
        
        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
    }
    
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        final Component comp = super.prepareRenderer(renderer, row, column);

        final ScannedMovieTableModel model = (ScannedMovieTableModel) this.getModel();
        final ScannedMovie movie = model.getMovieAt(row);
        final Color unselectedHighlightColor;
        // display row in different color if fetched title != movie folder name

        // MANTIS [24] use string comparison (similarity) library for foldername-fetched movie title check
        if(movie.getFolderPath().length() > 0 && movie.getTitle().equalsIgnoreCase(FileUtil.extractLastFolderName(movie.getFolderPath())) == false) {
            // TODO if foldername differs from movietitle, do not only use another row bg color, but also put a message in hint table
            unselectedHighlightColor = Color.ORANGE;
        } else {
            unselectedHighlightColor = null;
        }
        
        OmovListCellRenderer.prepareComponent(comp, this, row, unselectedHighlightColor);
        
        return comp;
    }

}
