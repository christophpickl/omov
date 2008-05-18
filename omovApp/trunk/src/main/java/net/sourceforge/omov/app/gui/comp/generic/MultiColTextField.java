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

package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.Point;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MultiColTextField extends JLabel {

//    private static final Log LOG = LogFactory.getLog(MultiColTextField.class);
    private static final long serialVersionUID = 2860809976529219917L;
    
    private final int visibleTextColumns;
    private static final Point POINT_0x0 = new Point(0, 0);
    private String realText;
    
    public MultiColTextField(int columns) {
        this("", columns);
    }
    
    public MultiColTextField(final String text, int columns) {
//    	this.setColumns(columns);
    	this.visibleTextColumns = columns;
    	this.realText = text;
//        this.setPreferredSize(new Dimension(columns * 2, (int) this.getPreferredSize().getHeight()));
        
    	
//        this.setOpaque(true); this.setBackground(Color.RED);
    	this.setOpaque(false);
        
        this.setHorizontalAlignment(JLabel.LEFT);
        this.setVerticalAlignment(JLabel.TOP);
        
        this.setBorder(BorderFactory.createEmptyBorder());
        
        this.setText(text);
    }
    
    @Override
    public void setText(String text) {
    	this.realText = text;
    	
    	
        final String limitedText;
        if(text.length() > this.visibleTextColumns) {
        	limitedText = text.substring(0, this.visibleTextColumns) + "...";
        } else {
        	limitedText = text;
        }
//        LOG.debug("Setting text to '"+limitedText+"' and (visibleTextColumns="+visibleTextColumns+") tooltip to '"+text+"'.");
        super.setText(limitedText);
        
        if(text.length() == 0) {
        	this.setToolTipText(null);
        } else {
        	this.setToolTipText(text);
        }
    }
    
    public String getFullText() {
    	return this.realText;
    }

    @Override
    public Point getToolTipLocation(MouseEvent e) {
        if (getToolTipText(e) == null) {
            return null;
        }
        return POINT_0x0;
    }
}
