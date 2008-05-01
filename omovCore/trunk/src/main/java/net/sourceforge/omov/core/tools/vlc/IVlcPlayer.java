package net.sourceforge.omov.core.tools.vlc;

import java.io.File;

import net.sourceforge.omov.core.BusinessException;



public interface IVlcPlayer {

	// VlcState getVlcStatus()
	// Playlist getPlaylist
	
	/**
	 * @return true if file was found in playlist and started, false otherwise.
	 */
	boolean playFile(final File file) throws BusinessException;
	
	void addFileToPlaylist(final File file) throws BusinessException;
	
}
