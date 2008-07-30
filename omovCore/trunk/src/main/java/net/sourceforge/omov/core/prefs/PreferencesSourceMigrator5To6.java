package net.sourceforge.omov.core.prefs;

import net.sourceforge.omov.core.util.LanguageUtil.LanguageCode;



public class PreferencesSourceMigrator5To6 extends AbstractPreferencesSourceMigrator<net.sourceforge.omov.core.prefs.v5.PreferencesData> {

	@Override
	net.sourceforge.omov.core.prefs.v5.PreferencesData internalMigrate(final PreferencesDao dao) {
		
		return new net.sourceforge.omov.core.prefs.v5.PreferencesData(
				dao.getCoversFolder().getAbsolutePath(),
				dao.getTemporaryFolder().getAbsolutePath(),
				dao.getDataFolder().getAbsolutePath(),
				dao.getUsername(),
				dao.getRecentExportDestination(),
				dao.getRecentCoverSelectorPath(),
				dao.getRecentMovieFolderPath(),
				dao.getRecentScanPath(),
				dao.getRecentBackupImportPath(),
				dao.isStartupVersionCheck(),
				dao.isStartupFilesystemCheck(),
				dao.getColumnsVisible(),
				dao.getProxyHost(),
				dao.getProxyPort(),
				dao.isProxyEnabled(),
				LanguageCode.ENGLISH.getCode() // new value for v5
				);
	}

	@Override
	public int getVersionFrom() {
		return net.sourceforge.omov.core.prefs.v4.PreferencesData.DATA_VERSION;
	}

	@Override
	public int getVersionTo() {
		return net.sourceforge.omov.core.prefs.v5.PreferencesData.DATA_VERSION;
	}

}
