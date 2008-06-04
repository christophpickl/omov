package net.sourceforge.omov.core.util;


public class TimeUtil {

    /**
     * @author aTunes team
     * @return eg: "13:02"
     */
//	@SuppressWarnings("boxing")
	public static String microSecondsToString(long millis) {
		long aux = millis / 1000000;
		int minutes = (int) aux / 60;
		aux = aux % 60;
		return StringUtil.getString(minutes, ":", (aux < 10 ? "0" : ""), aux);
	}
}
