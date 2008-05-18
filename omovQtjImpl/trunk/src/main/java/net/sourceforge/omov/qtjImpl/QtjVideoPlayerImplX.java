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
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.qtjApi.IQtjVideoPlayer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
public class QtjVideoPlayerImplX extends JFrame implements IQtjVideoPlayer {
	
    private static final Log LOG = LogFactory.getLog(QtjVideoPlayerImplX.class);
	private static final long serialVersionUID = -7527249992554309045L;



	
	private final net.sourceforge.omov.core.bo.Movie movie;
	private Movie qtMovie;
	private File movieFile;
	private boolean fullScreenMode = false;
	private boolean isMoviePlaying = false;

	
	private Dimension previousSize;
	private Point previousLocation;
	private GraphicsDevice display;

	private final QtjFullScreenX fullScreen;
	private final QtjSmallScreenX smallScreen;
	
	
//	private final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
//	private final QtjVideoController controller = new QtjVideoController(this);
	
	// TODO QTJava - JComboBox, wo man auch alle anderen movie files auswaehlen kann (oben links, JComboBox aus fileName machen)

	
	/*	
	public static Movie openQtMovie(File file) throws QTException {
		OpenMovieFile openFile = OpenMovieFile.asRead(new QTFile(file));
		return Movie.fromFile(openFile);
	}
	
	public static Dimension getMovieDimension(Movie movie) throws QTException {
		Region region = movie.getDisplayBoundsRgn();
		QDRect rect = region.getBounds();
		return new Dimension(rect.getWidth(), rect.getHeight());
	}
	*/
	private final JPanel wrapPanel = new JPanel();
	private final JComponent qtjComponent;
	
	public QtjVideoPlayerImplX(net.sourceforge.omov.core.bo.Movie movie, File movieFile, JFrame owner) throws QTException {
//		super(owner); ... frames cant be modal
		assert(movieFile.exists()) : movieFile.getAbsolutePath();

		this.movie = movie;
		this.movieFile = movieFile;
		LOG.info("Opening file '"+movieFile.getAbsolutePath()+"'.");
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        this.display = devices[0];
        if(display.isFullScreenSupported() == false) {
        	this.display = null;
        }
		

		this.qtjComponent = this.initQuicktimePlayer(movieFile);
		this.smallScreen = new QtjSmallScreenX(this);
		this.fullScreen = new QtjFullScreenX(this);
		
		final JPanel glassPane = (JPanel) this.getGlassPane();
		glassPane.setLayout(new BorderLayout());
		glassPane.add(this.fullScreen.getFloater(), BorderLayout.SOUTH);
		
		this.initComponentsSmallscreen();
		this.setUndecorated(true);
		this.getContentPane().add(this.wrapPanel);
		this.setBackground(Color.BLACK);
		this.pack();
		SimpleGuiUtil.setCenterLocation(this);
		
		this.addMouseListener(this.smallScreen);
		this.addMouseMotionListener(this.smallScreen);

		this.qtjComponent.addMouseListener(this.smallScreen);
		this.qtjComponent.addMouseMotionListener(this.smallScreen);

		this.qtjComponent.addMouseMotionListener(this.fullScreen.getFloater());
		this.wrapPanel.addMouseMotionListener(this.fullScreen.getFloater());
	}
	
	private void initComponentsFullscreen() { // TODO still got some top margin...
		LOG.debug("Initializing components for fullscreen mode.");
		this.wrapPanel.removeAll();
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		JPanel qtjWrapPanel = new JPanel(new BorderLayout(0, 0));
		qtjWrapPanel.add(this.qtjComponent, BorderLayout.CENTER);
		Dimension movieDim;
		try {
			movieDim = QtjUtil.getMovieDimension(this.qtMovie);
		} catch (QTException e) {
			throw new FatalException("Could not recalculate movie dimension of file '"+this.movieFile.getAbsolutePath()+"'!", e);
		}
		// TODO do not recalc every full/smallscreen-switch -> outsource in QtjFullScreenX
		final Dimension movieRecalcedSize = QtjUtil.recalcFullscreenMovieDimension(movieDim);
		qtjWrapPanel.setMinimumSize(movieRecalcedSize);
		qtjWrapPanel.setMaximumSize(movieRecalcedSize);
		qtjWrapPanel.setPreferredSize(movieRecalcedSize);
		qtjWrapPanel.setSize(movieRecalcedSize);
		
		panel.add(qtjWrapPanel, BorderLayout.CENTER);
		this.getGlassPane().setVisible(true);
		
//		panel.add(this.fullScreen.getFloater(), BorderLayout.SOUTH);
		this.wrapPanel.add(panel);
		this.wrapPanel.invalidate();
		this.wrapPanel.repaint();
	}
	
	private void initComponentsSmallscreen() {
		LOG.debug("Initializing components for smallscreen mode.");
		
		this.getGlassPane().setVisible(false);
		
		this.wrapPanel.removeAll();
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(4, 10, 10, 10));
		panel.add(this.smallScreen.getNorthPanel(), BorderLayout.NORTH);
		panel.add(this.qtjComponent, BorderLayout.CENTER);
		panel.add(this.smallScreen.getSouthPanel(), BorderLayout.SOUTH);
		this.wrapPanel.add(panel);
		this.wrapPanel.invalidate();
		this.wrapPanel.repaint();
	}

	
	private JComponent initQuicktimePlayer(File movieFile) throws QTException {
		QtjSessionInitializer.openSession();
		OpenMovieFile openFile = OpenMovieFile.asRead(new QTFile(movieFile));
		this.qtMovie = Movie.fromFile(openFile);
		
//		MovieController controller = new MovieController(qtMovie);
//		QTComponent qtControllerComponent = QTFactory.makeQTComponent(controller);
//		Component controllerComponent = qtControllerComponent.asComponent();
////	controller.setKeysEnabled(true); // enabling the keys so the user can interact with the movie with the keyboard
		
		MoviePlayer player = new MoviePlayer(qtMovie);
		QTJComponent qtPlayercomponent = QTFactory.makeQTJComponent(player);
		
		JComponent playerComponent = qtPlayercomponent.asJComponent();
		
		playerComponent.addMouseListener(new MouseAdapter() {
			public void mouseClicked(final MouseEvent e) {
				new GuiAction() {
					@Override
					protected void _action() {
						doClickedOnPlayerComponent(e);
					}
				}.doAction();
			}
		});
		
//		final JPanel panel = new JPanel(new BorderLayout());
//		panel.add(playerComponent, BorderLayout.CENTER);
//		panel.add(controllerComponent, BorderLayout.SOUTH);
		return playerComponent;
	}
	
	private void doClickedOnPlayerComponent(MouseEvent e) {
		if(e.getClickCount() == 2) {
			this.doSwitchFullscreen();
		}
	}
	
	void doPlayPause() {
		LOG.debug("doPlayPause(); this.isMoviePlaying="+this.isMoviePlaying);
		try {
			if(this.isMoviePlaying) {
				this.qtMovie.stop();
			} else {
				this.qtMovie.start();
			}
			
			this.isMoviePlaying = !this.isMoviePlaying;
			this.smallScreen.stateChanged(this.isMoviePlaying ? QtjState.PLAY : QtjState.PAUSE);
			this.fullScreen.stateChanged(this.isMoviePlaying ? QtjState.PLAY : QtjState.PAUSE);
		} catch (StdQTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	void doBack() {
		
	}
	
	void doClose() {
		if(this.fullScreenMode) {
			this.display.setFullScreenWindow(null);
		}
		this.dispose();
	}
	
	void doSwitchFullscreen() {
		assert(this.display != null);
		this.fullScreenMode = !this.fullScreenMode;
		
		if(this.fullScreenMode == true) {
			this.previousLocation = this.getLocation();
			this.previousSize = this.getSize();
		}
		
		this.display.setFullScreenWindow(this.fullScreenMode ? this : null);
		
		if(this.fullScreenMode == false) {
			this.setLocation(this.previousLocation);
			this.setSize(this.previousSize);
		}
		
		this.smallScreen.switchedFullscreen(this.fullScreenMode);
		this.fullScreen.switchedFullscreen(this.fullScreenMode);

		if(this.fullScreenMode) {
			this.initComponentsFullscreen();
		} else {
			this.initComponentsSmallscreen();
		}
	}
	
	
	static interface IVideoPlayerListener {
		void switchedFullscreen(boolean fullscreen);
		void stateChanged(QtjState state);
	}
	public enum QtjState {
		PLAY, PAUSE;
	}
	
	
    
    public boolean isFullScreenMode() {
    	return this.fullScreenMode;
    }
    
    public net.sourceforge.omov.core.bo.Movie getMovie() {
    	return this.movie;
    }
    public File getMovieFile() {
    	return this.movieFile;
    }
    
    public GraphicsDevice getDisplay() {
    	return this.display;
    }
}
