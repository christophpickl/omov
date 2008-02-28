package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;


public class TextCriterion extends AbstractColumnCriterion<TextMatch> {

    public static TextCriterion newTitle(TextMatch match) {
        return new TextCriterion(match, MovieField.TITLE);
    }
    
    private TextCriterion(TextMatch match, MovieField field) {
        super(match, field);
    }
}
