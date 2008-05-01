package net.sourceforge.omov.core.tools.osx;

import java.io.File;
import java.util.List;

import javax.swing.JMenuItem;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.gui.ImageFactory;
import net.sourceforge.omov.core.gui.ImageFactory.Icon16x16;
import net.sourceforge.omov.core.gui.comp.generic.BodyContext;
import net.sourceforge.omov.core.util.UserSniffer;
import net.sourceforge.omov.core.util.UserSniffer.OS;

import org.apache.log4j.Logger;

public class FinderReveal {

    private static final Logger LOG = Logger.getLogger(FinderReveal.class);
    
    private static final String SCRIPT =
        "set theFile to \"{0}\"\n" +
        "tell application \"Finder\"\n" +
            "reveal theFile\n" +
            "activate\n" + 
        "end tell";
    
    public static JMenuItem addRevealJMenuItem(final List<JMenuItem> menuItems, final String actionCommand) {
        final JMenuItem item;
        if(UserSniffer.isOS(OS.MAC) == true) {
            item = BodyContext.newJMenuItem(menuItems, "Reveal in Finder", actionCommand, ImageFactory.getInstance().getIcon(Icon16x16.REVEAL_FINDER));
        } else {
            item = null;
        }
        return item;
    }
    
    public static void revealFile(final File file) throws BusinessException {
        assert(UserSniffer.isOS(OS.MAC));
        
        final String osaScript = FinderReveal.createOsaScript(file);
        AppleScriptNativeExecuter.executeAppleScript(osaScript);
    }
    
    private static String createOsaScript(final File file) throws BusinessException {
        final String macFilePath = AppleScriptNativeExecuter.convertUnix2ApplePath(file);
        LOG.debug("Creating osa script for mac file path '"+macFilePath+"'.");
        String script = SCRIPT;
        script = script.replaceAll("\\{0\\}", macFilePath);
        return script;
    }
    
    public static void main(String[] args) throws BusinessException {
//        final String s = "/Volumes/MEGADISK/Movies_Holy/300";
        final String s = "/Users/phudy/Movies/_duplicate/Juno";
        final File f = new File(s);
        revealFile(f);
    }
}
