package at.ac.tuwien.e0525580.omov.gui.smartfolder;

import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.model.ISmartFolderDao;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

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

            final SmartFolder addedSmartfolder = addWindow.getConfirmedObject();
            try {
                DAO.insertSmartFolder(addedSmartfolder);
            } catch (BusinessException e) {
                GuiUtil.error(this.dialog, "Insert Error", "Could not save SmartFolder '"+addedSmartfolder.getName()+"'!");
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
        try {
            DAO.deleteSmartFolder(folder);
        } catch (BusinessException e) {
            LOG.error("Deleting smartfolder failed: " + folder, e);
            GuiUtil.error(this.dialog, "Delete Error", "Could not delete SmartFolder '"+folder.getName()+"'!");
        }
    }
}
