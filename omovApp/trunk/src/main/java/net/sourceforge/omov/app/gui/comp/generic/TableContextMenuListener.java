package net.sourceforge.omov.app.gui.comp.generic;

import javax.swing.JMenuItem;

public interface TableContextMenuListener {
    
    void contextMenuClicked(JMenuItem item, int tableRowSelected);
    
    void contextMenuClickedMultiple(JMenuItem item, int[] tableRowsSelected);
    
}
