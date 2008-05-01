package net.sourceforge.omov.core.gui.smartfolder.fields;

import javax.swing.ButtonGroup;
import javax.swing.JRadioButton;

public class BoolSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 3687599339607030534L;
    
    private final JRadioButton btnTrue;
    private final JRadioButton btnFalse;
    
    BoolSingleField(boolean initValue) {
        this.btnTrue = new JRadioButton("true" , initValue == true);
        this.btnFalse = new JRadioButton("false"   , initValue == false);

        this.btnTrue.setOpaque(false);
        this.btnFalse.setOpaque(false);

        final ButtonGroup btnGroup = new ButtonGroup();
        btnGroup.add(this.btnTrue);
        btnGroup.add(this.btnFalse);

        this.add(this.btnTrue);
        this.add(this.btnFalse);
    }

    @Override
    public Object[] getValues() {
        return new Boolean[] { this.btnTrue.isSelected() };
    }
    
}
