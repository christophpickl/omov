package net.sourceforge.omov.qtjImpl.playground.controlled;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.SliderUI;

public class TimeSlider extends JSlider {
	
	private static final long serialVersionUID = -5914252785406790551L;

	public static void main(String[] args) {
		System.out.println("TimeSlider demo...");
		JFrame f = new JFrame();
		
		((JPanel) f.getContentPane()).setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
		f.getContentPane().add(new TimeSlider());
		
		f.pack();
//		f.setSize(500, 30);
		f.setVisible(true);
	}
	
	public TimeSlider() {
		super(0, 403);
//		System.out.println("class name" + this.getUI().getClass().getSuperclass().getName());
		
//		this.setUI(new XyzUI(this));
		this.setUI(new XyzUI());
	}
	
	class XyzUI extends SliderUI {
		// only for BasicSliderUI
//		public XyzUI(JSlider b) {
//			super(b);
//		}
		
//		private int margin = 1;
		
		public void paint(Graphics g1, JComponent c){
	        Graphics2D g = (Graphics2D)g1;
	        JSlider slider = (JSlider)c;
	        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
	                RenderingHints.VALUE_ANTIALIAS_ON);

	        BoundedRangeModel model = slider.getModel();
	        
	        
//	        double size = getSize(slider);
//	        double z = size/100.0;
//	        double iz = 1.0/z;
//	        g.translate(margin,margin);
//	        g.scale(z,z);
	        
	        g.draw(makeFrame(g));
	        
	        g.fillPolygon(makeKnob(g, slider));
//	        g.draw(makeKnob(g));
	        
//	        g.translate(size, 0.0);

//	        int slots = model.getMaximum();
//	        int setting = model.getValue();
//	        double size = getSize(slider);
//	        double z = size/100.0;
//	        double iz = 1.0/z;
//	        g.translate(margin,margin);
//	        boolean enabled = slider.isEnabled();
//	        for(int i=0;i<slots;i++){
//	            g.scale(z,z);
//	            paintShape(g, i<setting?1.0:0.0, enabled);
//	            g.scale(iz,iz);
//	            g.translate(size,0);
//	        }
	    }
		
		
		private final int margin = 1;
//	    public Dimension getPreferredSize(JComponent c){
//	        JSlider slider = (JSlider)c;
//	        int size = 16;
//	        return new Dimension(
//	                size*slider.getMaximum()+(2*margin),
//	                size+2*margin);
//	    }
//	    public Dimension getMinimumSize(JComponent c){
//	        JSlider slider = (JSlider)c;
//	        int size = 10;
//	        return new Dimension(
//	                size*slider.getMaximum()+(2*margin),
//	                size+2*margin);
//	    }
//	    public Dimension getMaximumSize(JComponent c){
//	        JSlider slider = (JSlider)c;
//	        int size = 200;
//	        return new Dimension(
//	        		size*slider.getMaximum()+(2*margin),
//	        		size+2*margin);
//	    }
//	    
//	    private int getSize(JSlider slider){
//	        int size = (slider.getWidth()-(2*margin))/slider.getMaximum();
//	        int alt = slider.getHeight()-(2*margin);
//	        if(alt<size){
//	            size = alt;
//	        }
//	        return size;
//	    }

	    private MouseInputListener mouseListener;
	    protected class ShapeML extends MouseInputAdapter{
	        public void mouseClicked(MouseEvent evt){
	            select((JSlider)evt.getSource(), evt.getX(), evt.getY());
	        }
	        public void mouseDragged(MouseEvent evt){
	            select((JSlider)evt.getSource(), evt.getX(), evt.getY());
	        }
	    }
	    protected void select(JSlider slider, int x, int y){
	        slider.requestFocus();
	        if(slider.isEnabled()){
//	            int slot = 0;
//	            int size = getSize(slider);
//	            int treshold = size/3;
//	            if(x>0){
//	                slot = x / size;
//	                if(x%size>=treshold){
//	                    slot++;
//	                }
//	            }
//	            BoundedRangeModel model = slider.getModel();
//	            if(model.getValue()!=slot){
//	                model.setValue(slot);
//	            }
	        	System.out.println("select slider x/y = " + x+"/"+y + " (setting value to x-11)");
	        	slider.getModel().setValue(x-11);
	        }
	    }

	    private KeyListener keyListener;
	    protected class ShapeKL extends KeyAdapter{
	        public void keyPressed(KeyEvent evt){
	            pressed(evt);
	        }
	    }
	    protected void pressed(KeyEvent evt){
	        JSlider slider = (JSlider)evt.getSource();
	        BoundedRangeModel model = slider.getModel();
	        int pos = model.getValue();
	        int end = pos;
	        System.out.println("pressed; pos="+pos+";");
//	        if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
//	            if(pos<model.getMaximum())
//	                end++;
//	        }else if(evt.getKeyCode()==KeyEvent.VK_LEFT){
//	            if(pos>model.getMinimum())
//	                end--;
//	        }
//	        if(end!=pos){
//	            model.setValue(end);
//	        }
	    }

	    private ChangeListener changeListener;
	    protected class ShapeCL implements ChangeListener{
	        public void stateChanged(ChangeEvent e){
	            changed(e);
	        }
	    }
	    protected void changed(ChangeEvent evt){
	        if(evt.getSource() instanceof JComponent){
	            JComponent c = (JComponent)evt.getSource();
	            c.repaint();
	        }
	    }
	    

	    public void installUI(JComponent c){
	    	System.out.println("installUI");
	        super.installUI(c);
	        c.addMouseListener(mouseListener = new ShapeML());
	        c.addMouseMotionListener(mouseListener);
	        JSlider slider = (JSlider)c;
	        
	        slider.addChangeListener(changeListener = new ShapeCL());
	        slider.addKeyListener(keyListener = new ShapeKL());
	        
	    }
	    public void uninstallUI(JComponent c){
	        super.uninstallUI(c);
	        c.removeMouseListener(mouseListener);
	        c.removeMouseMotionListener(mouseListener);
	        JSlider slider = (JSlider)c;
	        slider.removeChangeListener(changeListener);
	        slider.removeKeyListener(keyListener);
	    }
	    
		private Shape makeFrame(Graphics2D g) {
			g.setColor(Color.RED);
			
			Polygon p = new Polygon();
			final int width = 402 + 21; // 400 + margin (1+1) + knob
			final int height = 21;
			p.addPoint(0, 0);
			p.addPoint(width, 0);
			p.addPoint(width, height);
			p.addPoint(0, height);
			return p;
		}
		
		private Polygon makeKnob(Graphics2D g, JSlider slider) {
			System.out.println("makeKnob");
			g.setColor(Color.GREEN);
			
			Polygon p = new Polygon();
			int plusx = slider.getModel().getValue();
			p.addPoint( 1+plusx, 11);
			p.addPoint(11+plusx,  1);
			p.addPoint(21+plusx, 11);
			p.addPoint(11+plusx, 21);
			return p;
		}
//		private Shape makeStar(){
//	        Polygon p = new Polygon();
//	        int outer = 50;
//	        int inner = 20;
//	        for(int deg=0; deg<360;){
//	            addPolarPoint(p, deg, outer);
//	            deg += 36;
//	            addPolarPoint(p, deg, inner);
//	            deg += 36;
//	        }
//	        return p;
//	    }
//		private void addPolarPoint(Polygon p, double deg, double mag){
//	        double r = (deg/180.0) * Math.PI;
//	        double x = (Math.sin(r)*mag)+50;
//	        double y = (Math.cos(r)*-mag)+50;
//	        p.addPoint((int)x,(int)y);
//	    }
	}
}
