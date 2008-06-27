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

import javax.swing.Icon;
import javax.swing.JButton;

import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.util.OmovGuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class FolderChooseButton extends JButton implements MouseListener {

    private static final Log LOG = LogFactory.getLog(FolderChooseButton.class);
    
    private static final long serialVersionUID = -5849286761216157191L;

    
    private final Set<IFolderChooseListener> listeners = new HashSet<IFolderChooseListener>();
    
    private Timer clickTimer;
    
    private boolean doubleclick;

    private final Component owner;

    private String initialPath;
    
    

    public FolderChooseButton(Component owner) {
    	this(owner, AppImageFactory.getInstance().getIconFolder());
    }

    public FolderChooseButton(Component owner, Icon icon) {
        super(icon);
        this.owner = owner;
        this.setPreferredSize(new Dimension(AppImageFactory.getInstance().getIconFolder().getIconWidth(),
                                            AppImageFactory.getInstance().getIconFolder().getIconHeight()));
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setToolTipText("<html>Single click: Choose Folder<br>Double click: Clear Folder</html>");

        this.addMouseListener(this);
        OmovGuiUtil.enableHandCursor(this);
    }


    public void addFolderChooseListener(IFolderChooseListener listener) {
        this.listeners.add(listener);
    }

    public void removeFolderChooseListener(IFolderChooseListener listener) {
        this.listeners.remove(listener);
    }

    private void notifyListeners(File folder) {
        for (IFolderChooseListener listener : this.listeners) {
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

    public void setInitialPath(String initialPath) {
    	this.initialPath = initialPath;
    }
    
    public void doClicked() {
        final File directory = OmovGuiUtil.getDirectory(this.owner, this.initialPath);
        if (directory == null) {
            LOG.debug("Setting movie folder aborted by user.");
            return;
        }

        this.initialPath = directory.getParent(); // automatically stores most recent path
        this.notifyListeners(directory);
    }
    
    
    


    public void mouseClicked(MouseEvent event) {
    	if(this.isEnabled() == false) {
    		LOG.debug("Ignoring mouseclick on folder button because component is disabled.");
    		return;
    	}
    	
    	if(event.getButton() == MouseEvent.BUTTON3) {
    		LOG.debug("Ignoring right mouseclick on folder button.");
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
                doClearFolder();
            } else {
                doClicked();
            }
            clickTimer.cancel();
        }
    }
}