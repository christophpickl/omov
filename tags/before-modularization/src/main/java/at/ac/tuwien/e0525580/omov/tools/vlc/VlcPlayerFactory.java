package at.ac.tuwien.e0525580.omov.tools.vlc;

import at.ac.tuwien.e0525580.omov.util.UserSniffer;

public final class VlcPlayerFactory {

	private VlcPlayerFactory() {
		// no instantiation
	}

    public static boolean isVlcCapable() {
//        return (UserSniffer.isMacOSX() || UserSniffer.isWindows()); // --> wait until VLC release 0.9.0 (JVLC will be supported, i think...)
        return UserSniffer.isMacOSX();
    }
    
	public static IVlcPlayer newVlcPlayer() {
		if(UserSniffer.isMacOSX() == true) {
			assert(isVlcCapable() == true);
			return new MacVlcPlayer();
		}
		
		assert(isVlcCapable() == false);
		return null;
	}
}
