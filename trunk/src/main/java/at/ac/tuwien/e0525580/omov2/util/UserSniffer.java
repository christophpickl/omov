package at.ac.tuwien.e0525580.omov2.util;

import org.apache.log4j.Logger;

/**
 * 
 * @author Christoph Pickl - e0525580@student.tuwien.ac.at
 */
public final class UserSniffer {

    /** class' own logger using log4j */
    private static final Logger LOG = Logger.getLogger(UserSniffer.class);

    /**  */
    public enum OS { WIN, MAC, LINUX, UNIX, SOLARIS, OS2, UNKOWN };

    /**  */
    private static OS os;
    
    static {
        final String osname = System.getProperty("os.name");
        
        if (osname.contains("Mac")) {
            UserSniffer.os = UserSniffer.OS.MAC;
        } else if (osname.contains("Windows")) {
            UserSniffer.os = UserSniffer.OS.WIN;
        } else if (osname.contains("Linux")) {
            UserSniffer.os = UserSniffer.OS.LINUX;
        } else if (osname.contains("Unix") || osname.contains("UNIX")  || osname.contains("HP-UX")
                || osname.contains("AIX") || osname.contains("BSD") || osname.contains("Irix")) {
            UserSniffer.os = UserSniffer.OS.UNIX;
        } else if (osname.contains("SunOS") || osname.contains("Solaris")) {
            UserSniffer.os = UserSniffer.OS.SOLARIS;
        } else if (osname.contains("OS/2")) {
            UserSniffer.os = UserSniffer.OS.OS2;
        } else {
            UserSniffer.LOG.warn("could not determine operating system by name [" + osname + "]");
            UserSniffer.os = UserSniffer.OS.UNKOWN;
        }
        LOG.debug("seems as user is running '" + UserSniffer.os + "'.");
    }
    
    /**
     * 
     * @return
     */
    public static OS getOS() {
        return UserSniffer.os;
    }
    
    /**
     * 
     * @param os
     * @return
     */
    public static boolean isOS(OS os) {
        return UserSniffer.os.equals(os);
    }
}
