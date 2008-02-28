package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import javax.swing.JLabel;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.NumberField;

public class NumberRangeField extends AbstractCriterionField {

    private static final long serialVersionUID = -847235851250679805L;

    private final NumberField numberFieldFrom;
    private final NumberField numberFieldTo;
    
    NumberRangeField(int size, int initValueFrom, int initValueTo, int minValue, int maxValue) {
        this.numberFieldFrom = new NumberField(initValueFrom, minValue, maxValue, size);
        this.numberFieldTo = new NumberField(initValueTo, minValue, maxValue, size);
        
        this.add(this.numberFieldFrom);
        this.add(new JLabel(" to "));
        this.add(this.numberFieldTo);
    }

    @Override
    public Object[] getValues() {
        return new Integer[] { this.numberFieldFrom.getNumber(), this.numberFieldTo.getNumber() };
    }
}
