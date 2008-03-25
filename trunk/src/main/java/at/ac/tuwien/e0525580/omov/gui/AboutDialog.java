package at.ac.tuwien.e0525580.omov.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.action.OpenBrowserAction;

import at.ac.tuwien.e0525580.omov.Constants;
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
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80)); // top, left, bottom, right


//        final JButton logo = new JButton(ImageFactory.getInstance().getAboutLogo());
//        logo.setBorderPainted(false);
//        GuiUtil.enableHandCursor(logo);
//        try {
//            logo.addActionListener(new OpenBrowserAction(Constants.OMOV_WEBSITE_URL));
//            logo.setToolTipText("Visit: " + Constants.OMOV_WEBSITE_URL);
//        } catch (MalformedURLException e) {
//            LOG.error("OpenBrowserAction failed.", e);
//        }
        final JLabel logo = new JLabel(ImageFactory.getInstance().getAboutLogo(), JLabel.CENTER);
        try {
            final OpenBrowserAction openBrowser = new OpenBrowserAction(Constants.OMOV_WEBSITE_URL);
            logo.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    openBrowser.actionPerformed(null);
                }
            });
            GuiUtil.enableHandCursor(logo);
        } catch (MalformedURLException e) {
            LOG.error("OpenBrowserAction failed.", e);
        }
        
        final JLabel title = new JLabel("OurMovies", JLabel.CENTER);
        title.setFont(new Font("default", Font.BOLD, 13));
        final JLabel versionLabel = new JLabel("Version " + Constants.VERSION_STRING, JLabel.CENTER);
        versionLabel.setFont(new Font("default", Font.PLAIN, 11));
        
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;

        c.insets = new Insets(0, 0, 20, 0); // top, left, bottom, right
        c.gridy = 0;
        panel.add(logo, c);

        c.insets = new Insets(0, 0, 6, 0); // top, left, bottom, right
        c.gridy = 1;
        panel.add(title, c);

        c.insets = new Insets(0, 0, 0, 0); // top, left, bottom, right
        c.gridy = 2;
        panel.add(versionLabel, c);
        
        
        return panel;
    }
    
    
    public static void main(String[] args) {
        new AboutDialog(null).setVisible(true);
    }
}
