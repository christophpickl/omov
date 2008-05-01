package net.sourceforge.omov.core.spielwiese;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

public class TableSelection {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        final JTable t = new JTable();
        final MyModel model = new MyModel();
        t.setModel(model);
        
        t.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if(event.getValueIsAdjusting() == false) {
                    if(t.getSelectedRowCount() == 0) {
                        System.out.println("no row");
                    } else if(t.getSelectedRowCount() == 1) {
                        System.out.println("single row: " + t.getSelectedRow());
                    } else {
                        System.out.println("multiple rows: " + Arrays.toString(t.getSelectedRows()));
                    }
                }
            }
        });
        
        JButton b = new JButton("tut");
        b.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent arg0) {
                model.deleteAll();
            }
        });
        JPanel p =new JPanel();
        p.add(t);
        p.add(b);
        f.getContentPane().add(p);
        
        f.pack();
        f.setVisible(true);
    }
    
    private static class MyModel extends DefaultTableModel {
        private static final long serialVersionUID = 1L;
        
        private String[][] data = new String[][] {
                new String[] { "1", "einz" },
                new String[] { "2", "zwei" },
                new String[] { "3", "matrix" },
                new String[] { "4", "tombo"}
        };
        private final String[] cols = new String[] { "id", "title" };
        
        public int getRowCount() {
            if(data == null) return 0;
            return data.length;
        }
        public Object getValueAt(int row, int col) {
            return data[row][col];
        }
        public int getColumnCount() {
            return cols.length;
        }
        public String getColumnName(int col) {
            return cols[col];
        }
        public void deleteAll() {
            this.data = new String[][] {};
            this.fireTableDataChanged();
        }
    }
}
