package net.sourceforge.omov.core.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;

public final class DateUtil {

    private DateUtil() {
        // no instantiation
    }
    
    public static Calendar getCalendarWithoutTime(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.HOUR_OF_DAY, 0);
        c.set(GregorianCalendar.MINUTE, 0);
        c.set(GregorianCalendar.SECOND, 0);
        c.set(GregorianCalendar.MILLISECOND, 0);
        
        return c;
    }
    
    public static Calendar getCalendarWithoutMilliSeconds(Date date) {
        Calendar c = GregorianCalendar.getInstance();
        c.setTime(date);
        c.set(GregorianCalendar.MILLISECOND, 0);
        
        return c;
    }
    
    public static Date getDateWithoutTime(Date date) {
        return getCalendarWithoutTime(date).getTime();
    }
    
    public static Date getDateWithoutMilliSeconds(Date date) {
        return getCalendarWithoutMilliSeconds(date).getTime();
    }
    
    public static Date getDateWithoutTimeAndChangedDays(Date date, int changeDays) {
        Calendar c = getCalendarWithoutTime(date);
        c.add(GregorianCalendar.DAY_OF_MONTH, changeDays);
        return c.getTime();
    }
    
    /**
     * @param daysBefore non negative number
     */
    public static Date getCurrentDateWithoutTimeAndSubtractedDays(int daysBefore) {
        assert(daysBefore >= 0);
        
        return getDateWithoutTimeAndChangedDays(new Date(), -daysBefore);
    }
    
    public static int getCurrentYear() {
        Calendar c = GregorianCalendar.getInstance();
        return c.get(Calendar.YEAR);
    }
    
    public static int compareWithoutTime(Date d1, Date d2) {
        d1 = getDateWithoutTime(d1);
        d2 = getDateWithoutTime(d2);
        
        return d1.compareTo(d2);
    }
    
    public static int compareWithoutMilliSeconds(Date d1, Date d2) {
        d1 = getDateWithoutMilliSeconds(d1);
        d2 = getDateWithoutMilliSeconds(d2);
        
        return d1.compareTo(d2);
    }
    
    public static void main(String[] args) throws ParseException {
        final SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        List<Date> dates = new LinkedList<Date>();
        dates.add(format.parse("2008-01-01 12:00:01.126"));
        dates.add(format.parse("2008-01-01 12:00:01.124"));
        dates.add(format.parse(""));
        Collections.sort(dates, new Comparator<Date>() {
            public int compare(Date d1, Date d2) {
                return compareWithoutMilliSeconds(d1, d2);
            }
            
        });
        System.out.println(Arrays.toString(dates.toArray()));
    }
}
