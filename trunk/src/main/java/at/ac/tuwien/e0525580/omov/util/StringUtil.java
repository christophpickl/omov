package at.ac.tuwien.e0525580.omov.util;

import java.io.File;

public class StringUtil {
    private StringUtil() {
        // no instantiation
    }
    
    public static String asString(File... files) {
        if(files.length == 0) return "[]";
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        boolean first = true;
        for (File file : files) {
            if(!first) sb.append(", ");
            sb.append(file.getAbsolutePath());
        }
        sb.append("]");
        return sb.toString();
    }
    
    public static String escapeLineFeeds(String withLineFeeds) {
        return withLineFeeds.replaceAll("\n", "\\\\n");
    }
}
