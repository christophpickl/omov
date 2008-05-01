package net.sourceforge.omov.core.common;

import net.sourceforge.omov.core.gui.ImageFactory.Icon16x16;

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
