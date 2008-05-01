package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;

public class DurationCriterion extends AbstractColumnCriterion<DurationMatch> {

    public static DurationCriterion newDuration(DurationMatch match) {
        return new DurationCriterion(match, MovieField.DURATION);
    }
    
    private DurationCriterion(DurationMatch match, MovieField field) {
        super(match, field);
    }
}
