package at.ac.tuwien.e0525580.omov.util;

public final class NumberUtil {
    private NumberUtil() {
        // no instantiation
    }

    public static String formatDurationShort(Duration duration) {
        return formatDuration(duration.minutes, duration.hours);
    }

    public static String formatDurationShort(int durationInMinutes) {
        int minutes = durationInMinutes % 60;
        int hours = (int) Math.floor(durationInMinutes / 60.0);
        return formatDuration(minutes, hours);
    }
    
    private static String formatDuration(final int minutes, final int hours) {
        final StringBuilder sb = new StringBuilder(10);
        sb.append(hours).append(":");
        if(minutes < 10) sb.append("0");
        sb.append(minutes);
        return sb.toString();
        
    }
    
    public static class Duration {
        private final int minutes;
        private final int hours;
        private final int totalInMinutes;
        private Duration(final int minutes, final int hours) {
            this.minutes = minutes;
            this.hours = hours;
            this.totalInMinutes = calcTotalMinutes(minutes, hours);
        }
        public int getHours() {
            return this.hours;
        }
        public int getMinutes() {
            return this.minutes;
        }
        public int getTotalInMinutes() {
            return this.totalInMinutes;
        }
        public String formatStringShort() {
            return NumberUtil.formatDurationShort(this);
        }
        
        private static int calcTotalMinutes(int minutes, int hours) {
            return hours * 60 + minutes;
        }
        public static Duration newByTotal(int totalMinutes) {
            final int minutes = totalMinutes % 60;
            final int hours = (int) Math.ceil(totalMinutes / 60);
            return new Duration(minutes, hours);
        }
        public static Duration newByMinHour(int minutes, int hours) {
            return new Duration(minutes, hours);
        }
    }

}
