package at.ac.tuwien.e0525580.omov.gui.smartcopy;

import java.util.LinkedList;
import java.util.List;

import javax.swing.table.DefaultTableModel;

import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopyPreprocessResult;

class PreprocessResultTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 4186970967102961991L;
    
    private static final String[] COLUMNS = new String[] { "" , "Message" };
    
    private List<RowData> rowData = new LinkedList<RowData>();
    
    
    public PreprocessResultTableModel(SmartCopyPreprocessResult result) {

        for (String msg : result.getFatalErrors()) {
            this.rowData.add(new RowData("Fatal", msg));
        }
        for (String msg : result.getMajorErrors()) {
            this.rowData.add(new RowData("Major", msg));
        }
        for (String msg : result.getMinorErrors()) {
            this.rowData.add(new RowData("Minor", msg));
        }
        
        this.fireTableDataChanged();
    }
    
    public int getRowCount() {
        if(this.rowData == null) return 0;
        return this.rowData.size();
    }
    
    public Object getValueAt(int row, int col) {
        final RowData rowData = this.rowData.get(row);
        if(col == 0) {
            return rowData.getSeverity();
        }
        return rowData.getMessage();
    }
    
    public int getColumnCount() {
        return COLUMNS.length;
    }
    
    public String getColumnName(int col) {
        return COLUMNS[col];
    }
    
    public boolean isEditable() {
        return false;
    }
    
    
    
    private static class RowData {
        private final String severity;
        private final String message;
        public RowData(String severity, String message) {
            this.severity = severity;
            this.message = message;
        }
        public String getSeverity() {
            return this.severity;
        }
        public String getMessage() {
            return this.message;
        }
    }
}