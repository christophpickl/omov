package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;


public class ResolutionCriterion extends AbstractColumnCriterion<ResolutionMatch> {

    public static ResolutionCriterion newResolution(ResolutionMatch match) {
        return new ResolutionCriterion(match, MovieField.RESOLUTION);
    }
    
    private ResolutionCriterion(ResolutionMatch match, MovieField field) {
        super(match, field);
    }

}
