package net.sourceforge.omov.app.spielwiese;

import java.awt.Color;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;

public class TableRendererBackground {
    
    public static void main(String[] args) {
        
        final JTable table = new JTable(new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            public int getRowCount() {
                return 2;
            }
            public String getColumnName(int col) {
                return col == 0 ? "Text" : "Bool";
            }
            public Class<?> getColumnClass(int col) {
                return col == 0 ? String.class : Boolean.class;
            }
            public int getColumnCount() {
                return 2;
            }
            public Object getValueAt(int row, int col) {
                if(col == 0) {
                    return "row " + row;
                }
                return true;
            }
        });
        
        final JCheckBox checkBox = new JCheckBox();
        checkBox.setHorizontalAlignment(SwingConstants.CENTER);
        checkBox.setBackground(Color.RED);
        table.getColumnModel().getColumn(1).setCellEditor(new BooleanEditor(checkBox));
        
        
        final JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(table));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
    static class BooleanEditor extends DefaultCellEditor implements TableCellEditor {
        private static final long serialVersionUID = 1L;

        public BooleanEditor(JCheckBox checkBox) {
            super(checkBox);
        }        
    }
    
}
