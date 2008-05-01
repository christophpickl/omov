package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import javax.swing.JMenuItem;

public interface TableContextMenuListener {
    
    void contextMenuClicked(JMenuItem item, int tableRowSelected);
    
    void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected);
    
}
