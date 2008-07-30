/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.logic.prefs;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.LanguageCode;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.logic.MovieTableColumns;
import net.sourceforge.omov.logic.prefs.v5.PreferencesData;
import net.sourceforge.omov.logic.util.LanguageUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreferencesDao {

    private static final Log LOG = LogFactory.getLog(PreferencesDao.class);
    private static final PreferencesDao INSTANCE = new PreferencesDao();
    private final Preferences prefs = Preferences.userNodeForPackage(PreferencesDao.class);
    
    private static final String MOVIE_COLUMN_PREFIX = "MovieColumn-";


    
    private static final boolean DEFAULT_STARTUP_VERSION_CHECK = true;
    
    private PreferencesData data;
    
    
    private PreferencesDao() {
        if(this.getStoredVersion() == PreferencesData.DATA_VERSION) {
            this.loadPreferences();
        }
    }
    
    public static int getPreferencesDataVersion() {
    	return net.sourceforge.omov.logic.prefs.v5.PreferencesData.DATA_VERSION;
    }
    
    void setPreferencesByMigrator(final AbstractPreferencesData data) {
    	this.setPreferences(PreferencesData.class.cast(data));
    }
    
    public void setPreferences(final PreferencesData data) {
        LOG.info("Setting preferences: " + data);
        
        assert(data.getFolderCovers() != null && data.getFolderTemporary() != null && data.getUsername() != null);
        assert(new File(data.getFolderCovers()).exists() && new File(data.getFolderTemporary()).exists());
        
        this.prefs.put(PrefKey.FOLDER_COVERS.name(), data.getFolderCovers());
        this.prefs.put(PrefKey.FOLDER_TEMPORARY.name(), data.getFolderTemporary());
        this.prefs.put(PrefKey.FOLDER_DATA.name(), data.getFolderData());
        this.prefs.put(PrefKey.USERNAME.name(), data.getUsername());

        this.prefs.put(PrefKey.IS_CONFIGURED.name(), String.valueOf(PreferencesData.DATA_VERSION));
        this.prefs.put(PrefKey.STARTUP_VERSION_CHECK.name(), Boolean.toString(data.isStartupVersionCheck()));
        this.prefs.put(PrefKey.STARTUP_FILESYSTEM_CHECK.name(), Boolean.toString(data.isStartupFilesystemCheck()));

        this.prefs.put(PrefKey.PROXY_HOST.name(), data.getProxyHost());
        this.prefs.putInt(PrefKey.PROXY_PORT.name(), data.getProxyPort());
        this.prefs.putBoolean(PrefKey.PROXY_ENABLED.name(), data.isProxyEnabled());
        
        this.prefs.put(PrefKey.LANGUAGE_CODE.name(), data.getLanguageCode());
        
        this.flush();
        this.loadPreferences();
    }
    
    void loadPreferences() {
        this.data = this.newDataByStoredValues();
    }
    
    private PreferencesData newDataByStoredValues() {
    	LOG.info("Loading data from preference source...");

		String folderCovers;
		String folderTemporary;
		String folderData;
		
		String username;
		
		String recentExportDestination;
		String recentCoverSelectorPath;
		String recentMovieFolderPath;
		String recentScanPath;
		String recentBackupImportPath;
		
		boolean startupVersionCheck;
		boolean startupFilesystemCheck;
		Map<String, Boolean> columnsVisible = new HashMap<String, Boolean>();
		
		String proxyHost;
		int proxyPort;
		boolean proxyEnabled;
		
		String languageCode;
		
		// ---
        
        folderCovers = this.prefs.get(PrefKey.FOLDER_COVERS.name(), null);
        folderTemporary = this.prefs.get(PrefKey.FOLDER_TEMPORARY.name(), null);
        folderData = this.prefs.get(PrefKey.FOLDER_DATA.name(), null);
        username = this.prefs.get(PrefKey.USERNAME.name(), null);
        startupVersionCheck = Boolean.valueOf(this.prefs.get(PrefKey.STARTUP_VERSION_CHECK.name(),
        		Boolean.toString(DEFAULT_STARTUP_VERSION_CHECK))).booleanValue();
        startupFilesystemCheck = Boolean.valueOf(this.prefs.get(PrefKey.STARTUP_FILESYSTEM_CHECK.name(),
        		Boolean.toString(false))).booleanValue();
        
//        this.serverPort = prefs.getInt(KEY.SERVER_PORT.name(), 1789);
        
        recentExportDestination = this.prefs.get(PrefKey.RECENT_EXPORT_DESTINATION.name(), File.listRoots()[0].getAbsolutePath());
        recentCoverSelectorPath = this.prefs.get(PrefKey.RECENT_COVER_SELECTOR_PATH.name(), File.listRoots()[0].getAbsolutePath());
        recentMovieFolderPath = this.prefs.get(PrefKey.RECENT_MOVIE_FOLDER_PATH.name(), File.listRoots()[0].getAbsolutePath());
        recentScanPath = this.prefs.get(PrefKey.RECENT_SCAN_PATH.name(), File.listRoots()[0].getAbsolutePath());
        recentBackupImportPath = this.prefs.get(PrefKey.RECENT_BACKUP_IMPORT_PATH.name(), File.listRoots()[0].getAbsolutePath());

        proxyHost = this.prefs.get(PrefKey.PROXY_HOST.name(), "");
        proxyPort = this.prefs.getInt(PrefKey.PROXY_PORT.name(), 0);
        proxyEnabled = this.prefs.getBoolean(PrefKey.PROXY_ENABLED.name(), false);
        
        languageCode = this.prefs.get(PrefKey.LANGUAGE_CODE.name(), LanguageCode.ENGLISH.getCode());
        
        for (String columnName : MovieTableColumns.getColumnNames()) {
            final Boolean initialVisible;
            if(columnName.equals(MovieField.TITLE.label()) ||
               columnName.equals(MovieField.RATING.label()) ||
               columnName.equals(MovieField.QUALITY.label()) ||
               columnName.equals(MovieField.LANGUAGES.label())) {
                initialVisible = Boolean.TRUE;
            } else {
                initialVisible = Boolean.FALSE;
            }
            final Boolean prefVisible = Boolean.valueOf(this.prefs.get(MOVIE_COLUMN_PREFIX + columnName, initialVisible.toString()));
            LOG.debug("Column '"+columnName+"' is initial visible '"+prefVisible+"'.");
            columnsVisible.put(columnName, prefVisible);
        }
        
        return new PreferencesData(
    			folderCovers,
    			folderTemporary,
    			folderData,
    			username,
    			recentExportDestination,
    			recentCoverSelectorPath,
    			recentMovieFolderPath,
    			recentScanPath,
    			recentBackupImportPath,
    			startupVersionCheck,
    			startupFilesystemCheck,
    			columnsVisible,
    			proxyHost,
    			proxyPort,
    			proxyEnabled,
    			languageCode);
    }
    
    public static PreferencesDao getInstance() {
        return INSTANCE;
    }
    
//    public PreferenceSourceState isConfigured() {
//        final String storedVersion = this.prefs.get(PrefKey.IS_CONFIGURED.name(), null);
//        LOG.debug("Checking preferences source version: stored="+storedVersion+"; application="+DATA_VERSION);
//        if(storedVersion == null) {
//            return PreferenceSourceState.IS_NOT_SET;
//        }
//        
//        final int storedVersionInt = Integer.parseInt(storedVersion); 
//        if(storedVersionInt != DATA_VERSION) {
//            return PreferenceSourceState.IS_VERSION_MISMATCH;
//        }
//        return PreferenceSourceState.IS_COMPATIBLE;
//    }
    
    public int getStoredVersion() {
        final String storedVersion = this.prefs.get(PrefKey.IS_CONFIGURED.name(), null);
        if(storedVersion == null) {
            return -1;
        }
        return Integer.parseInt(storedVersion);
    }
    
    public void clearPreferences() throws BusinessException {
        LOG.info("Clearing preferences.");
        try {
        	this.prefs.clear();
        	this.prefs.flush();
            this.data = null;
        } catch (BackingStoreException e) {
            throw new BusinessException("Could not clear preferences!", e);
        }
    }
    
    
    
    public File getCoversFolder() {
        return new File(this.data.getFolderCovers());
    }
    
//    public int getServerPort() {
//        return this.serverPort;
//    }
    
//    public void setServerPort(int serverPort) {
//        LOG.debug("setting serverPort to '"+serverPort+"'.");
//        this.prefs.putInt(KEY.SERVER_PORT.name(), serverPort);
//        this.serverPort = serverPort;
//        this.flush();
//    }
    
    public String getUsername() {
        return this.data.getUsername();
    }
    
    public void setUsername(String username) {
        LOG.debug("setting username to '"+username+"'.");
        this.prefs.put(PrefKey.USERNAME.name(), username);
        this.data.setUsername(username);
        this.flush();
    }
    
    public boolean isStartupVersionCheck() {
        return this.data.isStartupVersionCheck();
    }
    
    public void setStartupVersionCheck(final boolean startupVersionCheck) {
        LOG.debug("setting startupVersionCheck to '"+startupVersionCheck+"'.");
        this.prefs.put(PrefKey.STARTUP_VERSION_CHECK.name(), Boolean.toString(startupVersionCheck));
        this.setStartupVersionCheck(startupVersionCheck);
        this.flush();
    }
    
    public boolean isStartupFilesystemCheck() {
        return this.data.isStartupFilesystemCheck();
    }
    
    public void setStartupFilesystemCheck(final boolean startupFilesystemCheck) {
        LOG.debug("setting startupFilesystemCheck to '"+startupFilesystemCheck+"'.");
        this.prefs.put(PrefKey.STARTUP_FILESYSTEM_CHECK.name(), Boolean.toString(startupFilesystemCheck));
        this.setStartupFilesystemCheck(startupFilesystemCheck);
        this.flush();
    }
    
    private void flush() {
        try {
            this.prefs.flush();
        } catch (BackingStoreException e) {
            throw new FatalException("Could not flush preferences!", e);
        }
    }

    public File getTemporaryFolder() {
        return new File(this.data.getFolderTemporary());
    }

    public File getDataFolder() {
        return new File(this.data.getFolderData());
    }
    
    public String getRecentExportDestination() {
        return this.data.getRecentExportDestination();
    }
    
    public void setRecentExportDestination(String recentExportDestination) {
        this.setPreferencesString(PrefKey.RECENT_EXPORT_DESTINATION, recentExportDestination);
        this.data.setRecentExportDestination(recentExportDestination);
    }

    public String getRecentCoverSelectorPath() {
        return this.data.getRecentCoverSelectorPath();
    }
    public void setRecentCoverSelectorPath(String recentCoverSelectorPath) {
        this.setPreferencesString(PrefKey.RECENT_COVER_SELECTOR_PATH, recentCoverSelectorPath);
        this.data.setRecentCoverSelectorPath(recentCoverSelectorPath);
    }

    public String getRecentMovieFolderPath() {
        return this.data.getRecentMovieFolderPath();
    }
    public void setRecentMovieFolderPath(String recentMovieFolderPath) {
        this.setPreferencesString(PrefKey.RECENT_MOVIE_FOLDER_PATH, recentMovieFolderPath);
        this.data.setRecentMovieFolderPath(recentMovieFolderPath);
    }

    public String getRecentScanPath() {
        return this.data.getRecentScanPath();
    }
    public void setRecentScanPath(String recentScanPath) {
        this.setPreferencesString(PrefKey.RECENT_SCAN_PATH, recentScanPath);
        this.data.setRecentScanPath(recentScanPath);
    }

    public String getRecentBackupImportPath() {
        return this.data.getRecentBackupImportPath();
    }
    public void setRecentBackupImportPath(final String recentBackupImportPath) {
        this.setPreferencesString(PrefKey.RECENT_BACKUP_IMPORT_PATH, recentBackupImportPath);
        this.data.setRecentBackupImportPath(recentBackupImportPath);
    }
    
    public String getProxyHost() {
        return this.data.getProxyHost();
    }
    public void setProxyHost(final String proxyHost) {
        this.setPreferencesString(PrefKey.PROXY_HOST, proxyHost);
        this.data.setProxyHost(proxyHost);
    }
    
    public int getProxyPort() {
        return this.data.getProxyPort();
    }
    
    public void setProxyPort(final int proxyPort) {
        this.setPreferencesInt(PrefKey.PROXY_PORT, proxyPort);
        this.data.setProxyPort(proxyPort);
    }
    
    public boolean isProxyEnabled() {
        return this.data.isProxyEnabled();
    }
    
    public void setProxyEnabled(final boolean proxyEnabled) {
        this.setPreferencesBoolean(PrefKey.PROXY_ENABLED, proxyEnabled);
        this.data.setProxyEnabled(proxyEnabled);
    }
    
    public boolean isMovieColumnVisible(String columnName) {
        final Boolean visible = this.data.getColumnsVisible().get(columnName);
        assert(visible != null) : "Unkown column '"+columnName+"'!";
        return visible.booleanValue();
    }
    
    public void setMovieColumnVisibility(Map<String, Boolean> columns) {
        LOG.debug("Updating movie column visibility.");
        for(String columnName : columns.keySet()) {
            final Boolean visible = columns.get(columnName);
            this.prefs.put(MOVIE_COLUMN_PREFIX + columnName, visible.toString());
        }
        
        this.flush();
    }
    
    public void setLanguage(LanguageCode language) {
    	this.setPreferencesString(PrefKey.LANGUAGE_CODE, language.getCode());
        this.data.setLanguageCode(language.getCode());
    }
    
    public LanguageCode getLanguage() {
    	return LanguageUtil.byCode(this.data.getLanguageCode());
    }
    
    

    
    public void checkFolderExistence() throws BusinessException {
        this.createFolder(PreferencesDao.getInstance().getCoversFolder());
        this.createFolder(PreferencesDao.getInstance().getTemporaryFolder());
        this.createFolder(PreferencesDao.getInstance().getDataFolder());
    }
    
    private void createFolder(final File folder) throws BusinessException {
        if(folder.exists() == false) {
            LOG.info("Creating application folder '"+folder.getAbsolutePath()+"'.");
            if(folder.mkdirs() == false) {
                throw new BusinessException("Could not create folder '"+folder.getAbsolutePath()+"'!");
            }
        }
    }
    

    private void setPreferencesString(PrefKey key, String value) {
        LOG.debug("Setting '"+key.name()+"' to '"+value+"'.");
        this.prefs.put(key.name(), value);
        this.flush();
    }

    private void setPreferencesInt(PrefKey key, int value) {
        LOG.debug("Setting '"+key.name()+"' to '"+value+"'.");
        this.prefs.putInt(key.name(), value);
        this.flush();
    }

    private void setPreferencesBoolean(PrefKey key, boolean value) {
        LOG.debug("Setting '"+key.name()+"' to '"+value+"'.");
        this.prefs.putBoolean(key.name(), value);
        this.flush();
    }
    
    Map<String, Boolean> getColumnsVisible() {
    	return this.data.getColumnsVisible();
    }
}
