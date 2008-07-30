package net.sourceforge.omov.core.prefs;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public abstract class AbstractPreferencesSourceMigrator<T extends AbstractPreferencesData> {

    private static final Log LOG = LogFactory.getLog(AbstractPreferencesSourceMigrator.class);
    
	@SuppressWarnings("unchecked")
	public final void migrate() {
		LOG.info("Migrating preferences source by migrator: " + this.getClass().getName());
		
		final PreferencesDao dao = PreferencesDao.getInstance();
		dao.loadPreferences();
		final T newData = this.internalMigrate(dao);
		LOG.debug("Converted new data: " + newData);
		
		dao.setPreferencesByMigrator(newData);
	}
	
	abstract T internalMigrate(final PreferencesDao dao);
	
	abstract int getVersionFrom();
	
	abstract int getVersionTo();
}
