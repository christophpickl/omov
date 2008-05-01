package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import java.util.Date;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.DateField;

public class DateSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = -7617094880565985399L;

    private final DateField dateField;
    
    DateSingleField(Date initValue, int size) {
        this.dateField = new DateField(initValue, size);
        this.add(this.dateField);
    }

    @Override
    public Object[] getValues() {
        final Date date = this.dateField.getDate();
        if(date == null) return null;
        return new Date[] { date };
    }
    
}
