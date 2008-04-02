package at.ac.tuwien.e0525580.omov.gui.main;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;
import java.util.prefs.BackingStoreException;
import java.util.prefs.Preferences;

import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JScrollPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.App;
import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory.Icon16x16;
import at.ac.tuwien.e0525580.omov.help.HelpEntry;
import at.ac.tuwien.e0525580.omov.help.HelpSystem;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.util.CoverUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;


public class MenuBar extends JMenuBar implements ActionListener {

    private static final Log LOG = LogFactory.getLog(MenuBar.class);
    private static final long serialVersionUID = -140354789157881691L;

    private final JMenuItem preferencesItem;
    private final JMenuItem aboutItem;
    private final JMenuItem quitItem;

    
    
    // File
    private static final String CMD_IMPORT = "Import ...";
    private static final String CMD_EXPORT = "Export ...";
    private static final String CMD_SMART_COPY = "Smart Copy...";
    private static final String CMD_QUIT = "Quit";

    // Movie
    private static final String CMD_NEW_MOVIE = "New Movie";
    private static final String CMD_MOVIE_INFO = "Get Info";
    private static final String CMD_DELETE_MOVIE = "Delete Movie";
    private static final String CMD_MOVIE_REVEAL_FINDER = "Reveal in Finder";
    private static final String CMD_MOVIE_PLAY_VLC = "Play in VLC";
    private static final String CMD_FETCH_METADATA = "Fetch Metadata";
    
    // Window
    private static final String CMD_SHOW_XXX = "Show XXX";
    
    // Extras
    private static final String CMD_SCAN = "Scan repository...";
    private static final String CMD_FIND_DUPLICATES = "Find Duplicates";
    private static final String CMD_PREFERENCES = "Preferences...";
//    private static final String CMD_REMOTE = "Remote";

    // Help
    private static final String CMD_HELP = "Omov Help";
    private static final String CMD_ABOUT = "About";
    
    private final MainWindowController controller;
    
    public MenuBar(MainWindowController controller) {
        this.preferencesItem = GuiUtil.createMenuItem(null, 'P', CMD_PREFERENCES, this, KeyEvent.VK_P, ImageFactory.getInstance().getIcon(Icon16x16.PREFERENCES));
        this.aboutItem = GuiUtil.createMenuItem(null, 'A', CMD_ABOUT, this);
        this.quitItem = GuiUtil.createMenuItem(null, 'Q', CMD_QUIT, this, KeyEvent.VK_Q);
        
        this.add(this.menuFile());
        this.add(this.menuMovie());
//        this.add(this.menuWindow());
        this.add(this.menuExtras());
        this.add(this.menuHelp()); // in menubar menuHelp() irgendwann enablen -- genauso menuWindow()
        DebugMenu.maybeAddYourself(this);
        
        this.controller = controller;
    }

    private JMenu menuFile() {
        final JMenu menu = new JMenu("File");
        
        GuiUtil.createMenuItem(menu, 'E', CMD_EXPORT, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.EXPORT));
        GuiUtil.createMenuItem(menu, 'I', CMD_IMPORT, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.IMPORT));
        GuiUtil.createMenuItem(menu, 'S', CMD_SMART_COPY, this);
        
        if(UserSniffer.isMacOSX() == false) {
            menu.addSeparator();
            menu.add(this.quitItem);
        }
        
        return menu;
    }

    private JMenu menuMovie() {
        final JMenu menu = new JMenu("Movie");

        GuiUtil.createMenuItem(menu, 'N', CMD_NEW_MOVIE, this, KeyEvent.VK_N, ImageFactory.getInstance().getIcon(Icon16x16.NEW_MOVIE));
        menu.addSeparator();
        GuiUtil.createMenuItem(menu, 'I', CMD_MOVIE_INFO, this, KeyEvent.VK_I, ImageFactory.getInstance().getIcon(Icon16x16.INFORMATION));
        GuiUtil.createMenuItem(menu, 'D', CMD_DELETE_MOVIE, this, KeyEvent.VK_D, ImageFactory.getInstance().getIcon(Icon16x16.DELETE));
        GuiUtil.createMenuItem(menu, 'M', CMD_FETCH_METADATA, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.FETCH_METADATA));
        
        if(UserSniffer.isMacOSX()) {
            GuiUtil.createMenuItem(menu, 'R', CMD_MOVIE_REVEAL_FINDER, this, KeyEvent.VK_R, ImageFactory.getInstance().getIcon(Icon16x16.REVEAL_FINDER));
            GuiUtil.createMenuItem(menu, 'V', CMD_MOVIE_PLAY_VLC, this, KeyEvent.VK_V, ImageFactory.getInstance().getIcon(Icon16x16.VLC));
        }
        menu.addSeparator();
        
        GuiUtil.createMenuItem(menu, 'F', CMD_FIND_DUPLICATES, this);
        
        return menu;
    }

    @SuppressWarnings("unused")
    private JMenu menuWindow() {
        final JMenu menu = new JMenu("Window");
        
        GuiUtil.createMenuItem(menu, 'X', CMD_SHOW_XXX, this);
        
        return menu;
    }
    
    private JMenu menuExtras() {
        final JMenu menu = new JMenu("Extras");

        GuiUtil.createMenuItem(menu, 'S', CMD_SCAN, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.SCAN));
//      GuiUtil.createMenuItem(menu, CMD_REMOTE, this);
        
        if(UserSniffer.isMacOSX() == false) {
            menu.addSeparator();
            menu.add(this.preferencesItem);
        }
        
        return menu;
    }
    
    private JMenu menuHelp() {
        final JMenu menu = new JMenu("Help");

        final JMenuItem helpItem = GuiUtil.createMenuItem(menu, 'H', CMD_HELP, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.HELP));
        HelpSystem.enableHelp(helpItem, HelpEntry.HOME);
        
        if(UserSniffer.isMacOSX() == false) {
            menu.add(this.aboutItem);
        }
        
        return menu;
    }
    
    public void actionPerformed(final ActionEvent event) {
        new GuiAction() { protected void _action() {
            final JMenuItem menuItem = (JMenuItem) event.getSource();
            final String cmd = menuItem.getActionCommand();
            
            LOG.debug("Label clicked: '"+menuItem.getText()+"'");
            if(cmd.equals(CMD_SCAN)) {
                controller.doScan();
            } else if(cmd.equals(CMD_NEW_MOVIE)) {
                controller.doAddMovie();
            } else if(cmd.equals(CMD_QUIT)) {
                controller.doQuit();
            } else if(cmd.equals(CMD_SMART_COPY)) {
                controller.doSmartCopy();
            } else if(cmd.equals(CMD_MOVIE_INFO)) {
                controller.doEditMovie();
            } else if(cmd.equals(CMD_DELETE_MOVIE)) {
                controller.doDeleteMovie();
            } else if(cmd.equals(CMD_FETCH_METADATA)) {
                controller.doFetchMetaData();
            } else if(cmd.equals(CMD_IMPORT)) {
                controller.doImportBackup();
            } else if(cmd.equals(CMD_EXPORT)) {
                controller.doExport();
            } else if(cmd.equals(CMD_SHOW_XXX)) {
                GuiUtil.info("ups", "nothing implemented");
            } else if(cmd.equals(CMD_FIND_DUPLICATES)) {
                controller.doFindDuplicates();
            } else if(cmd.equals(CMD_PREFERENCES)) {
                controller.doShowPreferences();
            } else if(cmd.equals(CMD_HELP)) {
                // do nothing
            } else if(cmd.equals(CMD_ABOUT)) {
                controller.doShowAbout();
            } else if(cmd.equals(CMD_MOVIE_REVEAL_FINDER)) {
                controller.doRevealMovie();
            } else if(cmd.equals(CMD_MOVIE_PLAY_VLC)) {
                controller.doPlayVlc();
//            } else if(cmd.equals(CMD_REMOTE)) {
//                this.controller.doRemoteConnect();
            } else {
                assert(false) : "Unhandled menu item clicked '"+cmd+"'!";
            }
        }}.doAction();
    }
    
    
    
    private static class DebugMenu {
        public static void maybeAddYourself(JMenuBar bar) {
            if(App.isArgumentSet(PreferencesDao.APPARG_DEBUG_MENU) == false) {
                return;
            }
            LOG.info("adding debug menu.");
            final JMenu menu = new JMenu("Debug");
            
//            GuiUtil.createMenuItem(menu, 'R', "Reset Movies", new ActionListener() {
//                public void actionPerformed(ActionEvent event) {
//                    LOG.info("resetting movies.");
//                    try {
//                        IMovieDao dao = BeanFactory.getInstance().getMovieDao();
//                        for (Movie movie : dao.getMovies()) {
//                            dao.deleteMovie(movie);
//                        }
//                        // General: String title, boolean seen, int rating, String coverFile, Set genres, Set languages, String style
//                        // Detail: Set actors, int year, String comment
//                        // Technical: long fileSizeKb, String folderPath, String format, Set files, int duration, Resolution resolution, Set subtitles
//                        Movie m = Movie.create(-1).title("Indiana Jones 1").seen(true).rating(3).genres("Action", "Romance").languages("DE").style("Color")
//                        .actors("Harrison Ford").year(1998).comment("Sau lustig")
//                        .fileSizeKb(23542341).folderPath("").format("mp4").files("indi3.mp4").duration(142323).resolution(new Resolution(712, 568))
//                        .get();
//                    dao.insertMovie(m);
//                    
//                    m = Movie.create(-1).title("The Matrix").seen(false).rating(0).genres("SciFi").languages("EN", "DE").style("Color")
//                    .actors("Keanu Reeves").year(2002).comment("")
//                    .fileSizeKb(12352342).folderPath("").format("avi").duration(3523423).resolution(new Resolution(923, 600))
//                    .get();
//                    dao.insertMovie(m);
//                        
//                    } catch (BusinessException e) {
//                        LOG.error("Resetting movies failed!", e);
//                        GuiUtil.error("Reset failed", "Resetting movies failed: " + e.getMessage());
//                    }
//           }});

            class PrefDialog extends JDialog {
                private static final long serialVersionUID = 1577151281639336184L;
                public PrefDialog() {
                    final List<String> prefKeyValues = new LinkedList<String>();
                    final Preferences prefs = Preferences.userNodeForPackage(PreferencesDao.class);
                    try {
                        for (String key : prefs.keys()) {
                            String prefValue = prefs.get(key, "null");
                            prefKeyValues.add(key + " = " + prefValue);
                        }
                    } catch (BackingStoreException e) {
                        LOG.error("Could not load preferences!", e);
                    }
                    
                    prefKeyValues.add("------------------------- Preferences End");
                    prefKeyValues.add("current working directory = " + new File("").getAbsolutePath());
                    
                    final JList prefList = new JList(prefKeyValues.toArray());
                    final JScrollPane scroll = new JScrollPane(prefList);
                    this.getContentPane().add(scroll);
                    
                    this.setTitle("Preferences Window");
                    this.pack();
                }
            }
            final PrefDialog prefDialog = new PrefDialog();
            
            GuiUtil.createMenuItem(menu, 'P', "Show Preferences & Stuff", new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    prefDialog.setVisible(true);
                }
            });
            
            GuiUtil.createMenuItem(menu, 'D', "Drop Movies", new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    LOG.info("resetting movies.");
                    try {
                        IMovieDao dao = BeanFactory.getInstance().getMovieDao();
                        for (Movie movie : dao.getMovies()) {
                            dao.deleteMovie(movie);
                            CoverUtil.deleteCoverFileIfNecessary(movie);
                        }
                    } catch (BusinessException e) {
                        LOG.error("Resetting movies failed!", e);
                        GuiUtil.error("Reset failed", "Resetting movies failed: " + e.getMessage());
                    }
           }});
            
            
            bar.add(menu);
        }
    }

    public JMenuItem getPreferencesItem() {
        return this.preferencesItem;
    }
    public JMenuItem getAboutItem() {
        return this.aboutItem;
    }
    public JMenuItem getQuitItem() {
        return this.quitItem;
    }
}
