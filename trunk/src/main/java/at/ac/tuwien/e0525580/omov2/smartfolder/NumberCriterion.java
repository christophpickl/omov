package at.ac.tuwien.e0525580.omov2.smartfolder;

import at.ac.tuwien.e0525580.omov2.bo.movie.Movie.MovieField;


public class NumberCriterion extends AbstractColumnCriterion<NumberMatch> {

    public static NumberCriterion newYear(NumberMatch match) {
        return new NumberCriterion(match, MovieField.YEAR);
    }
    
    private NumberCriterion(NumberMatch match, MovieField field) {
        super(match, field);
    }
}
