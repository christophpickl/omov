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

package net.sourceforge.omov.app.gui.main;

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

import net.sourceforge.omov.app.App;
import net.sourceforge.omov.app.gui.comp.generic.ITableSelectionListener;
import net.sourceforge.omov.app.help.HelpEntry;
import net.sourceforge.omov.app.help.HelpSystem;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Icon16x16;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.model.IMovieDao;
import net.sourceforge.omov.core.tools.vlc.VlcPlayerFactory;
import net.sourceforge.omov.core.util.CoverUtil;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.core.util.UserSniffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MenuBar extends JMenuBar implements ActionListener, ITableSelectionListener {

    private static final Log LOG = LogFactory.getLog(MenuBar.class);
    private static final long serialVersionUID = -140354789157881691L;

    private final JMenuItem preferencesItem;
    private final JMenuItem aboutItem;
    private final JMenuItem quitItem;

    
    
    // File
    private static final String LBL_IMPORT = "Import ...";
    private static final String CMD_IMPORT = "CMD_IMPORT.";

    private static final String LBL_EXPORT = "Export ...";
    private static final String CMD_EXPORT = "CMD_EXPORT";

    private static final String LBL_SMART_COPY = "Smart Copy...";
    private static final String CMD_SMART_COPY = "CMD_SMART_COPY";

    private static final String LBL_QUIT = "Quit";
    private static final String CMD_QUIT = "CMD_QUIT";

    // Movie
    private static final String LBL_NEW_MOVIE = "New Movie";
    private static final String CMD_NEW_MOVIE = "CMD_NEW_MOVIE";
    
    private static final String LBL_MOVIE_INFO = "Get Info";
    private static final String LBL_MOVIES_INFO = "Get Infos";
    private static final String CMD_MOVIE_INFO = "CMD_MOVIE_INFO";
//    private static final String CMD_MOVIES_INFO = "CMD_MOVIES_INFO"; // CMD_MOVIE_INFO used for both: single and multiple

    private static final String LBL_MOVIE_DELETE = "Delete Movie";
    private static final String LBL_MOVIES_DELETE = "Delete Movies";
    private static final String CMD_MOVIE_DELETE = "CMD_MOVIE_DELETE";
    
    private static final String LBL_MOVIE_REVEAL_FINDER = "Reveal in Finder";
    private static final String CMD_MOVIE_REVEAL_FINDER = "CMD_MOVIE_REVEAL_FINDER";
    
    private static final String LBL_MOVIE_PLAY_VLC = "Play in VLC";
    private static final String CMD_MOVIE_PLAY_VLC = "CMD_MOVIE_PLAY_VLC";
    
    private static final String LBL_FETCH_METADATA = "Fetch Metadata";
    private static final String CMD_FETCH_METADATA = "CMD_FETCH_METADATA";
    
    // Window
    private static final String CMD_SHOW_XXX = "Show XXX";
    
    // Extras
    private static final String LBL_SCAN = "Scan repository...";
    private static final String CMD_SCAN = "CMD_SCAN";

    private static final String LBL_FIND_DUPLICATES = "Find Duplicates";
    private static final String CMD_FIND_DUPLICATES = "CMD_FIND_DUPLICATES";

    private static final String LBL_RESCAN_FOLDERS = "Rescan Folder(s)";
    private static final String CMD_RESCAN_FOLDERS = "CMD_RESCAN_FOLDERS";
    
    private static final String LBL_PREFERENCES = "Preferences...";
    private static final String CMD_PREFERENCES = "CMD_PREFERENCES";
//    private static final String CMD_REMOTE = "Remote";

    // Help
    private static final String LBL_HELP = "Omov Help";
    private static final String CMD_HELP = "CMD_HELP";
    
    private static final String LBL_ABOUT = "About";
    private static final String CMD_ABOUT = "CMD_ABOUT";
    
    private final MainWindowController controller;

    private JMenuItem itemMovieInfo;
    private JMenuItem itemMovieDelete;
    private JMenuItem itemMovieFetchMetadata;
    private JMenuItem itemMovieRevealFinder;
    private JMenuItem itemMoviePlayVlc;
    private JMenuItem itemRescanFolders;
    
    
    
    public MenuBar(MainWindowController controller) {
        this.preferencesItem = GuiUtil.createMenuItem(null, 'P', LBL_PREFERENCES, CMD_PREFERENCES, this, KeyEvent.VK_P, ImageFactory.getInstance().getIcon(Icon16x16.PREFERENCES));
        this.aboutItem = GuiUtil.createMenuItem(null, 'A', LBL_ABOUT, CMD_ABOUT, this);
        this.quitItem = GuiUtil.createMenuItem(null, 'Q', LBL_QUIT, CMD_QUIT, this, KeyEvent.VK_Q);
        
        this.add(this.menuFile());
        this.add(this.menuMovie());
//        this.add(this.menuWindow());
        this.add(this.menuExtras());
        this.add(this.menuHelp()); // in menubar menuHelp() irgendwann enablen -- genauso menuWindow()
        DebugMenu.maybeAddYourself(this);
        
        this.controller = controller;
        this.selectionEmptyChanged();
    }

    private JMenu menuFile() {
        final JMenu menu = new JMenu("File");
        
        GuiUtil.createMenuItem(menu, 'S', LBL_SMART_COPY, CMD_SMART_COPY, this);
        menu.addSeparator();
        GuiUtil.createMenuItem(menu, 'E', LBL_EXPORT, CMD_EXPORT, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.EXPORT));
        GuiUtil.createMenuItem(menu, 'I', LBL_IMPORT, CMD_IMPORT, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.IMPORT));
        
        
        if(UserSniffer.isMacOSX() == false) {
            menu.addSeparator();
            menu.add(this.quitItem);
        }
        
        return menu;
    }

    private JMenu menuMovie() {
        final JMenu menu = new JMenu("Movie");

        GuiUtil.createMenuItem(menu, 'N', LBL_NEW_MOVIE, CMD_NEW_MOVIE, this, KeyEvent.VK_N, ImageFactory.getInstance().getIcon(Icon16x16.NEW_MOVIE));
        menu.addSeparator();
        this.itemMovieInfo = GuiUtil.createMenuItem(menu, 'I', LBL_MOVIE_INFO, CMD_MOVIE_INFO, this, KeyEvent.VK_I, ImageFactory.getInstance().getIcon(Icon16x16.INFORMATION));
        this.itemMovieDelete = GuiUtil.createMenuItem(menu, 'D', LBL_MOVIE_DELETE, CMD_MOVIE_DELETE, this, KeyEvent.VK_BACK_SPACE, ImageFactory.getInstance().getIcon(Icon16x16.DELETE), 0); // disable meta mask
        this.itemMovieFetchMetadata = GuiUtil.createMenuItem(menu, 'M', LBL_FETCH_METADATA, CMD_FETCH_METADATA, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.FETCH_METADATA));
        
        if(VlcPlayerFactory.isVlcCapable() == true) {
            this.itemMoviePlayVlc = GuiUtil.createMenuItem(menu, 'V', LBL_MOVIE_PLAY_VLC, CMD_MOVIE_PLAY_VLC, this, KeyEvent.VK_V, ImageFactory.getInstance().getIcon(Icon16x16.VLC));
        }
        
        if(UserSniffer.isMacOSX()) {
            this.itemMovieRevealFinder = GuiUtil.createMenuItem(menu, 'R', LBL_MOVIE_REVEAL_FINDER, CMD_MOVIE_REVEAL_FINDER, this, KeyEvent.VK_R, ImageFactory.getInstance().getIcon(Icon16x16.REVEAL_FINDER));
        }
        
        return menu;
    }

    @SuppressWarnings("unused")
    private JMenu menuWindow() {
        final JMenu menu = new JMenu("Window");
        
        GuiUtil.createMenuItem(menu, 'X', "xxx", CMD_SHOW_XXX, this);
        
        return menu;
    }
    
    private JMenu menuExtras() {
        final JMenu menu = new JMenu("Extras");

        GuiUtil.createMenuItem(menu, 'S', LBL_SCAN, CMD_SCAN, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.SCAN));
        
        GuiUtil.createMenuItem(menu, 'F', LBL_FIND_DUPLICATES, CMD_FIND_DUPLICATES, this);
        
        this.itemRescanFolders = GuiUtil.createMenuItem(menu, 'R', LBL_RESCAN_FOLDERS, CMD_RESCAN_FOLDERS, this);
        
//      GuiUtil.createMenuItem(menu, CMD_REMOTE, this);
        
        if(UserSniffer.isMacOSX() == false) { // Mac OS X got its own preferences menuitem in a system-own menu
            menu.addSeparator();
            menu.add(this.preferencesItem);
        }
        
        return menu;
    }
    
    private JMenu menuHelp() {
        final JMenu menu = new JMenu("Help");

        final JMenuItem helpItem = GuiUtil.createMenuItem(menu, 'H', LBL_HELP, CMD_HELP, this, -1, ImageFactory.getInstance().getIcon(Icon16x16.HELP));
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
                controller.doEditMovie(); // either edits single or multiple movies
            } else if(cmd.equals(CMD_MOVIE_DELETE)) {
                controller.doDeleteMovie(); // either deletes single or multiple movies
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
            } else if(cmd.equals(CMD_RESCAN_FOLDERS)) {
                controller.doRescanFolders();
//            } else if(cmd.equals(CMD_REMOTE)) {
//                this.controller.doRemoteConnect();
            } else {
                assert(false) : "Unhandled menu item clicked '"+cmd+"'!";
            }
        }}.doAction();
    }
    
    
    
    private static class DebugMenu {
    	private static DebugDatabaseContents databaseContents;
        public static void maybeAddYourself(JMenuBar bar) {
            if(App.isArgumentSet(PreferencesDao.APP_ARG_DEBUG) == false) {
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
            
            GuiUtil.createMenuItem(menu, 'P', "Show Preferences & Stuff", "", new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    prefDialog.setVisible(true);
                }
            });
            
            GuiUtil.createMenuItem(menu, 'D', "Drop Movies", "",new ActionListener() {
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
            
            GuiUtil.createMenuItem(menu, 'S', "Show Database Contents", "",new ActionListener() {
                public void actionPerformed(ActionEvent event) {
                    LOG.info("displaying database contents.");
                    if(databaseContents == null) {
                    	databaseContents = new DebugDatabaseContents();
                    }
                    databaseContents.setVisible(true);
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

    
    
    public void selectionEmptyChanged() {
        this.itemMovieInfo.setText(LBL_MOVIE_INFO);
        this.itemMovieDelete.setText(LBL_MOVIE_DELETE);
        
        this.itemMovieDelete.setEnabled(false);
        this.itemMovieInfo.setEnabled(false);
        this.itemRescanFolders.setEnabled(false);
        
        this.itemMovieFetchMetadata.setEnabled(false);
        if(this.itemMoviePlayVlc != null) this.itemMoviePlayVlc.setEnabled(false);
        if(this.itemMovieRevealFinder != null) this.itemMovieRevealFinder.setEnabled(false);
    }

    public void selectionSingleChanged(Movie newSelectedMovie) {
        this.itemMovieInfo.setText(LBL_MOVIE_INFO);
        this.itemMovieDelete.setText(LBL_MOVIE_DELETE);
        
        this.itemMovieDelete.setEnabled(true);
        this.itemMovieInfo.setEnabled(true);
        this.itemRescanFolders.setEnabled(true);
        
        this.itemMovieFetchMetadata.setEnabled(true);
        if(this.itemMoviePlayVlc != null) this.itemMoviePlayVlc.setEnabled(true);
        if(this.itemMovieRevealFinder != null) this.itemMovieRevealFinder.setEnabled(true);
    }

    public void selectionMultipleChanged(List<Movie> newSelectedMovies) {
        this.itemMovieInfo.setText(LBL_MOVIES_INFO);
        this.itemMovieDelete.setText(LBL_MOVIES_DELETE);
        
        this.itemMovieDelete.setEnabled(true);
        this.itemMovieInfo.setEnabled(true);
        this.itemRescanFolders.setEnabled(true);
        
        this.itemMovieFetchMetadata.setEnabled(false);
        if(this.itemMoviePlayVlc != null) this.itemMoviePlayVlc.setEnabled(false);
        if(this.itemMovieRevealFinder != null) this.itemMovieRevealFinder.setEnabled(false);
    }

}
