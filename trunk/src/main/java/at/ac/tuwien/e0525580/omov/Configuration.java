package at.ac.tuwien.e0525580.omov;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.MovieTableModel;

public class Configuration {

    private static final Log LOG = LogFactory.getLog(Configuration.class);
    private static final Configuration INSTANCE = new Configuration();
    private final Preferences prefs = Preferences.userNodeForPackage(Configuration.class);
    
    private static final String MOVIE_COLUMN_PREFIX = "MovieColumn-";
    
    private enum PrefKey {
        IS_CONFIGURED,
        
        FOLDER_COVERS, FOLDER_TEMPORARY,
        SERVER_PORT, USERNAME,
        
        RECENT_EXPORT_DESTINATION, RECENT_COVER_SELECTOR_PATH, RECENT_MOVIE_FOLDER_PATH;
    }
    
    private String folderCovers, folderTemporary;
    
    private String username;
//    private int serverPort;
    
    private String recentExportDestination, recentCoverSelectorPath, recentMovieFolderPath;
    
    private Map<String, Boolean> columnsVisible = new HashMap<String, Boolean>();
    
    
    public static final String APPARG_DEBUG_MENU = "DEBUG_MENU";
    
    private Configuration() {
        if(this.isInitialized()) {
            this.loadPreferences();
        }
    }
    
    public void setPreferences(String folderCovers, String folderTemporary, String username) {
        assert(folderCovers != null && folderTemporary != null && username != null);
        assert(new File(folderCovers).exists() && new File(folderTemporary).exists());
        
        this.prefs.put(PrefKey.FOLDER_COVERS.name(), folderCovers);
        this.prefs.put(PrefKey.FOLDER_TEMPORARY.name(), folderTemporary);
        this.prefs.put(PrefKey.USERNAME.name(), username);
        
        this.prefs.put(PrefKey.IS_CONFIGURED.name(), "x");
        this.flush();
        this.loadPreferences();
    }
    
    private void loadPreferences() {
        this.folderCovers = prefs.get(PrefKey.FOLDER_COVERS.name(), null);
        this.folderTemporary = prefs.get(PrefKey.FOLDER_TEMPORARY.name(), null);
        this.username = prefs.get(PrefKey.USERNAME.name(), null);
//        this.serverPort = prefs.getInt(KEY.SERVER_PORT.name(), 1789);
        
        this.recentExportDestination = prefs.get(PrefKey.RECENT_EXPORT_DESTINATION.name(), File.listRoots()[0].getAbsolutePath());
        this.recentCoverSelectorPath = prefs.get(PrefKey.RECENT_COVER_SELECTOR_PATH.name(), File.listRoots()[0].getAbsolutePath());
        this.recentMovieFolderPath = prefs.get(PrefKey.RECENT_MOVIE_FOLDER_PATH.name(), File.listRoots()[0].getAbsolutePath());
        
        for (String columnName : MovieTableModel.getColumnNames()) {
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
    
    public static Configuration getInstance() {
        return INSTANCE;
    }
    
    public boolean isInitialized() {
        return this.prefs.get(PrefKey.IS_CONFIGURED.name(), null) != null; 
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

    public String getRecentExportDestination() {
        return this.recentExportDestination;
    }
    public void setRecentExportDestination(String recentExportDestination) {
        LOG.debug("setting recentExportDestination to '"+recentExportDestination+"'.");
        this.prefs.put(PrefKey.RECENT_EXPORT_DESTINATION.name(), recentExportDestination);
        this.recentExportDestination = recentExportDestination;
        this.flush();
    }

    public String getRecentCoverSelectorPath() {
        return this.recentCoverSelectorPath;
    }
    public void setRecentCoverSelectorPath(String recentCoverSelectorPath) {
        LOG.debug("setting recentCoverSelectorPath to '"+recentCoverSelectorPath+"'.");
        this.prefs.put(PrefKey.RECENT_COVER_SELECTOR_PATH.name(), recentCoverSelectorPath);
        this.recentCoverSelectorPath = recentCoverSelectorPath;
        this.flush();
    }

    public void setRecentMovieFolderPath(String recentMovieFolderPath) {
        LOG.debug("setting recentMovieFolderPath to '"+recentMovieFolderPath+"'.");
        this.prefs.put(PrefKey.RECENT_MOVIE_FOLDER_PATH.name(), recentMovieFolderPath);
        this.recentMovieFolderPath = recentMovieFolderPath;
        this.flush();
    }
    public String getRecentMovieFolderPath() {
        return this.recentMovieFolderPath;
    }
    
    public boolean isMovieColumnVisible(String columnName) {
        final Boolean visible = this.columnsVisible.get(columnName);
        assert(visible != null) : "Unkown column '"+columnName+"'!";
        return visible;
    }
    public void setMovieColumnVisibility(Map<String, Boolean> columns) {
        LOG.debug("Updating movie column visibility.");
        for(String columnName : columns.keySet()) {
            final Boolean visible = columns.get(columnName);
            this.prefs.put(MOVIE_COLUMN_PREFIX + columnName, visible.toString());
        }
        
        this.flush();
    }
}
