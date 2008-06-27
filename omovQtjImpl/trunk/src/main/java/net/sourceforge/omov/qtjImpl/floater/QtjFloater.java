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

package net.sourceforge.omov.qtjImpl.floater;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.lang.Thread.State;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.gui.GuiActionListener;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.QtjState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.jlib.util.GuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjFloater extends JPanel implements MouseMotionListener, MouseListener, ActionListener {

    private static final Log LOG = LogFactory.getLog(QtjFloater.class);
	private static final long serialVersionUID = -3470793850796585903L;

	private static Color OPAC_COLOR_FRONT = Constants.getColorLightGray();
	private static Color OPAC_COLOR_BACK = Constants.getColorDarkGray();

	private static final Map<Color, Map<Integer, Color>> OPACITY_MAP = new HashMap<Color, Map<Integer, Color>>();
	static {
		Color[] cc = new Color[] { OPAC_COLOR_BACK, OPAC_COLOR_FRONT };
		for(Color c : cc) {
			Map<Integer, Color> m = new HashMap<Integer, Color>();
			for (int i = 0; i <= 10; i++) {
				int alpha = (int) (140 * (i / 10.)); // not max of 255, but 140 to get still some transparency
				Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
				m.put(new Integer(i * 10), newC);
				
			}
			OPACITY_MAP.put(c, m);
		}
	}
	

	private static final String CMD_PLAY_PAUSE = "CMD_PLAY_PAUSE";
	
	private final JFrame frame;
	private final QtjVideoPlayerImplX player;

	private Lifetime lifetime = new Lifetime();
	private Opacity opacity = new Opacity(this);
	
	private boolean isMouseEntered = false;
	private FadeOutThread thread;

	// TODO QTJ GUI - implement QtjFloater
	private final JLabel lbl = new JLabel("... experimental implementation of a video player ...");
	private final JButton btnPlayPause = new JButton("Play");
	
	private final IQtjFloaterListener listener;
	
	
	
	public QtjFloater(JFrame frame, QtjVideoPlayerImplX player, IQtjFloaterListener listener) {
		this.frame = frame;
		this.player = player;
		this.listener = listener;
		this.addMouseListener(this);
		this.initComponents();
		this.setOpaque(true);
		this.opacityChanged(this.opacity.getValue(), true);
		
		final Dimension dim = new Dimension(600, 40);
		this.setPreferredSize(dim);
		this.setMaximumSize(dim);
		this.setMinimumSize(dim);
		this.setSize(dim);
	}
	
	private void initComponents() {
		btnPlayPause.setBorderPainted(false);
		btnPlayPause.setActionCommand(CMD_PLAY_PAUSE);
		btnPlayPause.addActionListener(this);
		GuiUtil.enableHandCursor(btnPlayPause);
		btnPlayPause.setForeground(OPAC_COLOR_FRONT);
		btnPlayPause.addActionListener(new GuiActionListener() {
			public void action(ActionEvent e) {
				listener.doPlayPause();
			}
		});
		
		this.lbl.setForeground(OPAC_COLOR_FRONT);
		this.add(lbl);
//		this.add(btnPlayPause);
	}

	void opacityChanged(int opacity, boolean firstTime) {
//		System.out.println("opacity = " + opacity + " (firstTime="+firstTime+")");
		final Color fg = OPACITY_MAP.get(OPAC_COLOR_FRONT).get(opacity);
		final Color bg = OPACITY_MAP.get(OPAC_COLOR_BACK).get(opacity);
		assert(fg != null && bg != null);
		
		this.setBackground(bg);
		this.lbl.setForeground(fg);
		this.btnPlayPause.setForeground(fg);
		
		this.frame.invalidate();
		this.frame.repaint();
		
		if(opacity == 0 && firstTime == false) {
			this.listener.didHide();
		}
	}
	
	public void setPlayPauseButton(QtjState state) {
		LOG.debug("setPlayPauseButton(state="+state+")");
		this.btnPlayPause.setText(state == QtjState.PLAY ? "Pause" : "Play" );
	}
	
	
	
	public void startFadeOutThread() {
		LOG.debug("Creating new FadeOutThread.");
		thread = new FadeOutThread(this.lifetime, this.opacity);
		thread.start();
	}
	
    public void mouseMoved(MouseEvent e) {
    	if(this.isMouseEntered == true || this.player.isFullScreenMode() == false) return;
//		System.out.println("mouseMoved (lifetime=10; opacity=0; thread.state="+(thread == null ? "null" : thread.getState())+")");
		
		this.lifetime.reset();
		this.opacity.reset();
		
		if(thread == null || thread.getState() == State.TERMINATED) {
			this.startFadeOutThread();
			this.listener.didShow(); // TODO QTJ - should this line also be moved into startFadeOutThread???
		}
	}

	public void mouseEntered(MouseEvent e) {
		if(this.player.isFullScreenMode() == false) return;
		// MINOR QTJ - will be invoked at very first time, therefore thread.shouldStop will be invoked and fadeOutThread will NOT decrease opacity!
		LOG.debug("mouseEntered");
		this.isMouseEntered = true;
		if(this.thread != null) {
			this.thread.shouldStop();
		}
		this.opacity.reset();
	}

	public void mouseExited(MouseEvent e) {
		if(this.player.isFullScreenMode() == false) return;
		
		LOG.debug("mouseExited"); // FIXME QTJ GUI - auch mouseExited, wenn ueber Button innerhalb Floater hovered!
		this.isMouseEntered = false;
	}

	public void mouseClicked(MouseEvent e) { /* not used */ }
	public void mousePressed(MouseEvent e) { /* not used */ }
	public void mouseReleased(MouseEvent e) { /* not used */ }
	public void mouseDragged(MouseEvent e) { /* not used */ }

	
	
	public void actionPerformed(final ActionEvent e) {
		new GuiAction() {
			@Override
			protected void _action() {
				final String cmd = e.getActionCommand();
				LOG.info("actionPerformed(cmd="+cmd+")");
				
				if(cmd.equals(CMD_PLAY_PAUSE)) {
					listener.doPlayPause(); // FIXME QTJ - play/pause funkt in floater nicht
				} else {
					throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
				}
			}
		}.doAction();
	}
	
}
