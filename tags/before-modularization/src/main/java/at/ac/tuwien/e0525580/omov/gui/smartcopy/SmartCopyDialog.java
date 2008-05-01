package at.ac.tuwien.e0525580.omov.gui.smartcopy;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.DirectoryChooser;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.IChooserListener;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.MacLikeTable;
import at.ac.tuwien.e0525580.omov.gui.main.MainWindowController;
import at.ac.tuwien.e0525580.omov.tools.smartcopy.SmartCopyPreprocessResult;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class SmartCopyDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(SmartCopyDialog.class);
    private static final long serialVersionUID = -2141494736445865021L;
    

    private final JButton btnCopyAnyway = new JButton("Copy Anyway");
    private final JButton btnAbortCopy = new JButton("Abort Copy");
    private final JTextField inpMovieIds = new JTextField("", 24);
    private final DirectoryChooser inpTargetDirectory = new DirectoryChooser("Copy target directory");
    private final JButton btnStartCopy = new JButton("Start Copying");
    private final JButton btnCancel = new JButton("Cancel");
    private final JProgressBar progressBar = new JProgressBar(0, 100);
    private final JButton btnUseSelectedMovies = new JButton("Use selected movies");
    private JPanel preprocessResultTableWrapper = new JPanel();
    
    private final SmartCopyDialogController controller;
    
    public SmartCopyDialog(JFrame owner, MainWindowController mainController) {
        super(owner, true);
        controller = new SmartCopyDialogController(this, mainController);
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
        this.preprocessResultTableWrapper.setOpaque(false);
        this.btnStartCopy.setEnabled(false);
        
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        panel.setBackground(Constants.getColorWindowBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        c.fill = GridBagConstraints.NONE;
        
        c.gridx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        c.insets = new Insets(0, 0, 12, 0); // top left bottom right
        c.gridy = 0;
        panel.add(this.panel1IdString(), c);
        c.gridy = 1;
        panel.add(this.panel2TargetDirectory(), c);
        c.gridy = 2;
        panel.add(this.panel3CopyButton(), c);
        c.gridy = 3;
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
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
        panel.setOpaque(false);

        final JLabel lblNumber = new JLabel(String.valueOf(number) + ".");
        lblNumber.setFont(new Font("default", Font.BOLD, 20));
        
        c.insets = new Insets(0, 0, 0, 8); // top left bottom right
        c.anchor = GridBagConstraints.PAGE_START;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(lblNumber, c);

        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.anchor = GridBagConstraints.LINE_START;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 0;
        panel.add(new JLabel(label), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.HORIZONTAL;
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
        contentPanel.setOpaque(false);
        
        this.btnUseSelectedMovies.setOpaque(false);
        this.btnUseSelectedMovies.setActionCommand(SmartCopyDialogController.CMD_USE_SELECTED_MOVIES);
        this.btnUseSelectedMovies.addActionListener(this.controller);

        this.inpMovieIds.setToolTipText("Enter somthing like: [[13, 42, 78]]");
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.anchor = GridBagConstraints.LINE_START;

        c.insets = new Insets(0, 0, 10, 0);
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        contentPanel.add(this.inpMovieIds, c);

        c.insets = new Insets(0, 0, 0, 0);
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
        this.inpTargetDirectory.addChooserListener(new IChooserListener() {
            public void doChoosen(File dir) {
                btnStartCopy.setEnabled(true);
            }
        });
        
        final JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(this.inpTargetDirectory, BorderLayout.WEST);
        contentPanel.setOpaque(false);
        
        JPanel panel = this._panel123(2, "Select a target directory:", contentPanel);
        return panel;
    }

    private JPanel panel3CopyButton() {
        this.btnStartCopy.setActionCommand(SmartCopyDialogController.CMD_START_COPY);
        this.btnStartCopy.addActionListener(this.controller);
        this.btnStartCopy.setOpaque(false);
        
        final JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.add(this.btnStartCopy, BorderLayout.WEST);
        contentPanel.setOpaque(false);
        
        JPanel panel = this._panel123(3, "Hit button and get a cup of tea:", contentPanel);
        return panel;
    }
    
    
    private JPanel panelProgress() {
        this.btnCancel.setOpaque(false);
        this.btnCancel.setEnabled(false);
        this.btnCancel.addActionListener(this.controller);
        this.btnCancel.setActionCommand(SmartCopyDialogController.CMD_CANCEL_COPY);
        
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        panel.setOpaque(false);

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(this.progressBar, c);
        
        c.insets = new Insets(0, 6, 0, 0);
        c.anchor = GridBagConstraints.LINE_END;
        c.gridx++;
        panel.add(this.btnCancel, c);

        return panel;
    }

    
    void fillPreprocessResultTable(SmartCopyPreprocessResult result) {
        
        if(result.isFatalError() == false && result.isMajorError() == false && result.isMinorError() == false) {
            LOG.debug("No error occured at all while preprocessing SmartCopy.");
            this.preprocessResultTableWrapper.removeAll();
            this.preprocessResultTableWrapper.revalidate();
            this.pack();
            return;
        }
        LOG.info("Preprocess found errors for smartcopy (would copy "+FileUtil.formatFileSize(result.getTotalCopySizeInKb())+").");
        
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setPreferredSize(new Dimension(this.getWidth() - 20, 140));
        panel.setOpaque(false);
        final PreprocessResultTableModel model = new PreprocessResultTableModel(result);
        final JXTable table = new MacLikeTable(model);
        table.setHorizontalScrollEnabled(true);
        table.getColumnModel().getColumn(0).setMinWidth(18);
        table.getColumnModel().getColumn(0).setPreferredWidth(18);
        table.getColumnModel().getColumn(0).setMaxWidth(18);
        
//        GuiUtil.setAlternatingBgColor(table);
//        table.setVisibleRowCount(4);
        table.packAll();
        
        final JScrollPane scrollPane = new JScrollPane(table);
        panel.add(new JLabel("Following errors occured:"), BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        final JPanel panelSouth = new JPanel();
        panelSouth.setOpaque(false);
        this.btnCopyAnyway.setOpaque(false);
        this.btnAbortCopy.setOpaque(false);
        this.btnCopyAnyway.setActionCommand(SmartCopyDialogController.CMD_START_COPY_ANYWAY);
        this.btnCopyAnyway.addActionListener(this.controller);
        
        this.btnAbortCopy.setActionCommand(SmartCopyDialogController.CMD_ABORT_COPY_ANYWAY);
        this.btnAbortCopy.addActionListener(this.controller);
        panelSouth.add(this.btnCopyAnyway);
        panelSouth.add(this.btnAbortCopy);
        panel.add(panelSouth, BorderLayout.SOUTH);
        
        if(result.isFatalError() == true || result.getTotalCopySizeInKb() == 0) {
            this.btnCopyAnyway.setEnabled(false);
            this.btnAbortCopy.setEnabled(false);
        }

        this.preprocessResultTableWrapper.removeAll();
        this.preprocessResultTableWrapper.add(panel);
        this.preprocessResultTableWrapper.revalidate();
        this.pack();
    }
    
    
    void doClose() {
        this.dispose();
    }
    
    void enableUiForCopy(boolean enableStateIdle) {
        this.btnStartCopy.setEnabled(enableStateIdle);
        this.btnUseSelectedMovies.setEnabled(enableStateIdle);
        this.inpMovieIds.setEnabled(enableStateIdle);
        this.inpTargetDirectory.setEnabled(enableStateIdle);
        this.btnCopyAnyway.setEnabled(enableStateIdle);
        this.btnAbortCopy.setEnabled(enableStateIdle);
        
        this.btnCancel.setEnabled(!enableStateIdle);
    }
    
    String getMovieIdString() {
        return this.inpMovieIds.getText().trim();
    }
    void setMovieIdString(String idString) {
        this.inpMovieIds.setText(idString);
    }
    
    File getTargetDirectory() {
        return this.inpTargetDirectory.getSelectedDirectory();
    }
    
    JProgressBar getProgressBar() {
        return this.progressBar;
    }
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            LOG.error("Unable to set system look&feel!", ex);
        }
        
        new SmartCopyDialog(null, null).setVisible(true);
    }
}
