package net.sourceforge.omov.app.gui.webdata;

import java.awt.Component;
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
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class WebSearchProgress<N extends Movie> extends JDialog {

    private static final Log LOG = LogFactory.getLog(WebSearchProgress.class);
	private static final long serialVersionUID = -5671264355595639975L;

	private boolean aborted = false;
    
    private final WebSearchWorker<N> worker;
    
	public WebSearchProgress(JFrame owner, Component parent, N movieFetchingData, IWebSearchWorkerListener<N> listener) {
		super(owner, "Fetching Metadata", true);
		GuiUtil.macSmallWindow(this.getRootPane());

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
        
        this.worker = new WebSearchWorker<N>(this, listener, movieFetchingData, parent);
        LOG.debug("worker.execute();");
        this.worker.execute();
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

    private JPanel initComponents() {
    	// TODO duplicate code: see WebFetchingProgress
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
        panel.add(new JLabel("Looking up movies..."), c);

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
}
