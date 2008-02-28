package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;


public class BoolCriterion extends AbstractColumnCriterion<BoolMatch> {

    public static BoolCriterion newSeen(BoolMatch match) {
        return new BoolCriterion(match, MovieField.SEEN);
    }
    
    private BoolCriterion(BoolMatch match, MovieField field) {
        super(match, field);
    }
}
