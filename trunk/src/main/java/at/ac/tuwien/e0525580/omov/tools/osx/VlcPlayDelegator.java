package at.ac.tuwien.e0525580.omov.tools.osx;

import java.io.File;
import java.util.List;

import javax.swing.JMenuItem;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory.Icon16x16;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.BodyContext;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;
import at.ac.tuwien.e0525580.omov.util.UserSniffer.OS;

public class VlcPlayDelegator {

    private static final String SCRIPT =
        "set theFile to \"{0}\"\n" +
        "tell application \"VLC\"\n" +
            "OpenURL theFile\n" +
            "play\n" +
        "end tell";

    public static void addVlcPlayJMenuItem(final List<JMenuItem> menuItems, final String actionCommand) {
        if(UserSniffer.isOS(OS.MAC) == true) {
            BodyContext.newJMenuItem(menuItems, "Play in VLC", actionCommand, ImageFactory.getInstance().getIcon(Icon16x16.VLC));
        }
    }

    public static void playFile(final File file) throws BusinessException {
        assert(UserSniffer.isOS(OS.MAC));
        
        final String osaScript = VlcPlayDelegator.createOsaScript(file);
        AppleScriptNativeExecuter.executeAppleScript(osaScript);
    }
    
    private static String createOsaScript(final File file) {
        String script = SCRIPT;
        script = script.replaceAll("\\{0\\}", file.getAbsolutePath());
        return script;
    }
    
}
