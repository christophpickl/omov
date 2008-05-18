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

package net.sourceforge.omov.core.tools.scan;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ScanTable { // extends AbstractTable {

    private static final long serialVersionUID = -4087819785894075216L;
//
//
//    
//    
//    public static final class ScanTableModel extends AbstractTableModel {
//        private static final long serialVersionUID = 5098874402211604965L;
//        private List<ScannedMovie> data = new ArrayList<ScannedMovie>();
//        
//        private static final String[] COLUMN_NAMES = { "", "Title" , "Filesize" };
//
//        public ScanTableModel() {
//            
//        }
//        
//        public void setScannedMovies(Set<ScannedMovie> movies) {
//            data = new ArrayList<ScannedMovie>(movies);
//            Collections.sort(data, ScannedMovie.COMPARATOR);
//            
//            this.fireTableDataChanged();
//        }
//        
//        public int getColumnCount() {
//            return COLUMN_NAMES.length;
//        }
//
//        public int getRowCount() {
//            return data.size();
//        }
//
//        public Object getValueAt(int row, int col) {
//            ScannedMovie movie = this.data.get(row);
//            switch(col) {
//                case 0: return movie.isSelected();
//                case 1: return movie.getTitle();
//                case 2: return movie.getFileSizeKb();
//                default: throw new RuntimeException("Illegal column index: " + col);
//            }
//        }
//        
//        public Class<?> getColumnClass(int col) {
//            switch(col) {
//                case 0: return Boolean.class;
//                case 1: 
//                case 2: return String.class;
//                default: throw new RuntimeException("Illegal column index: " + col);
//            }
//        }
//    }
    
}
