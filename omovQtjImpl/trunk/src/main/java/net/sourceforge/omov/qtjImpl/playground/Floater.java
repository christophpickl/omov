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

package net.sourceforge.omov.qtjImpl.playground;

import java.awt.Color;
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

import net.sourceforge.omov.core.util.SimpleGuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Floater extends JPanel implements MouseMotionListener, MouseListener {

	private static final long serialVersionUID = -3470793850796585903L;
	public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                createAndShowGUI();
            }
        });
        
//        try {
//			Thread.sleep(1000*4);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		p1.setOpaque(false);
//		p1.setOpaque(true);
//		int A = 10;
//		p1.setBackground(new Color(255, 0, 0, A));
//		p1.invalidate();
//		p1.repaint();
		
		
		
    }
    private static JPanel p1;
    private static void createAndShowGUI() {

    	JFrame win = new JFrame(); // {
//    		@Override
//    	    public boolean isShowing() {
//    	    	return true;
//    	    }
//    	};
    	win.setFocusableWindowState(true);
    	
    	Floater f = new Floater(win);
    	win.addMouseMotionListener(f);
    	
    	
    	JPanel p = new JPanel();
    	p.setBackground(Color.GREEN);
    	p.add(new JLabel("<html>bla<br>blubasdf asdf asfd asdf sfds dfs fd<br><br>das ist <b>GlassPane</b>bla<br>blub<br><br>das ist <b>GlassPane</b></html>"));
    	
    	p1 = new JPanel();
    	p1.add(new JLabel("xyz COLOR (alpha = 122)"));
    	p1.setBackground(new Color(255, 0, 0, 122));
    	p.add(p1);
        win.getContentPane().add(p);
//        win.setSize(400, 400);
//        win.setLocation(400, 400);
//        win.setGlassPane(new Floater());
        JPanel glassPane = (JPanel) win.getGlassPane();
        glassPane.add(f);
        glassPane.setOpaque(false);
        glassPane.setVisible(true);
        
        win.setUndecorated(true);
        win.pack();
        win.setVisible(true);
        
        System.out.println("tut");
//        GraphicsDevice display = GraphicsEnvironment.getLocalGraphicsEnvironment().getScreenDevices()[0];
//        display.setFullScreenWindow(win);
        
        
        
//        JFrame f = new JFrame();
//        f.getContentPane().add(new Floater(f));
////        f.setAlwaysOnTop(true);
//        f.setSize(400, 400);
//        f.setLocation(400, 400);
//        f.setVisible(true);
	}
	
	
	
    private static final int MAX_LIFETIME = 5; // 10
	private int lifetime = 0;
	private Opacity opacity = new Opacity(this);
	
	private final JFrame frame;
	
	public Floater(JFrame frame) {
//		this.setSize(80, 80);
		this.frame = frame;
		this.addMouseListener(this);
		this.initComponents();
		this.setOpaque(true);
		this.opacityChanged(this.opacity.getValue());
		
		
	}
	
	private final JLabel lbl = new JLabel("floater says:");
	private final JButton btnPlayPause = new JButton("Play");
	private void initComponents() {
		btnPlayPause.setBorderPainted(false);
		SimpleGuiUtil.enableHandCursor(btnPlayPause);
		btnPlayPause.setForeground(OPAC_COLOR_FRONT);
		btnPlayPause.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("play/pause");
			}
		});
		
		
		this.lbl.setForeground(OPAC_COLOR_FRONT);
		this.add(lbl);
		this.add(btnPlayPause);
	}

//    protected void paintComponent(Graphics g) {
//        g.setColor(Color.red);
////        g.fillOval(00, 20, 30, 40);
//        g.drawLine(6, 20, 60, 20);
//        g.drawString("xxxxxxx", 0, 0);
//    }
    
	public void mouseDragged(MouseEvent e) {
		System.out.println("mouseDragged");
		
	}

	private static class Opacity {
		private int value = 0;
		private final Floater listener;
		public Opacity(Floater listener) {
			this.listener = listener;
		}
		
//		public synchronized void setValue(int value) {
//			this.value = value;
//		}
		public synchronized int getValue() {
			return this.value;
		}
		public synchronized void decrease() {
			this.value -= 10;
			listener.opacityChanged(this.value);
		}
		/** sets to MAX */
		public synchronized void reset() {
			this.value = 100;
			listener.opacityChanged(this.value);
		}
		public String toString() {
			return "" + value;
		}
		public synchronized boolean isMinValue() {
			return this.value == 0;
		}
	}

	private static Color OPAC_COLOR_FRONT = Color.WHITE;
	private static Color OPAC_COLOR_BACK = Color.BLUE;
	
//	private TimerTask task;
//	private final Timer t = new Timer();

	private int oldOpacity = this.opacity.getValue();
	
	private static final Map<Color, Map<Integer, Color>> OPACITY_MAP = new HashMap<Color, Map<Integer, Color>>();
	static {
		Color[] cc = new Color[] { OPAC_COLOR_BACK, OPAC_COLOR_FRONT };
		for(Color c : cc) {
			Map<Integer, Color> m = new HashMap<Integer, Color>();
			for (int i = 0; i <= 10; i++) {
				int alpha = (int) (255 * (i / 10.));
				Color newC = new Color(c.getRed(), c.getGreen(), c.getBlue(), alpha);
				m.put(new Integer(i * 10), newC);
				System.out.println(i);
				
			}
			OPACITY_MAP.put(c, m);
		}
	}

	
	private void opacityChanged(int opacity) {
//		int newOpacity = (int) (255 * (opacity / 100.));
//		System.out.println("opacity changed: " + newOpacity + " ("+this.oldOpacity+"% -> "+opacity+"%)");
//		this.setBackground(new Color(255, 0, 0, newOpacity));
		
		if(OPACITY_MAP.get(OPAC_COLOR_BACK).get(opacity) == null) {
			System.err.println("!!! opacity=" + opacity + " !!!");
		}
		this.setBackground(OPACITY_MAP.get(OPAC_COLOR_BACK).get(opacity));
		
		final Color fg = OPACITY_MAP.get(OPAC_COLOR_FRONT).get(opacity);
		this.lbl.setForeground(fg);
		this.btnPlayPause.setForeground(fg);
		
		this.frame.invalidate();
		this.frame.repaint();
		
		this.oldOpacity = opacity;
	}
	
	private MyThread thread;
	
	private class MyThread extends Thread {
		public MyThread() {
			System.out.println("new MyThread()");
		}
		private boolean isShouldStop = false;
		public void shouldStop() {
			this.isShouldStop = true;
		}
		public void run() {
			while(isShouldStop == false && (lifetime > 0 || opacity.isMinValue() == false)) {
				System.out.println("thread: lifetime="+lifetime+"; opacity="+opacity);
				if(lifetime > 0) {
					lifetime--;
				} else {
					opacity.decrease();
				}
				try {
					Thread.sleep(200);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			System.out.println("thread DIES!");
		}
	}
	
    public void mouseMoved(MouseEvent e) {
    	if(this.isMouseEntered == true) return;
    	
		System.out.println("mouseMoved (lifetime=10; opacity=0; thread.state="+(thread == null ? "null" : thread.getState())+")");
		
		this.lifetime = MAX_LIFETIME;
		this.opacity.reset();
		
		if(thread == null || thread.getState() == State.TERMINATED) {
			thread = new MyThread();
			thread.start();
		}
		
	}
	

	public void mouseClicked(MouseEvent e) {
		System.out.println("mouseClicked -- lifetime="+lifetime+"; opacity="+opacity);
	}

	private boolean isMouseEntered = false;
	
	public void mouseEntered(MouseEvent e) {
		System.out.println("mouseEntered");
		this.isMouseEntered = true;
		if(this.thread != null) {
			this.thread.shouldStop();
		}
		this.opacity.reset();
	}

	public void mouseExited(MouseEvent e) {
		System.out.println("mouseExited");
		this.isMouseEntered = false;
	}

	public void mousePressed(MouseEvent e) {
		System.out.println("mousePressed");
		
	}

	public void mouseReleased(MouseEvent e) {
		System.out.println("mouseReleased");
		
	}
}
