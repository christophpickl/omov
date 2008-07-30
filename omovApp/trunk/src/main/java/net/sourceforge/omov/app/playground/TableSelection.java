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

package net.sourceforge.omov.app.playground;

import java.awt.event.ActionEvent;
import java.util.Arrays;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.omov.gui.GuiActionListener;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
        b.addActionListener(new GuiActionListener() {
            @Override
			public void action(ActionEvent e) {
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
        
        @Override
		public int getRowCount() {
            if(data == null) return 0;
            return data.length;
        }
        @Override
		public Object getValueAt(int row, int col) {
            return data[row][col];
        }
        @Override
		public int getColumnCount() {
            return cols.length;
        }
        @Override
		public String getColumnName(int col) {
            return cols[col];
        }
        public void deleteAll() {
            this.data = new String[][] {};
            this.fireTableDataChanged();
        }
    }
}
