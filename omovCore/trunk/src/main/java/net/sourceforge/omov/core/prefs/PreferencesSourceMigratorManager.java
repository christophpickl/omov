package net.sourceforge.omov.core.prefs;

import java.util.HashMap;
import java.util.Map;

public final class PreferencesSourceMigratorManager {

	/** String ... "<VersionFrom>-<VersionTo>" */
	private static final Map<String, AbstractPreferencesSourceMigrator<?>> MAP =
		new HashMap<String, AbstractPreferencesSourceMigrator<?>>();
	
	static {
		final PreferencesSourceMigrator5To6 migrator5To6 = new PreferencesSourceMigrator5To6();
		MAP.put(getKeyByVersions(migrator5To6.getVersionFrom(), migrator5To6.getVersionTo()), migrator5To6);
	}
	
	private PreferencesSourceMigratorManager() {
		// not instantiable
	}
	
	
	public static boolean isMigratable(final int versionFrom, final int versionTo) {
		return MAP.containsKey(getKeyByVersions(versionFrom, versionTo));
	}
	
	public static AbstractPreferencesSourceMigrator<?> getMigrator(final int versionFrom, final int versionTo) {
		assert(isMigratable(versionFrom, versionTo));
		return MAP.get(getKeyByVersions(versionFrom, versionTo));
	}
	
	private static String getKeyByVersions(final int versionFrom, final int versionTo) {
		return versionFrom + "-" + versionTo;
	}
}
