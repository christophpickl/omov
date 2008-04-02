package at.ac.tuwien.e0525580.omov;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.PreferencesDao.PreferenceSourceState;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.SetupWizard;
import at.ac.tuwien.e0525580.omov.gui.SplashScreen;
import at.ac.tuwien.e0525580.omov.gui.main.MainWindow;
import at.ac.tuwien.e0525580.omov.model.IDataVersionDao;
import at.ac.tuwien.e0525580.omov.model.IDatabaseConnection;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;
import at.ac.tuwien.e0525580.omov.tools.TemporaryFilesCleaner;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

/*

CLI ARGs
==================================
- "DEBUG_MENU" ... enables debug menubar entry

App FIXMES
==================================
- setup wizard gui ueberarbeiten
- *ListFilled nicht nur fuer actor und genre, sondern auch fuer: language, subtitle !!! aber immer dran denken das model auch UNREGISTERN!!!

TODOs
==================================
- at startup check if folderImage and folderTemporary exists & if its writeable
- beim scannen (per rechtemaustaste) andere metadata-fetches available machen (nur der gefundene title; dann wird details erst fetched)
- should catch all (runtime-)exceptions at top of every user-invocations (actionPerformed, mouseClicked, ...)
- MultiColMultiRowTextField komponente schreiben; fuer MovieDetailPanel.genre/actors/... da eine zeile nicht reicht

FEATURES
==================================
- beim tab durchgehen in AddEditMovieDialog -> gleich ganzen text selecten
- suggester auch fuer einzelne attribute in AddEditMovieDialog (zb: resolution; sonst noch was?)
- in preferences FolderPath-Prefix Cut list erstellbar machen: zb eintragen "/Volumes/MEGADISK/Holy/"
- other backgroundcolor in scanner table if successfully fetched metadata
- wenn kein movie selected => MenuBar/GetInfo (fetch metadata, ...) disablen
- SearchField als SuggestionField machen (wo ueberall suggestions suchen? -> Title, Actor, Directory, ... Genre, ...)
- FileSystem check at startup (per preferences einschaltbar)
- alles ESC enablen
- beim export/smartCopy den user entscheiden lassen welche movies genommen werden (alle || nur selected; beim smartCopy + anhand von IDs generiert von HTML-Report)
- wenn OS==MacOSX dann beim setup nicht nach tmp/cover-folder fragen, sondern in Contents/MacOS/Resources implizit anlegen

- remote zeugs
- help system

bzgl editable table cell rows: [http://forum.java.sun.com/thread.jspa?forumID=57&threadID=5120161]
  Given that we have very little information to go by, I'm guessing that you have a custom TableModel and you are not firing
  the TableCellUpdated() method when you change the data, so the table doesn't know it should repaint the cell.

bzgl listener von dao unregistern:
  eigentlich ist registern nicht notwendig, da wenn user movie added/edited, kann datenbestand gar nicht aendern.
  -> loesung: dem Movie*ListFilled einfach eine List<String> uebergeben!

*/

public class App {

    private static final Log LOG = LogFactory.getLog(App.class);

    private static final Set<String> cliArguments = new HashSet<String>();

    
    public static void main(String[] args) {
        App.cliArguments.addAll(Arrays.asList(args));
        new App().startUp();
    }
    
    public App() {
    }
    
    public void startUp() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            // UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception ex) {
            LOG.error("Unable to set system look&feel!", ex);
        }
        
        final SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
        
        try {
            final long timeStart = new Date().getTime();
            if(App.checkPreferenceSource() == false) {
                LOG.warn("Checking preference source version failed!");
                System.exit(1);
            }
    
            // FEATURE check file consistency at startup; for each movie/directory: check if files still exist (mantis: 2)
            //         -> maybe check if other moviefiles were added; maybe also recalculate size if files changed;
            
            TemporaryFilesCleaner.clean();
            App.addShutdownHook();
            
            if(App.checkDataVersions() == false) {
                LOG.warn("Checking core data versions failed!");
                System.exit(1);
            }
            
            JFrame.setDefaultLookAndFeelDecorated(true);
            final MainWindow mainWindow = new MainWindow();
            
            final long timeLasted = new Date().getTime() - timeStart;
            final long minimumTimeLasted = 1000L;
            if(timeLasted < minimumTimeLasted) { // avoid very short visibility of splash screen
                try { Thread.sleep(minimumTimeLasted - timeLasted); } catch (InterruptedException e) { /* delibaretely ignored */ }
            }
            
            splashScreen.setVisible(false);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    LOG.debug("Displaying main window...");
                    mainWindow.setVisible(true);
                }
            });
            
        } finally {
            splashScreen.setVisible(false); // e.g.: if setting configuration or cleaning temp folder failed
        }
    }
    
    private static boolean checkDataVersions() {
        final IDataVersionDao versionDao = BeanFactory.getInstance().getDataVersionDao();
        final int movieDataVersion = versionDao.getMovieDataVersion();
        final int smartfolderDataVersion = versionDao.getSmartfolderDataVersion();
        
        LOG.debug("checking data versions: stored movie = "+movieDataVersion+" (application:"+Movie.DATA_VERSION+"); stored smartfolder = "+smartfolderDataVersion+" (application:"+SmartFolder.DATA_VERSION+")");
        
        if(movieDataVersion == -1) {
            assert(smartfolderDataVersion == -1);
            
            LOG.info("Storing initial data versions (Movie="+Movie.DATA_VERSION+"; SmartFolder="+SmartFolder.DATA_VERSION+").");
            versionDao.storeDataVersions(Movie.DATA_VERSION, SmartFolder.DATA_VERSION);
            LOG.debug("Dataversions are now ok.");
            return true;
        }

        // TODO write troubleshooting topic for this problem (either delete db4-file for reset or use converter if possible)
        if(movieDataVersion != Movie.DATA_VERSION && smartfolderDataVersion != SmartFolder.DATA_VERSION) {
            GuiUtil.error("Datasource Version Mismatch", "It seems as you were using incompatible Movie and Preference Data Sources!\n" +
                    "Movie version: "+movieDataVersion+" -- Application version: "+Movie.DATA_VERSION + "\n" + 
                    "SmartFolder version: "+smartfolderDataVersion + " -- Application version: "+SmartFolder.DATA_VERSION);
            return false;
        }
        if(movieDataVersion != Movie.DATA_VERSION) {
            GuiUtil.error("Datasource Version Mismatch", "It seems as you were using an incompatible Movie Data Source!\n" +
                    "Movie version: "+movieDataVersion+" -- Application version: "+Movie.DATA_VERSION);
            return false;
        }
        if(smartfolderDataVersion != SmartFolder.DATA_VERSION) {
            GuiUtil.error("Datasource Version Mismatch", "It seems as you were using an incompatible SmartFolder Data Source!\n" +
                    "SmartFolder version: "+smartfolderDataVersion + " -- Application version: "+SmartFolder.DATA_VERSION);
            return false;
        }
        
        LOG.debug("Dataversions are ok.");
        return true;
    }
    
    public static boolean isArgumentSet(String argument) {
        return App.cliArguments.contains(argument);
    }
    
    private static boolean checkPreferenceSource() {
        LOG.debug("checking preference source...");
        try {
            final PreferenceSourceState preferenceSourceState = PreferencesDao.getInstance().isConfigured();
            if(preferenceSourceState == PreferenceSourceState.IS_NOT_SET) {
                LOG.info("Preference datasource was not yet initialized; starting setup wizard.");
                final SetupWizard wizard = new SetupWizard();
                wizard.setVisible(true);
                
                if(wizard.isConfirmed() == false) {
                    LOG.info("User aborted setup.");
                    return false;
                }
                assert(PreferencesDao.getInstance().isConfigured() == PreferenceSourceState.IS_COMPATIBLE);
                
            } else if(preferenceSourceState == PreferenceSourceState.IS_VERSION_MISMATCH) {
                GuiUtil.warning("Version Mismatch", "The version of the existing Preference Source\n" +
                                "does not match with the expected version!");
                // FIXME startup preference source data converter (if available)
                LOG.info("FIXME startup preference source data converter (if available)");
                // show confirm popup: user should either select to reset/delete all pref data, or: just abort and get a list of compatible OurMovies versions (could use old app and write down old preference values) 
                
                
                PreferencesDao.clearPreferences(); // otherwise clear all stored data and shutdown app by returning false
                return false;
                
            } else if(preferenceSourceState == PreferenceSourceState.IS_COMPATIBLE) {
                LOG.debug("Perferences source dataversion is compatible; nothing to do.");
                
            } else {
                throw new FatalException("Unhandled preferences source state '"+preferenceSourceState.name()+"'!");
            }
        } catch (Exception e) {
            LOG.error("Could not check/clear/set preferences!", e);
            GuiUtil.error("Setup failed!", "Could not set initial values: " + e.getMessage());
            return false;
        }
        
        
        try {
            PreferencesDao.getInstance().checkFolderExistence();
        } catch (BusinessException e) {
            LOG.error("Could not check folder existence!", e);
            GuiUtil.error("Startup failed!", "Could not create application folders!");
            return false;
        }
        
        return true;
    }
    
    private static void addShutdownHook() {
        Runtime.getRuntime().addShutdownHook(new Thread("OmovShutdownHook") {
            public void run() {
                LOG.info("Running shutdown hook.");
                try {
                    final IDatabaseConnection connection = BeanFactory.getInstance().getDatabaseConnection();
                    if(connection.isConnected()) {
                        connection.close();
                    }
                } catch(Exception e) {
                    LOG.error("Could not close database connection!", e);
                    e.printStackTrace();
                }
            }
        });
    }
    
}
