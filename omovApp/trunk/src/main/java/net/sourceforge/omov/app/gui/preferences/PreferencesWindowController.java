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

package net.sourceforge.omov.app.gui.preferences;

import net.sourceforge.omov.app.gui.FileSystemCheckDialog;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.tools.FileSystemChecker;
import net.sourceforge.omov.core.tools.FileSystemChecker.FileSystemCheckResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreferencesWindowController {

    private static final Log LOG = LogFactory.getLog(PreferencesWindowController.class);
    
    private final PreferencesWindow window;
    
    public PreferencesWindowController(PreferencesWindow window) {
        this.window = window;
    }
    
    public void doClearPreferences() throws BusinessException {
        PreferencesDao.clearPreferences();
    }
    
    public void doCheckApplicationVersion() {
        final VersionCheckDialog dialog = new VersionCheckDialog(this.window);
        dialog.startCheck();
        dialog.setVisible(true);
    }
    
    public void doCheckFileSystem() {
        LOG.info("User running filesystem check manually.");
        
        try {
            final FileSystemCheckResult result = FileSystemChecker.process();
            if(result.isEverythingOkay() == true) {
                GuiUtil.info(this.window, "File System Check", "Each and every file is at its the expected location.");
                
            } else { // (result.isEverythingOkay() == false)
                new FileSystemCheckDialog(result, this.window).setVisible(true);
            }
            
        } catch (BusinessException e) {
            LOG.error("File System Check failed!", e);
            GuiUtil.error("Filesystem Check failed", "Sorry, but could not perform the check because of an internal error.");
        }
    }
    
//    public boolean doStartServer(int port) {
//        try {
//            RemoteServer.getInstance().startUp(port);
//            return true;
//        } catch (BusinessException e) {
//            LOG.warn("Server startup at port '"+port+"' failed!", e);
//            JOptionPane.showMessageDialog(this.window, e.getMessage(), "Server startup failed!", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//    }
//    
//    public boolean doStopServer() {
//        try {
//            RemoteServer.getInstance().shutDown();
//            return true;
//        } catch (BusinessException e) {
//            GuiUtil.error(this.window, "Server shutdown failed", e.getMessage());
//            return false;
//        }
//    }
}
