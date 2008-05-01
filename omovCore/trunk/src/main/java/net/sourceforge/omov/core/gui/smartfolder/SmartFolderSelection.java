package net.sourceforge.omov.core.gui.smartfolder;

import net.sourceforge.omov.core.smartfolder.SmartFolder;

class SmartFolderSelection {
    
    public static final SmartFolderSelection ENUM_INACTIVE = new SmartFolderSelection(null, "-Inactive-");
    public static final SmartFolderSelection ENUM_MANAGE = new SmartFolderSelection(null, "Manage ...");
    
    private final SmartFolder smartFolder;
    private final String label;
    
    
    private SmartFolderSelection(SmartFolder smartFolder, String label) {
        this.smartFolder = smartFolder;
        this.label = label;
    }
    
    public SmartFolderSelection(SmartFolder smartFolder) {
        this.smartFolder = smartFolder;
        this.label = this.smartFolder.getName();
    }
    
    public SmartFolder getSmartFolder() {
        return this.smartFolder;
    }
    
    
    public String toString() {
        return this.label;
    }
}