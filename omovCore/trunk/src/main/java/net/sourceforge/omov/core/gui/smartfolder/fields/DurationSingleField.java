package net.sourceforge.omov.core.gui.smartfolder.fields;

import net.sourceforge.omov.core.gui.comp.DurationPanel;
import net.sourceforge.omov.core.util.NumberUtil.Duration;

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
