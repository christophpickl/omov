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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sourceforge.omov.app.gui.IPrevNextMovieProvider;
import net.sourceforge.omov.app.gui.main.tablex.IMovieTableContextMenuListener;
import net.sourceforge.omov.app.gui.main.tablex.MovieTableModel;
import net.sourceforge.omov.app.gui.main.tablex.MovieTableX;
import net.sourceforge.omov.app.gui.smartfolder.SmartFolderSelectionPanel;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.tools.osx.OSXAdapter;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.core.util.UserSniffer;
import net.sourceforge.omov.gui.brushed.BrushedMetalPanel;
import net.sourceforge.omov.gui.table.ITableSelectionListener;
import net.sourceforge.omov.qtjApi.QtjFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MainWindow extends JFrame implements IMovieTableContextMenuListener, ITableSelectionListener {

    private static final Log LOG = LogFactory.getLog(MainWindow.class);
    private static final long serialVersionUID = -1367955221953478216L;

    private final MainWindowController controller = new MainWindowController(this);

    private final MovieTableModel moviesModel = new MovieTableModel();
    private final MovieTableX moviesTable;

    private final MovieDetailPanel movieDetailPanel = new MovieDetailPanel();
    private Movie selectedMovie;
    private boolean activated = false;
    private final BrushedMetalPanel backgroundPanel = new BrushedMetalPanel();

    
    public MainWindow() {
        this.setTitle("OurMovies");
        this.setIconImage(AppImageFactory.getInstance().getFrameTitleIcon());

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                controller.doQuit();
            }
            public void windowActivated(WindowEvent event) {
            	windowDidActivate(true);
            }
            public void windowDeactivated(WindowEvent event) {
            	windowDidActivate(false);
            }
            public void windowOpened(WindowEvent event) {
                // Set up our application to respond to the Mac OS X application menu
                registerForMacOSXEvents();
            }
        });

        this.moviesTable = new MovieTableX(this, this.moviesModel);
        this.moviesModel.setTable(this.moviesTable);

//        this.setPreferredSize(new Dimension(860, 520));

        final MenuBar menuBar = new MenuBar(this.controller);
        this.moviesTable.addTableSelectionListener(menuBar);
        this.moviesTable.setRowHeight(25);

        this.setJMenuBar(menuBar);
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(true);
        GuiUtil.setCenterLocation(this); // FEATURE restore last window state (remember size, position and maybe also viewposition of movie table's scrollpane)
        GuiUtil.lockOriginalSizeAsMinimum(this);
    }
    
    private void windowDidActivate(boolean didActivate) {
//    	LOG.debug("Main window "+(didActivate ? "activated" : "deactivated")+".");
    	this.activated = didActivate;
    	this.backgroundPanel.setActive(didActivate);
    	this.repaint();
    }

    boolean isActivated() {
        return this.activated;
    }

    private JPanel initComponents() {
    	this.backgroundPanel.setLayout(new BorderLayout());
    	this.backgroundPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right
    	this.backgroundPanel.add(this.initNorthPanel(), BorderLayout.NORTH);
    	this.backgroundPanel.add(this.initComponentTable(), BorderLayout.CENTER);

        return this.backgroundPanel;
    }

    private JPanel initNorthPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

        panel.add(new SmartFolderSelectionPanel(this, this.moviesModel), BorderLayout.WEST);
        panel.add(new MovieSearchPanel(this.moviesModel), BorderLayout.EAST);

        return panel;
    }

    
    

    private Component initComponentTable() {
        this.initMovieTable();

        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);

//        JScrollPane moviesTableScrollPane = new JScrollPane(this.moviesTable);
        JScrollPane moviesTableScrollPane = new JScrollPane();
        moviesTableScrollPane.setViewportView(this.moviesTable);
        moviesTableScrollPane.setWheelScrollingEnabled(true);
        moviesTableScrollPane.setPreferredSize(new Dimension(850, 160));

        panel.add(moviesTableScrollPane, BorderLayout.CENTER);
        panel.add(this.movieDetailPanel.getPanel(), BorderLayout.SOUTH);

        return panel;
    }

    /**
     * adds following listeners: TableSelectionListener, MouseListener and KeyListener
     */
    private void initMovieTable() {
    	this.moviesTable.addTableSelectionListener(this);
    	
        this.moviesTable.addMouseListener(new MouseAdapter() {
            public void mouseClicked(final MouseEvent event) {
                new GuiAction() { protected void _action() {
                    LOG.debug("mouseClicked on moviesTable: event.getButton()="+event.getButton()+"; clickCount="+event.getClickCount()+"");
                    // if(event.getButton() != MouseEvent.BUTTON1) {
                    if( (event.getModifiers() & InputEvent.BUTTON1_MASK) != InputEvent.BUTTON1_MASK) {
                        LOG.debug("Ignoring mouseclick because mouse button was not button1 ("+MouseEvent.BUTTON1+") but '"+event.getButton()+"'.");
                        return;
                    }

                    int tableRow = moviesTable.getSelectedRow();
                    if (tableRow > -1) {
                        final int modelRow = moviesTable.getSelectedModelRow();
//                        selectedMovieChanged();

                        if (event.getClickCount() >= 2) {
                            LOG.debug("Double clicked on table tableRow "+tableRow+" (modelRow "+modelRow+"); displaying editDialog.");
                            
                            controller.doEditMovie(moviesModel.getMovieAt(modelRow), newPrevNextMovieProvider());
                        }
                    }
                }}.doAction();
            }
        });

        this.moviesTable.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent event) { /* nothing to do */ }
            public void keyReleased(final KeyEvent event) {
            	new GuiAction() {
					@Override
					protected void _action() {
						final int code = event.getKeyCode();
		                LOG.debug("main movies table got key event with code "+code+" ("+event.getKeyChar()+").");
		                
		                if(code == KeyEvent.VK_BACK_SPACE || code == KeyEvent.VK_DELETE) {
		                	LOG.debug("key event: backspace or delete");
		                	
		                	final List<Movie> selectedMovies = getSelectedMovies();
		                    if(selectedMovies.size() == 1) {
		                        controller.doDeleteMovie(selectedMovies.get(0));
		                        
		                    } else if(selectedMovies.size() > 1) {
		                        controller.doDeleteMovies(selectedMovies);
		                        
		                    } else {
		                        assert (selectedMovies.size() == 0);
		                        Toolkit.getDefaultToolkit().beep();
		                    }
		                } else if(code == KeyEvent.VK_SPACE && QtjFactory.isQtjAvailable()) {
		                	LOG.debug("key event: space");
		                	
		                	final List<Movie> selectedMovies = getSelectedMovies();
		                	if(selectedMovies.size() == 1) {
		                		controller.doPlayQuickView(selectedMovies.get(0));
		                	} else {
		                		Toolkit.getDefaultToolkit().beep();
		                	}
		                }
					}
            		
            	}.doAction();
            }
            public void keyTyped(KeyEvent event) { /* nothing to do */ }
        });
    }

    public void selectionEmptyChanged() {
        this.selectedMovie = null;
        this.movieDetailPanel.setMovie(null);
    }

    public void selectionSingleChanged(final Movie newSelectedMovie) {
        LOG.debug("Another movie selected: " + newSelectedMovie);

        if(newSelectedMovie != null && newSelectedMovie.equals(this.selectedMovie)) {
            LOG.debug("Another movie seems to be the same as last one.");
            return;
        }

        this.selectedMovie = newSelectedMovie;
        this.movieDetailPanel.setMovie(newSelectedMovie);
    }

    public void selectionMultipleChanged(final List<Movie> newSelectedMovies) {
        this.selectedMovie = null;
        this.movieDetailPanel.setMovie(null);
    }


    /**
     * until now, will be also invoked if selectedRowCount == 0 || == 1
     */
    public List<Movie> getSelectedMovies() {
        final int[] selectedRows = this.moviesTable.getSelectedRows();
        final List<Movie> selectedMovies = new ArrayList<Movie>(selectedRows.length);

        for (int i = 0; i < selectedRows.length; i++) {
            selectedMovies.add(this.moviesModel.getMovieAt(this.moviesTable.convertRowIndexToModel(selectedRows[i])));
        }

        return Collections.unmodifiableList(selectedMovies);
    }

    public List<Movie> getVisibleMovies() {
        final int rowCount = this.moviesTable.getRowCount();
        final List<Movie> visibleMovies = new ArrayList<Movie>(rowCount);

        for (int i = 0; i < rowCount; i++) {
            visibleMovies.add(this.moviesModel.getMovieAt(i));
        }

        return Collections.unmodifiableList(visibleMovies);
    }

    public void didAddMovie(Movie addedMovie) {
        this.moviesModel.setSelectedRowsByMovieId(true, addedMovie.getId());
    }

    public void didEditMovie(Movie editMovie) {
        this.moviesModel.setSelectedRowsByMovieId(true, editMovie.getId());
    }

    public void doEditMovie(int tableRowSelected) {
        final Movie movie = this.moviesModel.getMovieAt(this.moviesTable.convertRowIndexToModel(tableRowSelected));
        this.controller.doEditMovie(movie, this.newPrevNextMovieProvider());
    }

    public void doEditMovies(int[] tableRowSelected) {
        this.controller.doEditMovies(this.moviesModel.getMoviesAt(tableRowSelected));
    }
    public void doDeleteMovie(int tableRowSelected) {
        final Movie movie = this.moviesModel.getMovieAt(this.moviesTable.convertRowIndexToModel(tableRowSelected));
        this.controller.doDeleteMovie(movie);
    }

    public void doDeleteMovies(int[] tableRowSelected) {
        this.controller.doDeleteMovies(this.moviesModel.getMoviesAt(this.moviesTable.convertRowIndicesToModel(tableRowSelected)));
    }

    public void doFetchMetaData(int tableRowSelected) {
        this.controller.doFetchMetaData(this.moviesModel.getMovieAt(this.moviesTable.convertRowIndexToModel(tableRowSelected)));
    }

    public void doRevealMovie(int tableRowSelected) {
        this.controller.doRevealMovie(this.moviesModel.getMovieAt(this.moviesTable.convertRowIndexToModel(tableRowSelected)));
    }

    public void doPlayVlc(int tableRowSelected) {
        this.controller.doPlayVlc(this.moviesModel.getMovieAt(this.moviesTable.convertRowIndexToModel(tableRowSelected)));
    }

    IPrevNextMovieProvider newPrevNextMovieProvider() {
        LOG.debug("creating new IPrevNextMovieProvider (count="+moviesModel.getRowCount()+"; initial row="+moviesTable.getSelectedRow()+")");
        return new IPrevNextMovieProvider() {
            public int getCountIndices() {
                return moviesModel.getRowCount();
            }
            public int getInitialIndex() {
                return moviesTable.getSelectedRow();
            }
            public Movie getMovieAt(int tableRow) {
                return moviesModel.getMovieAt(moviesTable.convertRowIndexToModel(tableRow));
            }
        };
    }


    /**
     * Generic registration with the Mac OS X application menu.
     * Checks the platform, then attempts to register with the Apple EAWT.
     * @see OSXAdapter.java to see how this is done without directly referencing any Apple APIs
     */
    private void registerForMacOSXEvents() {
        if (UserSniffer.isMacOSX() == true) {
            LOG.info("Registering for osx events.");
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                final Object target = this.controller;
                final Class<?> tClass = target.getClass();

                OSXAdapter.setQuitHandler(target, tClass.getDeclaredMethod("doQuit", (Class[])null));
                OSXAdapter.setAboutHandler(target, tClass.getDeclaredMethod("doShowAbout", (Class[])null));
                OSXAdapter.setPreferencesHandler(target, tClass.getDeclaredMethod("doShowPreferences", (Class[])null));
                OSXAdapter.setFileHandler(target, tClass.getDeclaredMethod("doHandleFile", new Class[] { String.class }));
            } catch (Exception e) {
                LOG.error("Error while loading the OSXAdapter!", e);
                e.printStackTrace();
            }
        }
    }

}
