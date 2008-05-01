package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;

public class QualityCriterion extends AbstractColumnCriterion<QualityMatch> {

    public static QualityCriterion newQuality(QualityMatch match) {
        return new QualityCriterion(match, MovieField.QUALITY);
    }
    
    private QualityCriterion(QualityMatch match, MovieField field) {
        super(match, field);
    }
}
