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

package net.sourceforge.omov.qtjImpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.core.ImageFactory.IconQuickView;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.IVideoPlayerListener;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.QtjState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjSmallScreenX implements ActionListener, MouseListener, MouseMotionListener, IVideoPlayerListener {

    private static final Log LOG = LogFactory.getLog(QtjSmallScreenX.class);
    
	private static final Font WINDOW_TITLE_FONT = new Font("sans", Font.BOLD, 10);
	private static final Color COLOR_GRAY = new Color(168, 168, 168);

	private static final String CMD_PLAY_PAUSE = "CMD_PLAY_PAUSE";
	private static final String CMD_BACK = "CMD_BACK";
	private static final String CMD_CLOSE = "CMD_CLOSE";
	private static final String CMD_FULLSCREEN = "CMD_FULLSCREEN";
	
	private static final ImageIcon ICON_BACK = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_BACK);
	private static final ImageIcon ICON_FULLSCREEN = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_FULLSCREEN);
	private static final ImageIcon ICON_CLOSE_MINI = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_CLOSE_MINI);
	private static final ImageIcon ICON_PLAY = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_PLAY);
	private static final ImageIcon ICON_PAUSE = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_PAUSE);

	private final QtjVideoPlayerImplX player;
	
	private final JButton btnFullScreen = new JButtonUnfocusable(ICON_FULLSCREEN);
	private final JButton btnPlayPause = new JButtonUnfocusable(ICON_PLAY);
	private final JButton btnBack = new JButtonUnfocusable(ICON_BACK);

	private final JPanel northPanel;
	private final JPanel southPanel;

	private Point startDrag;
	private Point startLocation;
	
	public QtjSmallScreenX(QtjVideoPlayerImplX player) {
		this.player = player;
		this.northPanel = this.newNorthPanel();
		this.southPanel = this.newSouthPanel();
	}

	public JPanel getNorthPanel() {
		return this.northPanel;
	}
	public JPanel getSouthPanel() {
		return this.southPanel;
	}
	
	private static class JButtonUnfocusable extends JButton {
		private static final long serialVersionUID = 4692041677980312903L;
		public JButtonUnfocusable(Icon icon) {
			super(icon);
		}
		@Override
		public boolean isFocusTraversable() {
			return false;
		}
	}

	private JPanel newNorthPanel() {
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // margin-bottom
		
		final JLabel windowTitle = new JLabel(this.player.getMovie().getTitle() + " - " + this.player.getMovieFile().getName());
		windowTitle.setFont(WINDOW_TITLE_FONT);
		windowTitle.setBorder(BorderFactory.createEmptyBorder());
		windowTitle.setForeground(COLOR_GRAY);
		
		final JButton btnClose = new JButtonUnfocusable(ICON_CLOSE_MINI);
		btnClose.setActionCommand(CMD_CLOSE);
		btnClose.setBorderPainted(false);
		btnClose.addActionListener(this);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		SimpleGuiUtil.enableHandCursor(btnClose);
		
		panel.add(windowTitle, BorderLayout.WEST);
		panel.add(btnClose, BorderLayout.EAST);
		return panel;
	}
	
	private JPanel newSouthPanel() {
		final JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
		panel.setBackground(Constants.getColorWindowBackground());
		
		this.btnFullScreen.setActionCommand(CMD_FULLSCREEN);
		this.btnPlayPause.setActionCommand(CMD_PLAY_PAUSE);
		this.btnBack.setActionCommand(CMD_BACK);
		
		this.btnFullScreen.setToolTipText("Fullscreen");
		this.btnPlayPause.setToolTipText("Play/Pause");
		this.btnBack.setToolTipText("Back");
		
		this.btnFullScreen.setBorderPainted(false);
		this.btnPlayPause.setBorderPainted(false);
		this.btnBack.setBorderPainted(false);
		
		if(this.player.getDisplay() == null) {
			this.btnFullScreen.setEnabled(false);
			this.btnPlayPause.setEnabled(false);
			this.btnBack.setEnabled(false);
		} else {
			this.btnFullScreen.addActionListener(this);
			this.btnPlayPause.addActionListener(this);
			this.btnBack.addActionListener(this);
		}

//		panel.add(this.btnBack); // TODO QTJ GUI - implement controller buttons for smallscreen
		panel.add(this.btnPlayPause);
		panel.add(this.btnFullScreen);
		
		return panel;
	}

	public void actionPerformed(final ActionEvent e) {
		new GuiAction() {
			@Override
			protected void _action() {
				final String cmd = e.getActionCommand();
				if(cmd.equals(CMD_CLOSE)) {
					player.doClose();
				} else if(cmd.equals(CMD_FULLSCREEN)) {
					player.doSwitchFullscreen();
				} else if(cmd.equals(CMD_PLAY_PAUSE)) {
					player.doPlayPause();
				} else if(cmd.equals(CMD_BACK)) {
					player.doBack();
				} else {
					throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
				}
			}
		}.doAction();
	}
	
	


    public void mousePressed(MouseEvent e) {
    	if(this.player.isFullScreenMode() == true) return;
    	
        this.startDrag = this.getScreenLocation(e);
        this.startLocation = this.player.getLocation();
    }

    public void mouseClicked(MouseEvent e) {
    	// nothing to do
    }
    public void mouseReleased(MouseEvent e) {
    	// nothing to do
	}

    public void mouseDragged(MouseEvent e) {
    	if(this.player.isFullScreenMode() == true) return;
    	
    	Point current = this.getScreenLocation(e);
        Point offset = new Point((int)current.getX() - (int)startDrag.getX(),
                                 (int)current.getY() - (int)startDrag.getY());
        Point newLocation = new Point((int)(this.startLocation.getX() + offset.getX()),
                                      (int)(this.startLocation.getY() + offset.getY()));
        this.player.setLocation(newLocation);
     }

    public void mouseMoved(MouseEvent e) {
    	// nothing to do
    }
    
    public void mouseEntered(MouseEvent e) {
    	// nothing to do
    }
    
    public void mouseExited(MouseEvent e) {
    	// nothing to do
    }
    
    private Point getScreenLocation(MouseEvent e) {
        Point cursor = e.getPoint( );
        Point targetLocation = this.player.getLocationOnScreen();
        return new Point((int) (targetLocation.getX() + cursor.getX()),
                         (int) (targetLocation.getY() + cursor.getY()));
    }
    
    
    

	public void switchedFullscreen(boolean fullscreen) {
		// nothing to do ???
	}

	public void stateChanged(QtjState state) {
		LOG.debug("state changed to " + state);
		this.btnPlayPause.setIcon(state == QtjState.PAUSE ? ICON_PLAY: ICON_PAUSE);
	}
}
