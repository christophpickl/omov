package net.sourceforge.omov.logic.prefs.v5;

import java.util.HashMap;
import java.util.Map;

import net.sourceforge.omov.core.LanguageCode;
import net.sourceforge.omov.logic.prefs.AbstractPreferencesData;

public final class PreferencesData extends AbstractPreferencesData {

	/*
	DATA VERSION HISTORY

	===>   v1 -> v2
	- added:
	    * startup version check
	    * startup filesystem check

	===>   v2 -> v3
	- added: recent backup import path

	===>   v3 -> v4
	- added: proxy-host and proxy-port

	===>   v4 -> v5
	- added locale

	 */
    public static final int DATA_VERSION = 5;
    

    /** absolute path, set by setup wizard */
    private String folderCovers;

    /** absolute path, set by setup wizard */
    private String folderTemporary;

    /** absolute path, set by setup wizard */
    private String folderData;
    
    private String username;
//    private int serverPort;
    
    private String recentExportDestination;
    private String recentCoverSelectorPath;
    private String recentMovieFolderPath;
    private String recentScanPath;
    private String recentBackupImportPath;
    
    private boolean startupVersionCheck;
    private boolean startupFilesystemCheck;
    private Map<String, Boolean> columnsVisible = new HashMap<String, Boolean>();
    
    private String proxyHost;
    private int proxyPort;
    private boolean proxyEnabled;
    
    private String languageCode;

    public static PreferencesData newBySetupWizard(
			String folderCovers,
			String folderTemporary,
			String folderData,
			
			String username,
			
//			String recentExportDestination,
//			String recentCoverSelectorPath,
//			String recentMovieFolderPath,
//			String recentScanPath,
//			String recentBackupImportPath,
			
			boolean startupVersionCheck,
			boolean startupFilesystemCheck,
//			Map<String, Boolean> columnsVisible,
			
			String proxyHost,
			int proxyPort,
			boolean proxyEnabled,
			
			LanguageCode language
			) {
    	return new PreferencesData(folderCovers, folderTemporary, folderData, username,
    			null, null, null, null, null,
    			startupVersionCheck, startupFilesystemCheck, new HashMap<String, Boolean>(),
    			proxyHost, proxyPort, proxyEnabled, language.getCode());
    }
    
    public PreferencesData(
			String folderCovers,
			String folderTemporary,
			String folderData,
			
			String username,
			
			String recentExportDestination,
			String recentCoverSelectorPath,
			String recentMovieFolderPath,
			String recentScanPath,
			String recentBackupImportPath,
			
			boolean startupVersionCheck,
			boolean startupFilesystemCheck,
			Map<String, Boolean> columnsVisible,
			
			String proxyHost,
			int proxyPort,
			boolean proxyEnabled,
			
			String languageCode
			) {
		this.folderCovers = folderCovers;
		this.folderTemporary = folderTemporary;
		this.folderData = folderData;
		this.username = username;
		this.recentExportDestination = recentExportDestination;
		this.recentCoverSelectorPath = recentCoverSelectorPath;
		this.recentMovieFolderPath = recentMovieFolderPath;
		this.recentScanPath = recentScanPath;
		this.recentBackupImportPath = recentBackupImportPath;
		this.startupVersionCheck = startupVersionCheck;
		this.startupFilesystemCheck = startupFilesystemCheck;
		this.columnsVisible = columnsVisible;
		this.proxyHost = proxyHost;
		this.proxyPort = proxyPort;
		this.proxyEnabled = proxyEnabled;
		this.languageCode = languageCode;
	}

	@Override
	public String toString() {
		return "PreferencesData"+DATA_VERSION+"[username='"+username+"';folderCovers='"+folderCovers+"';folderTemporary='"+folderTemporary+"';" +
		"folderData='"+folderData+"';startupVersionCheck='"+startupVersionCheck+"';startupFilesystemCheck='"+startupFilesystemCheck+"';" +
		"proxyHost="+proxyHost+";proxyPort="+proxyPort+";proxyEnabled="+proxyEnabled+";languageCode="+languageCode+"]";
	}
	
	public String getFolderCovers() {
		return this.folderCovers;
	}

	public String getFolderTemporary() {
		return this.folderTemporary;
	}

	public String getFolderData() {
		return this.folderData;
	}

	public String getUsername() {
		return this.username;
	}

	public String getRecentExportDestination() {
		return this.recentExportDestination;
	}

	public String getRecentCoverSelectorPath() {
		return this.recentCoverSelectorPath;
	}

	public String getRecentMovieFolderPath() {
		return this.recentMovieFolderPath;
	}

	public String getRecentScanPath() {
		return this.recentScanPath;
	}

	public String getRecentBackupImportPath() {
		return this.recentBackupImportPath;
	}

	public boolean isStartupVersionCheck() {
		return this.startupVersionCheck;
	}

	public boolean isStartupFilesystemCheck() {
		return this.startupFilesystemCheck;
	}

	public Map<String, Boolean> getColumnsVisible() {
		return this.columnsVisible;
	}

	public String getProxyHost() {
		return this.proxyHost;
	}

	public int getProxyPort() {
		return this.proxyPort;
	}

	public boolean isProxyEnabled() {
		return this.proxyEnabled;
	}

	public String getLanguageCode() {
		return this.languageCode;
	}

	public void setFolderCovers(String folderCovers) {
		this.folderCovers = folderCovers;
	}

	public void setFolderTemporary(String folderTemporary) {
		this.folderTemporary = folderTemporary;
	}

	public void setFolderData(String folderData) {
		this.folderData = folderData;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public void setRecentExportDestination(String recentExportDestination) {
		this.recentExportDestination = recentExportDestination;
	}

	public void setRecentCoverSelectorPath(String recentCoverSelectorPath) {
		this.recentCoverSelectorPath = recentCoverSelectorPath;
	}

	public void setRecentMovieFolderPath(String recentMovieFolderPath) {
		this.recentMovieFolderPath = recentMovieFolderPath;
	}

	public void setRecentScanPath(String recentScanPath) {
		this.recentScanPath = recentScanPath;
	}

	public void setRecentBackupImportPath(String recentBackupImportPath) {
		this.recentBackupImportPath = recentBackupImportPath;
	}

	public void setStartupVersionCheck(boolean startupVersionCheck) {
		this.startupVersionCheck = startupVersionCheck;
	}

	public void setStartupFilesystemCheck(boolean startupFilesystemCheck) {
		this.startupFilesystemCheck = startupFilesystemCheck;
	}

	public void setColumnsVisible(Map<String, Boolean> columnsVisible) {
		this.columnsVisible = columnsVisible;
	}

	public void setProxyHost(String proxyHost) {
		this.proxyHost = proxyHost;
	}

	public void setProxyPort(int proxyPort) {
		this.proxyPort = proxyPort;
	}

	public void setProxyEnabled(boolean proxyEnabled) {
		this.proxyEnabled = proxyEnabled;
	}

	public void setLanguageCode(String languageCode) {
		this.languageCode = languageCode;
	}
    
    
}
