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

import javax.swing.JSlider;



/**
 * rating from 0 to 5, 5=best
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class RatingSlider extends JSlider {
    
    private static final long serialVersionUID = -4967962025253623550L;

    
    public RatingSlider(final int rating) {
        this(rating, null, null);
    }

    public RatingSlider(final int rating, Color primaryColor) {
        this(rating, primaryColor, null);
    }
    public RatingSlider(final int rating, Color primaryColor, Color secondaryColor) {
        super(0, 5, rating);
        assert(rating >= 0 && rating <= 5) : "rating must be between 0-5 (was: "+rating+")!";

        this.setEnabled(true);
        this.setOpaque(false);
        
        ShapeSliderUI ui = new ShapeSliderUI();
        if(primaryColor != null) ui.setPrimaryColor(primaryColor);
        if(secondaryColor != null) ui.setSecondaryColor(secondaryColor);
        this.setUI(ui); // or with star-outterline: StrokedShapeSliderUI
    }
    
    public int getRating() {
        return this.getValue();
    }
    
    @Override
    public void setValue(int value) {
        assert(value >= 0 && value <= 5) : "rating must be between 0-5 (was: "+value+")!";
        super.setValue(value);
    }
    
//    public void setPrimaryColor(Color color) {
//        ShapeSliderUI ui = new ShapeSliderUI();
//        ui.setPrimaryColor(color);
//        this.setUI(ui);
//    }
}
