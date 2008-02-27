package at.ac.tuwien.e0525580.omov2.gui.comp;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.Configuration;
import at.ac.tuwien.e0525580.omov2.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class ButtonMovieFolder extends JButton implements ActionListener {

    private static final Log LOG = LogFactory.getLog(ButtonMovieFolder.class);
    private static final long serialVersionUID = -5849286761216157191L;

    private final Set<IButtonFolderListener> listeners = new HashSet<IButtonFolderListener>();
    
    private final Component owner;
    public ButtonMovieFolder(Component owner) {
        super(ImageFactory.getInstance().getIconFolder());
        this.owner = owner;
        this.setOpaque(false);
        
        this.setBorderPainted(false);
        this.setToolTipText("Choose Movie Folder");
        
        this.addActionListener(this);
        GuiUtil.enableHandCursor(this);
    }
    
    
    public void actionPerformed(ActionEvent event) {
        LOG.info("Clicked on button.");
        File directory = GuiUtil.getDirectory(this.owner, Configuration.getInstance().getRecentMovieFolderPath());
        if(directory == null) return;
        
        Configuration.getInstance().setRecentMovieFolderPath(directory.getParent());
        this.notifyListeners(directory);
    }

    public void addButtonFolderListener(IButtonFolderListener listener) {
        this.listeners.add(listener);
    }
    
    public void removeButtonFolderListener(IButtonFolderListener listener) {
        this.listeners.remove(listener);
    }
    
    private void notifyListeners(File folder) {
        for (IButtonFolderListener listener : this.listeners) {
            listener.notifyFolderSelected(folder);
        }
    }
    
    public static interface IButtonFolderListener {
        void notifyFolderSelected(File folder);
    }
}