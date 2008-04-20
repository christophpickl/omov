package at.ac.tuwien.e0525580.omov.gui.main;

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
import javax.swing.JTable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.gui.IPrevNextMovieProvider;
import at.ac.tuwien.e0525580.omov.gui.ImageFactory;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ITableSelectionListener;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.brushed.BrushedMetalPanel;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.IMovieTableContextMenuListener;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.MovieTableModel;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.MovieTableX;
import at.ac.tuwien.e0525580.omov.gui.smartfolder.SmartFolderSelectionPanel;
import at.ac.tuwien.e0525580.omov.tools.osx.OSXAdapter;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;
import at.ac.tuwien.e0525580.omov.util.GuiUtil.GuiAction;

public class MainWindow extends JFrame implements IMovieTableContextMenuListener, ITableSelectionListener {

    private static final long serialVersionUID = -1367955221953478216L;
    private static final Log LOG = LogFactory.getLog(MainWindow.class);

    private final MainWindowController controller = new MainWindowController(this);

    private final MovieTableModel moviesModel = new MovieTableModel();
    private final MovieTableX moviesTable;

    private final MovieDetailPanel movieDetailPanel = new MovieDetailPanel();
    private Movie selectedMovie;
    private boolean activated = false;


    public MainWindow() {
        this.setTitle("OurMovies");
        this.setIconImage(ImageFactory.getInstance().getFrameTitleIcon());

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                controller.doQuit();
            }
            public void windowActivated(WindowEvent event) {
                LOG.debug("Main window activated.");
                activated = true;
            }
            public void windowDeactivated(WindowEvent event) {
                LOG.debug("Main window deactivated.");
                activated = false;
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

        this.setJMenuBar(menuBar);
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(true);
        GuiUtil.setCenterLocation(this); // FEATURE restore last window state (remember size, position and maybe also viewposition of movie table's scrollpane)
        GuiUtil.lockOriginalSizeAsMinimum(this);
    }

    boolean isActivated() {
        return this.activated;
    }

    private JPanel initComponents() {
        final JPanel panel = new BrushedMetalPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right

        panel.add(this.initNorthPanel(), BorderLayout.NORTH);
        panel.add(this.initComponentTable(), BorderLayout.CENTER);

        return panel;
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

    private void initMovieTable() {
        this.moviesTable.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

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
                            final Movie selectedMovie = moviesModel.getMovieAt(modelRow);
                            controller.doEditMovie(selectedMovie, newPrevNextMovieProvider());
                        }
                    }
                }}.doAction();
            }
        });

        this.moviesTable.addTableSelectionListener(this);

        this.moviesTable.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent event) { /* nothing to do */ }
            public void keyReleased(KeyEvent event) {
                final int code = event.getKeyCode();
                if(code == KeyEvent.VK_BACK_SPACE) {

                    final List<Movie> selectedMovies = getSelectedMovies();
                    if(selectedMovies.size() == 1) {
                        controller.doDeleteMovie(selectedMovies.get(0));
                    } else if(selectedMovies.size() > 1) {
                        controller.doDeleteMovies(selectedMovies);
                    } else {
                        assert (selectedMovies.size() == 0);
                        Toolkit.getDefaultToolkit().beep();
                    }
                }
            }
            public void keyTyped(KeyEvent event) { /* nothing to do */ }
        });
    }

//    void reloadTableData() {
//        LOG.debug("Reloading main movietable data.");
//        this.moviesModel.movieDataChanged();
//    }

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
