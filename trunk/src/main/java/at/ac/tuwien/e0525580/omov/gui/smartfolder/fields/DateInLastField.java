package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JComboBox;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.NumberField;

public class DateInLastField extends AbstractCriterionField {

    private static final long serialVersionUID = -3495069799227565507L;
    
    private final NumberField numberField;
    private final JComboBox comboRangeType = new JComboBox(new String[] { RangeType.DAYS.name().toLowerCase(),
                                                                          RangeType.MONTHS.name().toLowerCase(),
                                                                          RangeType.WEEKS.name().toLowerCase() } );
    
    DateInLastField(int days, RangeType preselectedRangeType) {
        final int initValue = days / preselectedRangeType.getDayAmount();
        final int maxValue = 999;
        
        this.comboRangeType.setSelectedItem(preselectedRangeType.name().toLowerCase());
        this.numberField = new NumberField(initValue, 0, maxValue, 4);
        

        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);

        c.insets = new Insets(4, 4, 0, 0); // top left bottom right
        c.gridy = 0;
        c.gridx = 0;
        this.add(this.numberField, c);
        
        c.insets = new Insets(4, 6, 0, 0); // top left bottom right
        c.gridx = 1;
        this.add(this.comboRangeType, c);
    }

    @Override
    public Object[] getValues() {
        int number = this.numberField.getNumber();
        int value = number * RangeType.getByString((String) this.comboRangeType.getSelectedItem()).getDayAmount();
        return new Integer[] { value };
    }
    
}
