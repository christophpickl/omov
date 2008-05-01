package at.ac.tuwien.e0525580.omov.help;

public enum HelpEntry {
    
    HOME("home"),
    SMARTFOLDER("smartfolder"),
    SMARTCOPY("smartcopy"),
    REPOSITORY_SCAN("repository_scan");
    
    private final String id;
    
    private HelpEntry(String id) {
        this.id = id;
    }
    
    public String getId() {
        return this.id;
    }
}
