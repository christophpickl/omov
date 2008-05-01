package net.sourceforge.omov.core.gui.comp.rating;

import java.awt.Color;

import javax.swing.JSlider;



/**
 * rating from 0 to 5, 5=best
 * 
 * @author Christoph Pickl - e0525580@student.tuwien.ac.at
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
