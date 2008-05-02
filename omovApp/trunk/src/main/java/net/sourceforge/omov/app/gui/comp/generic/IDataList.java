package net.sourceforge.omov.app.gui.comp.generic;

import java.util.Set;

import javax.swing.JPanel;

public interface IDataList {
    
    void setSelectedItem(String item);
    
    JPanel getPanel();
    
    Set<String> getSelectedItems();
    
    void setVisibleRowCount(int visibleRowCount);
}