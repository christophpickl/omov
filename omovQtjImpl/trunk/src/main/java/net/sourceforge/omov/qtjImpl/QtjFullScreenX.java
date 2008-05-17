package net.sourceforge.omov.qtjImpl;

import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.IVideoPlayerListener;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.QtjState;
import net.sourceforge.omov.qtjImpl.floater.IQtjFloaterListener;
import net.sourceforge.omov.qtjImpl.floater.QtjFloater;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class QtjFullScreenX implements IVideoPlayerListener, IQtjFloaterListener {

    private static final Log LOG = LogFactory.getLog(QtjFullScreenX.class);
    
    
	private final QtjVideoPlayerImplX player;
	private final QtjFloater floater;
	
	
	public QtjFullScreenX(QtjVideoPlayerImplX player) {
		this.player = player;
		this.floater = new QtjFloater(this.player, this.player, this);
	}

	public QtjFloater getFloater() {
		return this.floater;
	}

	public void stateChanged(QtjState state) {
		this.floater.setPlayPauseButton(state);
	}

	public void switchedFullscreen(boolean fullscreen) {
		// TODO
	}

	public void doPlayPause() {
		this.player.doPlayPause();
	}
}
