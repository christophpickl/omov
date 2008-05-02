package net.sourceforge.omov.core;

public enum Icon16x16 {

    NEW_MOVIE("new_movie.png"),
    HELP("help.png"),
    VLC("vlc.png"),
    PREFERENCES("preferences.png"),
    INFORMATION("information.png"),
    SCAN("scan.png"),
    DELETE("delete.png"),
    FETCH_METADATA("fetch_metadata.png"),
    IMPORT("import.png"),
    EXPORT("export.png"),
    REVEAL_FINDER("reveal_finder.png"),
    SEVERITY_INFO("severity_info.gif"),
    SEVERITY_WARNING("severity_warning.gif"),
    SEVERITY_ERROR("severity_error.gif");
    
    final String fileName;
    private Icon16x16(String fileName) {
        this.fileName = fileName;
    }
}
