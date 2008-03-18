package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;

public class RatingCriterion extends AbstractColumnCriterion<RatingMatch> {

    public static RatingCriterion newRating(RatingMatch match) {
        return new RatingCriterion(match, MovieField.RATING);
    }
    
    private RatingCriterion(RatingMatch match, MovieField field) {
        super(match, field);
    }
}
