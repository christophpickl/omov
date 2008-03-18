package at.ac.tuwien.e0525580.omov.gui.doubletten;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.WindowConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.tools.doubletten.DuplicatesFinder;
import at.ac.tuwien.e0525580.omov.tools.doubletten.DuplicatesFinder.IDuplicatesFinderListener;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class DuplicatesFinderProgressDialog extends JDialog implements IDuplicatesFinderListener {

    private static final Log LOG = LogFactory.getLog(DuplicatesFinderProgressDialog.class);
    private static final long serialVersionUID = -1210382497017009556L;

    private final DuplicatesFinder finder;
    
    private final JProgressBar progressBar = new JProgressBar();
    private final JButton btnCancel = new JButton("Cancel");
    
    
    public DuplicatesFinderProgressDialog(JFrame owner) {
        super(owner, "Finding duplicates ...", true);
        this.finder = new DuplicatesFinder();
        
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.setResizable(false);
        
        LOG.debug("Starting new thread to find duplicates...");
        final IDuplicatesFinderListener _this = this;
        new Thread(new Runnable() {
            public void run() {
                finder.findDuplicates(_this);
            }
        }) {}.start();
        
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();

        this.progressBar.setIndeterminate(true);
        this.btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                doCancel();
            }
        });
        
        panel.add(this.progressBar);
        panel.add(this.btnCancel);

        return panel;
    }
    
    private void doCancel() {
        LOG.debug("User clicked cancel button.");
        
        this.btnCancel.setEnabled(false);
        this.progressBar.setValue(0);
        this.finder.doAbort();
    }
    
    
    public void doProgressFinished(boolean successfullyFinished) {
        LOG.debug("doProgressFinished(successfullyFinished="+successfullyFinished+") invoked");
        if(successfullyFinished == false) {
            final Exception ex = this.finder.getThrownException();
            if(ex != null) {
                GuiUtil.error("Progress aborted", "Searching for duplicates was aborted\ndue to an internal error!");
                LOG.error("Searching for duplicates failed", ex);
            }
            this.dispose();
            return;
        }
        if(this.finder.getFoundDuplicates().size() == 0) {
            GuiUtil.info("No Duplicate Movies", "There could be not any duplicate movie found.");
            this.dispose();
        } else {
            final DuplicatesFinderDialog dialog = new DuplicatesFinderDialog((JFrame) this.getOwner(), this.finder);
            this.dispose();
            dialog.setVisible(true);
        }
    }
}
