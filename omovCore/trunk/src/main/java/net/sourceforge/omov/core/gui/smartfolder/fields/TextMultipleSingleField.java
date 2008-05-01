package net.sourceforge.omov.core.gui.smartfolder.fields;

import javax.swing.JTextField;

public class TextMultipleSingleField extends AbstractCriterionField {
    
    private static final long serialVersionUID = 1220527945391777110L;
    
    private final JTextField textField;
    
    TextMultipleSingleField(int size, String initValue) {
        this.textField = new JTextField(initValue, size);
        this.add(this.textField);
    }

    @Override
    public Object[] getValues() {
        return new String[] { this.textField.getText() };
    }
    
}
