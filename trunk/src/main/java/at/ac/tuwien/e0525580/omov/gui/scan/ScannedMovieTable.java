package at.ac.tuwien.e0525580.omov.gui.scan;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.table.TableCellEditor;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.MacLikeTable;

class ScannedMovieTable extends MacLikeTable {

    private static final long serialVersionUID = 6546071254953307095L;
    

    
    private static class BooleanEditor extends DefaultCellEditor implements TableCellEditor {
        private static final long serialVersionUID = -6806951966323456398L;
        public BooleanEditor(JCheckBox checkBox) {
            super(checkBox);
        }   
//        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
//            final Component comp = super.getTableCellEditorComponent(table, value, isSelected, row, column);
//            // component, just when it is being pressed/clicked
//            return comp;
//        }
    }
    
    public ScannedMovieTable(final ScannedMovieTableModel model) {
        super(model);

        this.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);
        this.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        
        final JCheckBox checkbox = new JCheckBox();
        checkbox.setBackground(Constants.getColorSelectedBackground());
        checkbox.setHorizontalAlignment(SwingConstants.CENTER);
        checkbox.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                final int selectedRow = getSelectedRow();
                model.changeSelectedRow(convertRowIndexToModel(selectedRow));
                getSelectionModel().setSelectionInterval(selectedRow, selectedRow);
                
        }});
        this.getColumnModel().getColumn(0).setCellEditor(new BooleanEditor(checkbox));
    }
    // FIXME grid is not painted right in scanned movie table!
    

//    @Override
//    public Component prepareEditor(TableCellEditor renderer, int row, int column) {
//        Component c = super.prepareEditor(renderer, row, column);
//        
//        c.setBackground(Color.RED);
//        fixMacOsCellRendererBorder((JComponent) c, true, true, true);
//        boolean focused = hasFocus();
//        boolean selected = isCellSelected(row, column);
//        if (selected) {
//            if (focused == false) {
//                c. setBackground(Constants.getColorSelectedBackground());
//                c.setForeground(Constants.getColorLightGray()); // selected, but got no focus
//            } else {
//                c.setBackground(Constants.getColorSelectedBackground());
//                c.setForeground(Constants.getColorSelectedForeground());
//            }
//        } else {
//            c.setBackground(colorForRow(row));
//            c.setForeground(UIManager.getColor("Table.foreground"));
//        }
//        
//        if (c instanceof JComponent) {
//            JComponent jc = (JComponent) c;
//            // jc.setOpaque(true);
//            
//            if (getCellSelectionEnabled() == false) {
//                fixMacOsCellRendererBorder(jc, selected, focused, isEditing());
//            }
//            
////            initToolTip(jc, row, column);
//        }
//        
//        return c;
//    }
    
    
//    @Override
//    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
//        final Component comp = super.prepareRenderer(renderer, row, column);
//
//        final ScannedMovieTableModel model = (ScannedMovieTableModel) this.getModel();
//        final ScannedMovie movie = model.getMovieAt(row);
//        final Color unselectedHighlightColor;
//        // display row in different color if fetched title != movie folder name
//
//        // MANTIS [24] use string comparison (similarity) library for foldername-fetched movie title check
//        if(movie.getFolderPath().length() > 0 && movie.getTitle().equalsIgnoreCase(FileUtil.extractLastFolderName(movie.getFolderPath())) == false) {
//            // TODO if foldername differs from movietitle, do not only use another row bg color, but also put a message in hint table
//            unselectedHighlightColor = Color.ORANGE;
//        } else {
//            unselectedHighlightColor = null;
//        }
//        
//        OmovListCellRenderer.prepareComponent(comp, this, row, unselectedHighlightColor);
//        
//        return comp;
//    }

}
