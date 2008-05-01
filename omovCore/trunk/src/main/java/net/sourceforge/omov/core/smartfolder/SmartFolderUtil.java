package net.sourceforge.omov.core.smartfolder;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/*
 * 
Types
-----
B ... Boolean
N ... Number
D ... Date
T ... Text
TT ... Multiple Text
R ... Rating
Q ... Quality
F ... File Size
U ... Duration
E ... Resolution

Attributes
----------
T:title;
T:style;
T:director;
T:format;
T:comment;
TT:subtitles;
TT:actors;
TT:genres;
TT:languages;
B:seen;
B:coverFileSet;
B:folderPathSet;
N:filesCount;
N:year;
R:rating;
Q:quality;
D:dateAdded;
F:fileSize;
U:duration;
E:resolution;

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
        for (String columnLabel : NumberCriterion.NUMBER_COLUMN_LABELS) {
            tmp.put(columnLabel, NumberMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : DateCriterion.DATE_COLUMN_LABELS) {
            tmp.put(columnLabel, DateMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : TextCriterion.TEXT_COLUMN_LABELS) {
            tmp.put(columnLabel, TextMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : TextMultipleCriterion.TEXT_MULTIPLE_COLUMN_LABELS) {
            tmp.put(columnLabel, TextMultipleMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : RatingCriterion.RATING_COLUMN_LABELS) {
            tmp.put(columnLabel, RatingMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : QualityCriterion.QUALITY_COLUMN_LABELS) {
            tmp.put(columnLabel, QualityMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : FileSizeCriterion.FILE_SIZE_COLUMN_LABELS) {
            tmp.put(columnLabel, FileSizeMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : DurationCriterion.DURATION_COLUMN_LABELS) {
            tmp.put(columnLabel, DurationMatch.ALL_MATCH_LABELS);
        }
        for (String columnLabel : ResolutionCriterion.RESOLUTION_COLUMN_LABELS) {
            tmp.put(columnLabel, ResolutionMatch.ALL_MATCH_LABELS);
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
    
    public static boolean isNumberColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.NUMBER_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isDateColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.DATE_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isTextColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.TEXT_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isTextMultipleColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.TEXT_MULTIPLE_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isRatingColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.RATING_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isQualityColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.QUALITY_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isFileSizeColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.FILE_SIZE_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isDurationColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.DURATION_COLUMN_LABELS.contains(columnLabel);
    }
    
    public static boolean isResolutionColumnLabel(String columnLabel) {
        return AbstractColumnCriterion.RESOLUTION_COLUMN_LABELS.contains(columnLabel);
    }
}





