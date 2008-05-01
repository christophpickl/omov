package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

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
