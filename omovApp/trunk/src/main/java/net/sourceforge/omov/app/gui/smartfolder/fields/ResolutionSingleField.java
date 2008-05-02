package net.sourceforge.omov.app.gui.smartfolder.fields;

import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.app.gui.comp.ResolutionPanel;

public class ResolutionSingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 4003062908287999477L;

    private final ResolutionPanel resolutionPanel;
    
    ResolutionSingleField(Resolution initValue) {
        this.resolutionPanel = new ResolutionPanel(initValue);
        this.add(this.resolutionPanel);
    }

    @Override
    public Object[] getValues() {
        return new Resolution[] { this.resolutionPanel.getResolution() };
    }
    
}
