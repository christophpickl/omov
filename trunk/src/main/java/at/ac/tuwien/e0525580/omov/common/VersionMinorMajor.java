package at.ac.tuwien.e0525580.omov.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class VersionMinorMajor {

    private static final Log LOG = LogFactory.getLog(VersionMinorMajor.class);
    
    private final int minor;
    
    private final int major;
    
    private final String versionString;
    
    
    
    public VersionMinorMajor(final int minor, final int major) {
        this.minor = minor;
        this.major = major;
        this.versionString = minor + "." + major;
    }
    
    /**
     * @param versionString something like "0.1"
     */
    public VersionMinorMajor(String versionString) {
        LOG.debug("Creating version instance by string '"+versionString+"'.");
        String parts[] = versionString.split("\\.");
        int minor = Integer.parseInt(parts[0]);
        int major = Integer.parseInt(parts[1]);

        this.minor = minor;
        this.major = major;
        this.versionString = minor + "." + major;
    }
    
    
    public int getMajor() {
        return this.major;
    }
    
    public int getMinor() {
        return this.minor;
    }
    
    @Override
    public boolean equals(final Object object) {
        if( (object instanceof VersionMinorMajor) == false) {
            return false;
        }
        final VersionMinorMajor that = (VersionMinorMajor) object;
        return (this.minor == that.minor && this.major == that.major);
    }
    
    @Override
    public int hashCode() {
        return this.minor * 3 + this.major * 17;
    }
    
    public String getVersionString() {
        return this.versionString;
    }
    
    @Override
    public String toString() {
        return this.versionString;
    }
}