package at.ac.tuwien.e0525580.omov2.gui.comp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import at.ac.tuwien.e0525580.omov2.bo.movie.Resolution;
import at.ac.tuwien.e0525580.omov2.gui.comp.generic.NumberField;

public class ResolutionPanel extends JPanel {

    private static final long serialVersionUID = 1537997093262769402L;
    
    private final NumberField inpWidth;
    private final NumberField inpHeight;
    
    public ResolutionPanel(Resolution resolution) {
        this.setOpaque(false);
        final int columnSize = 4;
        this.inpWidth  = new NumberField(resolution.getWidth(), 0, 9999, columnSize);
        this.inpHeight = new NumberField(resolution.getHeight(), 0, 9999, columnSize);
        
        this.initComponents();
    }
    
    private void initComponents() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);

        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridy = 0;

        c.gridx = 0;
        this.add(this.inpWidth, c);
        c.gridx = 1;
        this.add(new JLabel(" x "), c);
        c.gridx = 2;
        this.add(this.inpHeight, c);
    }
    
    public Resolution getResolution() {
        return new Resolution(inpWidth.getNumber(), inpHeight.getNumber());
    }
}
