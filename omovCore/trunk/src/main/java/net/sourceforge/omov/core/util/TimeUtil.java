package net.sourceforge.omov.core.util;


public class TimeUtil {

    /**
     * @author aTunes team
     * @return eg: "0:13:02"
     */
//	@SuppressWarnings("boxing")
	public static String microSecondsToString(long micros) {
		long seconds = micros / 1000000;
		
		int minutes = (int) seconds / 60;
		seconds = seconds % 60;
		
		int hours = minutes / 60;
		minutes = minutes % 60; 
		
		return StringUtil.getString(
				hours, ":",
				(minutes < 10 ? "0" : ""),
				minutes, ":",
				(seconds < 10 ? "0" : ""),
				seconds);
	}
	
}
