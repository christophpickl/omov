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


package net.sourceforge.omov.app.gui.comp.rating;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;

import javax.swing.BoundedRangeModel;
import javax.swing.JComponent;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.SliderUI;

/** The <code>ShapeSliderUI</code> class is built to present a series of
 * <code>Shape</code> objects. A primary shape is displayed if the position is
 * included in the set and a secondary shape is displayed if not. The secondary
 * shapes are only displayed if the control is enabled. You could any shape you
 * want, the default primary shape is a star and the secondary shape is a small
 * dot.
 *
 * For input, it assumes a click on the rightmost two-thirds of a star is a
 * click on that shape, otherwise it is the previous position. This allows the
 * user to select zero shapes. The widget also responds to dragging the mouse
 * in the control area. When the control is focused, the left and right arrow
 * buttons can change the value.
 *
 * @author Adam Walker <adam@walksoftware.net>
 */
class ShapeSliderUI extends SliderUI{

//    private static final Log LOG = LogFactory.getLog(ShapeSliderUI.class);
    private static final Color COLOR_PRIMARY = new Color(127, 127, 127);
    private static final Color COLOR_SECONDARY = new Color(204, 204, 204);

    private Shape primaryShape;
    private Shape secondaryShape;
    private Color primaryColor = null;
    private Color secondaryColor = null;
    private int margin = 1;
    private MouseInputListener mouseListener;
    private ChangeListener changeListener;
    private KeyListener keyListener;

    public ShapeSliderUI() {
        /* nothing to do */
    }

    public static ComponentUI createUI(JComponent c){
        return new ShapeSliderUI();
    }

    protected class ShapeML extends MouseInputAdapter{
        @Override
		public void mouseClicked(MouseEvent evt){
            select((JSlider)evt.getSource(), evt.getX(), evt.getY());
        }
        @Override
		public void mouseDragged(MouseEvent evt){
            select((JSlider)evt.getSource(), evt.getX(), evt.getY());
        }
    }
    protected class ShapeCL implements ChangeListener{
        public void stateChanged(ChangeEvent e){
            changed(e);
        }
    }
    protected class ShapeKL extends KeyAdapter{
        @Override
		public void keyPressed(KeyEvent evt){
            pressed(evt);
        }
    }
    protected void pressed(KeyEvent evt){
        JSlider slider = (JSlider)evt.getSource();
        BoundedRangeModel model = slider.getModel();
        int pos = model.getValue();
        int end = pos;

        if(evt.getKeyCode()==KeyEvent.VK_RIGHT){
            if(pos<model.getMaximum())
                end++;
        }else if(evt.getKeyCode()==KeyEvent.VK_LEFT){
            if(pos>model.getMinimum())
                end--;
        }
        if(end!=pos){
            model.setValue(end);
        }
    }
    protected void changed(ChangeEvent evt){
        if(evt.getSource() instanceof JComponent){
            JComponent c = (JComponent)evt.getSource();
            c.repaint();
        }
    }
    @SuppressWarnings("unused")
    protected void select(JSlider slider, int x, int y){
        slider.requestFocus();
        if(slider.isEnabled()){
            int slot = 0;
            int size = getSize(slider);
            int treshold = size/3;
            if(x>0){
                slot = x / size;
                if(x%size>=treshold){
                    slot++;
                }
            }
            BoundedRangeModel model = slider.getModel();
            if(model.getValue()!=slot){
                model.setValue(slot);
            }
        }
    }
    @Override
	public void uninstallUI(JComponent c){
        super.uninstallUI(c);
        c.removeMouseListener(this.mouseListener);
        c.removeMouseMotionListener(this.mouseListener);
        JSlider slider = (JSlider)c;
        slider.removeChangeListener(this.changeListener);
        slider.removeKeyListener(this.keyListener);
    }
    @Override
	public void installUI(JComponent c){
        super.installUI(c);
        c.addMouseListener(this.mouseListener = new ShapeML());
        c.addMouseMotionListener(this.mouseListener);
        JSlider slider = (JSlider)c;
        slider.addChangeListener(this.changeListener = new ShapeCL());
        slider.addKeyListener(this.keyListener = new ShapeKL());
        installColors();
    }
    public void installColors(){
        if(this.primaryShape==null){
        	this.primaryShape = makeStar();
        }
        if(this.secondaryShape==null){
        	this.secondaryShape = new Ellipse2D.Double(40,40,20,20);
        }
        if(this.primaryColor==null)
        	this.primaryColor = COLOR_PRIMARY;
        if(this.secondaryColor==null)
        	this.secondaryColor = COLOR_SECONDARY;

    }
    /**
     * Build the star shape. Five armed radial symmetric.
     **/
    protected Shape makeStar(){
        Polygon p = new Polygon();
        int outer = 50;
        int inner = 20;
        for(int deg=0; deg<360;){
            addPolarPoint(p, deg, outer);
            deg += 36;
            addPolarPoint(p, deg, inner);
            deg += 36;
        }
        return p;
    }
    /**
     * Add a point to a polygon using polar co-ordinates.
     * Assumes a square area between 0 and 100.
     **/
    private void addPolarPoint(Polygon p, double deg, double mag){
        double r = (deg/180.0) * Math.PI;
        double x = (Math.sin(r)*mag)+50;
        double y = (Math.cos(r)*-mag)+50;
        p.addPoint((int)x,(int)y);
    }
    @Override
	public Dimension getPreferredSize(JComponent c){
        JSlider slider = (JSlider)c;
        int size = 16;
        return new Dimension(
                size*slider.getMaximum()+(2*this.margin),
                size+2*this.margin);
    }
    @Override
	public Dimension getMinimumSize(JComponent c){
        JSlider slider = (JSlider)c;
        int size = 10;
        return new Dimension(
                size*slider.getMaximum()+(2*this.margin),
                size+2*margin);
    }
    @Override
	public Dimension getMaximumSize(JComponent c){
        JSlider slider = (JSlider)c;
        int size = 200;
        return new Dimension(
                size*slider.getMaximum()+(2*this.margin),
                size+2*margin);
    }
    protected int getSize(JSlider slider){
        int size = (slider.getWidth()-(2*this.margin))/slider.getMaximum();
        int alt = slider.getHeight()-(2*this.margin);
        if(alt<size){
            size = alt;
        }
        return size;
    }
    @Override
	public void paint(Graphics g1, JComponent c){
        Graphics2D g = (Graphics2D)g1;
        JSlider slider = (JSlider)c;
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);

        BoundedRangeModel model = slider.getModel();
        int slots = model.getMaximum();
        int setting = model.getValue();
        double size = getSize(slider);
        double z = size/100.0;
        double iz = 1.0/z;
        g.translate(margin,margin);
        boolean enabled = slider.isEnabled();
        for(int i=0;i<slots;i++){
            g.scale(z,z);
            paintShape(g, i<setting?1.0:0.0, enabled);
            g.scale(iz,iz);
            g.translate(size,0);
        }
    }
    /**
     * Paint a shape. All translation and scaling are already taken care of.
     **/
    @SuppressWarnings("unused")
    public void paintShape(Graphics2D g, double percent, boolean enabled){
        Shape s = secondaryShape;
        if(percent > 0.0){
            s = primaryShape;
//            g.setColor(enabled?primaryColor:secondaryColor);
            g.setColor(primaryColor);
        } else {
//            if(!enabled)
//                return;
            s = secondaryShape;
            g.setColor(secondaryColor);
        }
        g.fill(s);
    }

    public Shape getPrimaryShape() {
        return primaryShape;
    }
    /**
     * Shape should be centered in the rect (0,0,100,100).
     **/
    public void setPrimaryShape(Shape primaryShape) {
        this.primaryShape = primaryShape;
    }

    public Shape getSecondaryShape() {
        return secondaryShape;
    }

    /**
     * Shape should be centered in the rect (0,0,100,100).
     **/
    public void setSecondaryShape(Shape secondaryShape) {
        this.secondaryShape = secondaryShape;
    }

    public Color getPrimaryColor() {
        return primaryColor;
    }

    public void setPrimaryColor(Color primaryColor) {
        this.primaryColor = primaryColor;
    }

    public Color getSecondaryColor() {
        return secondaryColor;
    }

    public void setSecondaryColor(Color secondaryColor) {
        this.secondaryColor = secondaryColor;
    }

    public int getMargin() {
        return margin;
    }

    public void setMargin(int margin) {
        this.margin = margin;
    }
}
