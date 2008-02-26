package at.ac.tuwien.e0525580.omov2.gui.comp.generic;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Font;

import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class LabeledCheckedComponent extends JPanel {

    private static final long serialVersionUID = 4775489699927023914L;

    public LabeledCheckedComponent(final Component component, final String labelText, final JCheckBox checkBox) {
        super(new BorderLayout());
        
        final JLabel label = new JLabel(labelText);
        label.setFont(new Font("sans", Font.BOLD, 10));
        
        this.add(label, BorderLayout.NORTH);
        this.add(checkBox, BorderLayout.WEST);
        this.add(component, BorderLayout.CENTER);
    }

}
