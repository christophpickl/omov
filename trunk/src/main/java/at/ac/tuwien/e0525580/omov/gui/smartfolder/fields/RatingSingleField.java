package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import java.awt.Color;

import at.ac.tuwien.e0525580.omov.gui.comp.rating.RatingField;

public class RatingSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 7660102104895779619L;
    
    private final RatingField ratingField;
    
    RatingSingleField(int initRating) {
        this.ratingField = new RatingField(initRating, null, Color.WHITE);
        this.add(this.ratingField);
    }

    @Override
    public Object[] getValues() {
        return new Integer[] { this.ratingField.getRating() };
    }
    
}
