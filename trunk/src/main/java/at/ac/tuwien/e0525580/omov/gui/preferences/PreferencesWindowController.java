package at.ac.tuwien.e0525580.omov.gui.preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.PreferencesDao;

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
