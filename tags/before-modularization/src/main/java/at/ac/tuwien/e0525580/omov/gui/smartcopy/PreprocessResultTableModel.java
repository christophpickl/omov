package at.ac.tuwien.e0525580.omov.gui.smartcopy;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import at.ac.tuwien.e0525580.omov.common.Severity;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopyPreprocessResult;

class PreprocessResultTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 4186970967102961991L;
    
    private static final String[] COLUMNS = new String[] { "" , "Message" };
    
    private List<RowData> rowData = new LinkedList<RowData>();
    
    
    public PreprocessResultTableModel(SmartCopyPreprocessResult result) {

        for (String msg : result.getFatalErrors()) {
            this.rowData.add(new RowData(Severity.ERROR, msg));
        }
        for (String msg : result.getMajorErrors()) {
            this.rowData.add(new RowData(Severity.WARNING, msg));
        }
        for (String msg : result.getMinorErrors()) {
            this.rowData.add(new RowData(Severity.INFO, msg));
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
            return ImageFactory.getInstance().getSeverityIcon(rowData.getSeverity());
        }
        return rowData.getMessage();
    }
    
    public Class<?> getColumnClass(final int col) {
        if(col == 0) {
            return ImageIcon.class;
        }
        return String.class;
    }
    
    public int getColumnCount() {
        return COLUMNS.length;
    }
    
    public String getColumnName(int col) {
        return COLUMNS[col];
    }
    
    @SuppressWarnings("unused")
    public boolean isCellEditable(int col, int row) {
        return false;
    }
    
    
    
    private static class RowData {
        private final Severity severity;
        private final String message;
        public RowData(Severity severity, String message) {
            this.severity = severity;
            this.message = message;
        }
        public Severity getSeverity() {
            return this.severity;
        }
        public String getMessage() {
            return this.message;
        }
    }
}