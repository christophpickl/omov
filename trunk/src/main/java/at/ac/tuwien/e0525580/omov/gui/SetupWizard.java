package at.ac.tuwien.e0525580.omov.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.DirectoryChooser;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.IChooserListener;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;

public class SetupWizard extends JDialog {

    private static final Log LOG = LogFactory.getLog(SetupWizard.class);
    private static final long serialVersionUID = 4418685920529669271L;

    private final JTextField inpUsername = new JTextField(21);
    
    private final DirectoryChooser inpFolderTemporary = new DirectoryChooser("Choose Temporary Folder");
    
    private final DirectoryChooser inpFolderCovers = new DirectoryChooser("Choose Covers Folder");

    private final DirectoryChooser inpFolderData = new DirectoryChooser("Choose Data Folder");

    private boolean isConfirmed = false;
    
    
    public SetupWizard() {
        this.setModal(true);
        this.setTitle("OurMovies Setup Wizard");

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });
        
//        this.setPreferredSize(new Dimension(600, 400));
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
        GuiUtil.lockOriginalSizeAsMinimum(this);
        
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout(0, 15));

        panel.add(this.panelNorth(), BorderLayout.NORTH);
        panel.add(this.panelCenter(), BorderLayout.CENTER);
        panel.add(this.panelSouth(), BorderLayout.SOUTH);

        return panel;
    }

    private JPanel panelNorth() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 0, 0));
        panel.setBackground(Color.WHITE);
        final JLabel lbl = new JLabel(ImageFactory.getInstance().getSetupWizardBanner(), JLabel.RIGHT);
        panel.add(lbl);
        return panel;
    }

    private JPanel panelCenter() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 20));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        panel.setLayout(layout);
        layout.setConstraints(panel, c);

        this.inpFolderTemporary.__unchecked_setFileOrDir(new File("temp"));
        this.inpFolderCovers.__unchecked_setFileOrDir(new File("covers"));
        this.inpFolderData.__unchecked_setFileOrDir(new File("data"));

        this.inpFolderTemporary.addChooserListener(new IChooserListener() { public void doChoosen(File dir) {
            inpFolderCovers.setDefaultPath(dir.getParentFile());
            inpFolderData.setDefaultPath(dir.getParentFile());
        }});
        this.inpFolderCovers.addChooserListener(new IChooserListener() { public void doChoosen(File dir) {
            inpFolderTemporary.setDefaultPath(dir.getParentFile());
            inpFolderData.setDefaultPath(dir.getParentFile());
        }});
        this.inpFolderData.addChooserListener(new IChooserListener() { public void doChoosen(File dir) {
            inpFolderTemporary.setDefaultPath(dir.getParentFile());
            inpFolderCovers.setDefaultPath(dir.getParentFile());
        }});
        
        c.anchor = GridBagConstraints.FIRST_LINE_START;

        final Insets insetColLeft  = new Insets(10, 0, 0, 28);
        final Insets insetColRight = new Insets(10, 0, 0,  0);
        
        c.gridy = 0;
        c.gridx = 0;
        c.insets = insetColLeft;
        panel.add(new JLabel("Username"), c);
        c.gridx = 1;
        c.insets = insetColRight;
        panel.add(this.inpUsername, c);

        if(UserSniffer.isMacOSX() == true) {
            final File omovAppSupport = Constants.getOsxApplicationSupportFolder();
            
            final File coversFolder = new File(omovAppSupport, "covers");
            final File tempFolder = new File(omovAppSupport, "temp");
            final File dataFolder = new File(omovAppSupport, "data");
            
            this.inpFolderCovers.__unchecked_setFileOrDir(coversFolder);
            this.inpFolderTemporary.__unchecked_setFileOrDir(tempFolder);
            this.inpFolderData.__unchecked_setFileOrDir(dataFolder);
        } else {
            c.gridy++;
            c.gridx = 0;
            c.insets = insetColLeft;
            panel.add(new JLabel("Temporary Folder"), c);
            c.gridx = 1;
            c.insets = insetColRight;
            panel.add(this.inpFolderTemporary, c);
    
            c.gridy++;
            c.gridx = 0;
            c.insets = insetColLeft;
            panel.add(new JLabel("Cover Folder"), c);
            c.gridx = 1;
            c.insets = insetColRight;
            panel.add(this.inpFolderCovers, c);
    
            c.gridy++;
            c.gridx = 0;
            c.insets = insetColLeft;
            panel.add(new JLabel("Data Folder"), c);
            c.gridx = 1;
            c.insets = insetColRight;
            panel.add(this.inpFolderData, c);
        }

//        c.gridy++;
//        c.gridx = 0;
//        panel.add(new JLabel("some other"), c);
//        c.gridx = 1;
//        panel.add(new JLabel("---"), c);

        return panel;
    }
    
    private JPanel panelSouth() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        final JButton btnConfirm = new JButton("Setup");
        final JButton btnCancel = new JButton("Cancel");

        btnConfirm.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doConfirm();
        }});
        btnCancel.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doCancel();
        }});
        
        this.getRootPane().setDefaultButton(btnConfirm);
        
        panel.add(btnCancel);
        panel.add(btnConfirm);

        return panel;
    }
    
    private boolean validateInput() {
        final List<String> warnings = new LinkedList<String>();
        
        if(this.inpUsername.getText().length() == 0) {
            warnings.add("The username may not be empty.");
        }
        
        if(warnings.size() > 0) {
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < warnings.size(); i++) {
                if(i != 0) sb.append("\n");
                sb.append(warnings.get(i));
            }
            GuiUtil.warning(this, "Invalid Input", sb.toString());
        }
        
        return warnings.size() == 0;
    }
    
    private void doCancel() {
        this.dispose();
        System.exit(0);
    }
    
    private void doConfirm() {
        if(this.validateInput() == false) {
            return;
        }
        
        this.isConfirmed = true;

        final String folderCovers = this.setupFolder(this.inpFolderCovers.getSelectedDirectory());
        final String folderTemporary = this.setupFolder(this.inpFolderTemporary.getSelectedDirectory());
        final String folderData = this.setupFolder(this.inpFolderData.getSelectedDirectory());
        final String username = this.inpUsername.getText();
        
        final boolean startupVersionCheck = false; // by default
        
        // finally store entered values in preferences source
        PreferencesDao.getInstance().setPreferences(folderCovers, folderTemporary, folderData, username, startupVersionCheck);
        
        this.dispose();
    }
    
    private String setupFolder(File folder) {
        if(folder.exists() == false) {
            LOG.info("Creating folder '"+folder.getAbsolutePath()+"'.");
            if(folder.mkdirs() == false) {
                throw new FatalException("Could not create folder '"+folder.getAbsolutePath()+"'!");
            }
        } else {
            if(folder.isDirectory() == false) {
                throw new FatalException("The folder '"+folder.getAbsolutePath()+"' is not a directory!");
            }
        }
        return folder.getAbsolutePath();
    }
    
    public boolean isConfirmed() {
        return this.isConfirmed;
    }
}
