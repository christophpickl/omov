package at.ac.tuwien.e0525580.omov.tools.vlc;

import java.io.File;

import at.ac.tuwien.e0525580.omov.BusinessException;



public interface IVlcPlayer {

	// VlcState getVlcStatus()
	// Playlist getPlaylist
	
	/**
	 * @return true if file was found in playlist and started, false otherwise.
	 */
	boolean playFile(final File file) throws BusinessException;
	
	void addFileToPlaylist(final File file) throws BusinessException;
	
}
