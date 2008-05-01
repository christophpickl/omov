package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;


public class NumberCriterion extends AbstractColumnCriterion<NumberMatch> {

    public static NumberCriterion newYear(NumberMatch match) {
        return new NumberCriterion(match, MovieField.YEAR);
    }
    
    private NumberCriterion(NumberMatch match, MovieField field) {
        super(match, field);
    }
}
