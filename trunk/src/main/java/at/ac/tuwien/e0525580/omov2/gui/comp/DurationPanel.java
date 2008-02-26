package at.ac.tuwien.e0525580.omov2.gui.comp;

import java.awt.FlowLayout;

import javax.swing.JLabel;
import javax.swing.JPanel;

import at.ac.tuwien.e0525580.omov2.gui.comp.generic.NumberField;
import at.ac.tuwien.e0525580.omov2.util.NumberUtil.Duration;

public class DurationPanel extends JPanel {
    
    private static final long serialVersionUID = 6476608166509749830L;

    private final NumberField inpMin;
    private final NumberField inpHours;
    
    public DurationPanel(Duration duration) {
        
        final int columnSize = 2;
        this.inpMin = new NumberField(duration.getMinutes(), 0, 999, columnSize);
        this.inpHours = new NumberField(duration.getHours(), 0,  99, columnSize);
        
        this.initComponents();
    }
    
    private void initComponents() {
        this.setLayout(new FlowLayout(FlowLayout.LEFT));

        this.add(this.inpHours);
        this.add(new JLabel("h"));
        
        this.add(this.inpMin);
        this.add(new JLabel("min"));
    }
    
    public Duration getDuration() {
        return Duration.newByMinHour(this.inpMin.getNumber(), this.inpHours.getNumber());
    }
}
