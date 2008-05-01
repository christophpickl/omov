package net.sourceforge.omov.core.gui.smartfolder.fields;

import javax.swing.JPanel;

public abstract class AbstractCriterionField extends JPanel {
    
    public AbstractCriterionField() {
        this.setOpaque(false);
    }
    
    /**
     * @return null if user entered invalid input (see DateField)
     */
    public abstract Object[] getValues();
}
