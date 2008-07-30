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

import java.awt.Graphics2D;
import java.awt.Shape;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

/** I also made a <code>StrokeShapeSliderUI</code> which is similar but only 
 * uses one shape. It uses filled and not filled shapes to denote the value, 
 * much like the Netflix website.  However to build a replacement for their 
 * control would require creating a new model class and adding some more 
 * methods to a subclass of <code>JSlider</code>. This because the Netflix 
 * widget also displays the type of rating (User, System Average, and Friends 
 * Rating) by the color used to fill the shape. It also has an optional button 
 * to indicate no interest.
 *
 * @author Adam Walker <adam@walksoftware.net>
 * copyright: {@link http://www.walkersoftware.net/2005/06/11/fun-with-jslider-itunes-and-netflix-style/}
 */
class StrokedShapeSliderUI extends ShapeSliderUI{
    
    public StrokedShapeSliderUI(){
        // nothing to do
    }
    
    public static ComponentUI createUI(JComponent c){
        return new StrokedShapeSliderUI();
    }
    
    @Override
	public void paintShape(Graphics2D g, double percent, boolean enabled){
        Shape s = getPrimaryShape();
        if(percent>0.0){
            g.setColor(enabled ? getPrimaryColor() : getSecondaryColor());
            g.fill(s);
            g.setColor(getSecondaryColor());
            g.draw(s);
        }else{
            if(!enabled)
                return;
            g.setColor(getSecondaryColor());
            g.draw(s);
        }
        
    }
}
