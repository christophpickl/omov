package at.ac.tuwien.e0525580.omov.gui.smartfolder.fields;

import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.gui.comp.ResolutionPanel;

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
