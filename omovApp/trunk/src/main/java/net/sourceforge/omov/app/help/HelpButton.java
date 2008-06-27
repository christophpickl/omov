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

package net.sourceforge.omov.app.help;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.help.HelpBroker;
import javax.help.HelpSet;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import net.sourceforge.omov.app.util.AppImageFactory;
import at.ac.tuwien.e0525580.jlib.tools.UserSniffer;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class HelpButton extends JButton implements MouseListener {
    
    private static final long serialVersionUID = -2059655885830373484L;
    
    private static ImageIcon image;

    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    
    
    public HelpButton(HelpBroker helpBroker, HelpSet helpSet, HelpEntry entry, String tooltipText) {
    	
    	if(UserSniffer.isMacOSX()) {
    		this.putClientProperty("JButton.buttonType", "help");
    	} else {
	        if(image == null) {
	            image = AppImageFactory.getInstance().getHelp();
	        }
	        this.setPreferredSize(new Dimension(image.getIconWidth(), image.getIconHeight()));
	        this.setIcon(image);
	        this.setBorderPainted(false);
    	}
    	

        this.setOpaque(false);
        
        if(tooltipText != null) {
            this.setToolTipText(tooltipText);
        }
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
