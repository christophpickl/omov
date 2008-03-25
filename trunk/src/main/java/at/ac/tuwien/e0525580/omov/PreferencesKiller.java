package at.ac.tuwien.e0525580.omov;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesKiller {
    public static void main(String[] args) {
        final Preferences prefs = Preferences.userNodeForPackage(Configuration.class);
        try {
            prefs.clear();
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException("Could not clear preferences!", e);
        }
    }
}
