package at.ac.tuwien.e0525580.omov.gui;

import java.awt.BorderLayout;
import java.net.MalformedURLException;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.action.OpenBrowserAction;

import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class AboutDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(AboutDialog.class);
    private static final long serialVersionUID = -6058616320816195022L;
    
    
    public AboutDialog(JFrame owner) {
        super(owner, "About", true);
        
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());

        final JButton btnWebsite = new JButton("visit Website");
        try {
            btnWebsite.addActionListener(new OpenBrowserAction("http://omov.sourceforge.net"));
            panel.add(btnWebsite, BorderLayout.NORTH);
        } catch (MalformedURLException e) {
            LOG.error("OpenBrowserAction failed.", e);
        }
        
        // TODO add logo in AboutDialog
        final JLabel logo = new JLabel("LOGO", JLabel.CENTER);
        
        
        panel.add(logo, BorderLayout.CENTER);
        
        panel.add(new JLabel("OurMovies", JLabel.CENTER), BorderLayout.SOUTH);

        return panel;
    }
}
