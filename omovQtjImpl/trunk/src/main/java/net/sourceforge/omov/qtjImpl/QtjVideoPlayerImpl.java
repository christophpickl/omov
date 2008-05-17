package net.sourceforge.omov.qtjImpl;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JWindow;

import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.qtjApi.IQtjVideoPlayer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import quicktime.QTException;



public class QtjVideoPlayerImpl extends JWindow implements IQtjVideoPlayer, MouseListener, MouseMotionListener {

    private static final Log LOG = LogFactory.getLog(QtjVideoPlayerImpl.class);
	private static final long serialVersionUID = 9106609207036907447L;
	
	private final QtjVideoController controller;
	private final QtjSmallScreen smallScreen;
	private final QtjFullScreen fullScreen;

	private final JPanel wrapPanel = new JPanel(new BorderLayout());

	private Dimension previousSize;
	private Point previousLocation;

	
	private Point startDrag;
	private Point startLocation;
	
	
	
	public QtjVideoPlayerImpl(net.sourceforge.omov.core.bo.Movie movie, File movieFile, JFrame owner) throws QTException {
		this.controller = new QtjVideoController(this, movie, movieFile);
		this.controller.getQtjComponent().addMouseListener(this);
		this.controller.getQtjComponent().addMouseMotionListener(this);

//		this.smallScreen.setSize(400, 300);
//		this.setSize(440, 330);
		this.smallScreen = new QtjSmallScreen(this.controller);
		this.fullScreen = new QtjFullScreen(this.controller);
		

		this.addMouseListener(this);
		this.addMouseMotionListener(this);
		
		this.switchFullscreen(false);
//		this.wrapPanel.add(this.smallScreen, BorderLayout.CENTER);
		
//		this.getContentPane().setLayout(new FlowLayout(FlowLayout.CENTER));
		this.getContentPane().add(this.wrapPanel);
		this.pack();
//		this.validate();
		SimpleGuiUtil.setCenterLocation(this);

		this.setBackground(Color.BLACK);
	}
	
	public void switchFullscreen(final boolean fullscreen) {
		LOG.debug("Initializing components for fullscreen mode = "+fullscreen+".");
		
		if(fullscreen == true) {
			this.previousLocation = this.getLocation();
			this.previousSize = this.getSize();
		}
		
		this.wrapPanel.removeAll();
		this.wrapPanel.add(fullscreen ? this.fullScreen : this.smallScreen, BorderLayout.CENTER);
		this.wrapPanel.invalidate();
		this.wrapPanel.repaint();
		
		this.controller.getDisplay().setFullScreenWindow(fullscreen ? this : null);
		
		if(fullscreen == false && this.previousLocation != null) { // if not the first time, invoked by this constructor
			this.setLocation(this.previousLocation);
			this.setSize(this.previousSize);
		}
	}
	

    public void mousePressed(MouseEvent e) {
    	if(this.controller.isFullscreen() == true) return;
    	
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
    	if(this.controller.isFullscreen() == true) return;
    	
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
