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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Toolkit;

import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.IVideoPlayerListener;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.QtjState;
import net.sourceforge.omov.qtjImpl.floater.IQtjFloaterListener;
import net.sourceforge.omov.qtjImpl.floater.QtjFloater;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjFullScreenX implements IVideoPlayerListener, IQtjFloaterListener {

    private static final Log LOG = LogFactory.getLog(QtjFullScreenX.class);
    
    // TODO QTJ - hide cursor if not moved for a while
    
	private final QtjVideoPlayerImplX player;
	private final QtjFloater floater;
	
	private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
	private static final Cursor HIDDEN_CURSOR;
	static {
		HIDDEN_CURSOR = Toolkit.getDefaultToolkit().createCustomCursor(
				QtjImageFactory.getInstance().getTransparentPixelImage(), new Point(0, 0), "");
	}
	
	
	public QtjFullScreenX(QtjVideoPlayerImplX player) {
		LOG.debug("Creating new QtjFullScreenX instance.");
		this.player = player;
		this.player.addVideoPlayerListener(this);
		this.floater = new QtjFloater(this.player, this.player, this);
	}

	public QtjFloater getFloater() {
		return this.floater;
	}
	
	void startFloaterFadeoutThread() {
		this.floater.startFadeOutThread();
	}

	public void stateChanged(QtjState state) {
		this.floater.setPlayPauseButton(state);
	}

	public void switchedFullscreen(boolean fullscreen) {
		// nothing to do?
	}

	public void doPlayPause() {
		this.player.doPlayPause();
	}

	public void didHide() {
		if(this.player.isFullScreenMode() == true) {
			LOG.debug("Hiding Cursor");
			this.player.setCursor(HIDDEN_CURSOR);
		} else {
			this.didShow();
		}
		
	}
	
	public void didShow() {
		LOG.debug("Showing Cursor");
		this.player.setCursor(DEFAULT_CURSOR);
	}
}
