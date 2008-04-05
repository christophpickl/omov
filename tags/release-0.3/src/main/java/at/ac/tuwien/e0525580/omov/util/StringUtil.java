package at.ac.tuwien.e0525580.omov.util;

import java.io.File;

public class StringUtil {

//    private static final Log LOG = LogFactory.getLog(StringUtil.class);
    
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
    
//    private static final NeedlemanWunch NEEDLEMAN_WUNCH = new NeedlemanWunch();
//    /**
//     * @see StringSimilarity#needleman()
//     */
//    public static float similarity(String s1, String s2) {
//        final float similarity = NEEDLEMAN_WUNCH.getSimilarity(s1, s2);
//        LOG.debug("returning similarity of '"+similarity+"' for string s1 '"+s1+"' and s2 '"+s2+"'.");
//        return similarity;
//    }
}
