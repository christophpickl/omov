package net.sourceforge.omov.app.gui.comp;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import net.sourceforge.omov.core.bo.Quality;

public class QualityField extends JComboBox {

    private static final long serialVersionUID = 6284832762280153422L;


    public QualityField(Quality initValue) {
        this.setOpaque(false);
        
        this.setModel(new DefaultComboBoxModel() {
            private static final long serialVersionUID = -6244895415596156603L;
            public Object getElementAt(int row) {
                return Quality.getQualityById(row).label();
            }
            public int getSize() {
                return 5;
            }
        });
        this.setSelectedIndex(initValue.getId());
    }
    
    public void setQuality(Quality quality) {
        this.setSelectedIndex(quality.getId());
    }
    
    public Quality getQuality() {
        return Quality.getQualityById(this.getSelectedIndex());
    }
}