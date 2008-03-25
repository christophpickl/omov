package at.ac.tuwien.e0525580.omov.gui.smartcopy;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingworker.SwingWorker;
import org.jdesktop.swingx.JXTable;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.DirectoryChooser;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopy;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopyPreprocessResult;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class SmartCopyDialog extends JDialog implements ActionListener {

    private static final Log LOG = LogFactory.getLog(SmartCopyDialog.class);
    private static final long serialVersionUID = -2141494736445865021L;

    private static final String CMD_START_COPY = "startCopy";
    private static final String CMD_START_COPY_ANYWAY = "startCopyAnyway";
    private static final String CMD_ABORT_COPY_ANYWAY = "abortCopyAnyway";

    
    private SmartCopy smartCopy = null;
    private final JTextField inpMovieIds = new JTextField("[[460]]", 20);
    private final DirectoryChooser inpTargetDirectory = DirectoryChooser.newSimple("Copy target directory");
    private final JButton btnStartCopy = new JButton("Start Copying");
//    private final JButton btnCancel = new JButton("Cancel");
    
    
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
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        c.gridx = 0;
        c.anchor = GridBagConstraints.LINE_START;
        
        
        c.gridy = 0;
        panel.add(this.panel1IdString(), c);
        c.gridy = 1;
        panel.add(this.panel2TargetDirectory(), c);
        c.gridy = 2;
        panel.add(this.panel3CopyButton(), c);
        c.gridy = 3;
        panel.add(this.preprocessResultTableWrapper, c);
        c.gridy = 4;
        panel.add(this.panelProgress(), c);

        return panel;
    }
    
    private JPanel _panel123(int number, String label, JPanel contentPanel) {
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("-{"+number+"}-"), c);

        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(new JLabel(label), c);

        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 1;
        panel.add(contentPanel, c);
        
        return panel;
        
    }

    private JPanel panel1IdString() {
        final JPanel contentPanel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(contentPanel, c);
        contentPanel.setLayout(layout);

        final JButton btnUseSelectedMovies = new JButton("Use selected movies"); // TODO implement me: use only selected movies for smart copy dialog
        c.anchor = GridBagConstraints.LINE_START;

        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(this.inpMovieIds, c);

        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        contentPanel.add(new JLabel("or "), c);
        c.gridx = 1;
        c.gridy = 1;
        contentPanel.add(btnUseSelectedMovies, c);
        
        return this._panel123(1, "Paste the ID-String:", contentPanel);
    }

    private JPanel panel2TargetDirectory() {
        final JPanel contentPanel = new JPanel();
        contentPanel.add(this.inpTargetDirectory);
        
        return this._panel123(2, "Select a target directory:", contentPanel);
    }

    private JPanel panel3CopyButton() {
        this.btnStartCopy.setActionCommand(CMD_START_COPY);
        this.btnStartCopy.addActionListener(this);
        
        final JPanel contentPanel = new JPanel();
        contentPanel.add(this.btnStartCopy);
        
        return this._panel123(3, "Hit button and get a cup of tea:", contentPanel);
    }
    
    private JPanel panelProgress() {
        final JPanel panel = new JPanel();

        JProgressBar progressBar = new JProgressBar();
        JButton btnCancel = new JButton("cancel");
        btnCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.out.println("cancel pressed");
            }
            
        });
        
        panel.add(new JLabel("Progress"));
        panel.add(progressBar);
        panel.add(btnCancel);

        return panel;
    }


    public void actionPerformed(final ActionEvent event) {
        new GuiAction() {protected void _action() {
            final String cmd = event.getActionCommand();
            
            if(cmd.equals(CMD_START_COPY)) {
                doStartCopy();
            } else if(cmd.equals(CMD_START_COPY_ANYWAY)) {
                doStartCopyAnyway();
            } else if(cmd.equals(CMD_ABORT_COPY_ANYWAY)) {
                doClose();
            } else {
                throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
            }
        }}.doAction();
    }
    
    private void doStartCopy() {
        LOG.debug("user hit start copy button.");
        
        if(this.inpMovieIds.getText().trim().length() == 0) {
            GuiUtil.warning(this, "SmartCopy aborted", "The ID-String field is empty.");
            return;
        }
        if(this.inpTargetDirectory.getDirectory() == null) {
            GuiUtil.warning(this, "SmartCopy aborted", "No target directory set.");
            return;
        }
        
        this.inpMovieIds.setEnabled(false);
        this.inpTargetDirectory.setButtonEnabled(false);
        this.btnStartCopy.setEnabled(false);
        
        this.smartCopy = new SmartCopy(this.inpMovieIds.getText().trim(), this.inpTargetDirectory.getDirectory());
        final SmartCopyPreprocessResult result = this.smartCopy.preprocess();
        
        this.fillPreprocessResultTable(result);
        
        if(result.isFatalError() == true) {
            GuiUtil.error(this, "SmartCopy aborted", "A fatal error occured. See table for details.");
            return;
        }
        
        if(result.isMajorError() || result.isMinorError()) {
            GuiUtil.warning(this, "SmartCopy suspended", "Some errors occured. See table for details.");
            return;
        }
        
        this.doCopy();
    }
    
    private JPanel preprocessResultTableWrapper = new JPanel();
    private void fillPreprocessResultTable(SmartCopyPreprocessResult result) {
        if(result.isFatalError() == false && result.isMajorError() == false && result.isMinorError() == false) {
            LOG.debug("No error occured at all while preprocessing SmartCopy.");
            return;
        }
        
        final JPanel panel = new JPanel(new BorderLayout());
        
        final PreprocessResultTableModel model = new PreprocessResultTableModel(result);
        final JXTable table = new JXTable(model);
        // TODO set columns not resizable
        GuiUtil.setAlternatingBgColor(table);
        table.setVisibleRowCount(4);
        table.packAll();
        
        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(new JLabel("Following errors occured:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        final JPanel panelSouth = new JPanel();
        final JButton btnCopyAnyway = new JButton("Copy Anyway");
        btnCopyAnyway.setActionCommand(CMD_START_COPY_ANYWAY);
        btnCopyAnyway.addActionListener(this);
        final JButton btnAbortCopy = new JButton("Abort Copy");
        btnAbortCopy.setActionCommand(CMD_ABORT_COPY_ANYWAY);
        btnAbortCopy.addActionListener(this);
        panelSouth.add(btnCopyAnyway);
        panelSouth.add(btnAbortCopy);
        panel.add(panelSouth, BorderLayout.SOUTH);
        
        if(result.isFatalError() == true) {
            btnCopyAnyway.setEnabled(false);
        }
        
        this.preprocessResultTableWrapper.add(panel);
        this.preprocessResultTableWrapper.revalidate();
        this.pack();
    }
    
    
    private void doStartCopyAnyway() {
        this.doCopy();
    }
    
    private void doCopy() {
        LOG.info("do copy");
//        try {
//            this.smartCopy.process();
//            GuiUtil.info(this, "SmartCopy finished", "Successfully copied all movies.");
//        } catch (BusinessException e) {
//            LOG.error("SmartCopy failed!", e);
//            GuiUtil.error(this, "SmartCopy failed", "Could not copy movies to target directory.");
//        }
//        this.doClose();
        CopySwingWorker worker = new CopySwingWorker();
        worker.execute();
    }
    
    // http://java.sun.com/javase/6/docs/api/javax/swing/SwingWorker.html
    private static class CopySwingWorker extends SwingWorker<String, String> {

        @Override
        protected String doInBackground() throws Exception {
//            this.setProgress(progress)
            // FIXME implement smartcopy swingworker
            Thread.sleep(1000 * 10);
            return null;
        }
        
        @Override
        protected void done() {
            System.out.println("work done");
        }
    }
    
    
    private void doClose() {
        this.dispose();
    }
    
    public static void main(String[] args) {
        new SmartCopyDialog(null).setVisible(true);
    }
}
