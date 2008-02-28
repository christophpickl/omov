package at.ac.tuwien.e0525580.omov;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class __PreferencesKiller__ {
    
    public static void main(String[] args) throws BackingStoreException {
        System.out.println("getting preferences...");
        final Preferences prefs = Preferences.userNodeForPackage(Configuration.class);
        System.out.println("clearing...");
        prefs.clear();
        System.out.println("flushing...");
        prefs.flush();
        System.out.println("finished");
    }
}
