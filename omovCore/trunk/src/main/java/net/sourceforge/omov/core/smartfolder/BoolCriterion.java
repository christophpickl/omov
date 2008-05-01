package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;


public class BoolCriterion extends AbstractColumnCriterion<BoolMatch> {

    public static BoolCriterion newSeen(BoolMatch match) {
        return new BoolCriterion(match, MovieField.SEEN);
    }
    
    
    private BoolCriterion(BoolMatch match, MovieField field) {
        super(match, field);
    }
}
