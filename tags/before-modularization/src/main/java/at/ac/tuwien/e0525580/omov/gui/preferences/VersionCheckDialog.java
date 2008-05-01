package at.ac.tuwien.e0525580.omov.gui.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.common.VersionMinorMajor;
import at.ac.tuwien.e0525580.omov.tools.ApplicationVersionFetcher;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class VersionCheckDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(VersionCheckDialog.class);
    private static final long serialVersionUID = 6136505135179155308L;
    private static final String DIALOG_TITLE = "Checking Version";
    
    private final VersionCheckWorker worker = new VersionCheckWorker();
    private final boolean shouldSuccessfullDialogDisplayed;

    public VersionCheckDialog() {
        this.shouldSuccessfullDialogDisplayed = false; // invoked by App because of startup version check enabled
        this.setTitle(DIALOG_TITLE);
        this.pseudoConstructor();
    }
    
    public VersionCheckDialog(JDialog owner) {
        super(owner, DIALOG_TITLE, true);
        this.shouldSuccessfullDialogDisplayed = true; // invoked by PreferenceWindowController/explicitly by user
        this.pseudoConstructor();
    }
    
    private void pseudoConstructor() {
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();

        final JProgressBar progressBar = new JProgressBar();
        progressBar.setIndeterminate(true);
        
        final JButton btnCancel = new JButton("Cancel");
        btnCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) { new GuiAction() { protected void _action() {
            doCancel();
        }}.doAction(); }});

        panel.add(progressBar);
        panel.add(btnCancel);
        
        return panel;
    }
    
    public void startCheck() {
        LOG.debug("Starting check by executing swing worker.");
        this.worker.execute();
    }
    
    private void doCancel() {
        LOG.debug("do cancel pressed by user.");
        this.worker.cancel(true);
    }
    
    private void workerFinished() {
        LOG.debug("Worker finished work (isCancelled = " + worker.isCancelled() + ").");
        if(this.worker.isCancelled() == false) {
            VersionMinorMajor versionFetched = null;
            try {
                versionFetched = this.worker.get();
            } catch (Exception e) { // InterruptedException, ExecutionException
                LOG.error("Could not get result from swing worker!", e);
            }
            if(versionFetched == null) {
                GuiUtil.warning(this, "Application Software Update", "Could not connect to the internet.");
            } else { // (versionFetched != null)
                final VersionMinorMajor versionInUse = BeanFactory.getInstance().getCurrentApplicationVersion();
                if(versionInUse.equals(versionFetched) == true) {
                    
                    LOG.debug("Application in use (v"+versionInUse+") is up to date.");
                    if(this.shouldSuccessfullDialogDisplayed == true) {
                        GuiUtil.info(this, "Application Software Update", "Your version " + versionInUse + " is up to date.");
                    }
                    
                } else { // versionInUse.equals(versionFetched) == false
                    GuiUtil.info(this, "Application Software Update", "It seems as you were running an old application version.\n" +
                            "You are using " + versionInUse + " but version " + versionFetched + " is available.\n" + 
                            "Check the website to download the most recent release:\n" +
                            Constants.getWebUrl());
                }
            }
        }
        this.dispose();
    }
    
    private class VersionCheckWorker extends SwingWorker<VersionMinorMajor, String> {

        @Override
        protected VersionMinorMajor doInBackground() throws Exception {
            LOG.debug("worker is working in background");
            
            return ApplicationVersionFetcher.fetchVersion();
        }
        
        @Override
        protected void done() {
            VersionCheckDialog.this.workerFinished();
        }
    }
}
