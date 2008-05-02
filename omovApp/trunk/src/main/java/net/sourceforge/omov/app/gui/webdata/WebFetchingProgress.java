package net.sourceforge.omov.app.gui.webdata;

import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import net.sourceforge.omov.app.gui.webdata.FetchWebDetailWorker.IFetchedWebDetail;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.tools.webdata.WebSearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class WebFetchingProgress extends JDialog {

    private static final Log LOG = LogFactory.getLog(WebFetchingProgress.class);
    private static final long serialVersionUID = -3610540267639682892L;
    
    private boolean aborted = false;
    
    private final FetchWebDetailWorker worker;
    
    public WebFetchingProgress(JDialog owner, IFetchedWebDetail fetchedDetailListener, WebSearchResult searchResult) {
        super(owner, "Fetching Metadata", true);
        
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
        
        
        this.worker = new FetchWebDetailWorker(this, searchResult, fetchedDetailListener);
        LOG.debug("worker.execute();");
        this.worker.execute();
    }
    
    private JPanel initComponents() {
        final JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        progressBar.setPreferredSize(new Dimension(200, 30));
        
        final JButton btnCancel = new JButton("Cancel");
        btnCancel.setOpaque(false);
        this.getRootPane().setDefaultButton(btnCancel);
        btnCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doCancel();
       }});
        

        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Constants.getColorWindowBackground());
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 6, 0);
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        panel.add(new JLabel("Communicating with server..."), c);

        c.anchor = GridBagConstraints.LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(progressBar, c);

        c.anchor = GridBagConstraints.LINE_END;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 1;
        c.gridy = 1;
        c.gridwidth = 1;
        panel.add(btnCancel, c);

        return panel;
    }
    
    private void doCancel() {
        LOG.info("User canceled fetching detail metadata");
        this.worker.cancel(true);
        this.aborted = true;
        this.dispose();
    }
    
    public boolean isAborted() {
        return this.aborted;
    }

}
