package at.ac.tuwien.e0525580.omov.tools.scan;



public class PreparerHint  implements Comparable<PreparerHint> {
    
    private HintSeverity severity;
    
    private final String msg;
    
    private PreparerHint(HintSeverity severity, String msg) {
        if(msg == null) throw new NullPointerException("msg");
        this.severity = severity;
        this.msg = msg;
    }
    
    public HintSeverity getSeverity() {
        return this.severity;
    }
    
    public String getMsg() {
        return this.msg;
    }
    
    public String toString() {
        return "Hint."+severity.name() + ": " + msg;
    }
    
    static PreparerHint success(String msg) {
        return new PreparerHint(HintSeverity.SUCCESS, msg);
    }
    
    static PreparerHint info(String msg) {
        return new PreparerHint(HintSeverity.INFO, msg);
    }
    
    static PreparerHint warning(String msg) {
        return new PreparerHint(HintSeverity.WARNING, msg);
    }
    
    static PreparerHint error(String msg) {
        return new PreparerHint(HintSeverity.ERROR, msg);
    }

    public static enum HintSeverity {
        SUCCESS(0), INFO(1), WARNING(2), ERROR(3);
        private final int lvl;
        private HintSeverity(int lvl) {
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
