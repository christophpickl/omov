package at.ac.tuwien.e0525580.omov.gui;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.tools.SmartCopy;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

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
    private void doCopy() throws BusinessException {
        final SmartCopy copy = new SmartCopy();
        // FIXME implement me
        
        final int ids[] = new int[] { 10 };
        final File targetDirectory = new File("/OmovMoviesTarget");
        
        copy.copy(ids, targetDirectory, this);
    }
    
    private void doClose() {
        this.dispose();
    }
    
    public static void main(String[] args) throws BusinessException {
        new SmartCopyDialog(null).doCopy();
    }
}
