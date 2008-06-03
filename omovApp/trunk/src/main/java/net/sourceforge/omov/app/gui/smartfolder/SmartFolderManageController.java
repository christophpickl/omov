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

package net.sourceforge.omov.app.gui.smartfolder;

import javax.swing.JFrame;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.model.ISmartFolderDao;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class SmartFolderManageController {

    private static final Log LOG = LogFactory.getLog(SmartFolderManageController.class);

    private static final ISmartFolderDao DAO = BeanFactory.getInstance().getSmartFolderDao();
    private final SmartFolderManageDialog dialog;
    private final JFrame owner;
    
    SmartFolderManageController(SmartFolderManageDialog dialog, JFrame owner) {
        this.dialog = dialog;
        this.owner = owner;
    }
    
    private JFrame getOwner() {
        return this.owner;
    }
    
    void doAddSmartFolder() {
        AddEditSmartFolderDialog addWindow = new AddEditSmartFolderDialog(this.getOwner(), null);
        addWindow.setVisible(true);
        if(addWindow.isActionConfirmed()) {
            LOG.info("Confirmed adding new smartfolder.");

            final SmartFolder confirmedSmartfolder = addWindow.getConfirmedObject();
            try {
                final SmartFolder addedSmartfolder = DAO.insertSmartFolder(confirmedSmartfolder);
                this.dialog.setSelectedSmartFolder(addedSmartfolder);
            } catch (BusinessException e) {
                GuiUtil.error(this.dialog, "Insert Error", "Could not save SmartFolder '"+confirmedSmartfolder.getName()+"'!");
            }
        }
    }
    
    void doEditSmartFolder(final SmartFolder folder) {
        LOG.info("Editing smartfolder: " + folder);
        AddEditSmartFolderDialog editWindow = new AddEditSmartFolderDialog(this.getOwner(), folder);
        editWindow.setVisible(true);
        if(editWindow.isActionConfirmed()) {
            LOG.info("Confirmed editing smartfolder.");
            
            final SmartFolder editedSmartfolder = editWindow.getConfirmedObject();
            try {
                DAO.updateSmartFolder(editedSmartfolder);
            } catch (BusinessException e) {
                GuiUtil.error(this.dialog, "Update Error", "Could not update SmartFolder '"+editedSmartfolder.getName()+"'!");
            }
        }
    }
    
    void doDeleteSmartFolder(final SmartFolder folder) {
    	if(GuiUtil.getYesNoAnswer(this.owner, "Delete SmartFolder", "Do you really want to delete '"+folder.getName()+"'?") == false) {
    		return;
    	}
    		
        try {
            DAO.deleteSmartFolder(folder);
        } catch (BusinessException e) {
            LOG.error("Deleting smartfolder failed: " + folder, e);
            GuiUtil.error(this.dialog, "Delete Error", "Could not delete SmartFolder '"+folder.getName()+"'!");
        }
    }
}
