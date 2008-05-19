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

package net.sourceforge.omov.core.tools.vlc;

import java.io.File;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.tools.osx.AppleScriptNativeExecuter;

/*
FEATURE vlc integration for windows

- needs some jvlc.dll and libvlc.dll library
- jvlc download page: http://jvlc.ihack.it/releases/
*/

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
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
