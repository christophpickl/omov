package net.sourceforge.omov.qtjImpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.core.ImageFactory.IconQuickView;
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

public class QtjVideoPlayerImpl extends JWindow implements IQtjVideoPlayer, ActionListener, MouseListener, MouseMotionListener {
	
    private static final Log LOG = LogFactory.getLog(QtjVideoPlayerImpl.class);
	private static final long serialVersionUID = -7527249992554309045L;
	private static final Font WINDOW_TITLE_FONT = new Font("sans", Font.BOLD, 10);
	private static final Color COLOR_GRAY = new Color(168, 168, 168);

	private static final String CMD_PLAY_PAUSE = "CMD_PLAY_PAUSE";
	private static final String CMD_BACK = "CMD_BACK";
	private static final String CMD_CLOSE = "CMD_CLOSE";
	private static final String CMD_FULLSCREEN = "CMD_FULLSCREEN";

	private static final ImageIcon ICON_PLAY = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_PLAY);
	private static final ImageIcon ICON_PAUSE = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_PAUSE);
	private static final ImageIcon ICON_BACK = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_BACK);
	private static final ImageIcon ICON_FULLSCREEN = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_FULLSCREEN);
	private static final ImageIcon ICON_CLOSE_MINI = ImageFactory.getInstance().getIcon(IconQuickView.BUTTON_CLOSE_MINI);

	
	private final net.sourceforge.omov.core.bo.Movie movie;
	private Movie qtMovie;
	private File movieFile;
	private boolean isFullScreenMode = false;
	private boolean isMoviePlaying = false;

	private Point startDrag;
	private Point startLocation;
	
	private Dimension previousSize;
	private Point previousLocation;
	private GraphicsDevice graphicsDevice;
	
	
	private final JButton btnFullScreen = new JButton(ICON_FULLSCREEN);
	private final JButton btnPlayPause = new JButton(ICON_PLAY);
	private final JButton btnBack = new JButton(ICON_BACK);
	
	private final JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 4, 0));
	private final QtjVideoController controller = new QtjVideoController(this);
	
	// TODO QTJava - JComboBox, wo man auch alle anderen movie files auswaehlen kann (oben links, JComboBox aus fileName machen)

	// FIXME QTJava - retain video size aspect when is in fullscreen mode
	
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
	
	public QtjVideoPlayerImpl(net.sourceforge.omov.core.bo.Movie movie, File movieFile, JFrame owner) throws QTException {
		super(owner);
		assert(movieFile.exists()) : movieFile.getAbsolutePath();

		this.movie = movie;
		this.movieFile = movieFile;
		LOG.info("Opening file '"+movieFile.getAbsolutePath()+"'.");
		
		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        this.graphicsDevice = devices[0];
        if(graphicsDevice.isFullScreenSupported() == false) {
        	this.graphicsDevice = null;
        }
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);

		this.qtjComponent = this.initQuicktimePlayer(movieFile);
		this.initComponentsSmallscreen();
		this.getContentPane().add(this.wrapPanel);
		this.setBackground(Color.BLACK);
		this.pack();
		SimpleGuiUtil.setCenterLocation(this);
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
		final Dimension movieRecalcedSize = QtjUtil.recalcFullscreenMovieDimension(movieDim);
		qtjWrapPanel.setMinimumSize(movieRecalcedSize);
		qtjWrapPanel.setMaximumSize(movieRecalcedSize);
		qtjWrapPanel.setPreferredSize(movieRecalcedSize);
		qtjWrapPanel.setSize(movieRecalcedSize);
		
		panel.add(qtjWrapPanel, BorderLayout.CENTER);
		this.wrapPanel.add(panel);
		this.wrapPanel.invalidate();
		this.wrapPanel.repaint();
	}

	private void initComponentsSmallscreen() {
		LOG.debug("Initializing components for smallscreen mode.");
		this.wrapPanel.removeAll();
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(4, 10, 10, 10));
		panel.add(this.newNorthPanel(), BorderLayout.NORTH);
		panel.add(this.qtjComponent, BorderLayout.CENTER);
		panel.add(this.initSouthPanel(), BorderLayout.SOUTH);
		this.wrapPanel.add(panel);
		this.wrapPanel.invalidate();
		this.wrapPanel.repaint();
	}

	private JPanel newNorthPanel() {
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0)); // margin-bottom
		
		final JLabel windowTitle = new JLabel(this.movie.getTitle() + " - " + this.movieFile.getName());
		windowTitle.setFont(WINDOW_TITLE_FONT);
		windowTitle.setBorder(BorderFactory.createEmptyBorder());
		windowTitle.setForeground(COLOR_GRAY);
		
		final JButton btnClose = new JButton(ICON_CLOSE_MINI);
		btnClose.setActionCommand(CMD_CLOSE);
		btnClose.setBorderPainted(false);
		btnClose.addActionListener(this);
		btnClose.setBorder(BorderFactory.createEmptyBorder());
		SimpleGuiUtil.enableHandCursor(btnClose);
		
		panel.add(windowTitle, BorderLayout.WEST);
		panel.add(btnClose, BorderLayout.EAST);
		return panel;
	}
	
	private JPanel initSouthPanel() {
		this.southPanel.setBackground(Constants.getColorWindowBackground());
		
		this.btnFullScreen.setActionCommand(CMD_FULLSCREEN);
		this.btnPlayPause.setActionCommand(CMD_PLAY_PAUSE);
		this.btnBack.setActionCommand(CMD_BACK);
		
		this.btnFullScreen.setToolTipText("Fullscreen");
		this.btnPlayPause.setToolTipText("Play/Pause");
		this.btnBack.setToolTipText("Back");
		
		this.btnFullScreen.setBorderPainted(false);
		this.btnPlayPause.setBorderPainted(false);
		this.btnBack.setBorderPainted(false);
		
		if(this.graphicsDevice == null) {
			this.btnFullScreen.setEnabled(false);
			this.btnPlayPause.setEnabled(false);
			this.btnBack.setEnabled(false);
		} else {
			this.btnFullScreen.addActionListener(this);
			this.btnPlayPause.addActionListener(this);
			this.btnBack.addActionListener(this);
		}

		this.southPanel.add(this.btnBack);
		this.southPanel.add(this.btnPlayPause);
		this.southPanel.add(this.btnFullScreen);
		
		return this.southPanel;
	}
	
	private JComponent initQuicktimePlayer(File movieFile) throws QTException {
		SessionInitializer.openSession();
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
		playerComponent.addMouseListener(this);
		playerComponent.addMouseMotionListener(this);
		
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
	
	private void doPlayPause() {
		try {
			if(this.isMoviePlaying) {
				this.qtMovie.stop();
			} else {
				this.qtMovie.start();
			}
			this.btnPlayPause.setIcon(this.isMoviePlaying ? ICON_PLAY: ICON_PAUSE);
			
			this.isMoviePlaying = !this.isMoviePlaying;
		} catch (StdQTException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		if(cmd.equals(CMD_CLOSE)) {
			this.doClose();
		} else if(cmd.equals(CMD_FULLSCREEN)) {
			this.doSwitchFullscreen();
		} else if(cmd.equals(CMD_PLAY_PAUSE)) {
			this.doPlayPause();
		} else if(cmd.equals(CMD_BACK)) {
			this.doBack();
		} else {
			throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
		}
	}
	
	private void doBack() {
		
	}
	
	private void doClose() {
		if(this.isFullScreenMode) {
			this.graphicsDevice.setFullScreenWindow(null);
		}
		this.dispose();
	}
	
	private void doSwitchFullscreen() {
		assert(this.graphicsDevice != null);
		this.isFullScreenMode = !this.isFullScreenMode;
		
		if(this.isFullScreenMode == true) {
			this.previousLocation = this.getLocation();
			this.previousSize = this.getSize();
		}
		
		this.graphicsDevice.setFullScreenWindow(this.isFullScreenMode ? QtjVideoPlayerImpl.this : null);
		
		if(this.isFullScreenMode == false) {
			this.setLocation(this.previousLocation);
			this.setSize(this.previousSize);
		}

//		this.southPanel.setBackground(isFullScreenMode ? Color.BLACK : Constants.getColorWindowBackground());
		if(this.isFullScreenMode) {
			this.initComponentsFullscreen();
		} else {
			this.initComponentsSmallscreen();
		}
	}
	
	

    public void mousePressed(MouseEvent e) {
    	if(this.isFullScreenMode == true) return;
    	
        this.startDrag = this.getScreenLocation(e);
        this.startLocation = this.getLocation( );
    }

    public void mouseClicked(MouseEvent e) {
    	// nothing to do
    }
    public void mouseReleased(MouseEvent e) {
    	// nothing to do
	}

    public void mouseDragged(MouseEvent e) {
    	if(this.isFullScreenMode == true) return;
    	
    	Point current = this.getScreenLocation(e);
        Point offset = new Point(
            (int)current.getX() - (int)startDrag.getX(),
            (int)current.getY() - (int)startDrag.getY());
        Point new_location = new Point(
            (int)(this.startLocation.getX() + offset.getX()),
            (int)(this.startLocation.getY() + offset.getY()));
        this.setLocation(new_location);
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
        Point targetLocation = this.getLocationOnScreen();
        return new Point(
            (int) (targetLocation.getX() + cursor.getX()),
            (int) (targetLocation.getY() + cursor.getY()));
    }
	
}
