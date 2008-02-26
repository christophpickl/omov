package at.ac.tuwien.e0525580.omov2.gui.main.table;

import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.table.TableCellRenderer;

public class SelectionTableCellRenderer implements TableCellRenderer {

//    private static Color COLOR_PRESSED = Color.RED;
//    private static Color COLOR_NOT_PRESSED = Color.GREEN;
    
    private int pressedColumn = -1;
    
//    private int sortColumn = 0; // default initial sort column

    public Component getTableCellRendererComponent(final JTable table, final Object value, final boolean isSelected, final boolean hasFocus, final int row, final int column) {
//        System.out.println("SelectionTableCellRenderer: column="+column+"; sortColumn="+sortColumn);
        JLabel b = new JLabel((value == null) ? "" : value.toString());
        
        if (column == this.pressedColumn) {
            b.setBackground(UIManager.getColor("control"));
            b.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("controlHighlight"), UIManager.getColor("controlShadow")));
        } else {
            b.setBackground(UIManager.getColor("controlShadow"));
            b.setBorder(BorderFactory.createEtchedBorder(UIManager.getColor("controlLtHighlight"), UIManager.getColor("controlDkShadow")));
        }
        
        
        // FEATURE movietable header anders darstellen wenn sie sortiert sind
        //      geht nicht ganz, da per column index gemerkt; und wenn header verschieben, dann wird falscher header als sortiert angezeigt
        
//        if(column == this.sortColumn) {
//            b.setForeground(COLOR_PRESSED);
//        } else {
//            b.setForeground(COLOR_NOT_PRESSED);
//        }
        
        return b;
    }
    
//    public void setSortColumn(final int sortColumn) {
////        System.out.println("SelectionTableCellRenderer: setting sortColumn to " + sortColumn);
//        this.sortColumn = sortColumn;
//    }

    public void setPressedColumn(final int pressedColumn) {
//        System.out.println("SelectionTableCellRenderer: setting pressedColumn to " + pressedColumn);
        this.pressedColumn = pressedColumn;
    }

}