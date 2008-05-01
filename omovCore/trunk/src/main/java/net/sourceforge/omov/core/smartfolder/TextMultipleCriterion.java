package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;

public class TextMultipleCriterion extends AbstractColumnCriterion<TextMultipleMatch> {

    public static TextMultipleCriterion newSubtitles(TextMultipleMatch match) {
        return new TextMultipleCriterion(match, MovieField.SUBTITLES);
    }
    
    public static TextMultipleCriterion newActors(TextMultipleMatch match) {
        return new TextMultipleCriterion(match, MovieField.ACTORS);
    }
    
    public static TextMultipleCriterion newGenres(TextMultipleMatch match) {
        return new TextMultipleCriterion(match, MovieField.GENRES);
    }
    
    public static TextMultipleCriterion newLanguages(TextMultipleMatch match) {
        return new TextMultipleCriterion(match, MovieField.LANGUAGES);
    }
    
    
    private TextMultipleCriterion(TextMultipleMatch match, MovieField field) {
        super(match, field);
    }
}
