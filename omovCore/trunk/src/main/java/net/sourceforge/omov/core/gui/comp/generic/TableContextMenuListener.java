package net.sourceforge.omov.core.gui.comp.generic;

import javax.swing.JMenuItem;

public interface TableContextMenuListener {
    
    void contextMenuClicked(JMenuItem item, int tableRowSelected);
    
    void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected);
    
}
