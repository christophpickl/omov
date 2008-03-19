package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.awt.BorderLayout;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DirectoryChooser extends JPanel implements ActionListener{

    private static final Log LOG = LogFactory.getLog(DirectoryChooser.class);
    
    private static final long serialVersionUID = -3831195688364368872L;
    
    private File directory;
    
    private JTextField directoryPath = new JTextField(15);

    private File defaultPath;

    private final String dialogTitle;
    
    public static enum ButtonPosition {
        LEFT, RIGHT;
    }

    private static final String DEFAULT_BUTTON_LABEL = "Set";
    
    private static final ButtonPosition DEFAULT_BUTTON_POSITION = ButtonPosition.RIGHT;
    
    public static DirectoryChooser newSimple(String dialogTitle) {
        return new DirectoryChooser(DEFAULT_BUTTON_LABEL, null, DEFAULT_BUTTON_POSITION, dialogTitle);
    }

    public static DirectoryChooser newPosition(String dialogTitle, ButtonPosition position) {
        return new DirectoryChooser(DEFAULT_BUTTON_LABEL, null, position, dialogTitle);
    }

    public static DirectoryChooser newPathAndPosition(String dialogTitle, File defaultPath, ButtonPosition position) {
        return new DirectoryChooser(DEFAULT_BUTTON_LABEL, defaultPath, position, dialogTitle);
    }
    
    public DirectoryChooser(String buttonLabel, File defaultPath, ButtonPosition position, String dialogTitle) {
        LOG.info("Constructing new directory chooser instance (buttonLabel="+buttonLabel+"; defaultPath="+(defaultPath == null ? "null" : defaultPath.getAbsolutePath())+").");
        this.defaultPath = defaultPath;
        this.directoryPath.setEditable(false);
        this.dialogTitle = dialogTitle;
        
        final JButton button = new JButton(buttonLabel);
        button.setOpaque(false);
        button.addActionListener(this);
        
        final JPanel panel = new JPanel(new BorderLayout(4, 0));
        panel.setOpaque(false);
        panel.add(this.directoryPath, position == ButtonPosition.LEFT ? BorderLayout.EAST : BorderLayout.WEST);
        panel.add(button, position == ButtonPosition.LEFT ? BorderLayout.WEST : BorderLayout.EAST);
        this.add(panel);
    }
    
    public void setDirectory(final File directory) {
        this.directory = directory;
        
        if(this.directory == null) {
            this.clearYourself();
        } else {
            if(this.directory.exists() == false || this.directory.isDirectory() == false) {
                JOptionPane.showMessageDialog(this, "Folder at '"+directory.getAbsolutePath()+"' must be an existing directory!", "", JOptionPane.WARNING_MESSAGE);
                this.clearYourself();
            } else {
                this.directoryPath.setText(this.directory.getAbsolutePath());
            }
        }
    }

    public void __unchecked_setDirectory(final File directory) {
        this.directory = directory;
        this.directoryPath.setText(this.directory.getAbsolutePath());
    }
    
    // kann in zukunft zb auch folder-icon resetten
    private void clearYourself() {
        this.directoryPath.setText("");
    }
    
    /**
     * @return can be null
     */
    public File getDirectory() {
        return this.directory;
    }
    
    public void setDefaultPath(File defaultPath) {
        this.defaultPath = defaultPath;
    }
    
    public void actionPerformed(ActionEvent event) {
        LOG.debug("showing file chooser with default path '"+(defaultPath == null?"null":defaultPath.getAbsolutePath())+"'...");
        JFileChooser chooser = new JFileChooser(this.defaultPath);
        
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
//        chooser.setFileFilter(new FileFilter() {
//            @Override
//            public boolean accept(File file) {
//                return file.isDirectory();
//            }
//            @Override
//            public String getDescription() {
//                // TO DO: use message bundle
//                return "directories containing movie files";
//            }
//        });
        chooser.setDialogTitle(this.dialogTitle);
        
        
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = chooser.getSelectedFile();
            assert(selectedFile != null);
            this.setDirectory(selectedFile);
            
            for (IDirectoryChooserListener listener : this.listeners) {
                listener.choosenDirectory(selectedFile);
            }
        }
    }
    
    
    private final Set<IDirectoryChooserListener> listeners = new HashSet<IDirectoryChooserListener>();
    
    public void addDirectoryChooserListener(IDirectoryChooserListener listener) {
        this.listeners.add(listener);
    }
    public void removeDirectoryChooserListener(IDirectoryChooserListener listener) {
        this.listeners.remove(listener);
    }
    
    public static interface IDirectoryChooserListener {
        /**
         * gets invoked if user has choosen directory and approved operation.
         * @param dir is never null
         */
        void choosenDirectory(File dir);
    }
    
}
