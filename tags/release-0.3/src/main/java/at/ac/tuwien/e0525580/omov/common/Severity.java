package at.ac.tuwien.e0525580.omov.common;

import at.ac.tuwien.e0525580.omov.gui.ImageFactory.Icon16x16;

public enum Severity {
    INFO(Icon16x16.SEVERITY_INFO),
    WARNING(Icon16x16.SEVERITY_WARNING),
    ERROR(Icon16x16.SEVERITY_ERROR);
    
    private final Icon16x16 icon;
    
    private Severity(Icon16x16 icon) {
        this.icon = icon;
    }
    
    public Icon16x16 getIcon() {
        return this.icon;
    }
}
