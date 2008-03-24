package at.ac.tuwien.e0525580.omov.help;

import java.awt.Cursor;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import at.ac.tuwien.e0525580.omov.gui.ImageFactory;

public class HelpButton extends JButton implements MouseListener {
    
    private static final long serialVersionUID = -2059655885830373484L;
    
    private static ImageIcon image;

    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    
    
    public HelpButton(HelpBroker helpBroker, HelpSet helpSet, HelpEntry entry, String tooltipText) {
        if(image == null) {
            image = ImageFactory.getInstance().getHelp();
        }
        this.setIcon(image);
        this.setOpaque(false);
        
        if(tooltipText != null) {
            this.setToolTipText(tooltipText);
        }
        this.setBorderPainted(false);
        this.addMouseListener(this);
        
        helpBroker.enableHelpOnButton(this, entry.getId(), helpSet);
    }
    
    public HelpButton(HelpBroker helpBroker, HelpSet helpSet, HelpEntry entry) {
        this(helpBroker, helpSet, entry, null);
    }
    
    public void mouseEntered(MouseEvent event) {
        this.setCursor(HAND_CURSOR);
    }
    public void mouseExited(MouseEvent event) {
        this.setCursor(DEFAULT_CURSOR);
        
    }
    public void mouseClicked(MouseEvent event) {
        // nothing todo
        
    }
    public void mousePressed(MouseEvent event) {
        // nothing todo
        
    }
    public void mouseReleased(MouseEvent event) {
        // nothing todo
    }
    
}
