package at.ac.tuwien.e0525580.omov2.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 
 * Bool: Seen
 *   - is
 *   - is not
 * 
 * Date: Date Added
 *   - equals
 *   - not equals
 *   - is after
 *   - is before
 *   - is in range
 *   - is in the last
 * 
 * Number: Year
 *   - equals
 *   - not equals
 *   - greater
 *   - less
 *   - in the range
 * 
 * Resolution: Resolution
 *   - equals
 *   - greater
 * 
 * Text: Title
 *   - equals
 *   - not equals
 *   - contains
 *   - not contains
 *   - starts with
 *   - ends with
 */
public class SmartFolderUtil {
    
    private static final List<String> COLUMN_LABELS;
    static {
        final List<String> tmp = new ArrayList<String>();
        tmp.addAll(AbstractColumnCriterion.ALL_COLUMN_LABELS);
        Collections.sort(tmp, String.CASE_INSENSITIVE_ORDER);
        COLUMN_LABELS = Collections.unmodifiableList(tmp);
    }
    
    private static final Map<String, List<String>> MATCHES_LABEL;
    static {
        Map<String, List<String>> tmp = new HashMap<String, List<String>>();

        for (String columnLabel : BoolCriterion.BOOL_COLUMN_LABELS) {
            tmp.put(columnLabel, BoolMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : DateCriterion.DATE_COLUMN_LABELS) {
            tmp.put(columnLabel, DateMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : NumberCriterion.NUMBER_COLUMN_LABELS) {
            tmp.put(columnLabel, NumberMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : ResolutionCriterion.RESOLUTION_COLUMN_LABELS) {
            tmp.put(columnLabel, ResolutionMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : TextCriterion.TEXT_COLUMN_LABELS) {
            tmp.put(columnLabel, TextMatch.ALL_MATCH_LABELS);
        }
        
        MATCHES_LABEL = Collections.unmodifiableMap(tmp);
    }
    
    public static List<String> getAllColumnLabels() {
        return COLUMN_LABELS;
    }
    
    public static List<String> getMatchesLabelByColumnLabel(String columnLabel) {
        return MATCHES_LABEL.get(columnLabel);
    }
    
    public static boolean isBoolColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.BOOL_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isDateColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.DATE_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isNumberColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.NUMBER_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isResolutionColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.RESOLUTION_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isTextColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.TEXT_COLUMN_LABELS.contains(columnLabel);
    }
}





