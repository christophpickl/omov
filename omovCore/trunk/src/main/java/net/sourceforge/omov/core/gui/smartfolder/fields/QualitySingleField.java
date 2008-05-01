package net.sourceforge.omov.core.gui.smartfolder.fields;

import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.gui.comp.QualityField;

public class QualitySingleField extends AbstractCriterionField {

    private static final long serialVersionUID = 826878088252779719L;
    
    private final QualityField qualityField;
    
    QualitySingleField(Quality initValue) {
        this.qualityField = new QualityField(initValue);
        this.add(this.qualityField);
    }

    @Override
    public Object[] getValues() {
        return new Quality[] { this.qualityField.getQuality() };
    }
    
}
