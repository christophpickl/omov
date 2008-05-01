package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import at.ac.tuwien.e0525580.omov.gui.comp.DurationPanel;
import at.ac.tuwien.e0525580.omov.util.NumberUtil.Duration;

public class DurationSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 3195329381904855753L;
    
    private final DurationPanel durationPanel;
    
    DurationSingleField(Duration initValue) {
        this.durationPanel = new DurationPanel(initValue);
        this.add(this.durationPanel);
    }

    @Override
    public Object[] getValues() {
        return new Duration[] { this.durationPanel.getDuration() };
    }
    
}
