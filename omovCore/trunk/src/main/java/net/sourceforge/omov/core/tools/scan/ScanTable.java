package net.sourceforge.omov.core.tools.scan;

import net.sourceforge.omov.core.gui.comp.generic.AbstractTable;

public class ScanTable extends AbstractTable {

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
