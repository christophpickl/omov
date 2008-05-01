package at.ac.tuwien.e0525580.omov.smartfolder;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;

public class FileSizeCriterion extends AbstractColumnCriterion<FileSizeMatch> {

    public static FileSizeCriterion newFileSize(FileSizeMatch match) {
        return new FileSizeCriterion(match, MovieField.FILE_SIZE_KB);
    }
    
    private FileSizeCriterion(FileSizeMatch match, MovieField field) {
        super(match, field);
    }
}
