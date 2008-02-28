package at.ac.tuwien.e0525580.omov.tools.scan;

public class ScanHint {

    private final HintSeverity severity;
    
    private final String hint;

    public static ScanHint info(String hint) {
        return new ScanHint(HintSeverity.INFO, hint);
    }
    public static ScanHint warning(String hint) {
        return new ScanHint(HintSeverity.WARNING, hint);
    }
    public static ScanHint error(String hint) {
        return new ScanHint(HintSeverity.ERROR, hint);
    }
    
    private ScanHint(HintSeverity severity, String hint) {
        if(hint == null || hint.length() == 0) throw new IllegalArgumentException("hint: '"+hint+"'");
        this.severity = severity;
        this.hint = hint;
    }
    
    public HintSeverity getSeverity() {
        return this.severity;
    }
    
    public String getHint() {
        return this.hint;
    }
    
    
    public static enum HintSeverity {
        INFO, WARNING, ERROR;
    }
}
