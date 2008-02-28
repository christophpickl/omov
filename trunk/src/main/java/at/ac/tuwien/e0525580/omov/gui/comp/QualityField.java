package at.ac.tuwien.e0525580.omov.gui.comp;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;

import at.ac.tuwien.e0525580.omov.Constants;

public class QualityField extends JComboBox {

    private static final long serialVersionUID = 6284832762280153422L;


    public QualityField(int initValue) {
        if(initValue < 0 || initValue > 4) throw new IllegalArgumentException("initValue: " + initValue);
        
        this.setModel(new DefaultComboBoxModel() {
            private static final long serialVersionUID = -6244895415596156603L;
            public Object getElementAt(int row) {
                return Constants.mapQuality(row);
            }
            public int getSize() {
                return 5;
            }
        });
        this.setSelectedIndex(initValue);
    }
    
    public int getNumber() {
        return this.getSelectedIndex();
    }
}
