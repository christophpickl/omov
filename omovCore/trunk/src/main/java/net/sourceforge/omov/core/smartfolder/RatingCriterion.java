package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;

public class RatingCriterion extends AbstractColumnCriterion<RatingMatch> {

    public static RatingCriterion newRating(RatingMatch match) {
        return new RatingCriterion(match, MovieField.RATING);
    }
    
    private RatingCriterion(RatingMatch match, MovieField field) {
        super(match, field);
    }
}
