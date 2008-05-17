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
import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX;
import net.sourceforge.omov.qtjImpl.QtjVideoPlayerImplX.QtjState;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

	private final JLabel lbl = new JLabel("This is just a preview ...");
	private final JButton btnPlayPause = new JButton("Play");
	
	private final IQtjFloaterListener listener;
	
	
	
	public QtjFloater(JFrame frame, QtjVideoPlayerImplX player, IQtjFloaterListener listener) {
		this.frame = frame;
		this.player = player;
		this.listener = listener;
		this.addMouseListener(this);
		this.initComponents();
		this.setOpaque(true);
		this.opacityChanged(this.opacity.getValue());
		
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
		SimpleGuiUtil.enableHandCursor(btnPlayPause);
		btnPlayPause.setForeground(OPAC_COLOR_FRONT);
		btnPlayPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				listener.doPlayPause();
			}
		});
		
		this.lbl.setForeground(OPAC_COLOR_FRONT);
		this.add(lbl);
//		this.add(btnPlayPause);
	}

	void opacityChanged(int opacity) {
		final Color fg = OPACITY_MAP.get(OPAC_COLOR_FRONT).get(opacity);
		final Color bg = OPACITY_MAP.get(OPAC_COLOR_BACK).get(opacity);
		assert(fg != null && bg != null);
		
		this.setBackground(bg);
		this.lbl.setForeground(fg);
		this.btnPlayPause.setForeground(fg);
		
		this.frame.invalidate();
		this.frame.repaint();
	}
	
	public void setPlayPauseButton(QtjState state) {
		LOG.debug("setPlayPauseButton(state="+state+")");
		this.btnPlayPause.setText(state == QtjState.PLAY ? "Pause" : "Play" );
	}
	
	
	
	
	
    public void mouseMoved(MouseEvent e) {
    	if(this.isMouseEntered == true || this.player.isFullScreenMode() == false) return;
//		System.out.println("mouseMoved (lifetime=10; opacity=0; thread.state="+(thread == null ? "null" : thread.getState())+")");
		
		this.lifetime.reset();
		this.opacity.reset();
		
		if(thread == null || thread.getState() == State.TERMINATED) {
			LOG.debug("Creating new FadeOutThread.");
			thread = new FadeOutThread(this.lifetime, this.opacity);
			thread.start();
		}
	}

	public void mouseEntered(MouseEvent e) {
		if(this.player.isFullScreenMode() == false) return;
		
		LOG.debug("mouseEntered");
		this.isMouseEntered = true;
		if(this.thread != null) {
			this.thread.shouldStop();
		}
		this.opacity.reset();
	}

	public void mouseExited(MouseEvent e) {
		if(this.player.isFullScreenMode() == false) return;
		
		LOG.debug("mouseExited"); // FIXME auch mouseExited, wenn ueber Button innerhalb Floater hovered!
		this.isMouseEntered = false;
	}

	public void mouseClicked(MouseEvent e) { /* not used */ }
	public void mousePressed(MouseEvent e) { /* not used */ }
	public void mouseReleased(MouseEvent e) { /* not used */ }
	public void mouseDragged(MouseEvent e) { /* not used */ }

	
	
	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		LOG.info("actionPerformed(cmd="+cmd+")");
		
		if(cmd.equals(CMD_PLAY_PAUSE)) {
			this.listener.doPlayPause(); // FIXME funkt nicht
		} else {
			throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
		}
	}
	
}
