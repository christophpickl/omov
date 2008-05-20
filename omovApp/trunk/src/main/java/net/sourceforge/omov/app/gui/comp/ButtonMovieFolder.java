/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.app.gui.comp;

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

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.core.PreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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


    private void doClearFolder() {
        this.notifyListeners(null);
    }

    public void doClicked() {
        final File directory = GuiUtil.getDirectory(this.owner, PreferencesDao.getInstance().getRecentMovieFolderPath());
        if (directory == null) {
            LOG.debug("Setting movie folder aborted by user.");
            return;
        }

        PreferencesDao.getInstance().setRecentMovieFolderPath(directory.getParent());
        this.notifyListeners(directory);
    }


    public void mouseClicked(MouseEvent event) {
    	if(event.getButton() == MouseEvent.BUTTON3) {
    		LOG.debug("Ignoring right mouseclick on button.");
    		return;
    	}
    	
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


    private class ClickTimerTask extends TimerTask {
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