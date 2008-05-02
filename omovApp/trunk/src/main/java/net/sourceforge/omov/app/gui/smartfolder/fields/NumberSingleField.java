package net.sourceforge.omov.app.gui.smartfolder.fields;

import net.sourceforge.omov.app.gui.comp.generic.NumberField;

public class NumberSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 1502771838440981253L;

    private final NumberField numberField;
    
    NumberSingleField(int size, long initValue, long minValue, long maxValue) {
        this.numberField = new NumberField(initValue, minValue, maxValue, size);
        this.add(this.numberField);
    }

    @Override
    public Object[] getValues() {
        return new Long[] { this.numberField.getNumber() };
    }
    
}
