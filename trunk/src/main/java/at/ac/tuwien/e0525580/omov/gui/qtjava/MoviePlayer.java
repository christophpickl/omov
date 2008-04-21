package at.ac.tuwien.e0525580.omov.gui.qtjava;


public class MoviePlayer {
/*
public class MoviePlayer extends JWindow implements ActionListener, MouseListener, MouseMotionListener {
	
    private static final Log LOG = LogFactory.getLog(MoviePlayer.class);
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

	
	private final at.ac.tuwien.e0525580.omov.bo.Movie movie;
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
	
	
	// TODO JComboBox, wo man auch alle anderen movie files auswaehlen kann

	// FIXME retain video size aspect when is in fullscreen mode
	
	public MoviePlayer(at.ac.tuwien.e0525580.omov.bo.Movie movie, File movieFile, JFrame owner) throws QTException {
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
		
		this.getContentPane().add(this.initComponents());
		this.setBackground(Color.BLACK);
		this.pack();
		GuiUtil.setCenterLocation(this);
	}
	
	private JPanel initComponents() throws QTException {
		final JComponent content = this.initQuicktimePlayer(movieFile);
		
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setOpaque(false);
		panel.setBorder(BorderFactory.createEmptyBorder(4, 10, 10, 10));
		panel.add(this.newNorthPanel(), BorderLayout.NORTH);
		panel.add(content, BorderLayout.CENTER);
		panel.add(this.initSouthPanel(), BorderLayout.SOUTH);
		
		return panel;
	}

	private JPanel newNorthPanel() {
		final JPanel panel = new JPanel(new BorderLayout(0, 0));
		panel.setBackground(Color.BLACK);
		panel.setBorder(BorderFactory.createEmptyBorder(0, 0, 4, 0)); // margin-bottom
		
		final JLabel windowTitle = new JLabel(this.movie.getTitle() + " - " + this.movieFile.getName());
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
			this.doFullscreen();
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
	
	private void doFullscreen() {
		assert(this.graphicsDevice != null);
		this.isFullScreenMode = !this.isFullScreenMode;
		
		if(this.isFullScreenMode == true) {
			this.previousLocation = this.getLocation();
			this.previousSize = this.getSize();
		}
		
		this.graphicsDevice.setFullScreenWindow(this.isFullScreenMode ? MoviePlayer.this : null);
		
		if(this.isFullScreenMode == false) {
			this.setLocation(this.previousLocation);
			this.setSize(this.previousSize);
		}

		this.southPanel.setBackground(isFullScreenMode ? Color.BLACK : Constants.getColorWindowBackground());
		
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
	*/
}
