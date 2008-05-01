package net.sourceforge.omov.core.bo;

import java.awt.Dimension;
import java.util.List;

import net.sourceforge.omov.core.util.CollectionUtil;

public enum CoverFileType {
    
    THUMBNAIL(40, 40), // use in HTML report
    NORMAL(120, 160);

    
    
    private final int maxWidth;
    
    private final int maxHeight;
    
    private final String filenamePart;
    
    private final String toString;
    
    
    private CoverFileType(int maxWidth, int maxHeight) {
        this.maxWidth = maxWidth;
        this.maxHeight = maxHeight;
        
        this.filenamePart = "-" + maxWidth + "x" + maxHeight;
        this.toString = this.name() + "[maxWidth="+maxWidth+";maxHeight="+maxHeight+"]";
    }
    
    public int getMaxHeight() {
        return this.maxHeight;
    }
    
    public int getMaxWidth() {
        return this.maxWidth;
    }
    
    public Dimension getDimension() {
        return new Dimension(this.maxWidth, this.maxHeight);
    }
    
    String getFilenamePart() {
        return this.filenamePart;
    }
    
    public String toString() {
        return toString;
    }

    public static List<CoverFileType> getAllTypes() {
        return new CollectionUtil<CoverFileType>().asImmutableList(THUMBNAIL, NORMAL);
    }
}
