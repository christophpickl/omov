package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import java.awt.Color;

import javax.swing.JLabel;

import at.ac.tuwien.e0525580.omov.gui.comp.rating.RatingField;

public class RatingRangeField extends AbstractCriterionField {

    private static final long serialVersionUID = 7660102104895779619L;

    private final RatingField ratingFieldFrom;
    private final RatingField ratingFieldTo;
    
    RatingRangeField(int initRatingFrom, int initRatingTo) {
        this.ratingFieldFrom = new RatingField(initRatingFrom, null, Color.WHITE);
        this.ratingFieldTo = new RatingField(initRatingTo, null, Color.WHITE);
        
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
