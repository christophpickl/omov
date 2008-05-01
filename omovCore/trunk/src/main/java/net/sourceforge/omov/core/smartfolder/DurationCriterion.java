package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;

public class DurationCriterion extends AbstractColumnCriterion<DurationMatch> {

    public static DurationCriterion newDuration(DurationMatch match) {
        return new DurationCriterion(match, MovieField.DURATION);
    }
    
    private DurationCriterion(DurationMatch match, MovieField field) {
        super(match, field);
    }
}
