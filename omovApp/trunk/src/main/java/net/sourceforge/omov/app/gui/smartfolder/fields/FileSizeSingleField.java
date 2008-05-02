package net.sourceforge.omov.app.gui.smartfolder.fields;

import javax.swing.JLabel;

import net.sourceforge.omov.app.gui.comp.generic.NumberField;

public class FileSizeSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = -7321191196520421565L;
    
    private final NumberField numberField;
    
    FileSizeSingleField(int size, long initValue, long minValue, long maxValue) {
        this.numberField = new NumberField(initValue, minValue, maxValue, size);
        this.add(this.numberField);
        this.add(new JLabel("MB"));
    }

    @Override
    public Object[] getValues() {
        return new Long[] { this.numberField.getNumber() * 1024L }; // convert from MB to KB
    }
    
}
