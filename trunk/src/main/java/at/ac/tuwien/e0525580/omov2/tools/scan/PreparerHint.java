package at.ac.tuwien.e0525580.omov2.tools.scan;



public class PreparerHint  implements Comparable<PreparerHint> {
    
    private Severity severity;
    
    private final String msg;
    
    private PreparerHint(Severity severity, String msg) {
        if(msg == null) throw new NullPointerException("msg");
        this.severity = severity;
        this.msg = msg;
    }
    
    public Severity getSeverity() {
        return this.severity;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public String toString() {
        return "Hint."+severity.name() + ": " + msg;
    }
    
    static PreparerHint success(String msg) {
        return new PreparerHint(Severity.SUCCESS, msg);
    }
    
    static PreparerHint info(String msg) {
        return new PreparerHint(Severity.INFO, msg);
    }
    
    static PreparerHint warning(String msg) {
        return new PreparerHint(Severity.WARNING, msg);
    }
    
    static PreparerHint error(String msg) {
        return new PreparerHint(Severity.ERROR, msg);
    }

    public static enum Severity {
        SUCCESS(0), INFO(1), WARNING(2), ERROR(3);
        private final int lvl;
        private Severity(int lvl) {
            this.lvl = lvl;
        }
    }

    public int compareTo(PreparerHint that) {
        if(this.severity.lvl != that.severity.lvl) {
            return this.severity.lvl - that.severity.lvl;
        }
        return this.msg.compareTo(that.msg);
    }
}
