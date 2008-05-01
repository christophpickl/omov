package net.sourceforge.omov.core.gui.smartfolder.fields;

import java.awt.Color;

import javax.swing.JLabel;

import net.sourceforge.omov.core.gui.comp.rating.RatingSlider;

public class RatingRangeField extends AbstractCriterionField {

    private static final long serialVersionUID = 7660102104895779619L;

    private final RatingSlider ratingFieldFrom;
    private final RatingSlider ratingFieldTo;
    
    RatingRangeField(int initRatingFrom, int initRatingTo) {
        this.ratingFieldFrom = new RatingSlider(initRatingFrom, null, Color.WHITE);
        this.ratingFieldTo = new RatingSlider(initRatingTo, null, Color.WHITE);
        
        this.add(this.ratingFieldFrom);
        this.add(new JLabel(" to "));
        this.add(this.ratingFieldTo);
    }

    @Override
    public Object[] getValues() {
        return new Integer[] { this.ratingFieldFrom.getRating(),
                               this.ratingFieldTo.getRating()};
    }
    
}
