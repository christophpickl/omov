package at.ac.tuwien.e0525580.omov.bo;

import java.util.Set;


public class MovieFolderInfo {
    
    private final String folderPath;
    
    /** filename (with extension) relative to folderPath */
    private final Set<String> files;
    
    private final long fileSizeKB;
    
    private final String format;
    
    public MovieFolderInfo(final String folderPath, final Set<String> files, final long fileSizeKb, final String format) {
        // assert files are all valid movieFiles
        this.folderPath = folderPath;
        this.files = files;
        this.fileSizeKB = fileSizeKb;
        this.format = format;
    }
    
    public Set<String> getFiles() {
        return this.files;
    }
    
    public String getFormat() {
        return this.format;
    }
    
    public String getFolderPath() {
        return this.folderPath;
    }
    
    public long getFileSizeKB() {
        return this.fileSizeKB;
    }
}
