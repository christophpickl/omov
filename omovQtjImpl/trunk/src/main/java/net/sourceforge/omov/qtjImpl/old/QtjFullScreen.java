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

package net.sourceforge.omov.qtjImpl.old;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import net.sourceforge.omov.qtjImpl.ISmallFullScreenConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 * @deprecated replaced by QtjFullScreenX
 */
public class QtjFullScreen extends JPanel implements ISmallFullScreenConstants {

    private static final Log LOG = LogFactory.getLog(QtjFullScreen.class);
	private static final long serialVersionUID = -2703895060374341666L;
	
	private final QtjVideoController controller;
	
	
	
	public QtjFullScreen(QtjVideoController controller) {
		this.controller = controller;

		this.add(this.initComponents());
	}
	
	private JPanel initComponents() {
		LOG.debug("Initializing components for fullscreen mode.");
//		JButton btn = new JButton("2small");
//		btn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				controller.doSwitchSmallscreen();
//			}
//		});
		
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setOpaque(true);
		this.setOpaque(true);
		
		JPanel qtjWrapPanel = new JPanel(new BorderLayout(0, 0));
		qtjWrapPanel.add(this.controller.getQtjComponent(), BorderLayout.CENTER);
		
		final Dimension movieRecalcedSize = this.controller.getRecalcedFullMovieDimension(); // NOTODO still got some top margin...
		qtjWrapPanel.setMinimumSize(movieRecalcedSize);
		qtjWrapPanel.setMaximumSize(movieRecalcedSize);
		qtjWrapPanel.setPreferredSize(movieRecalcedSize);
		qtjWrapPanel.setSize(movieRecalcedSize);
		
		panel.add(qtjWrapPanel, BorderLayout.CENTER);
		return panel;
	}
	
	
}
