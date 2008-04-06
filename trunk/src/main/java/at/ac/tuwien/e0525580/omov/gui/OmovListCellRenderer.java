package at.ac.tuwien.e0525580.omov.gui;

import java.awt.Component;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JList;

import at.ac.tuwien.e0525580.omov.Constants;

public class OmovListCellRenderer extends DefaultListCellRenderer {
    
    private static final long serialVersionUID = 5300720838872292929L;
    
    
    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        final Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        
        if(isSelected == true) {
            c.setBackground(Constants.getColorSelectedBackground());
            c.setForeground(Constants.getColorSelectedForeground());
        } else if(index % 2 == 1) {
            c.setBackground(Constants.getColorRowBackgroundOdd());
        } else if(index % 2 == 0) {
            c.setBackground(Constants.getColorRowBackgroundEven());
        }
        return c;
    }
}