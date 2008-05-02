package net.sourceforge.omov.app.gui;

import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import net.sourceforge.omov.core.util.UserSniffer;
import net.sourceforge.omov.core.util.UserSniffer.OS;


/**
 * A scrollpane layout that handles the resize box in the bottom right corner.
 * 
 * @author <a href="mailto:jesse@swank.ca">Jesse Wilson</a>
 * {@link http://www.publicobject.com/publicobject/2005_12_01_index.html}
 */
public class MacCornerScrollPaneLayoutManager extends ScrollPaneLayout {

    private static final long serialVersionUID = 3083570240418461214L;
    
    private static final int CORNER_HEIGHT = 14;
    
    public static void install(JScrollPane scrollPane) {
        if(UserSniffer.isOS(OS.MAC)) {
            scrollPane.setLayout(new MacCornerScrollPaneLayoutManager());
        }
    }
    
    public void layoutContainer(Container container) {
        super.layoutContainer(container);
        if(!hsb.isVisible() && vsb != null) {
            Rectangle bounds = new Rectangle(vsb.getBounds());
            bounds.height = Math.max(0, bounds.height - CORNER_HEIGHT);
            vsb.setBounds(bounds);
        }
    }
}
