/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.app.gui.smartfolder.fields;

import java.util.Arrays;
import java.util.Date;

import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.smartfolder.AbstractColumnCriterion;
import net.sourceforge.omov.core.smartfolder.BoolCriterion;
import net.sourceforge.omov.core.smartfolder.BoolMatch;
import net.sourceforge.omov.core.smartfolder.DateCriterion;
import net.sourceforge.omov.core.smartfolder.DateMatch;
import net.sourceforge.omov.core.smartfolder.DurationCriterion;
import net.sourceforge.omov.core.smartfolder.DurationMatch;
import net.sourceforge.omov.core.smartfolder.FileSizeCriterion;
import net.sourceforge.omov.core.smartfolder.FileSizeMatch;
import net.sourceforge.omov.core.smartfolder.NumberCriterion;
import net.sourceforge.omov.core.smartfolder.NumberMatch;
import net.sourceforge.omov.core.smartfolder.QualityCriterion;
import net.sourceforge.omov.core.smartfolder.QualityMatch;
import net.sourceforge.omov.core.smartfolder.RatingCriterion;
import net.sourceforge.omov.core.smartfolder.RatingMatch;
import net.sourceforge.omov.core.smartfolder.ResolutionCriterion;
import net.sourceforge.omov.core.smartfolder.ResolutionMatch;
import net.sourceforge.omov.core.smartfolder.SmartFolderUtil;
import net.sourceforge.omov.core.smartfolder.TextCriterion;
import net.sourceforge.omov.core.smartfolder.TextMatch;
import net.sourceforge.omov.core.smartfolder.TextMultipleCriterion;
import net.sourceforge.omov.core.smartfolder.TextMultipleMatch;
import net.sourceforge.omov.core.util.DateUtil;
import net.sourceforge.omov.core.util.NumberUtil.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class CriterionFieldFactory {

    private static final Log LOG = LogFactory.getLog(CriterionFieldFactory.class);

    private static final int TEXTFIELD_COLUMN_SIZE = 14;
    

    public static AbstractColumnCriterion<?> newCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        LOG.info("creating new AbstractColumnCriterion by column '"+columnLabel+"', match '"+matchLabel+"' and values '"+Arrays.toString(values)+"'.");

        if(SmartFolderUtil.isBoolColumnLabel(columnLabel)) {
            return newBoolCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isNumberColumnLabel(columnLabel)) {
            return newNumberCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isDateColumnLabel(columnLabel)) {
            return newDateCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isTextColumnLabel(columnLabel)) {
            return newTextCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isTextMultipleColumnLabel(columnLabel)) {
            return newTextMultipleCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isRatingColumnLabel(columnLabel)) {
            return newRatingCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isQualityColumnLabel(columnLabel)) {
            return newQualityCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isFileSizeColumnLabel(columnLabel)) {
            return newFileSizeCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isDurationColumnLabel(columnLabel)) {
            return newDurationCriterion(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isResolutionColumnLabel(columnLabel)) {
            return newResolutionCriterion(columnLabel, matchLabel, values);
        }
        
        throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
    }
    
    
    /**
     * @param columnLabel
     * @param matchLabel
     * @param values is null, if default values should be used
     */
    public static AbstractCriterionField newField(final String columnLabel, final String matchLabel, final Object[] values) {
        LOG.info("creating new AbstractCriterionField by column '"+columnLabel+"', match '"+matchLabel+"' and values '"+(values==null?"null":Arrays.toString(values))+"'.");
        

        if(SmartFolderUtil.isBoolColumnLabel(columnLabel)) {
            return newBoolField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isNumberColumnLabel(columnLabel)) {
            return newNumberField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isDateColumnLabel(columnLabel)) {
            return newDateField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isTextColumnLabel(columnLabel)) {
            return newTextField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isTextMultipleColumnLabel(columnLabel)) {
            return newTextMultipleField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isRatingColumnLabel(columnLabel)) {
            return newRatingField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isQualityColumnLabel(columnLabel)) {
            return newQualityField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isFileSizeColumnLabel(columnLabel)) {
            return newFileSizeField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isDurationColumnLabel(columnLabel)) {
            return newDurationField(columnLabel, matchLabel, values);
        }
        
        if(SmartFolderUtil.isResolutionColumnLabel(columnLabel)) {
            return newResolutionField(columnLabel, matchLabel, values);
        }
        
        throw new IllegalArgumentException("Could not get field by column '"+columnLabel+"' and match '"+matchLabel+"'!");
    }
    
    


    /******************************************************************************************************************/
    /**    S M A R T F O L D E R   C R I T E R A
    /******************************************************************************************************************/

    
    private static BoolCriterion newBoolCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Boolean value = (Boolean) values[0];
        final BoolMatch match;
        
        if(matchLabel.equals(BoolMatch.LABEL_IS)) {
            match = BoolMatch.newEquals(value);
        } else if(matchLabel.equals(BoolMatch.LABEL_IS_NOT)) {
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
    
    private static NumberCriterion newNumberCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Long value = (Long) values[0];
        final Long value2 = (values.length > 0) ? (Long) values[1] : null;
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
            throw new IllegalArgumentException("Unhandled matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')!");
        }
        
        final DateCriterion criterion;
        if(columnLabel.equals(MovieField.DATE_ADDED.label())) {
            criterion = DateCriterion.newDateAdded(match);
        } else {
            throw new IllegalArgumentException("Unhandled columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')!");
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
        } else if(columnLabel.equals(MovieField.STYLE.label())) {
            criterion = TextCriterion.newStyle(match);
        } else if(columnLabel.equals(MovieField.DIRECTOR.label())) {
            criterion = TextCriterion.newDirector(match);
        } else if(columnLabel.equals(MovieField.FORMAT.label())) {
            criterion = TextCriterion.newFormat(match);
        } else if(columnLabel.equals(MovieField.COMMENT.label())) {
            criterion = TextCriterion.newComment(match);
        } else if(columnLabel.equals(MovieField.FOLDER_PATH.label())) {
            criterion = TextCriterion.newFolderPath(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static TextMultipleCriterion newTextMultipleCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final String value = (String) values[0];
        final TextMultipleMatch match;
        
        if(matchLabel.equals(TextMultipleMatch.LABEL_CONTAINS)) {
            match = TextMultipleMatch.newContains(value);
        } else if(matchLabel.equals(TextMultipleMatch.LABEL_NOT_CONTAINS)) {
            match = TextMultipleMatch.newNotContains(value);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }

        final TextMultipleCriterion criterion;
        if(columnLabel.equals(MovieField.SUBTITLES.label())) {
            criterion = TextMultipleCriterion.newSubtitles(match);
        } else if(columnLabel.equals(MovieField.ACTORS.label())) {
            criterion = TextMultipleCriterion.newActors(match);
        } else if(columnLabel.equals(MovieField.GENRES.label())) {
            criterion = TextMultipleCriterion.newGenres(match);
        } else if(columnLabel.equals(MovieField.LANGUAGES.label())) {
            criterion = TextMultipleCriterion.newLanguages(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static RatingCriterion newRatingCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Integer value = (Integer) values[0];
        final Integer value2 = (values.length > 1) ? (Integer) values[1] : null;
        final RatingMatch match;
        
        if(matchLabel.equals(RatingMatch.LABEL_EQUALS)) {
            match = RatingMatch.newEquals(value);
        } else if(matchLabel.equals(RatingMatch.LABEL_NOT_EQUALS)) {
            match = RatingMatch.newNotEquals(value);
        } else if(matchLabel.equals(RatingMatch.LABEL_GREATER)) {
            match = RatingMatch.newGreater(value);
        } else if(matchLabel.equals(RatingMatch.LABEL_LESS)) {
            match = RatingMatch.newLess(value);
        } else if(matchLabel.equals(RatingMatch.LABEL_IN_THE_RANGE)) {
            match = RatingMatch.newInRange(value, value2);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final RatingCriterion criterion;
        if(columnLabel.equals(MovieField.RATING.label())) {
            criterion = RatingCriterion.newRating(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static QualityCriterion newQualityCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Quality value = (Quality) values[0];
        final QualityMatch match;
        
        if(matchLabel.equals(QualityMatch.LABEL_EQUALS)) {
            match = QualityMatch.newEquals(value);
        } else if(matchLabel.equals(QualityMatch.LABEL_BETTER)) {
            match = QualityMatch.newBetter(value);
        } else if(matchLabel.equals(QualityMatch.LABEL_WORSE)) {
            match = QualityMatch.newWorse(value);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }

        final QualityCriterion criterion;
        if(columnLabel.equals(MovieField.QUALITY.label())) {
            criterion = QualityCriterion.newQuality(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static FileSizeCriterion newFileSizeCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Long value = (Long) values[0];
        final FileSizeMatch match;
        
        if(matchLabel.equals(FileSizeMatch.LABEL_EQUALS)) {
            match = FileSizeMatch.newEquals(value);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }

        final FileSizeCriterion criterion;
        if(columnLabel.equals(MovieField.FILE_SIZE_KB.label())) {
            criterion = FileSizeCriterion.newFileSize(match);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        return criterion;
    }
    
    private static DurationCriterion newDurationCriterion(final String columnLabel, final String matchLabel, final Object[] values) {
        final Duration value = (Duration) values[0];
        final DurationMatch match;
        
        if(matchLabel.equals(DurationMatch.LABEL_EQUALS)) {
            match = DurationMatch.newEquals(value);
        } else if(matchLabel.equals(DurationMatch.LABEL_LONGER)) {
            match = DurationMatch.newLonger(value);
        } else if(matchLabel.equals(DurationMatch.LABEL_SHORTER)) {
            match = DurationMatch.newShorter(value);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }

        final DurationCriterion criterion;
        if(columnLabel.equals(MovieField.DURATION.label())) {
            criterion = DurationCriterion.newDuration(match);
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


    
    /******************************************************************************************************************/
    /**    G U I   F I E L D S
    /******************************************************************************************************************/

    private static AbstractCriterionField newBoolField(final String columnLabel, final String matchLabel, final Object[] values) {
        final boolean initValue = (values != null) ? (Boolean) values[0] : false;
        
        if(columnLabel.equals(MovieField.SEEN.label()) == false) {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        if( (matchLabel.equals(BoolMatch.LABEL_IS) || matchLabel.equals(BoolMatch.LABEL_IS_NOT)) == false) {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        return new BoolSingleField(initValue);
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
            size = 12;
            
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

    private static AbstractCriterionField newDateField(final String columnLabel, final String matchLabel, final Object[] values) {
        
        if(matchLabel.equals(DateMatch.LABEL_IN_THE_LAST) || matchLabel.equals(DateMatch.LABEL_NOT_IN_THE_LAST)) {

            // if(columnLabel.equals(DateCriterion.LABEL_DATE_ADDED)) ... not of interest
            int initValue = (values != null) ? DateMatch.extractInt((Date) values[0]) : 0;
            
            final RangeType rangeType;
            if(initValue == 0) {
                rangeType = RangeType.DAYS;
            } else if(initValue % 30 == 0) {
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
                    throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
                }
            }
            return new DateRangeField(initValueFrom, initValueTo, 5);
            
        } else if((matchLabel.equals(DateMatch.LABEL_EQUALS) ||
                   matchLabel.equals(DateMatch.LABEL_NOT_EQUALS) ||
                   matchLabel.equals(DateMatch.LABEL_AFTER) ||
                   matchLabel.equals(DateMatch.LABEL_BEFORE)) == true) {
            final Date initValue;
            if(columnLabel.equals(MovieField.DATE_ADDED.label())) {
                initValue = (values != null) ? (Date) values[0] : new Date();
            } else {
                throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
            }
            return new DateSingleField(initValue, TEXTFIELD_COLUMN_SIZE);
        } else {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
    }
    
    private static AbstractCriterionField newTextField(final String columnLabel, final String matchLabel, final Object[] values) {
        final int size = TEXTFIELD_COLUMN_SIZE; // displayed width of gui-widget (in columns)
        

        if( (columnLabel.equals(MovieField.TITLE.label()) ||
             columnLabel.equals(MovieField.STYLE.label()) || 
             columnLabel.equals(MovieField.DIRECTOR.label()) || 
             columnLabel.equals(MovieField.FORMAT.label()) || 
             columnLabel.equals(MovieField.COMMENT.label()) ||
             columnLabel.equals(MovieField.FOLDER_PATH.label())
                ) == false) {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        if( (matchLabel.equals(TextMatch.LABEL_EQUALS) ||
             matchLabel.equals(TextMatch.LABEL_NOT_EQUALS) ||
             matchLabel.equals(TextMatch.LABEL_CONTAINS) ||
             matchLabel.equals(TextMatch.LABEL_NOT_CONTAINS) ||
             matchLabel.equals(TextMatch.LABEL_STARTS_WITH) ||
             matchLabel.equals(TextMatch.LABEL_ENDS_WITH)
                
                ) == false) {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        // no need of different initial values for textfields
        final String initValue = (values != null) ? (String) values[0] : "";
        
        return new TextSingleField(size, initValue);
    }
    
    private static AbstractCriterionField newTextMultipleField(final String columnLabel, final String matchLabel, final Object[] values) {
        final int size = TEXTFIELD_COLUMN_SIZE; // displayed width of gui-widget (in columns)
        

        if( (columnLabel.equals(MovieField.SUBTITLES.label()) ||
             columnLabel.equals(MovieField.ACTORS.label()) || 
             columnLabel.equals(MovieField.GENRES.label()) || 
             columnLabel.equals(MovieField.LANGUAGES.label())
                ) == false) {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        if( (matchLabel.equals(TextMultipleMatch.LABEL_CONTAINS) ||
             matchLabel.equals(TextMultipleMatch.LABEL_NOT_CONTAINS)
                ) == false) {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        // no need of different initial values for textfields
        final String initValue = (values != null) ? (String) values[0] : "";
        
        return new TextMultipleSingleField(size, initValue);
    }
    
    private static AbstractCriterionField newRatingField(final String columnLabel, final String matchLabel, final Object[] values) {
        
        if(columnLabel.equals(MovieField.RATING.label()) == false) {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"' (matchLabel was '"+matchLabel+"')");
        }
        
        final AbstractCriterionField field;
        if(matchLabel.equals(RatingMatch.LABEL_IN_THE_RANGE)) {
            final int ratingFrom = values != null ? (Integer) values[0] : 0;
            final int ratingTo = values != null ? (Integer) values[1] : 5;
            field = new RatingRangeField(ratingFrom, ratingTo);
        } else {
            field = new RatingSingleField(values != null ? (Integer) values[0] : 0);
        }
        
        return field;
    }
    
    private static AbstractCriterionField newQualityField(final String columnLabel, final String matchLabel, final Object[] values) {
        if( (matchLabel.equals(QualityMatch.LABEL_EQUALS) ||
             matchLabel.equals(QualityMatch.LABEL_BETTER) ||
             matchLabel.equals(QualityMatch.LABEL_WORSE)
                         ) == false) {
                     throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final Quality initValue;
        if(columnLabel.equals(MovieField.QUALITY.label())) {
            initValue = (values != null) ? (Quality) values[0] : Quality.UNRATED;
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"'");
        }
        return new QualitySingleField(initValue);
    }
    
    private static AbstractCriterionField newFileSizeField(final String columnLabel, final String matchLabel, final Object[] values) {
        if( (matchLabel.equals(FileSizeMatch.LABEL_EQUALS) ||
             matchLabel.equals(FileSizeMatch.LABEL_GREATER) ||
             matchLabel.equals(FileSizeMatch.LABEL_LESS)
                      ) == false) {
                  throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final Long initValue;
        if(columnLabel.equals(MovieField.FILE_SIZE_KB.label())) {
            initValue = (values != null) ? (Long) values[0] : 0L;
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"'");
        }
        return new FileSizeSingleField(4, initValue, 0, 99999);
    }
    
    private static AbstractCriterionField newDurationField(final String columnLabel, final String matchLabel, final Object[] values) {
        if( (matchLabel.equals(DurationMatch.LABEL_EQUALS) ||
             matchLabel.equals(DurationMatch.LABEL_LONGER) ||
             matchLabel.equals(DurationMatch.LABEL_SHORTER)
                   ) == false) {
               throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final Duration initValue;
        if(columnLabel.equals(MovieField.DURATION.label())) {
            initValue = (values != null) ? (Duration) values[0] : Duration.newByTotal(0);
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"'");
        }
        return new DurationSingleField(initValue);
    }
    
    private static AbstractCriterionField newResolutionField(final String columnLabel, final String matchLabel, final Object[] values) {
        if( (matchLabel.equals(ResolutionMatch.LABEL_EQUALS) ||
             matchLabel.equals(ResolutionMatch.LABEL_GREATER)
                ) == false) {
            throw new IllegalArgumentException("matchLabel: '"+matchLabel+"' (columnLabel was '"+columnLabel+"')");
        }
        
        final Resolution initValue;
        
        if(columnLabel.equals(MovieField.RESOLUTION.label())) {
            initValue = (values != null) ? (Resolution) values[0] : Resolution.R800x600;
        } else {
            throw new IllegalArgumentException("columnLabel: '"+columnLabel+"'");
        }
        
        return new ResolutionSingleField(initValue);
    }
}
