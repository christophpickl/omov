package at.ac.tuwien.e0525580.omov2.gui.export;

import java.awt.BorderLayout;
import java.awt.Font;

import javax.swing.JLabel;
import javax.swing.JPanel;

public class XmlExportPanel extends JPanel {

    private static final long serialVersionUID = -8464758891434308258L;

    public XmlExportPanel() {

        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());
        
        final String infoString = "<html>" +
            "There is not really much to say about this.<br>" +
            "Actually, its not even usefull, because<br>" +
            "its counterpart (import) is not yet implemented.</html>";
        
        final JLabel infoText = new JLabel(infoString);
        infoText.setFont(new Font("default", Font.PLAIN, 11));
        
        this.add(infoText);
    }
}
