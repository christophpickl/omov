package at.ac.tuwien.e0525580.omov.tools.osx;

import java.io.File;
import java.util.List;

import javax.swing.JMenuItem;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory.Icon16x16;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;

public class VlcPlayDelegator {

    private static final String SCRIPT =
        "set theFile to \"{0}\"\n" +
        "tell application \"VLC\"\n" +
            "OpenURL theFile\n" +
            "play\n" +
        "end tell";

    public static boolean isVlcCapable() {
        return (UserSniffer.isMacOSX() || UserSniffer.isWindows());
    }
    
    /**
     * @return null if this feature is not supported on running system
     */
    public static JMenuItem addVlcPlayJMenuItem(final List<JMenuItem> menuItems, final String actionCommand) {
        final JMenuItem item;
        if(isVlcCapable()) {
            item = BodyContext.newJMenuItem(menuItems, "Play in VLC", actionCommand, ImageFactory.getInstance().getIcon(Icon16x16.VLC));
        } else {
            item = null;
        }
        return item;
    }

    public static void playFile(final File file) throws BusinessException {
        assert(isVlcCapable());
        
        if(UserSniffer.isMacOSX() == true) {
            playFileMac(file);
        } else if(UserSniffer.isWindows() == true) {
            playFileWin(file);
        } else {
            throw new FatalException("Unhandled operating system: " + UserSniffer.getOS());
        }
    }
    
    private static void playFileMac(final File file) throws BusinessException {
        final String osaScript = SCRIPT.replaceAll("\\{0\\}", file.getAbsolutePath());
        AppleScriptNativeExecuter.executeAppleScript(osaScript);
    }
    
    private static void playFileWin(final File file) throws BusinessException {
        // FIXME implement me
    }
    
    
}
