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

package net.sourceforge.omov.app.gui;

import java.awt.Container;
import java.awt.Rectangle;

import javax.swing.JScrollPane;
import javax.swing.ScrollPaneLayout;

import net.sourceforge.jpotpourri.tools.PtOperatingSystem;
import net.sourceforge.jpotpourri.tools.PtUserSniffer;


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
        if(PtUserSniffer.isOperatingSystem(PtOperatingSystem.MAC)) {
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
