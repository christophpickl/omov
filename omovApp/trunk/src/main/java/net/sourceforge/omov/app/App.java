package net.sourceforge.omov.app;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import net.sourceforge.omov.app.gui.FileSystemCheckDialog;
import net.sourceforge.omov.app.gui.SetupWizard;
import net.sourceforge.omov.app.gui.SplashScreen;
import net.sourceforge.omov.app.gui.main.MainWindow;
import net.sourceforge.omov.app.gui.preferences.VersionCheckDialog;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.model.IDataVersionDao;
import net.sourceforge.omov.core.model.IDatabaseConnection;
import net.sourceforge.omov.core.smartfolder.SmartFolder;
import net.sourceforge.omov.core.tools.FileSystemChecker;
import net.sourceforge.omov.core.tools.TemporaryFilesCleaner;
import net.sourceforge.omov.core.tools.FileSystemChecker.FileSystemCheckResult;
import net.sourceforge.omov.core.tools.vlc.IVlcPlayer;
import net.sourceforge.omov.core.tools.vlc.VlcPlayerFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/*
CLI ARGs
==================================
- "DEBUG" ... enables debug menubar entry


TODOs
==================================
! wenn window deactivated => farbe ändern wie 10.5
- gui: in windows lockOriginalSize does not work properly! (also min size of mainwindow is set too big)
- gui general enhancement: enable in lists/tables doubleclick (edit) and backspace (delete).
- at startup check if folderImage and folderTemporary exists & if its writeable
- should catch all (runtime-)exceptions at top of every user-invocations (actionPerformed, mouseClicked, ...)
- *ListFilled nicht nur fuer actor und genre, sondern auch fuer: language, subtitle !!! aber immer dran denken das model auch UNREGISTERN!!!
- test class which automatically checks data source converters (e.g.: reset pref version to 1, then use code which needs v2 -> check updated values)
- if main movie table row is selected, then popup edit dialog, hit next-button -> previously selected row will be unselected (because dao update will be performed -> reselect these rows)
- wenn man in movie table metadata fetched, blocked gui -> eigener swingworker + progress dialog
- in MovieDetailPanel unten zusaetzliche zeile mit werten (trailer, und...?)
- bei kleinen fenster: jRootPane.putClientProperty("Window.style", "small")

*** web imdb subproject
* websearch: make webextractor configurable ScanDialog.java
*  zuerst "Popular Titles" anzeigen, dann andere ImdbStartPage.java

? not seperating between here means they might use the same files (e.g.: coverfolder/db-file will not be deleted) ... hm... :( Constants.java
? wirklich auf index 0 ruecksetzen, wenn auf doManage-smartfolders geklickt hat?!   SmartFolderSelectionPanel.java

... minor/misc
- outsource common code (NumberMatch) into superclass (or something like that; factory, ...) RatingMatch.java
- private final boolean metadataFetched; ... oder so halt.   ScannedMovie.java
- look if this is really necessary anymore (since isPopupTrigger was added); otherwise remove this.  BodyContext.java
- :( too bad, how to check without wrapper?  MovieCreator.java


** write more external plugins
  fetch data from different websites; e.g.: fetch slideshow images from www.apunkachoice.com/movies

** bzgl editable table cell rows: [http://forum.java.sun.com/thread.jspa?forumID=57&threadID=5120161]
  Given that we have very little information to go by, I'm guessing that you have a custom TableModel and you are not firing
  the TableCellUpdated() method when you change the data, so the table doesn't know it should repaint the cell.

** bzgl listener von dao unregistern:
  eigentlich ist registern nicht notwendig, da wenn user movie added/edited, kann datenbestand gar nicht aendern.
  -> loesung: dem Movie*ListFilled einfach eine List<String> uebergeben!


FEATURES
==================================
- write more external plugins: fetch data from different websites; e.g.:
    - provide list of more selectable cover files  for one movie (fetched by different internet provider)
    - fetch slideshow images from www.apunkachoice.com/movies  App.java


-> http://www.theserverside.com/tt/articles/article.tss?l=Insidedb4o
-> http://db4o-tools.blogspot.com/search/label/db4o


ubi brainstorming
==================================
- movie titles auch als foldernamen am filesystem umbennen lassen koennen per preference
    - mit movie files wirds haglig; wenn mehrere existieren -> reihenfolge?; was wenn dvd ist -> eigentlich vordefinierter name!
- bilder von slideshow
- links baum zum verwalten von kategorien/smartfolders (wie itunes hat)
    - darin darstellen einen "autmatisch synced smartfolder" fuer genres. sobald neuer film mit neuem genre eintragen, dann hat dieser smartfolder automatisch eine neue "playlist" mit diesem genre
- serien verwaltung; displayed directly in main window, in same table
- ordner repository zwang aufheben: movies muessen nicht als ordner am filesystem existieren -> sondern koennen auch nur einzelnes file sein
    - dazu muessen beim scannen "movie-pt1.avi" und "movie-pt2.avi" zusammenerkannt werden

*/

public class App {

    private static final Log LOG = LogFactory.getLog(App.class);

    private static final Set<String> cliArguments = new HashSet<String>();
    
    public static final IVlcPlayer VLC_PLAYER = VlcPlayerFactory.newVlcPlayer();

    public static void main(String[] args) {
        try {
            App.cliArguments.addAll(Arrays.asList(args));
            new App().startUp();
        } catch(Exception e) {
            e.printStackTrace();
            LOG.fatal("Application could not startup!", e);
            System.exit(1);
        }
    }

    public void startUp() {
        JFrame.setDefaultLookAndFeelDecorated(true);
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

            TemporaryFilesCleaner.clean();
            App.addShutdownHook();

            if(App.checkDataVersions() == false) {
                LOG.warn("Checking core data versions failed!");
                System.exit(1);
            }

            if(PreferencesDao.getInstance().isStartupVersionCheck() == true) {
                LOG.info("Running initial application version check...");
                final VersionCheckDialog dialog = new VersionCheckDialog();
                dialog.startCheck();
                dialog.setVisible(true);
            }


            App.checkFileSystem();

            LOG.debug("Startup nearly finished; displaying main window left.");
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

    private static void checkFileSystem() {
        if(PreferencesDao.getInstance().isStartupFilesystemCheck() == false) {
            return;
        }
        LOG.info("Running automatic file system check.");

        try {
            final FileSystemCheckResult result = FileSystemChecker.process();
            if(result.isEverythingOkay() == false) {
                new FileSystemCheckDialog(result).setVisible(true);
            }

        } catch (BusinessException e) {
            LOG.error("Filesystem check failed!", e);
            GuiUtil.error("Filesystem Check failed", "Sorry, but could not perform the check because of an internal error.");
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

        // MANTIS [21] write converte for core sources and if none is available, ask for deletion (or display downloadlink for older version)
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
            final int preferenceSourceData = PreferencesDao.getInstance().getSoredVersion();
            LOG.debug("Stored preferences source version '"+preferenceSourceData+"'; application version in use '"+PreferencesDao.DATA_VERSION+"'.");
            if(preferenceSourceData == -1) {
                LOG.info("Preference datasource was not yet initialized; starting setup wizard.");
                final SetupWizard wizard = new SetupWizard();
                wizard.setVisible(true);

                if(wizard.isConfirmed() == false) {
                    LOG.info("User aborted setup.");
                    return false;
                }
                assert(PreferencesDao.getInstance().getSoredVersion() == PreferencesDao.DATA_VERSION);

            } else if(preferenceSourceData != PreferencesDao.DATA_VERSION) {
                GuiUtil.warning("Version Mismatch", "The version of the existing Preference Source\n" +
                                "does not match with the expected version!");



                // MANTIS [23] startup preference source data converter, if available
                // MANTIS [23] writer automatic converter v1 to v2 for Preferences Source, because new field 'should check application version at startup'

//                PreferenceSourceConverter converter = new PreferenceSourceConverter(preferenceSourceData, PreferencesDao.DATA_VERSION);
//                if(converter.isConvertable() == true) {
//                    final IConverter realConverter = converter.getConverter();
//                    realConverter.convertSource(PreferencesDao.getInstance());
//                    LOG.info("Converted preferences source with converter '"+realConverter.getClass().getSimpleName()+"'.");
//                    return true;
//                }

                /* show confirm popup: user should either select to reset/delete all pref data, or: just abort and get a list of compatible OurMovies versions (could use old app and write down old preference values) */
                if(GuiUtil.getYesNoAnswer(null, "Data not convertable", "Do you want to delete the old Preferences Source data\nand shutdown OurMovies immediatley to take effect?") == true) {
                    PreferencesDao.clearPreferences(); // otherwise clear all stored data and shutdown app by returning false
                }

                return false;


            } else if(preferenceSourceData == PreferencesDao.DATA_VERSION) {
                LOG.debug("Perferences source dataversion is compatible; nothing to do.");

            } else {
                throw new FatalException("Unhandled preferences source version '"+preferenceSourceData+"'!");
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