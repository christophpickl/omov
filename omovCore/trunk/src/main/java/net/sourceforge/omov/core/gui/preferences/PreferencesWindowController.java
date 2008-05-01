package net.sourceforge.omov.core.gui.preferences;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.gui.FileSystemCheckDialog;
import net.sourceforge.omov.core.tools.FileSystemChecker;
import net.sourceforge.omov.core.tools.FileSystemChecker.FileSystemCheckResult;
import net.sourceforge.omov.core.util.GuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
