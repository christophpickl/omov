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
    
//    @Override
//    public Component prepareEditor(TableCellEditor editor, int row, int column) {
//        Component comp = super.prepareEditor(editor, row, column);
//        OmovListCellRenderer.prepareComponent(comp, this, row);
//        return comp;
//    }
    
    
    
    @Override
    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        final Component comp = super.prepareRenderer(renderer, row, column);

        final ScannedMovieTableModel model = (ScannedMovieTableModel) this.getModel();
        final ScannedMovie movie = model.getMovieAt(row);
        final Color unselectedHighlightColor;
        // display row in different color if fetched title != movie folder name
        if(movie.getFolderPath().length() > 0 && movie.getTitle().equalsIgnoreCase(FileUtil.extractLastFolderName(movie.getFolderPath())) == false) {
            unselectedHighlightColor = Color.ORANGE;
        } else {
            unselectedHighlightColor = null;
        }
        
        OmovListCellRenderer.prepareComponent(comp, this, row, unselectedHighlightColor);
        
        return comp;
    }

//    private static class MyDefaultRenderer extends DefaultTableCellRenderer {
//        private static final long serialVersionUID = -8927947683805849617L;
//        
//        public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int colIndex) {
//            final Component superComponent = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, colIndex);
//            
//            final ScannedMovieTableModel model = (ScannedMovieTableModel) table.getModel();
//            final ScannedMovie movie = model.getMovieAt(row);
//            
//            // MANTIS [24] use string comparison (similarity) library for foldername-fetched movie title check
//            if(movie.getFolderPath().length() > 0 && movie.getTitle().equalsIgnoreCase(FileUtil.extractLastFolderName(movie.getFolderPath())) == false) {
//                 superComponent.setBackground(Color.ORANGE); // display row in different color if fetched title != movie folder name
//                 // TODO if foldername differs from movietitle, do not only use another row bg color, but also put a message in hint table
//            }
//            
//            return superComponent;
//        }
//    }
//    private static class MyBooleanRenderer extends JCheckBox implements TableCellRenderer {
//
//        private static final long serialVersionUID = -5947305831514840661L;
//
//        MyBooleanRenderer() {
//            setHorizontalAlignment(JLabel.CENTER);
//        }
//
//        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
//            setSelected((value != null && ((Boolean) value).booleanValue()));
//            return this;
//        }
//    }
    
}
