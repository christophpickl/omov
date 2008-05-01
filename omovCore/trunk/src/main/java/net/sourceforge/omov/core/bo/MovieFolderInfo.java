package net.sourceforge.omov.core.bo;

import java.util.List;


public class MovieFolderInfo {
    
    private final String folderPath;
    
    /** filename (with extension) relative to folderPath */
    private final List<String> files;
    
    private final long fileSizeKB;
    
    private final String format;
    
    public MovieFolderInfo(final String folderPath, final List<String> files, final long fileSizeKb, final String format) {
        // assert files are all valid movieFiles
        this.folderPath = folderPath;
        this.files = files;
        this.fileSizeKB = fileSizeKb;
        this.format = format;
    }
    
    public List<String> getFiles() {
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
