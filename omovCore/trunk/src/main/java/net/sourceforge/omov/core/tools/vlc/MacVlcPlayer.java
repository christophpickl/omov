package net.sourceforge.omov.core.tools.vlc;

import java.io.File;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.tools.osx.AppleScriptNativeExecuter;

/*
FEATURE vlc integration for windows

- needs some jvlc.dll and libvlc.dll library
- jvlc download page: http://jvlc.ihack.it/releases/
*/
class MacVlcPlayer extends WebinterfaceVlcPlayer implements IVlcPlayer {

    private static final String APPLE_SCRIPT_OPEN =
        "set theFile to \"{0}\"\n" +
        "tell application \"VLC\"\n" +
            "OpenURL theFile\n" +
        "end tell";
    
    private static final String APPLE_SCRIPT_ACTIVATE =
        "tell application \"VLC\"\n" +
            "activate\n" +
        "end tell";

    
	public void addFileToPlaylist(final File file) throws BusinessException {
		final String osaScript = APPLE_SCRIPT_OPEN.replaceAll("\\{0\\}", file.getAbsolutePath());
        AppleScriptNativeExecuter.executeAppleScript(osaScript);
	}
    
	@Override
    public boolean playFile(final File file) throws BusinessException {
    	final boolean result = super.playFile(file);
    	AppleScriptNativeExecuter.executeAppleScript(APPLE_SCRIPT_ACTIVATE);
    	return result;
    }
}
