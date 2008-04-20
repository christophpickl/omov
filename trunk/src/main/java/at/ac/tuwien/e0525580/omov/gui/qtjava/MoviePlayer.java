package at.ac.tuwien.e0525580.omov.gui.qtjava;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import quicktime.QTException;
import quicktime.app.view.QTFactory;
import quicktime.app.view.QTJComponent;
import quicktime.io.OpenMovieFile;
import quicktime.io.QTFile;
import quicktime.std.StdQTException;
import quicktime.std.movies.Movie;
import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class MoviePlayer extends JWindow implements ActionListener, MouseListener, MouseMotionListener {

    private static final Log LOG = LogFactory.getLog(MoviePlayer.class);
	private static final long serialVersionUID = -7527249992554309045L;

	private static final String CMD_CLOSE = "CMD_CLOSE";
	private static final String CMD_FULLSCREEN = "CMD_FULLSCREEN";
	private static final String LBL_FULLSCREEN_YES = "Enter Fullscreen";
	private static final String LBL_FULLSCREEN_NO = "Exit Fullscreen";

	private static final String CMD_PLAY_PAUSE = "CMD_PLAY_PAUSE";
	private static final String LBL_PLAY = "Play";
	private static final String LBL_PAUSE = "Pause";
	
	
	private boolean isFullScreenMode = false;
	private final JButton btnFullScreen = new JButton(LBL_FULLSCREEN_YES);
	private final JButton btnPlayPause = new JButton(LBL_PLAY);

	private Point startDrag;
	private Point startLocation;
	
	private Dimension previousSize;
	private Point previousLocation;
	private GraphicsDevice graphicsDevice;
	
	private Movie qtMovie;
	private boolean isMoviePlaying = false;
	
	// TODO JComboBox, wo man auch alle anderen movie files auswaehlen kann

	public MoviePlayer(at.ac.tuwien.e0525580.omov.bo.Movie movie, File movieFile, JFrame owner) throws QTException {
		super(owner);
		
		assert(movieFile.exists()) : movieFile.getAbsolutePath();
		LOG.debug("Opening file '"+movieFile.getAbsolutePath()+"'.");
		final JComponent content = this.getQuicktimePlayer(movieFile);

		GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        this.graphicsDevice = devices[0];
        if(graphicsDevice.isFullScreenSupported() == false) {
        	this.graphicsDevice = null;
        }
		
		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		final JPanel panel = new JPanel(new BorderLayout());
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		panel.add(content, BorderLayout.CENTER);
		panel.add(this.newSouthPanel(), BorderLayout.SOUTH);
		
		this.getContentPane().add(panel);
//		this.setTitle("Movie Player - " + movie.getTitle());
//		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		this.setBackground(Color.BLACK);
		this.pack();
		GuiUtil.setCenterLocation(this);
	}

	
	private JPanel newSouthPanel() {
		final JPanel panel = new JPanel();
		panel.setBackground(Constants.getColorWindowBackground());
		
		final JButton btnClose = new JButton("Close");

		btnClose.setActionCommand(CMD_CLOSE);
		this.btnFullScreen.setActionCommand(CMD_FULLSCREEN);
		this.btnPlayPause.setActionCommand(CMD_PLAY_PAUSE);
		
		btnClose.addActionListener(this);
		
		if(this.graphicsDevice == null) {
			this.btnFullScreen.setEnabled(false);
			this.btnPlayPause.setEnabled(false);
		} else {
			this.btnFullScreen.addActionListener(this);
			this.btnPlayPause.addActionListener(this);
		}

		btnClose.putClientProperty("JButton.buttonType", "textured");
		btnFullScreen.putClientProperty("JButton.buttonType", "textured");
		btnPlayPause.putClientProperty("JButton.buttonType", "textured");

		panel.add(btnClose);
		panel.add(this.btnPlayPause);
		panel.add(this.btnFullScreen);
		
		return panel;
	}
	
	private JComponent getQuicktimePlayer(File movieFile) throws QTException {
		SessionInitializer.openSession();
		OpenMovieFile openFile = OpenMovieFile.asRead(new QTFile(movieFile));
		this.qtMovie = Movie.fromFile(openFile);
		
//		MovieController controller = new MovieController(qtMovie);
//		QTComponent qtControllerComponent = QTFactory.makeQTComponent(controller);
//		Component controllerComponent = qtControllerComponent.asComponent();
////	controller.setKeysEnabled(true); // enabling the keys so the user can interact with the movie with the keyboard
		
		quicktime.app.view.MoviePlayer player = new quicktime.app.view.MoviePlayer(qtMovie);
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
			this.doFullscreen();
		}
	}
	
	private void doPlayPause() {
		try {
			if(this.isMoviePlaying) {
				this.qtMovie.stop();
			} else {
				this.qtMovie.start();
			}
			this.btnPlayPause.setText(this.isMoviePlaying ? LBL_PLAY: LBL_PAUSE);
			
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
			this.doFullscreen();
		} else if(cmd.equals(CMD_PLAY_PAUSE)) {
			this.doPlayPause();
		} else {
			throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
		}
	}
	
	private void doClose() {
		if(this.isFullScreenMode) {
			this.graphicsDevice.setFullScreenWindow(null);
		}
		this.dispose();
	}
	
	private void doFullscreen() {
		assert(this.graphicsDevice != null);
		
		if(this.isFullScreenMode == false) {
			this.previousLocation = this.getLocation();
			this.previousSize = this.getSize();
		}
		
		this.graphicsDevice.setFullScreenWindow(this.isFullScreenMode ? null : MoviePlayer.this);
		this.btnFullScreen.setText(this.isFullScreenMode ? LBL_FULLSCREEN_YES : LBL_FULLSCREEN_NO);
		
		if(this.isFullScreenMode == true) {
			this.setLocation(this.previousLocation);
			this.setSize(this.previousSize);
		}

		this.isFullScreenMode = !this.isFullScreenMode;
	}
	
	

    public void mousePressed(MouseEvent e) {
    	if(this.isFullScreenMode == true) return;
    	
        this.startDrag = this.getScreenLocation(e);
        this.startLocation = this.getLocation( );
    }

    public void mouseClicked(MouseEvent e) { /* nothing to do */ }
    public void mouseReleased(MouseEvent e) { /* nothing to do */ }

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

    public void mouseMoved(MouseEvent e) { /* nothing to do */}
    public void mouseEntered(MouseEvent e) { /* nothing to do */ }
    public void mouseExited(MouseEvent e) { /* nothing to do */ }
    
    private Point getScreenLocation(MouseEvent e) {
        Point cursor = e.getPoint( );
        Point targetLocation = this.getLocationOnScreen();
        return new Point(
            (int) (targetLocation.getX() + cursor.getX()),
            (int) (targetLocation.getY() + cursor.getY()));
    }
	
}
