package net.sourceforge.omov.core;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import net.sourceforge.omov.core.bo.Movie.MovieField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PreferencesDao {

    private static final Log LOG = LogFactory.getLog(PreferencesDao.class);
    private static final PreferencesDao INSTANCE = new PreferencesDao();
    private final Preferences prefs = Preferences.userNodeForPackage(PreferencesDao.class);
    
    private static final String MOVIE_COLUMN_PREFIX = "MovieColumn-";
    

/*

DATA VERSION HISTORY

===>   v1 -> v2
- added:
    * startup version check
    * startup filesystem check


 */
    public static final int DATA_VERSION = 2;
    
    private enum PrefKey {
        IS_CONFIGURED,
        
        FOLDER_COVERS, FOLDER_TEMPORARY, FOLDER_DATA,
        // SERVER_PORT,
        USERNAME,
        STARTUP_VERSION_CHECK, STARTUP_FILESYSTEM_CHECK,
        
        RECENT_EXPORT_DESTINATION, RECENT_COVER_SELECTOR_PATH, RECENT_MOVIE_FOLDER_PATH, RECENT_SCAN_PATH;
    }
    
    private String folderCovers, folderTemporary, folderData;
    
    private String username;
//    private int serverPort;
    
    private String recentExportDestination, recentCoverSelectorPath, recentMovieFolderPath, recentScanPath;
    private boolean startupVersionCheck, startupFilesystemCheck;
    private Map<String, Boolean> columnsVisible = new HashMap<String, Boolean>();
    
    
    public static final String APP_ARG_DEBUG = "DEBUG";
    
    private PreferencesDao() {
        if(this.getSoredVersion() == DATA_VERSION) {
            this.loadPreferences();
        }
    }
    
    public void setPreferences(String folderCovers, String folderTemporary, String folderData, String username, boolean checkVersionStartup, boolean checkFilesystemStartup) {
        LOG.info("Setting preferences (username='"+username+"';folderCovers='"+folderCovers+"';folderTemporary='"+folderTemporary+"';folderData='"+folderData+"';checkVersionStartup='"+checkVersionStartup+"';checkFilesystemStartup='"+checkFilesystemStartup+"').");
        
        assert(folderCovers != null && folderTemporary != null && username != null);
        assert(new File(folderCovers).exists() && new File(folderTemporary).exists());
        
        this.prefs.put(PrefKey.FOLDER_COVERS.name(), folderCovers);
        this.prefs.put(PrefKey.FOLDER_TEMPORARY.name(), folderTemporary);
        this.prefs.put(PrefKey.FOLDER_DATA.name(), folderData);
        this.prefs.put(PrefKey.USERNAME.name(), username);

        this.prefs.put(PrefKey.IS_CONFIGURED.name(), String.valueOf(DATA_VERSION));
        this.prefs.put(PrefKey.STARTUP_VERSION_CHECK.name(), Boolean.toString(checkVersionStartup));
        this.prefs.put(PrefKey.STARTUP_FILESYSTEM_CHECK.name(), Boolean.toString(checkFilesystemStartup));
        
        this.flush();
        this.loadPreferences();
    }
    
    private void loadPreferences() {
        LOG.info("Loading data from preference source...");
        this.folderCovers = prefs.get(PrefKey.FOLDER_COVERS.name(), null);
        this.folderTemporary = prefs.get(PrefKey.FOLDER_TEMPORARY.name(), null);
        this.folderData = prefs.get(PrefKey.FOLDER_DATA.name(), null);
        this.username = prefs.get(PrefKey.USERNAME.name(), null);
        this.startupVersionCheck = Boolean.valueOf(prefs.get(PrefKey.STARTUP_VERSION_CHECK.name(), Boolean.toString(false)));
        this.startupFilesystemCheck = Boolean.valueOf(prefs.get(PrefKey.STARTUP_FILESYSTEM_CHECK.name(), Boolean.toString(false)));
        
//        this.serverPort = prefs.getInt(KEY.SERVER_PORT.name(), 1789);
        
        this.recentExportDestination = prefs.get(PrefKey.RECENT_EXPORT_DESTINATION.name(), File.listRoots()[0].getAbsolutePath());
        this.recentCoverSelectorPath = prefs.get(PrefKey.RECENT_COVER_SELECTOR_PATH.name(), File.listRoots()[0].getAbsolutePath());
        this.recentMovieFolderPath = prefs.get(PrefKey.RECENT_MOVIE_FOLDER_PATH.name(), File.listRoots()[0].getAbsolutePath());
        this.recentScanPath = prefs.get(PrefKey.RECENT_SCAN_PATH.name(), File.listRoots()[0].getAbsolutePath());
        
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
            final Boolean prefVisible = Boolean.valueOf(prefs.get(MOVIE_COLUMN_PREFIX + columnName, initialVisible.toString()));
            LOG.debug("Column '"+columnName+"' is initial visible '"+prefVisible+"'.");
            this.columnsVisible.put(columnName, prefVisible);
        }
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
    
    public int getSoredVersion() {
        final String storedVersion = this.prefs.get(PrefKey.IS_CONFIGURED.name(), null);
        if(storedVersion == null) {
            return -1;
        }
        return Integer.parseInt(storedVersion);
    }
    
    public static void clearPreferences() throws BusinessException {
        LOG.info("Clearing preferences.");
        final Preferences prefs = Preferences.userNodeForPackage(PreferencesDao.class);
        try {
            prefs.clear();
            prefs.flush();
        } catch (BackingStoreException e) {
            throw new BusinessException("Could not clear preferences!", e);
        }
    }
    
    public File getCoversFolder() {
        return new File(this.folderCovers);
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
        return this.username;
    }
    public void setUsername(String username) {
        LOG.debug("setting username to '"+username+"'.");
        this.prefs.put(PrefKey.USERNAME.name(), username);
        this.username = username;
        this.flush();
    }
    
    public boolean isStartupVersionCheck() {
        return this.startupVersionCheck;
    }
    public void setStartupVersionCheck(final boolean startupVersionCheck) {
        LOG.debug("setting startupVersionCheck to '"+startupVersionCheck+"'.");
        this.prefs.put(PrefKey.STARTUP_VERSION_CHECK.name(), Boolean.toString(startupVersionCheck));
        this.startupVersionCheck = startupVersionCheck;
        this.flush();
    }
    
    public boolean isStartupFilesystemCheck() {
        return this.startupFilesystemCheck;
    }
    public void setStartupFilesystemCheck(final boolean startupFilesystemCheck) {
        LOG.debug("setting startupFilesystemCheck to '"+startupFilesystemCheck+"'.");
        this.prefs.put(PrefKey.STARTUP_FILESYSTEM_CHECK.name(), Boolean.toString(startupFilesystemCheck));
        this.startupFilesystemCheck = startupFilesystemCheck;
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
        return new File(this.folderTemporary);
    }

    public File getDataFolder() {
        return new File(this.folderData);
    }

    private void setPreferencesString(PrefKey key, String value) {
        LOG.debug("Setting '"+key.name()+"' to '"+value+"'.");
        this.prefs.put(key.name(), value);
        this.flush();
    }
    
    
    public String getRecentExportDestination() {
        return this.recentExportDestination;
    }
    public void setRecentExportDestination(String recentExportDestination) {
        this.setPreferencesString(PrefKey.RECENT_EXPORT_DESTINATION, recentExportDestination);
        this.recentExportDestination = recentExportDestination;
    }

    public String getRecentCoverSelectorPath() {
        return this.recentCoverSelectorPath;
    }
    public void setRecentCoverSelectorPath(String recentCoverSelectorPath) {
        this.setPreferencesString(PrefKey.RECENT_COVER_SELECTOR_PATH, recentCoverSelectorPath);
        this.recentCoverSelectorPath = recentCoverSelectorPath;
    }

    public String getRecentMovieFolderPath() {
        return this.recentMovieFolderPath;
    }
    public void setRecentMovieFolderPath(String recentMovieFolderPath) {
        this.setPreferencesString(PrefKey.RECENT_MOVIE_FOLDER_PATH, recentMovieFolderPath);
        this.recentMovieFolderPath = recentMovieFolderPath;
    }

    public String getRecentScanPath() {
        return this.recentScanPath;
    }
    public void setRecentScanPath(String recentScanPath) {
        this.setPreferencesString(PrefKey.RECENT_SCAN_PATH, recentScanPath);
        this.recentScanPath = recentScanPath;
    }
    
    
    
    
    public boolean isMovieColumnVisible(String columnName) {
        final Boolean visible = this.columnsVisible.get(columnName);
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
}
