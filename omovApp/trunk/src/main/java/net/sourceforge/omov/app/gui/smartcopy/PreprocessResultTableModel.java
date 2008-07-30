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

package net.sourceforge.omov.app.gui.smartcopy;

import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.common.Severity;
import net.sourceforge.omov.core.tools.smartcopy.SmartCopyPreprocessResult;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class PreprocessResultTableModel extends DefaultTableModel {

    private static final long serialVersionUID = 4186970967102961991L;
    
    private static final String[] COLUMNS = new String[] { "" , "Message" };
    
    private List<RowData> rowDataList = new LinkedList<RowData>();
    
    
    public PreprocessResultTableModel(SmartCopyPreprocessResult result) {

        for (String msg : result.getFatalErrors()) {
            this.rowDataList.add(new RowData(Severity.ERROR, msg));
        }
        for (String msg : result.getMajorErrors()) {
            this.rowDataList.add(new RowData(Severity.WARNING, msg));
        }
        for (String msg : result.getMinorErrors()) {
            this.rowDataList.add(new RowData(Severity.INFO, msg));
        }
        
        this.fireTableDataChanged();
    }
    
    @Override
	public int getRowCount() {
        if(this.rowDataList == null) return 0;
        return this.rowDataList.size();
    }
    
    @Override
	public Object getValueAt(int row, int col) {
        final RowData rowData = this.rowDataList.get(row);
        if(col == 0) {
            return AppImageFactory.getInstance().getSeverityIcon(rowData.getSeverity());
        }
        return rowData.getMessage();
    }
    
    @Override
	public Class<?> getColumnClass(final int col) {
        if(col == 0) {
            return ImageIcon.class;
        }
        return String.class;
    }
    
    @Override
	public int getColumnCount() {
        return COLUMNS.length;
    }
    
    @Override
	public String getColumnName(int col) {
        return COLUMNS[col];
    }
    
    @Override
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