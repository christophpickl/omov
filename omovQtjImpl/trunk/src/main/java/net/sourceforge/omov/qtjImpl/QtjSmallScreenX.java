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
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;

import net.sourceforge.jpotpourri.jpotface.button.PtPressableButton;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.jpotpourri.util.PtTimeUtil;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.qtjImpl.QtjImageFactory.ButtonSmallScreenIcon;
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
	private static final String CMD_BACKWARD = "CMD_BACKWARD";
	private static final String CMD_FORWARD  = "CMD_FORWARD";

	private static final ImageIcon ICON_PLAY = QtjImageFactory.getInstance().getButtonSmallScreen(ButtonSmallScreenIcon.PLAY);
	private static final ImageIcon ICON_PLAY_PRESSED = QtjImageFactory.getInstance().getButtonSmallScreenPressed(ButtonSmallScreenIcon.PLAY);
	private static final ImageIcon ICON_PAUSE = QtjImageFactory.getInstance().getButtonSmallScreen(ButtonSmallScreenIcon.PAUSE);
	private static final ImageIcon ICON_PAUSE_PRESSED = QtjImageFactory.getInstance().getButtonSmallScreenPressed(ButtonSmallScreenIcon.PAUSE);


	private final QtjVideoPlayerImplX player;
	
	private final JButton btnFullScreen = new ControlButton(ButtonSmallScreenIcon.FULLSCREEN);
	private final JButton btnPlayPause = new ControlButton(ButtonSmallScreenIcon.PLAY);
	private final JButton btnBack = new ControlButton(ButtonSmallScreenIcon.BACK);
	private final JButton btnBackward = new ControlButton(ButtonSmallScreenIcon.BACKWARD);
	private final JButton btnForward = new ControlButton(ButtonSmallScreenIcon.FORWARD);

	private final MySlider timeSlider = new MySlider();
	
	private static class MySlider extends JSlider {
		private static final long serialVersionUID = 1L;
		private boolean pressed;
		public void setPressed(boolean pressed) {
			this.pressed = pressed;
		}
		public boolean isPressed() {
			return this.pressed;
		}
	}
	
	
	private final JPanel northPanel;
	private final JPanel southPanel;

	private Point startDrag;
	private Point startLocation;
	
	private final int movieTimeMaxInMicros;

	private final JLabel lblTime = new JLabel("");
	
	
	public QtjSmallScreenX(QtjVideoPlayerImplX player) {
		this.player = player;
		this.player.addVideoPlayerListener(this);
		this.movieTimeMaxInMicros = this.player.getMovieTimeMaxInMicros();
		this.northPanel = this.newNorthPanel();
		this.southPanel = this.newSouthPanel();
	}

	public JPanel getNorthPanel() {
		return this.northPanel;
	}
	public JPanel getSouthPanel() {
		return this.southPanel;
	}
	
	private static class ControlButton extends PtPressableButton  {
		private static final long serialVersionUID = 4692041677980312903L;
		public ControlButton(ButtonSmallScreenIcon icon) {
			super(QtjImageFactory.getInstance().getButtonSmallScreen(icon),
			      QtjImageFactory.getInstance().getButtonSmallScreenPressed(icon));
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
		
		final JButton btnClose = new JButton(QtjImageFactory.getInstance().getCloseMini());
		btnClose.setActionCommand(CMD_CLOSE);
		btnClose.setBorderPainted(false);
		btnClose.addActionListener(this);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		PtGuiUtil.enableHandCursor(btnClose);
		
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
		this.btnBackward.setActionCommand(CMD_BACKWARD);
		this.btnForward.setActionCommand(CMD_FORWARD);
		
		this.btnFullScreen.setToolTipText("Fullscreen");
		this.btnPlayPause.setToolTipText("Play/Pause");
		this.btnBack.setToolTipText("Back");
		this.btnBackward.setToolTipText("Seek Backward");
		this.btnForward.setToolTipText("Seek Forward");
		
		this.btnFullScreen.setBorderPainted(false);
		this.btnPlayPause.setBorderPainted(false);
		this.btnBack.setBorderPainted(false);
		this.btnBackward.setBorderPainted(false);
		this.btnForward.setBorderPainted(false);
		
		
		
		this.timeSlider.addMouseListener(new MouseAdapter() {
			public void mousePressed(final MouseEvent e) {
				LOG.debug("timeSlider pressed");
				timeSlider.setPressed(true);
			}
			public void mouseReleased(final MouseEvent e) {
				LOG.debug("timeSlider released");
				timeSlider.setPressed(false);
				if (e.getSource().equals(timeSlider) && timeSlider.isEnabled()) {
//					final int value = timeSlider.getValue();
//					System.out.println("mouse released; timeSlider.getValue() = " + value);
//					seekToMs(value);
					
//					int value = ((JSlider)e.getSource()).getValue();
//					double perCent = (double) value / ((JSlider)e.getSource()).getMaximum();
//					double perCentOfSong = value * 1000000 / duration;
					
					int widthClicked = e.getPoint().x;
					int widthOfProgressBar = timeSlider.getSize().width;
					double perCent = (double) widthClicked / (double) widthOfProgressBar;
//					System.out.println("widthClicked="+widthClicked+"; widthOfProgressBar="+widthOfProgressBar);

					if(perCent > 1.0) perCent = 1.0;
					if(perCent < 0.0) perCent = 0.0;
					
					final int newTimeMicros = (int) (movieTimeMaxInMicros * perCent);
					LOG.debug("perCent="+(perCent*100)+"% -> seeking to " + newTimeMicros+"micros ("+PtTimeUtil.microSecondsToString(newTimeMicros)+")");
					player.doSeek(newTimeMicros);
				}
			}
		});
		
		this.timeSlider.setFocusable(false);
		this.timeSlider.setValue(0);
		this.timeSlider.setMaximum(movieTimeMaxInMicros);
		
		

		this.btnPlayPause.addActionListener(this);
		this.btnBackward.addActionListener(this);
		this.btnForward.addActionListener(this);
		this.btnBack.addActionListener(this);
		if(this.player.getDisplay() == null) {
			this.btnFullScreen.setEnabled(false);
//			this.btnPlayPause.setEnabled(false); ... sollte eigentlich zum loeschen gehen (?)
//			this.btnBack.setEnabled(false);
		} else {
			this.btnFullScreen.addActionListener(this);
		}

		panel.add(this.btnBack);
		panel.add(this.btnBackward);
		panel.add(this.btnPlayPause);
		panel.add(this.btnForward);
		panel.add(this.btnFullScreen);
		panel.add(this.timeSlider);
		this.updateLblTime(0);
		panel.add(this.lblTime);
		
		return panel;
	}
	
	private void updateLblTime(final int curMs) {
		this.lblTime.setText(PtTimeUtil.microSecondsToString(curMs) + " / " + this.player.getMovieTimeMaxFormatted());
	}
	
	void updateUi() {
		if(timeSlider.isPressed()) {
//			System.out.println("updateTimeUi aborting; timeSlider.isPressed == true");
			updateLblTime(timeSlider.getValue());
			return;
		}

		final int curMs = this.player.getMovieCurrentTimeInMicros();
		this.updateLblTime(curMs);
		this.timeSlider.setValue(curMs);
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
					player.doSeekBeginning();
					
				} else if(cmd.equals(CMD_BACKWARD)) {
					player.doSeekBackward();
					
				} else if(cmd.equals(CMD_FORWARD)) {
					player.doSeekForward();
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
		LOG.debug("state changed to: " + state);
		this.btnPlayPause.setIcon(state == QtjState.PAUSE ? ICON_PLAY: ICON_PAUSE);
		this.btnPlayPause.setPressedIcon(state == QtjState.PAUSE ? ICON_PLAY_PRESSED: ICON_PAUSE_PRESSED);
	}
}
