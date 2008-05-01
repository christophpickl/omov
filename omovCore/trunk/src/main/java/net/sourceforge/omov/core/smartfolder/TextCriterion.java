package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;


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
    public static TextCriterion newFormat(TextMatch match) {
        return new TextCriterion(match, MovieField.FORMAT);
    }
    public static TextCriterion newComment(TextMatch match) {
        return new TextCriterion(match, MovieField.COMMENT);
    }
    public static TextCriterion newFolderPath(TextMatch match) {
        return new TextCriterion(match, MovieField.FOLDER_PATH);
    }
    
    private TextCriterion(TextMatch match, MovieField field) {
        super(match, field);
    }
}
