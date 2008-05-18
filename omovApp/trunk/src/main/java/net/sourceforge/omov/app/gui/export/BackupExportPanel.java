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

package net.sourceforge.omov.app.gui.export;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.core.bo.Movie;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class BackupExportPanel extends JPanel {

    private static final long serialVersionUID = -8464758891434308258L;

    public BackupExportPanel() {

        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        
        final String infoString = "<html>" +
            "This export format will let you create a <b>backup</b><br>" +
            "of your movies, so they can be imported again,<br>" +
            "either in your or in a friend's application.<br>" +
            "(<i>The exported movie data version is <b>v"+Movie.DATA_VERSION+"</b></i>)" +
            "</html>";
        
        final JLabel infoText = new JLabel(infoString);
        infoText.setFont(new Font("default", Font.PLAIN, 12));
        
        this.add(infoText);
    }
}
