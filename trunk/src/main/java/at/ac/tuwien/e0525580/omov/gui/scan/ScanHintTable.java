package at.ac.tuwien.e0525580.omov.gui.scan;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.TableColumn;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.tools.scan.ScanHint;



class ScanHintTable extends JTable {
    
    private static final long serialVersionUID = -2976103722587870541L;
    
    public ScanHintTable(ScanHintTableModel model) {
        super(model);
        
//        model.adjustColumns(this.getColumnModel());
    }
    
}

class ScanHintTableModel extends AbstractTableModel {

    private static final Log LOG = LogFactory.getLog(ScanHintTableModel.class);
    private static final long serialVersionUID = -4119173899904754489L;

    private static final Col[] COLUMNS = new Col[] { Col.SEVERITY, Col.MESSAGE };
    
    private static final List<String> ALL_COLUMN_NAMES;
    static {
        List<String> list = new ArrayList<String>(COLUMNS.length);
        for (int i = 0; i < COLUMNS.length; i++) {
            list.add(COLUMNS[i].label());
        }
        ALL_COLUMN_NAMES = Collections.unmodifiableList(list);
    }
    
    private List<ScanHint> scanHints = new ArrayList<ScanHint>(0);
    
//    void adjustColumns(TableColumnModel model) {
//        for (int i = 0; i < model.getColumnCount(); i++) {
//            COLUMNS[i].setWidths(model.getColumn(i));
//        }
//    }
    
    public void setData(List<ScanHint> scanHints) {
        LOG.info("setting data to scan hints.size="+scanHints.size());
        this.scanHints = scanHints;
        this.fireTableDataChanged();
    }
    public int getColumnCount() {
        return ALL_COLUMN_NAMES.size();
    }

    public int getRowCount() {
        return this.scanHints.size();
    }

    public Object getValueAt(int row, int col) {
        final ScanHint hint = this.scanHints.get(row);
        switch(col) {
            case IND_SEVERITY: return hint.getSeverity().name();
            case IND_MESSAGE: return hint.getHint();
            default: throw new IllegalArgumentException("unhandled column: " + col);
        }
    }

    public String getColumnName(final int col) {
        return ALL_COLUMN_NAMES.get(col);
    }
    

    private static final int IND_SEVERITY = 0;
    private static final int IND_MESSAGE = 1;
    private static enum Col {
        SEVERITY(IND_SEVERITY, "Severity", 0, 70, 70), // TODO show severity icons
        MESSAGE(IND_MESSAGE, "Message", 300, -1, 900);

        
        private final int index;
        private final String label;
        private final int minWidth;
        private final int prefWidth;
        private final int maxWidth;
        private Col(final int index, final String label, final int minWidth, final int prefWidth, final int maxWidth) {
            this.index = index;
            this.label = label;
            this.minWidth = minWidth;
            this.prefWidth = prefWidth;
            this.maxWidth = maxWidth;
        }
        public int index() {
            return this.index;
        }
        public String label() {
            return this.label;
        }
        public void setWidths(TableColumn column) {
            if(this.minWidth >= 0) column.setMinWidth(this.minWidth);
            if(this.prefWidth >= 0) column.setPreferredWidth(this.prefWidth);
            if(this.maxWidth >= 0) column.setMaxWidth(this.maxWidth);
        }
    }
}