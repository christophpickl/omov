package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;


public class DateCriterion extends AbstractColumnCriterion<DateMatch> {

    public static DateCriterion newDateAdded(DateMatch match) {
        return new DateCriterion(match, MovieField.DATE_ADDED);
    }
    
    private DateCriterion(DateMatch match, MovieField field) {
        super(match, field);
    }
}
