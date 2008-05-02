package net.sourceforge.omov.app.gui.smartfolder.fields;

import javax.swing.JLabel;

import net.sourceforge.omov.app.gui.comp.generic.NumberField;

public class NumberRangeField extends AbstractCriterionField {

    private static final long serialVersionUID = -847235851250679805L;

    private final NumberField numberFieldFrom;
    private final NumberField numberFieldTo;
    
    NumberRangeField(int size, long initValueFrom, long initValueTo, long minValue, long maxValue) {
        this.numberFieldFrom = new NumberField(initValueFrom, minValue, maxValue, size);
        this.numberFieldTo = new NumberField(initValueTo, minValue, maxValue, size);
        
        this.add(this.numberFieldFrom);
        this.add(new JLabel(" to "));
        this.add(this.numberFieldTo);
    }

    @Override
    public Object[] getValues() {
        return new Long[] { this.numberFieldFrom.getNumber(), this.numberFieldTo.getNumber() };
    }
}
