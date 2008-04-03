package at.ac.tuwien.e0525580.omov.gui.comp;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.NumberField;
import at.ac.tuwien.e0525580.omov.util.NumberUtil.Duration;

public class DurationPanel extends JPanel {
    
    private static final long serialVersionUID = 6476608166509749830L;

    private final NumberField inpMin;
    private final NumberField inpHours;
    
    public DurationPanel(Duration duration) {
        this.setOpaque(false);
        final int columnSize = 2;
        this.inpMin = new NumberField(duration.getMinutes(), 0, 999, columnSize);
        this.inpHours = new NumberField(duration.getHours(), 0,  99, columnSize);
        
        this.initComponents();
    }
    
    private void initComponents() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        this.setLayout(layout);
        layout.setConstraints(this, c);

        c.gridy = 0;

        c.insets = new Insets(0, 0, 0, 3); // top left bottom right
        c.gridx = 0;
        this.add(this.inpHours, c);

        c.insets = new Insets(0, 0, 0, 6); // top left bottom right
        c.gridx++;
        this.add(new JLabel("h"), c);
        

        c.insets = new Insets(0, 0, 0, 3); // top left bottom right
        c.gridx++;
        this.add(this.inpMin, c);

        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridx++;
        this.add(new JLabel("min"), c);
    }
    
    public void setDuration(Duration duration) {
        this.inpMin.setNumber(duration.getMinutes());
        this.inpHours.setNumber(duration.getHours());
    }
    
    public Duration getDuration() {
        return Duration.newByMinHour((int) this.inpMin.getNumber(), (int) this.inpHours.getNumber());
    }
}
