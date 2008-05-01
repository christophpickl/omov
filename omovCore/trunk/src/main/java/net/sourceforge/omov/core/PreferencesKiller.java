package net.sourceforge.omov.core;

import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

public class PreferencesKiller {
    public static void main(String[] args) {
        final Preferences prefs = Preferences.userNodeForPackage(PreferencesDao.class);
        try {
            prefs.clear();
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new RuntimeException("Could not clear preferences!", e);
        }
    }
}
