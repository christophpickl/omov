package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;


public class TextCriterion extends AbstractColumnCriterion<TextMatch> {

    public static TextCriterion newTitle(TextMatch match) {
        return new TextCriterion(match, MovieField.TITLE);
    }
    public static TextCriterion newStyle(TextMatch match) {
        return new TextCriterion(match, MovieField.STYLE);
    }
    public static TextCriterion newDirector(TextMatch match) {
        return new TextCriterion(match, MovieField.DIRECTOR);
    }
    public static TextCriterion newComment(TextMatch match) {
        return new TextCriterion(match, MovieField.COMMENT);
    }
    
    private TextCriterion(TextMatch match, MovieField field) {
        super(match, field);
    }
}
