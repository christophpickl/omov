package at.ac.tuwien.e0525580.omov;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.gui.SetupWizard;
import at.ac.tuwien.e0525580.omov.gui.SplashScreen;
import at.ac.tuwien.e0525580.omov.gui.main.MainWindow;
import at.ac.tuwien.e0525580.omov.model.IDatabaseConnection;
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

    private static List<String> cliArguments;
    
    
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            LOG.error("Unable to set system look&feel!", ex);
        }
        
        App.cliArguments = Arrays.asList(args);
        final SplashScreen splashScreen = new SplashScreen();
        splashScreen.setVisible(true);
        
        try {
            final long timeStart = new Date().getTime();
            if(App.checkConfiguration() == false) {
                LOG.debug("Checking configuration failed.");
                return;
            }
    
            // FEATURE check file consistency at startup; for each movie/directory: check if files still exist (mantis: 2)
            //         -> maybe check if other moviefiles were added; maybe also recalculate size if files changed;
            
            TemporaryFilesCleaner.clean();
            App.addShutdownHook();
            
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
    
    public static boolean isArgumentSet(String argument) {
        return App.cliArguments.contains(argument);
    }
    
    private static boolean checkConfiguration() {
        try {
            if(Configuration.getInstance().isInitialized() == false) {
                LOG.info("Configuration was not yet initialized; starting setup wizard.");
                final SetupWizard wizard = new SetupWizard();
                wizard.setVisible(true);
                
                if(wizard.isConfirmed() == false) {
                    LOG.info("User aborted setup.");
                    return false;
                }
                assert(Configuration.getInstance().isInitialized());
            }
        } catch (Exception e) {
            LOG.error("Could not check/set preferences!", e);
            GuiUtil.error("Setup failed!", "Could not set initial values: " + e.getMessage());
            return false;
        }
        try {
            Configuration.getInstance().checkFolderExistence();
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
