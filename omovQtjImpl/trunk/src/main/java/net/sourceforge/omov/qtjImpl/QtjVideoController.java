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

import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JComponent;

import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.util.GuiAction;
import quicktime.QTException;
import quicktime.app.view.MoviePlayer;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTJComponent;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class QtjVideoController {
	
	private final QtjVideoPlayerImpl player;
	private final net.sourceforge.omov.core.bo.Movie movie;
	private boolean fullscreen = false;
	
	private final File movieFile;
	private final JComponent qtjComponent;
	
	/** currently played qtj movie file */
	private Movie qtjMovie;

	/** if full screen is not supported, its value is null! */
	private final GraphicsDevice display;
	
	
	
	public QtjVideoController(QtjVideoPlayerImpl player, net.sourceforge.omov.core.bo.Movie movie, File movieFile) throws QTException {
		this.player = player;
		this.movie = movie;
		this.movieFile = movieFile;


		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
		GraphicsDevice[] displays = env.getScreenDevices();
        if(displays[0].isFullScreenSupported() == true) {
        	this.display = displays[0];
        } else {
        	this.display = null;
        }
        
        
		this.qtjMovie = openQtMovie(movieFile);
		MoviePlayer qtjPlayer = new MoviePlayer(this.qtjMovie);
		QTJComponent qtjPlayercomponent = QTFactory.makeQTJComponent(qtjPlayer);
		this.qtjComponent = qtjPlayercomponent.asJComponent();
		
		
//		MovieController controller = new MovieController(qtMovie);
//		QTComponent qtControllerComponent = QTFactory.makeQTComponent(controller);
//		Component controllerComponent = qtControllerComponent.asComponent();
////	controller.setKeysEnabled(true); // enabling the keys so the user can interact with the movie with the keyboard

		this.qtjComponent.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				new GuiAction() {
					@Override
					protected void _action() {
						if(e.getClickCount() == 2) {
							if(isFullscreen()) doSwitchSmallscreen();
							else doSwitchFullscreen();
						}
					}
				}.doAction();
			}
		});
		
	}
	
	public void doBack() {
		System.out.println("not yet implemented"); // TODO implement
	}
	
	public GraphicsDevice getDisplay() {
		return this.display;
	}
	
	public Dimension getRecalcedFullMovieDimension() {
		Dimension movieDim;
		try {
			movieDim = QtjUtil.getMovieDimension(this.qtjMovie);
		} catch (QTException e) {
			throw new FatalException("Could not recalculate movie dimension of file '"+this.movieFile.getAbsolutePath()+"'!", e);
		}
		return QtjUtil.recalcFullscreenMovieDimension(movieDim);
	}
	
	public JComponent getQtjComponent() {
		return this.qtjComponent;
	}
	
	public net.sourceforge.omov.core.bo.Movie getMovie() {
		return this.movie;
	}
	
	public File getMovieFile() {
		return this.movieFile; // currently played movie file
	}

	public void doSwitchFullscreen() {
		this.fullscreen = true;
//		this.broadcastScreenSwitchListener(true);
		this.player.switchFullscreen(true);
	}
	
	public void doSwitchSmallscreen() {
		this.fullscreen = false;
//		this.broadcastScreenSwitchListener(false);
		this.player.switchFullscreen(false);
	}
	
	public boolean isFullscreen() {
		return this.fullscreen;
	}
	
	public void doClose() {
		if(this.isFullscreen() && this.getDisplay() != null) {
			this.getDisplay().setFullScreenWindow(null);
		}
		
		this.player.dispose();
	}
	


	private final Set<IQtjScreenListener> switchListeners = new HashSet<IQtjScreenListener>();
	public void addQtjScreenListener(IQtjScreenListener listener) {
		this.switchListeners.add(listener);
	}
	public void removeQtjScreenListener(IQtjScreenListener listener) {
		this.switchListeners.remove(listener);
	}
	private void broadcastSwitchFullscreen(boolean fullscreen) {
		for (IQtjScreenListener listener : this.switchListeners) {
			listener.switchedFullscreen(fullscreen);
		}
	}
	private void broadcastStateChanged(QtjState state) {
		for (IQtjScreenListener listener : this.switchListeners) {
			listener.stateChanged(state);
		}
	}
	public static interface IQtjScreenListener {
		void switchedFullscreen(boolean fullscreen);
		void stateChanged(QtjState state);
	}
	public enum QtjState {
		PAUSE, PLAY;
	}

	private boolean isMoviePlaying = false;
	public void doPlayPause() {
		try {
			if(this.isMoviePlaying == true) {
				this.qtjMovie.stop();
			} else {
				this.qtjMovie.start();
			}
			
			
			this.isMoviePlaying = !this.isMoviePlaying;
			this.broadcastStateChanged(this.isMoviePlaying ? QtjState.PLAY : QtjState.PAUSE);
		} catch (StdQTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	private static Movie openQtMovie(File file) throws QTException {
		QtjSessionInitializer.openSession();
		OpenMovieFile openFile = OpenMovieFile.asRead(new QTFile(file));
		return Movie.fromFile(openFile);
	}
	

	/*	
	
	
	public static Dimension getMovieDimension(Movie movie) throws QTException {
		Region region = movie.getDisplayBoundsRgn();
		QDRect rect = region.getBounds();
		return new Dimension(rect.getWidth(), rect.getHeight());
	}
	*/
}
