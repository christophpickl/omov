package at.ac.tuwien.e0525580.omov.gui.comp;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import javax.swing.JButton;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class ButtonMovieFolder extends JButton implements MouseListener {

    private static final Log LOG = LogFactory.getLog(ButtonMovieFolder.class);
    private static final long serialVersionUID = -5849286761216157191L;

    private final Set<IButtonFolderListener> listeners = new HashSet<IButtonFolderListener>();
    private Timer clickTimer;
    private boolean doubleclick;

    private final Component owner;


    public ButtonMovieFolder(Component owner) {
        super(ImageFactory.getInstance().getIconFolder());
        this.owner = owner;
        this.setPreferredSize(new Dimension(ImageFactory.getInstance().getIconFolder().getIconWidth(),
                                            ImageFactory.getInstance().getIconFolder().getIconHeight()));
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setToolTipText("Choose Movie Folder");

//        this.addActionListener(this);
        this.addMouseListener(this);
        GuiUtil.enableHandCursor(this);
    }


//    public void actionPerformed(ActionEvent event) {
//        LOG.info("Clicked on button.");
//    }

    public void addButtonFolderListener(IButtonFolderListener listener) {
        this.listeners.add(listener);
    }

    public void removeButtonFolderListener(IButtonFolderListener listener) {
        this.listeners.remove(listener);
    }

    private void notifyListeners(File folder) {
        for (IButtonFolderListener listener : this.listeners) {
            if(folder == null) {
                listener.notifyFolderCleared();
            } else {
                listener.notifyFolderSelected(folder);
            }
        }
    }

    public static interface IButtonFolderListener {
        void notifyFolderSelected(File folder);
        void notifyFolderCleared();
    }


    private void doClearFolder() {
        this.notifyListeners(null);
    }

    private void doClicked() {
        final File directory = GuiUtil.getDirectory(this.owner, PreferencesDao.getInstance().getRecentMovieFolderPath());
        if (directory == null) {
            LOG.debug("Setting movie folder aborted by user.");
            return;
        }

        PreferencesDao.getInstance().setRecentMovieFolderPath(directory.getParent());
        this.notifyListeners(directory);

    }


    public void mouseClicked(MouseEvent event) {
        this.clickTimer = new Timer();
        if (event.getClickCount() == 2){
            this.doubleclick = true;
        } else if(event.getClickCount() == 1){
            this.doubleclick = false;
            this.clickTimer.schedule(new ClickTimerTask(), 300);
        }
    }
    public void mouseEntered(MouseEvent event) { /* nothing to do */ }
    public void mouseExited(MouseEvent event) { /* nothing to do */ }
    public void mousePressed(MouseEvent event) { /* nothing to do */ }
    public void mouseReleased(MouseEvent event) { /* nothing to do */ }


    class ClickTimerTask extends TimerTask {
        public void run() {
            if (doubleclick) {
//                System.out.println("doClearFolder();");
                doClearFolder();
            } else {
//                System.out.println("doClicked();");
                doClicked();
            }
            clickTimer.cancel();
        }
    }
}