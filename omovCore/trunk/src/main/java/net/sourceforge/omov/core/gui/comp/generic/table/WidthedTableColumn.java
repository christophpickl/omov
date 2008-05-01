package net.sourceforge.omov.core.gui.comp.generic.table;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;


public abstract class WidthedTableColumn<T> {
    private final String label;

    private final int widthMax;

    private final int widthPref;

    private final int widthMin;

    public WidthedTableColumn(String label, int widthMax, int widthPref, int widthMin) {
        this.label = label;
        this.widthMax = widthMax;
        this.widthPref = widthPref;
        this.widthMin = widthMin;
    }
    
    public static void prepareColumns(TableColumnModel columnModel, Map<String, ? extends WidthedTableColumn<?>> columnNameMap) {
        for (int i = 0; i < columnModel.getColumnCount(); i++) {
            final TableColumn tableColumn = columnModel.getColumn(i);
            final String header = (String) tableColumn.getHeaderValue();
            final WidthedTableColumn<?> column = columnNameMap.get(header);
            tableColumn.setMaxWidth(column.getWidthMax());
            tableColumn.setPreferredWidth(column.getWidthPref());
            tableColumn.setMinWidth(column.getWidthMin());
        }
    }

    public static List<String> getColumnLabels(Collection<? extends WidthedTableColumn<?>> originalColumns) {
        final List<String> columnNames = new ArrayList<String>(originalColumns.size());
        for (WidthedTableColumn<?> column : originalColumns) {
            columnNames.add(column.getLabel());
        }
        return Collections.unmodifiableList(columnNames);
    }

    public static <V extends WidthedTableColumn<?>> Map<String, V> getColumnLabelsMap(Collection<V> originalColumns) {
        final Map<String, V> columnNamesMap = new HashMap<String, V>(originalColumns.size());
        for (V column : originalColumns) {
            columnNamesMap.put(column.getLabel(), column);
        }
        return Collections.unmodifiableMap(columnNamesMap);
    }
    
    public abstract Object getValue(T value);

    public abstract Class<?> getValueClass();


    
    public int getWidthMax() {
        return this.widthMax;
    }

    public int getWidthPref() {
        return this.widthPref;
    }

    public int getWidthMin() {
        return this.widthMin;
    }

    public String getLabel() {
        return this.label;
    }
}
