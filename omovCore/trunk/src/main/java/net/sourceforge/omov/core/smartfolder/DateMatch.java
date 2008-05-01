package net.sourceforge.omov.core.smartfolder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import net.sourceforge.omov.core.util.DateUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.query.Constraint;
import com.db4o.query.Query;

public abstract class DateMatch extends AbstractMatch<Date> {

    private static final Log LOG = LogFactory.getLog(TextMatch.class);

    public static final String LABEL_EQUALS = "equals";
    public static final String LABEL_NOT_EQUALS = "not equals";
    public static final String LABEL_AFTER = "is after";
    public static final String LABEL_BEFORE = "is before";
    public static final String LABEL_IN_RANGE = "is in range";
    public static final String LABEL_IN_THE_LAST = "is in the last";
    public static final String LABEL_NOT_IN_THE_LAST = "is not in the last";

    static final List<String> ALL_MATCH_LABELS;
    static {
        List<String> tmp = new ArrayList<String>();
        tmp.add(LABEL_EQUALS);
        tmp.add(LABEL_NOT_EQUALS);
        tmp.add(LABEL_AFTER);
        tmp.add(LABEL_BEFORE);
        tmp.add(LABEL_IN_RANGE);
        tmp.add(LABEL_IN_THE_LAST);
        tmp.add(LABEL_NOT_IN_THE_LAST);
        ALL_MATCH_LABELS = Collections.unmodifiableList(tmp);
    }
    
    public static DateMatch newEquals(Date value) {
        return new DateMatch(LABEL_EQUALS, new Date[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing equals for value '"+this.getValueAt(0)+"'.");
                final Date equalsDate = DateUtil.getDateWithoutTime(this.getValueAt(0));
                final Date equalsDate2 = DateUtil.getDateWithoutTimeAndChangedDays(equalsDate, +1);
                // actually its a "within range" query, because it checks for whole day
                return query.constrain(equalsDate).greater().equal().and(query.constrain(equalsDate2).smaller());
            }
        };
    }
    public static DateMatch newNotEquals(Date value) {
        return new DateMatch(LABEL_NOT_EQUALS, new Date[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing not equals for value '"+this.getValueAt(0)+"'.");
                Date value = this.getValueAt(0);
                return query.constrain(value).not();
            }
        };
    }
    public static DateMatch newAfter(Date value) {
        return new DateMatch(LABEL_AFTER, new Date[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing after for value '"+this.getValueAt(0)+"'.");
                Date value = this.getValueAt(0);
                return query.constrain(value).greater();
            }
        };
    }
    public static DateMatch newBefore(Date value) {
        return new DateMatch(LABEL_BEFORE, new Date[] { value } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                LOG.debug("Preparing before for value '"+this.getValueAt(0)+"'.");
                Date value = this.getValueAt(0);
                return query.constrain(value).smaller();
            }
        };
    }
    
    /**
     * @param lowerBound inclusive
     * @param upperBound inclusive
     */
    public static DateMatch newInRange(Date lowerBound, Date upperBound) {
        return new DateMatch(LABEL_IN_RANGE, new Date[] { lowerBound, upperBound } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                Date lower = this.getValueAt(0);
                Date upper = this.getValueAt(1);
                LOG.debug("Preparing in range of '"+lower+"' to '"+upper+"'.");
                return query.constrain(lower).greater().equal().and(query.constrain(upper).smaller().equal());
            }
        };
    }
    
    /**
     * @param days positive value (eg: today is "2008/02/16 20:04:46" days=17 -> "2008/01/30 00:00:00")
     */
    public static DateMatch newInTheLast(int days) {
        if(days < 0) throw new IllegalArgumentException("days: " + days);
        return new DateMatch(LABEL_IN_THE_LAST, new Date[] { wrapInt(days) } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final int daysBefore = extractInt(this.getValueAt(0));
                LOG.debug("Preparing in the last '"+daysBefore+"' days.");
                final Date date = DateUtil.getCurrentDateWithoutTimeAndSubtractedDays(daysBefore);
                return query.constrain(date).greater().equal();
            }
        };
    }
    public static DateMatch newNotInTheLast(int days) {
        if(days < 0) throw new IllegalArgumentException("days: " + days);
        return new DateMatch(LABEL_NOT_IN_THE_LAST, new Date[] { wrapInt(days) } ) {
            @Override
            Constraint prepareDb4oQuery(Query query) {
                final int daysBefore = extractInt(this.getValueAt(0));
                LOG.debug("Preparing not in the last '"+daysBefore+"' days.");
                final Date date = DateUtil.getCurrentDateWithoutTimeAndSubtractedDays(daysBefore);
                return query.constrain(date).smaller();
            }
        };
    }
    
    public static int extractInt(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        return c.get(Calendar.MILLISECOND); // missuse of MILLISECOND field
    }
    
    private static Date wrapInt(int daysBeforeOrSomething) {
        Calendar c = GregorianCalendar.getInstance();
        c.set(Calendar.MILLISECOND, daysBeforeOrSomething); // missuse of MILLISECOND field
        return c.getTime();
    }
    
    private DateMatch(String label, Date[] values) {
        super(label, values);
    }

}
