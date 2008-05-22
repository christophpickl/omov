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

package net.sourceforge.omov.gui;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.action.OpenBrowserAction;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class HyperlinkLabel extends JLabel {

    private static final Log LOG = LogFactory.getLog(HyperlinkLabel.class);
    private static final long serialVersionUID = -2856402859433242420L;

    public HyperlinkLabel(String url) {
        this(url, url);
    }

    public HyperlinkLabel(final String text, final String _url) {
        super(text);
        
        try {
            final URL url = new URL(_url);
            final Color oldForeground = this.getForeground();
            this.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setForeground(Color.BLUE);
                    setText("<html><u>" + text + "</u></html>");
                }

                public void mouseExited(MouseEvent e) {
                    setForeground(oldForeground);
                    setText(text);
                }

                public void mouseClicked(MouseEvent e) {
                    new OpenBrowserAction(url).actionPerformed(null);
                }
            });
        } catch (MalformedURLException e) {
            LOG.error("Invalid url '"+_url+"'!", e);
        }
        
        
    }
}
