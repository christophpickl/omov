package at.ac.tuwien.e0525580.omov2.gui.comp.generic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabeledComponent extends JPanel {

    private static final long serialVersionUID = -3101237571525060234L;
    
    
    public LabeledComponent(final Component component, final String labelText) {
        super(new BorderLayout());
        
        final JLabel label = new JLabel(labelText);
        label.setFont(new Font("sans", Font.BOLD, 10));
        
        this.add(label, BorderLayout.NORTH);
        this.add(component, BorderLayout.CENTER);
    }
}
