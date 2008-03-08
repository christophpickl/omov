package at.ac.tuwien.e0525580.omov.gui.main;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
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
import at.ac.tuwien.e0525580.omov.gui.comp.generic.brushed.BrushedMetalPanel;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.IMovieTableContextMenuListener;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.MovieTableModel;
import at.ac.tuwien.e0525580.omov.gui.main.tablex.MovieTableX;
import at.ac.tuwien.e0525580.omov.gui.smartfolder.SmartFolderSelectionPanel;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class MainWindow extends JFrame implements IMovieTableContextMenuListener {

    private static final long serialVersionUID = -1367955221953478216L;
    private static final Log LOG = LogFactory.getLog(MainWindow.class);
    
    private final MainWindowController controller = new MainWindowController(this);

    private final MovieTableModel moviesModel = new MovieTableModel();
    private final MovieTableX moviesTable;
    
    private final MovieDetailPanel movieDetailPanel = new MovieDetailPanel();
    private Movie selectedMovie;
    
    
    public MainWindow() {
        this.setTitle("OurMovies");
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                controller.doQuit();
            }
        });
        
        this.moviesTable = new MovieTableX(this, this.moviesModel);
        
//        this.setPreferredSize(new Dimension(860, 520));
        
        this.setJMenuBar(new MenuBar(this.controller));
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(true);
        GuiUtil.setCenterLocation(this);
        GuiUtil.lockOriginalSizeAsMinimum(this);
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
            public void mouseClicked(MouseEvent event) {
                LOG.debug("mouseClicked on moviesTable: event.getButton()="+event.getButton()+"; clickCount="+event.getClickCount()+"");
                if(event.getButton() != MouseEvent.BUTTON1) {
                    LOG.debug("Ignoring mouseclick because mouse button was not button1 ("+MouseEvent.BUTTON1+").");
                    return;
                }
                
                int tableRow = moviesTable.getSelectedRow();
                if (tableRow > -1) {
                    final int modelRow = moviesTable.getSelectedModelRow();
                    selectedMovieChanged();
                    
                    if (event.getClickCount() >= 2) {
                        LOG.debug("Double clicked on table tableRow "+tableRow+" (modelRow "+modelRow+"); displaying editDialog.");
                        final Movie selectedMovie = moviesModel.getMovieAt(modelRow);
                        controller.doEditMovie(selectedMovie, newPrevNextMovieProvider());
                    }
                }
                
            }
        });
        
        this.moviesTable.addKeyListener(new KeyListener() {
            public void keyPressed(KeyEvent event) { }
            public void keyReleased(KeyEvent event) {
                final int code = event.getKeyCode();
                if(code == KeyEvent.VK_UP || code == KeyEvent.VK_DOWN) {
                    selectedMovieChanged();
                }
            }
            public void keyTyped(KeyEvent event) { }
        });
    }
    
    private void selectedMovieChanged() {
        final Movie newSelectedMovie = this.moviesModel.getMovieAt(this.moviesTable.getSelectedModelRow());
        LOG.debug("Another movie selected: " + newSelectedMovie);
        
        if(newSelectedMovie != null && newSelectedMovie.equals(this.selectedMovie)) {
            LOG.debug("Another movie seems to be the same as last one.");
            return;
        }
        
        this.selectedMovie = newSelectedMovie;
        this.movieDetailPanel.setMovie(newSelectedMovie);
    }
    
    public List<Movie> getSelectedMovies() {
        final int[] selectedRows = this.moviesTable.getSelectedRows();
        final List<Movie> selectedMovies = new ArrayList<Movie>(selectedRows.length);
        
        for (int i = 0; i < selectedRows.length; i++) {
            selectedMovies.add(this.moviesModel.getMovieAt(this.moviesTable.convertRowIndexToModel(selectedRows[i])));
        }
        
        return Collections.unmodifiableList(selectedMovies);
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
    

    IPrevNextMovieProvider newPrevNextMovieProvider() {
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

}
