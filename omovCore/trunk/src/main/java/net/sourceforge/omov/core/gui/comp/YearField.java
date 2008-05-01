package net.sourceforge.omov.core.gui.comp;

import net.sourceforge.omov.core.gui.comp.generic.NumberField;

public class YearField extends NumberField {

    private static final long serialVersionUID = 6188821348943116461L;

    private static final int DEFAULT_SIZE = 4;
    
    public YearField(int initVal) {
        this(initVal, DEFAULT_SIZE);
    }

    public YearField(int initVal, int size) {
        super(initVal, 0, 9999, size);
    }
    
}
