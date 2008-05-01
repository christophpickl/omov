package net.sourceforge.omov.core.bo;

import java.util.Comparator;
import java.util.List;


public class RawScannedMovie extends MovieFolderInfo {

    private final String title;
    
//    private boolean selected = true;

    public static Comparator<RawScannedMovie> COMPARATOR = new Comparator<RawScannedMovie>() {
        public int compare(RawScannedMovie m1, RawScannedMovie m2) {
            return m1.title.compareTo(m2.title);
        }
    };
    
    public Movie toMovie() {
        return Movie.create(-1).title(title).seen(false).rating(0).coverFile("").
            year(0).comment("").
            fileSizeKb(this.getFileSizeKB()).folderPath(this.getFolderPath()).format(this.getFormat()).files(this.getFiles()).duration(0).get();
    }
    
    public String toString() {
        return "ScannedMovie[title="+title+"]";
    }
    
    public RawScannedMovie(String title, String folderPath, long fileSizeKb, String format, List<String> files) {
        super(folderPath, files, fileSizeKb, format);
        this.title = title;
    }
    
}
