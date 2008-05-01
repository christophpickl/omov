package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;


public class DateCriterion extends AbstractColumnCriterion<DateMatch> {

    public static DateCriterion newDateAdded(DateMatch match) {
        return new DateCriterion(match, MovieField.DATE_ADDED);
    }
    
    private DateCriterion(DateMatch match, MovieField field) {
        super(match, field);
    }
}
