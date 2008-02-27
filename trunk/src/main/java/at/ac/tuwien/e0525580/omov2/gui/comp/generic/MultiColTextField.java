package at.ac.tuwien.e0525580.omov2.gui.comp.generic;

import javax.swing.BorderFactory;
import javax.swing.JTextField;

public class MultiColTextField extends JTextField {

    private static final long serialVersionUID = 2860809976529219917L;
    
    public MultiColTextField(int columns) {
        this("", columns);
    }
    public MultiColTextField(String text, int columns) {
        super(text, columns);
        this.setBorder(BorderFactory.createEmptyBorder());
        this.setEditable(false);
        this.setOpaque(false);
        
    }
    @Override
    public void setText(String text) {
        super.setText(text);
        this.setCaretPosition(0);
    }
}
