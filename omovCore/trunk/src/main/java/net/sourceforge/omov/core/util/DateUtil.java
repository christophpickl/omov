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

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
        return getDateWithoutTime(d1).compareTo(getDateWithoutTime(d2));
    }
    
    public static int compareWithoutMilliSeconds(Date d1, Date d2) {
        return getDateWithoutMilliSeconds(d1).compareTo(getDateWithoutMilliSeconds(d2));
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
