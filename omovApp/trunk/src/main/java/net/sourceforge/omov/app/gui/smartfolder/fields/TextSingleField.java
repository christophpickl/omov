package net.sourceforge.omov.app.gui.smartfolder.fields;

import javax.swing.JTextField;

public class TextSingleField extends AbstractCriterionField {
    
    private static final long serialVersionUID = 2595678130879838710L;

    private final JTextField textField;
    
    TextSingleField(int size, String initValue) {
        this.textField = new JTextField(initValue, size);
        this.add(this.textField);
    }

    @Override
    public Object[] getValues() {
        return new String[] { this.textField.getText() };
    }
    
}
