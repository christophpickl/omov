package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.util.Set;

import javax.swing.JPanel;

public interface IDataList {
    void setSelectedItem(String item);
    
    JPanel getPanel();
    
    Set<String> getSelectedItems();
}
