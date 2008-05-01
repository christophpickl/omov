package net.sourceforge.omov.core.tools.scan;

import net.sourceforge.omov.core.common.Severity;

public class ScanHint {

    private final Severity severity;
    
    private final String hint;

    public static ScanHint info(String hint) {
        return new ScanHint(Severity.INFO, hint);
    }
    public static ScanHint warning(String hint) {
        return new ScanHint(Severity.WARNING, hint);
    }
    public static ScanHint error(String hint) {
        return new ScanHint(Severity.ERROR, hint);
    }
    
    private ScanHint(Severity severity, String hint) {
        if(hint == null || hint.length() == 0) throw new IllegalArgumentException("hint: '"+hint+"'");
        this.severity = severity;
        this.hint = hint;
    }
    
    public Severity getSeverity() {
        return this.severity;
    }
    
    public String getHint() {
        return this.hint;
    }
    
}
