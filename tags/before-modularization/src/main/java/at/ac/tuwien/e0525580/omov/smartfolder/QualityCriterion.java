package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;

public class QualityCriterion extends AbstractColumnCriterion<QualityMatch> {

    public static QualityCriterion newQuality(QualityMatch match) {
        return new QualityCriterion(match, MovieField.QUALITY);
    }
    
    private QualityCriterion(QualityMatch match, MovieField field) {
        super(match, field);
    }
}
