package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import java.util.Date;

import javax.swing.JLabel;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.DateField;

public class DateRangeField extends AbstractCriterionField {

    private static final long serialVersionUID = 6543547462167817291L;

    private final DateField dateFrom;
    private final DateField dateTo;
    
    DateRangeField(Date initFromValue, Date initToValue, int size) {
        this.dateFrom = new DateField(initFromValue, size);
        this.dateTo = new DateField(initToValue, size);
        
        this.add(dateFrom);
        this.add(new JLabel("to"));
        this.add(dateTo);
    }

    @Override
    public Object[] getValues() {
        final Date dateFrom = this.dateFrom.getDate();
        final Date dateTo = this.dateTo.getDate();
        if(dateFrom == null || dateTo == null) return null;
        return new Date[] { dateFrom, dateTo };
    }
    
}
