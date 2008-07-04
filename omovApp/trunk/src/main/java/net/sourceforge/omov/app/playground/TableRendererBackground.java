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

import java.awt.Color;

import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
    
    static class BooleanEditor extends DefaultCellEditor {
        private static final long serialVersionUID = 1L;

        public BooleanEditor(JCheckBox checkBox) {
            super(checkBox);
        }        
    }
    
}
