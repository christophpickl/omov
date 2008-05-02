package net.sourceforge.omov.core.gui.smartfolder.fields;

import java.awt.Color;

import net.sourceforge.omov.core.gui.comp.rating.RatingSlider;

public class RatingSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 7660102104895779619L;
    
    private final RatingSlider ratingField;
    
    RatingSingleField(int initRating) {
        this.ratingField = new RatingSlider(initRating, null, Color.WHITE);
        this.add(this.ratingField);
    }

    @Override
    public Object[] getValues() {
        return new Integer[] { this.ratingField.getRating() };
    }
    
}