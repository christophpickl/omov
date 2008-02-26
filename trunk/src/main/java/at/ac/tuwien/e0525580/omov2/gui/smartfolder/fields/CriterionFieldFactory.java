package at.ac.tuwien.e0525580.omov2.gui.smartfolder.fields;

import java.util.Arrays;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.bo.movie.Resolution;
import at.ac.tuwien.e0525580.omov2.bo.movie.Movie.MovieField;
import at.ac.tuwien.e0525580.omov2.smartfolder.AbstractColumnCriterion;
import at.ac.tuwien.e0525580.omov2.smartfolder.BoolCriterion;
import at.ac.tuwien.e0525580.omov2.smartfolder.BoolMatch;
import at.ac.tuwien.e0525580.omov2.smartfolder.DateCriterion;
import at.ac.tuwien.e0525580.omov2.smartfolder.DateMatch;
import at.ac.tuwien.e0525580.omov2.smartfolder.NumberCriterion;
import at.ac.tuwien.e0525580.omov2.smartfolder.NumberMatch;
import at.ac.tuwien.e0525580.omov2.smartfolder.ResolutionCriterion;
import at.ac.tuwien.e0525580.omov2.smartfolder.ResolutionMatch;
import at.ac.tuwien.e0525580.omov2.smartfolder.SmartFolderUtil;
import at.ac.tuwien.e0525580.omov2.smartfolder.TextCriterion;
import at.ac.tuwien.e0525580.omov2.smartfolder.TextMatch;
import at.ac.tuwien.e0525580.omov2.util.DateUtil;

public class CriterionFieldFactory {

    private static final Log LOG = LogFactory.getLog(CriterionFieldFactory.class);

    

    public static AbstractColumnCriterion newCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        LOG.info("creating new AbstractColumnCriterion by column '"+columnLabel+"', match '"+matchLabel+"' and values '"+Arrays.toString(values)+"'.");

        if(SmartFolderUtil.isBoolColumnLabel(columnLabel)) {
            return newBoolCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isDateColumnLabel(columnLabel)) {
            return newDateCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isNumberColumnLabel(columnLabel)) {
            return newNumberCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isResolutionColumnLabel(columnLabel)) {
            return newResolutionCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isTextColumnLabel(columnLabel)) {
            return newTextCriterion(columnLabel, matchLabel, values);
        }
        
        throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
    }
    
    private static BoolCriterion newBoolCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Boolean value = (Boolean) values[0];
        final BoolMatch match;
        
        if(matchLabel.equals(BoolMatch.LABEL_IS)) {
            match = BoolMatch.newEquals(value);
        } else if(matchLabel.equals(BoolMatch.LABEL_IS)) {
            match = BoolMatch.newNotEquals(value);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final BoolCriterion criterion;
        if(columnLabel.equals(MovieField.SEEN.label())) {
            criterion = BoolCriterion.newSeen(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static DateCriterion newDateCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final DateMatch match;
        
        if(matchLabel.equals(DateMatch.LABEL_EQUALS)) {
            final Date value = (Date) values[0];
            match = DateMatch.newEquals(value);
        } else if(matchLabel.equals(DateMatch.LABEL_NOT_EQUALS)) {
            final Date value = (Date) values[0];
            match = DateMatch.newNotEquals(value);
        } else if(matchLabel.equals(DateMatch.LABEL_AFTER)) {
            final Date value = (Date) values[0];
            match = DateMatch.newAfter(value);
        } else if(matchLabel.equals(DateMatch.LABEL_BEFORE)) {
            final Date value = (Date) values[0];
            match = DateMatch.newBefore(value);
        } else if(matchLabel.equals(DateMatch.LABEL_IN_RANGE)) {
            final Date lowerBound = (Date) values[0];
            final Date upperBound = (Date) values[1];
            match = DateMatch.newInRange(lowerBound, upperBound);
        } else if(matchLabel.equals(DateMatch.LABEL_IN_THE_LAST)) {
            final Integer days = (Integer) values[0];
            match = DateMatch.newInTheLast(days);
        } else if(matchLabel.equals(DateMatch.LABEL_NOT_IN_THE_LAST)) {
            final Integer days = (Integer) values[0];
            match = DateMatch.newNotInTheLast(days);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final DateCriterion criterion;
        if(columnLabel.equals(MovieField.DATE_ADDED.label())) {
            criterion = DateCriterion.newDateAdded(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static NumberCriterion newNumberCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Integer value = (Integer) values[0];
        final Integer value2 = (values.length > 0) ? (Integer) values[1] : null;
        final NumberMatch match;
        
        if(matchLabel.equals(NumberMatch.LABEL_EQUALS)) {
            match = NumberMatch.newEquals(value);
        } else if(matchLabel.equals(NumberMatch.LABEL_NOT_EQUALS)) {
            match = NumberMatch.newNotEquals(value);
        } else if(matchLabel.equals(NumberMatch.LABEL_GREATER)) {
            match = NumberMatch.newGreater(value);
        } else if(matchLabel.equals(NumberMatch.LABEL_LESS)) {
            match = NumberMatch.newLess(value);
        } else if(matchLabel.equals(NumberMatch.LABEL_IN_THE_RANGE)) {
            match = NumberMatch.newInRange(value, value2);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final NumberCriterion criterion;
        if(columnLabel.equals(MovieField.YEAR.label())) {
            criterion = NumberCriterion.newYear(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static ResolutionCriterion newResolutionCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Resolution value = (Resolution) values[0];
        final ResolutionMatch match;
        
        if(matchLabel.equals(ResolutionMatch.LABEL_EQUALS)) {
            match = ResolutionMatch.newEquals(value);
        } else if(matchLabel.equals(ResolutionMatch.LABEL_GREATER)) {
            match = ResolutionMatch.newGreater(value);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final ResolutionCriterion criterion;
        if(columnLabel.equals(MovieField.RESOLUTION.label())) {
            criterion = ResolutionCriterion.newResolution(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static TextCriterion newTextCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final String value = (String) values[0];
        final TextMatch match;
        
        if(matchLabel.equals(TextMatch.LABEL_EQUALS)) {
            match = TextMatch.newEquals(value);
        } else if(matchLabel.equals(TextMatch.LABEL_NOT_EQUALS)) {
            match = TextMatch.newNotEquals(value);
        } else if(matchLabel.equals(TextMatch.LABEL_CONTAINS)) {
            match = TextMatch.newContains(value);
        } else if(matchLabel.equals(TextMatch.LABEL_NOT_CONTAINS)) {
            match = TextMatch.newNotContains(value);
        } else if(matchLabel.equals(TextMatch.LABEL_STARTS_WITH)) {
            match = TextMatch.newStartsWith(value);
        } else if(matchLabel.equals(TextMatch.LABEL_ENDS_WITH)) {
            match = TextMatch.newEndsWith(value);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final TextCriterion criterion;
        if(columnLabel.equals(MovieField.TITLE.label())) {
            criterion = TextCriterion.newTitle(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    /**
     * @param columnLabel
     * @param matchLabel
     * @param values is null, if default values should be used
     */
    public static AbstractCriterionField newField(final String columnLabel, final String matchLabel, final Object[] values) {
        LOG.info("creating new AbstractCriterionField by column '"+columnLabel+"', match '"+matchLabel+"' and values '"+(values==null?"null":Arrays.toString(values))+"'.");
        

        if(SmartFolderUtil.isBoolColumnLabel(columnLabel)) {
            return newBoolField(values);
        }
        
        if(SmartFolderUtil.isDateColumnLabel(columnLabel)) {
            return newDateField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isNumberColumnLabel(columnLabel)) {
            return newNumberField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isResolutionColumnLabel(columnLabel)) {
            return newResolutionField(columnLabel, values);
        }
        
        if(SmartFolderUtil.isTextColumnLabel(columnLabel)) {
            return newTextField(columnLabel, values);
        }
        
        throw new IllegalArgumentException("Could not get field by column '"+columnLabel+"' and match '"+matchLabel+"'!");
//        final String initValue = (values != null) ? (String) values[0] : "ups";
//        return new TextSingleField(20, initValue);
    }
    
    

    private static AbstractCriterionField newBoolField(final Object[] values) {
        final boolean initValue = (values != null) ? (Boolean) values[0] : false;
        return new BoolSingleField(initValue);
    }
    

    private static AbstractCriterionField newDateField(final String columnLabel, final String matchLabel, final Object[] values) {
        
        if(matchLabel.equals(DateMatch.LABEL_IN_THE_LAST) || matchLabel.equals(DateMatch.LABEL_NOT_IN_THE_LAST)) {

            // if(columnLabel.equals(DateCriterion.LABEL_DATE_ADDED)) ... not of interest
            int initValue = (values != null) ? (Integer) values[0] : 0;
            
            final RangeType rangeType;
            if(initValue == 0) {
                rangeType = RangeType.DAYS;
            } else if(initValue % 60 == 0) {
                rangeType = RangeType.MONTHS;
            } else if(initValue % 7 == 0) {
                rangeType = RangeType.WEEKS;
            } else {
                rangeType = RangeType.DAYS;
            }
            
            return new DateInLastField(initValue, rangeType);
            
        } else if(matchLabel.equals(DateMatch.LABEL_IN_RANGE)) {
            
            final Date initValueFrom;
            final Date initValueTo;
            
            if(values != null) {
                initValueFrom = (Date) values[0];
                initValueTo = (Date) values[1];
            } else {
                if(columnLabel.equals(MovieField.DATE_ADDED.label())) {
                    initValueFrom = DateUtil.getCurrentDateWithoutTimeAndSubtractedDays(1);
                    initValueTo = DateUtil.getCurrentDateWithoutTimeAndSubtractedDays(0);
                } else {
                    throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
                }
            }
            
            return new DateRangeField(initValueFrom, initValueTo, 5);
            
        } else {
            final Date initValue;
            if(columnLabel.equals(MovieField.DATE_ADDED.label())) {
                initValue = (values != null) ? (Date) values[0] : new Date();
            } else {
                throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
            }
            return new DateSingleField(initValue, TEXTFIELD_COLUMN_SIZE);
            
        }
    }
    
    
    private static AbstractCriterionField newNumberField(final String columnLabel, final String matchLabel, final Object[] values) {
        final int size; // displayed width of gui-widget (in columns)
        final int minValue;
        final int maxValue;
        
        if(columnLabel.equals(MovieField.YEAR.label())) {
            minValue = 0;
            maxValue = 9999;
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        final AbstractCriterionField field;
        if(matchLabel.equals(NumberMatch.LABEL_IN_THE_RANGE)) {
            size = 5;
            
            final int initValueFrom;
            final int initValueTo;
            
            if(columnLabel.equals(MovieField.YEAR.label())) {
                initValueFrom = (values != null) ? (Integer) values[0] : DateUtil.getCurrentYear() - 1;
                initValueTo   = (values != null) ? (Integer) values[1] : DateUtil.getCurrentYear(); 
            } else {
                throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
            }
            
            field = new NumberRangeField(size, initValueFrom, initValueTo, minValue, maxValue);
        } else {
            size = TEXTFIELD_COLUMN_SIZE;
            
            final int initValue;
            if(columnLabel.equals(MovieField.YEAR.label())) {
                initValue = (values != null) ? (Integer) values[0] : DateUtil.getCurrentYear();
            } else {
                throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
            }
            field = new NumberSingleField(size, initValue, minValue, maxValue);
        }
        
        return field;
    }
    
    
    private static AbstractCriterionField newResolutionField(final String columnLabel, final Object[] values) {
        final Resolution initValue;
        if(columnLabel.equals(MovieField.RESOLUTION.label())) {
            initValue = (values != null) ? (Resolution) values[0] : Resolution.R800x600;
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"'");
        }
        return new ResolutionSingleField(initValue);
    }
    

    private static AbstractCriterionField newTextField(final String columnLabel,final Object[] values) {
        final int size = TEXTFIELD_COLUMN_SIZE; // displayed width of gui-widget (in columns)
        final String initValue;
        if(columnLabel.equals(MovieField.TITLE.label())) {
            initValue = (values != null) ? (String) values[0] : "";
        } else {
            throw new IllegalArgumentException("unhandled columnLabel: '"+columnLabel+"'");
        }
        
        return new TextSingleField(size, initValue);
    }
    
    private static final int TEXTFIELD_COLUMN_SIZE = 14;
}
