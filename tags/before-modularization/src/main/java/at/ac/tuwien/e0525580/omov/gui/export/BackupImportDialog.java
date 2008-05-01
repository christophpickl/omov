package at.ac.tuwien.e0525580.omov.gui.export;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ButtonPosition;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.FileChooser;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.IChooserListener;
import at.ac.tuwien.e0525580.omov.tools.export.ImportExportConstants;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class BackupImportDialog extends JDialog implements ActionListener {

    private static final Log LOG = LogFactory.getLog(BackupImportDialog.class);
    private static final long serialVersionUID = 2471103972559399413L;

    private static final String CMD_IMPORT = "CMD_IMPORT";
    private static final String CMD_CLOSE = "CMD_CLOSE";
    // MANTIS [26] backup import: display list of movies which are stored in choosen backup file and let user select (checkbox) which movies should be imported
    
    private final BackupImportController controller;

    private final FileChooser inpFileChooser = new FileChooser("Open", "Choose BackupFile", null, ButtonPosition.LEFT, ImportExportConstants.BACKUP_FILE_EXTENSION);
    private final JProgressBar progressBar = new JProgressBar();
    private final JButton btnImport = new JButton("Import");
    private final JButton btnClose = new JButton("Close");
    
    public BackupImportDialog(JFrame owner) {
        super(owner, "Import Backup", true);
        
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.controller = new BackupImportController(this);
        
        this.inpFileChooser.addChooserListener(new IChooserListener() {
            public void doChoosen(final File backupFile) {
                new GuiAction() {
                    @Override
                    protected void _action() {
                        btnImport.setEnabled(true);
                    }
                }.doAction();
            }
        });
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    public void setZipFile(File backupFile) {
        assert(FileUtil.extractExtension(backupFile).equalsIgnoreCase(ImportExportConstants.BACKUP_FILE_EXTENSION)); // assert *.omo extension
        
        this.inpFileChooser.setFile(backupFile);
        this.btnImport.setEnabled(true);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Constants.getColorWindowBackground());
        
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(this.northPanel(), c);
        
        c.insets = new Insets(8, 0, 0, 0);
        c.gridy = 1;
        panel.add(this.southPanel(), c);

        return panel;
    }

    private JPanel northPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        panel.add(this.inpFileChooser, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel southPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        
        panel.setOpaque(false);
        this.progressBar.setOpaque(false);
        this.btnImport.setOpaque(false);
        this.btnClose.setOpaque(false);
        
        this.btnImport.addActionListener(this);
        this.btnImport.setActionCommand(CMD_IMPORT);
        this.btnImport.setEnabled(false);
        
        this.btnClose.addActionListener(this);
        this.btnClose.setActionCommand(CMD_CLOSE);
        
        panel.add(this.btnImport, BorderLayout.WEST);
        panel.add(this.progressBar, BorderLayout.CENTER);
        panel.add(this.btnClose, BorderLayout.EAST);

        return panel;
    }
    
    void enableInterface(final boolean enabled) {
        this.inpFileChooser.setEnabled(enabled);
        this.btnImport.setEnabled(enabled);
        this.btnClose.setEnabled(enabled);
        this.progressBar.setIndeterminate(!enabled);
        this.setDefaultCloseOperation(enabled ? JFrame.DISPOSE_ON_CLOSE : JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void actionPerformed(final ActionEvent event) {
        new GuiAction() {
            @Override
            protected void _action() {
                final String cmd = event.getActionCommand();
                LOG.debug("Action performed with command '"+cmd+"'.");
                
                if(cmd.equals(CMD_IMPORT)) {
                    controller.doImportBackup(inpFileChooser.getSelectedFile());
                } else if(cmd.equals(CMD_CLOSE)) {
                    BackupImportDialog.this.dispose();
                } else {
                    throw new FatalException("Unhandled action command '"+cmd+"'!");
                }
            }
        }.doAction();
    }
    
}
