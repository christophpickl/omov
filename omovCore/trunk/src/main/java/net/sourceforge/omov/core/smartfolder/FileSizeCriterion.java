package net.sourceforge.omov.core.smartfolder;

import net.sourceforge.omov.core.bo.Movie.MovieField;

public class FileSizeCriterion extends AbstractColumnCriterion<FileSizeMatch> {

    public static FileSizeCriterion newFileSize(FileSizeMatch match) {
        return new FileSizeCriterion(match, MovieField.FILE_SIZE_KB);
    }
    
    private FileSizeCriterion(FileSizeMatch match, MovieField field) {
        super(match, field);
    }
}
