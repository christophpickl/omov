package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;


public class ResolutionCriterion extends AbstractColumnCriterion<ResolutionMatch> {

    public static ResolutionCriterion newResolution(ResolutionMatch match) {
        return new ResolutionCriterion(match, MovieField.RESOLUTION);
    }
    
    private ResolutionCriterion(ResolutionMatch match, MovieField field) {
        super(match, field);
    }

}
