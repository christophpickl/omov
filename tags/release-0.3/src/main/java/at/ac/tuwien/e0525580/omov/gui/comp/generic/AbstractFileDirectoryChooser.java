package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

abstract class AbstractFileDirectoryChooser extends JPanel implements ActionListener {

    private static final Log LOG = LogFactory.getLog(AbstractFileDirectoryChooser.class);

    static final String DEFAULT_BUTTON_LABEL = "Set";
    
    static final ButtonPosition DEFAULT_BUTTON_POSITION = ButtonPosition.RIGHT;
    
    
    private File fileOrDir;
    
    private JTextField directoryPath = new JTextField(15);

    /** can be null */
    private File defaultPath;

    private final String dialogTitle;
    
    private final JButton button;

    private final Set<IChooserListener> listeners = new HashSet<IChooserListener>();
    
    
    
    public AbstractFileDirectoryChooser(String dialogTitle) {
        this(DEFAULT_BUTTON_LABEL, null, DEFAULT_BUTTON_POSITION, dialogTitle);
    }

    public AbstractFileDirectoryChooser(String dialogTitle, ButtonPosition position) {
        this(DEFAULT_BUTTON_LABEL, null, position, dialogTitle);
    }

    public AbstractFileDirectoryChooser(String dialogTitle, File defaultPath, ButtonPosition position) {
        this(DEFAULT_BUTTON_LABEL, defaultPath, position, dialogTitle);
    }
    
    public AbstractFileDirectoryChooser(String buttonLabel, File defaultPath, ButtonPosition position, String dialogTitle) {
        LOG.info("Constructing new directory chooser instance (buttonLabel="+buttonLabel+"; defaultPath="+(defaultPath == null ? "null" : defaultPath.getAbsolutePath())+").");
        assert(buttonLabel.length() > 0);
        
        this.defaultPath = defaultPath;
        this.directoryPath.setEditable(false);
        this.dialogTitle = dialogTitle;
        
        this.button = new JButton(buttonLabel);
        this.button.setOpaque(false);
        this.button.addActionListener(this);
        
        this.initComponents(position);
    }
    
    
    
    private void initComponents(final ButtonPosition position) {
        this.setOpaque(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(this, c);
        this.setLayout(layout);
        c.gridy = 0;

        c.insets = new Insets(0, 0, 0, 4);
        c.gridx = 0;
        this.add((position == ButtonPosition.LEFT) ? this.button : this.directoryPath);

        c.insets = new Insets(0, 0, 0, 0);
        c.gridx++;
        this.add((position == ButtonPosition.LEFT) ? this.directoryPath : this.button);
    }
    
    final void setFileOrDir(final File fileOrDir) {
        this.fileOrDir = fileOrDir;
        
        if(this.fileOrDir == null) {
            this.clearYourself();
        } else {
            if(this.fileOrDir.exists() == false || this.getIsRightFileOrDir(fileOrDir) == false) {
                JOptionPane.showMessageDialog(this, "The file at '"+fileOrDir.getAbsolutePath()+"' is invalid!", "", JOptionPane.WARNING_MESSAGE);
                this.clearYourself();
            } else {
                this.directoryPath.setText(this.fileOrDir.getAbsolutePath());
            }
        }
    }

    public final void __unchecked_setFileOrDir(final File directory) {
        this.fileOrDir = directory;
        this.directoryPath.setText(this.fileOrDir.getAbsolutePath());
    }
    
    // kann in zukunft zb auch folder-icon resetten
    private void clearYourself() {
        this.directoryPath.setText("");
    }
    
    /**
     * @return can be null
     */
    final File getFileOrDir() {
        return this.fileOrDir;
    }
    
    abstract boolean getIsRightFileOrDir(final File file);
    
    public final void setDefaultPath(File defaultPath) {
        this.defaultPath = defaultPath;
    }

    abstract int getSelectionMode();
    abstract FileFilter getFileFilter();
    
    public final void actionPerformed(ActionEvent event) {
        new GuiAction() { protected void _action() {
            LOG.debug("showing file chooser with default path '"+(defaultPath == null?"null":defaultPath.getAbsolutePath())+"'...");
            
            final JFileChooser chooser = new JFileChooser(defaultPath);
            chooser.setDialogTitle(dialogTitle);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(getSelectionMode());
            
            if(getFileFilter() != null) {
                chooser.setFileFilter(getFileFilter());
            }
            
            int returnVal = chooser.showOpenDialog(AbstractFileDirectoryChooser.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File selectedFileOrDir = chooser.getSelectedFile();
                assert(selectedFileOrDir != null);
                setFileOrDir(selectedFileOrDir);
                
                for (final IChooserListener listener : listeners) {
                    listener.doChoosen(selectedFileOrDir);
                }
            } else {
                LOG.debug("User canceled action.");
            }
        }}.doAction();
    }
    
    
    public final void addChooserListener(IChooserListener listener) {
        this.listeners.add(listener);
    }
    public final void removeDirectoryChooserListener(IChooserListener listener) {
        this.listeners.remove(listener);
    }
    
    @Override
    public final void setEnabled(boolean enabled) {
        this.button.setEnabled(enabled);
    }

    
}