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
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.qtjImpl.ISmallFullScreenConstants;
import net.sourceforge.omov.qtjImpl.QtjImageFactory;
import net.sourceforge.omov.qtjImpl.QtjImageFactory.ButtonSmallScreenIcon;
import net.sourceforge.omov.qtjImpl.old.QtjVideoController.IQtjScreenListener;
import net.sourceforge.omov.qtjImpl.old.QtjVideoController.QtjState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.jlib.util.GuiUtil;

//public class QtjSmallScreen extends JWindow implements IQtjVideoPlayer, ,  {

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 * @deprecated replaced by QtjSmallScreenX
 */
public class QtjSmallScreen extends JPanel implements ISmallFullScreenConstants, ActionListener, IQtjScreenListener { // implements IScreenSwitchListener

    private static final Log LOG = LogFactory.getLog(QtjSmallScreen.class);
	private static final long serialVersionUID = -2694906256500411249L;

	private static final String CMD_PLAY_PAUSE = "CMD_PLAY_PAUSE";
	private static final String CMD_BACK = "CMD_BACK";
	private static final String CMD_FULLSCREEN = "CMD_FULLSCREEN";
	private static final String CMD_CLOSE = "CMD_CLOSE";
	
	private static final ImageIcon ICON_CLOSE_MINI = QtjImageFactory.getInstance().getCloseMini();
	private static final ImageIcon ICON_PLAY = QtjImageFactory.getInstance().getButtonSmallScreen(ButtonSmallScreenIcon.PLAY);
	private static final ImageIcon ICON_PAUSE = QtjImageFactory.getInstance().getButtonSmallScreen(ButtonSmallScreenIcon.PAUSE);
	private static final ImageIcon ICON_BACK = QtjImageFactory.getInstance().getButtonSmallScreen(ButtonSmallScreenIcon.BACK);
	private static final ImageIcon ICON_FULLSCREEN = QtjImageFactory.getInstance().getButtonSmallScreen(ButtonSmallScreenIcon.FULLSCREEN);
	
	
	private final QtjVideoController controller;
	
	private final JButton btnFullScreen = new JButton(ICON_FULLSCREEN);
	private final JButton btnPlayPause = new JButton(ICON_PLAY);
	private final JButton btnBack = new JButton(ICON_BACK);
	
	
	
	public QtjSmallScreen(QtjVideoController controller) {
		this.controller = controller;
//		this.controller.addScreenSwitchListener(this);
		
		this.setBackground(Color.BLACK);
		this.add(this.initComponents());
	}
	
	private JPanel initComponents() {
		LOG.debug("Initializing components for smallscreen mode.");
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
		
		panel.add(this.newNorthPanel(), BorderLayout.NORTH);
		panel.add(this.controller.getQtjComponent(), BorderLayout.CENTER);
		panel.add(this.newSouthPanel(), BorderLayout.SOUTH);
		
		return panel;
	}

	private JPanel newNorthPanel() {
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setBackground(Color.BLACK);
//		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // margin-bottom
//		
		final JLabel windowTitle = new JLabel(this.controller.getMovie().getTitle() + " - " + this.controller.getMovieFile().getName());
		windowTitle.setFont(WINDOW_TITLE_FONT);
		windowTitle.setBorder(BorderFactory.createEmptyBorder());
		windowTitle.setForeground(COLOR_GRAY);
		
		final JButton btnClose = new JButton(ICON_CLOSE_MINI);
		btnClose.setActionCommand(CMD_CLOSE);
		btnClose.setBorderPainted(false);
		btnClose.addActionListener(this);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		GuiUtil.enableHandCursor(btnClose);
		
		panel.add(windowTitle, BorderLayout.WEST);
		panel.add(btnClose, BorderLayout.EAST);
		return panel;
	}

	private JPanel newSouthPanel() {
		final JPanel southPanel = new JPanel();
		southPanel.setBackground(Constants.getColorWindowBackground());
		
		this.btnFullScreen.setActionCommand(CMD_FULLSCREEN);
		this.btnPlayPause.setActionCommand(CMD_PLAY_PAUSE);
		this.btnBack.setActionCommand(CMD_BACK);
		
		this.btnFullScreen.setToolTipText("Fullscreen");
		this.btnPlayPause.setToolTipText("Play/Pause");
		this.btnBack.setToolTipText("Back");
		
		this.btnFullScreen.setBorderPainted(false);
		this.btnPlayPause.setBorderPainted(false);
		this.btnBack.setBorderPainted(false);
		
		if(this.controller.getDisplay() == null) {
			this.btnFullScreen.setEnabled(false);
			this.btnPlayPause.setEnabled(false);
			this.btnBack.setEnabled(false);
		} else {
			this.btnFullScreen.addActionListener(this);
			this.btnPlayPause.addActionListener(this);
			this.btnBack.addActionListener(this);
		}

		southPanel.add(this.btnBack);
		southPanel.add(this.btnPlayPause);
		southPanel.add(this.btnFullScreen);
		
		return southPanel;
//		final JPanel panel = new JPanel();
//
//		JButton btn = new JButton("2full");
//		btn.addActionListener(new ActionListener() {
//			public void actionPerformed(ActionEvent e) {
//				controller.doSwitchFullscreen();
//			}
//		});
//		panel.add(btn);
//		
//		return panel;
	}
	
//	public void switchedFullscreen(boolean fullscreen) {
//		LOG.debug("Got switchedFullscreen event; fullscreen="+fullscreen);
//		
//		if(fullscreen == true) {
//		} else {
//			this.setLocation(this.previousLocation);
//			this.setSize(this.previousSize);
//		}
//	}
	

	public void actionPerformed(final ActionEvent e) {
		new GuiAction() {
			@Override
			protected void _action() {
				final String cmd = e.getActionCommand();
				if(cmd.equals(CMD_CLOSE)) {
					controller.doClose();
				} else if(cmd.equals(CMD_FULLSCREEN)) {
					controller.doSwitchFullscreen();
				} else if(cmd.equals(CMD_PLAY_PAUSE)) {
					controller.doPlayPause();
				} else if(cmd.equals(CMD_BACK)) {
					controller.doBack();
				} else {
					throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
				}
			}
		}.doAction();
	}

	public void stateChanged(QtjState state) {
		if(state == QtjState.PLAY) {
			this.btnPlayPause.setIcon(ICON_PAUSE);
		} else if(state == QtjState.PAUSE) {
			this.btnPlayPause.setIcon(ICON_PLAY);
		}
	}

	public void switchedFullscreen(boolean fullscreen) {
		// NOTODO Auto-generated method stub
		
	}
}
