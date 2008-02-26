package at.ac.tuwien.e0525580.omov2.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.ac.tuwien.e0525580.omov2.tools.SmartCopy;
import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class SmartCopyDialog extends JDialog {

    private static final long serialVersionUID = -2141494736445865021L;

    public SmartCopyDialog(JFrame owner) {
        super(owner, true);
        this.setTitle("Smart Copy");
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);

        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();

        panel.add(new JLabel("tut"));

        return panel;
    }
    
    private void doCopy() {
        final SmartCopy copy = new SmartCopy();
        // FIXME implement me
    }
    
    private void doClose() {
        this.dispose();
    }
}
