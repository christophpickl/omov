package at.ac.tuwien.e0525580.omov.gui.movie;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.util.Date;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.gui.IPrevNextMovieProvider;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.AbstractAddEditDialog;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class AddEditMovieDialog extends AbstractAddEditDialog<Movie> {
    // FEATURE preselect added/edited movie in MainMovieTable

    private static final Log LOG = LogFactory.getLog(AddEditMovieDialog.class);
    private static final long serialVersionUID = -499631022640948375L;

    private final JTabbedPane tabbedPane = new JTabbedPane();
    
    private MovieTabInfo tabInfo;
    private MovieTabDetails tabDetails;
    private MovieTabNotes tabNotes;
    
    private JButton btnPrev = new JButton("Previous");
    private JButton btnNext = new JButton("Next");

    private int moviePrevNextIndex = -1;
    private int moviePrevNextCount = -1;
    private IPrevNextMovieProvider prevNextProvider = null;
    
    public static AddEditMovieDialog newAddDialog(JFrame owner) {
        return new AddEditMovieDialog(owner, null, null);
    }
    public static AddEditMovieDialog newEditDialog(JFrame owner, Movie editMovie, IPrevNextMovieProvider prevNextProvider) {
        return new AddEditMovieDialog(owner, editMovie, prevNextProvider);
    }

    public static AddEditMovieDialog newEditScanMovieDialog(JFrame owner, ScannedMovie scannedMovie) {
        return new AddEditMovieDialog(owner, scannedMovie, null);
    }

    private AddEditMovieDialog(JFrame owner, Movie editObject, IPrevNextMovieProvider prevNextProvider) {
        super(owner, editObject);
        this.setModal(true);
        
        this.tabbedPane.setBackground(Constants.COLOR_WINDOW_BACKGROUND);
        
        if(prevNextProvider == null) {
            this.btnPrev.setEnabled(false);
            this.btnNext.setEnabled(false);
        } else {
            this.moviePrevNextIndex = prevNextProvider.getInitialIndex();
            this.moviePrevNextCount = prevNextProvider.getCountIndices();
            if(this.moviePrevNextIndex == 0) {
                this.btnPrev.setEnabled(false);
            } else if(this.moviePrevNextIndex == moviePrevNextCount - 1) {
                this.btnNext.setEnabled(false);
            }
            
            this.prevNextProvider = prevNextProvider;
        }
        
        final Movie editItem = (this.isAddMode() ? null : this.editItem()); 
        this.initEditMovie(editItem, 0);
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }

    @Override
    public void dispose() {
        this.tabInfo.unregisterFilledListListeners();
        this.tabDetails.unregisterFilledListListeners();
        super.dispose();
    }
    
    private void initEditMovie(final Movie movie, final int preselectedTabIndex) {
        this.setTitle(isAddMode() ? "Add new Movie" : movie.getTitle());
        
        this.tabInfo = new MovieTabInfo(this, this.isAddMode(), movie);
        this.tabDetails = new MovieTabDetails(this, this.isAddMode(), movie);
        this.tabNotes = new MovieTabNotes(this, this.isAddMode(), movie);
        
        this.initTabbedPane(preselectedTabIndex);
    }
    
    private void initTabbedPane(int preselectedTabIndex) {
        this.tabbedPane.removeAll();
        
        this.tabbedPane.add(" "+this.tabInfo.getTabTitle()+" ", this.tabInfo);
        this.tabbedPane.setMnemonicAt(0, KeyEvent.VK_I);
        this.tabbedPane.add(" "+this.tabDetails.getTabTitle()+" ", this.tabDetails);
        this.tabbedPane.setMnemonicAt(1, KeyEvent.VK_D);
        this.tabbedPane.add(" "+this.tabNotes.getTabTitle()+" ", this.tabNotes);
        this.tabbedPane.setMnemonicAt(2, KeyEvent.VK_N);

        this.tabbedPane.setSelectedIndex(preselectedTabIndex);
        this.tabbedPane.revalidate();
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.COLOR_WINDOW_BACKGROUND);
        
        this.initTabbedPane(0);
        // tabbedPane.setSelectedIndex(1); // SHORTCUT
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(this.newSouthPanel(), BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel newSouthPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        panel.add(this.newSouthWestPanel(), BorderLayout.WEST);
        panel.add(this.newCommandPanel(), BorderLayout.EAST);

        return panel;
    }
    
    private JPanel newSouthWestPanel() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        this.btnPrev.setOpaque(false);
        this.btnNext.setOpaque(false);
        
        this.btnPrev.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doShowPrevMovie();
        }});
        this.btnNext.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            doShowNextMovie();
        }});
        
        panel.add(this.btnPrev);
        panel.add(this.btnNext);

        return panel;
    }
    
    private void doShowNextMovie() {
        LOG.debug("doShowNextMovie()");
        this.moviePrevNextIndex++;
        if(this.moviePrevNextIndex == moviePrevNextCount - 1) {
            this.btnNext.setEnabled(false);
        }
        this.setNewPrevNextMovie();
        if(this.btnPrev.isEnabled() == false) this.btnPrev.setEnabled(true);
    }
    private void doShowPrevMovie() {
        LOG.debug("doShowPrevMovie()");
        this.moviePrevNextIndex--;
        if(this.moviePrevNextIndex == 0) {
            this.btnPrev.setEnabled(false);
        }
        this.setNewPrevNextMovie();
        if(this.btnNext.isEnabled() == false) this.btnNext.setEnabled(true);
    }
    
    private void setNewPrevNextMovie() {
        final Movie newMovie = this.prevNextProvider.getMovieAt(this.moviePrevNextIndex);
        LOG.info("displaying new movie: " + newMovie);
        // FEATURE if prev/next moviebtn hit -> update movie in database automatically
        this.initEditMovie(newMovie, this.tabbedPane.getSelectedIndex());
    }
    
    
    
    public boolean isCoverChanged() {
        return this.tabInfo.isCoverChanged();
    }
    

    /** AbstractAddEditDialog */    
    @Override
    protected Movie _getConfirmedObject() {
        final int id = this.isAddMode() ? -1 : this.editItem().getId();
        
        final String title = this.tabInfo.getTitle();
        final boolean seen = this.tabInfo.getSeen();
        final int rating = this.tabInfo.getRating();
        final String coverFile = this.tabInfo.getCoverFile();
        final Set<String> genres = this.tabInfo.getGenres();
        final Set<String> languages = this.tabDetails.getLanguages();
        final String style = this.tabInfo.getStyle();
        
        final String director = this.tabDetails.getDirector();
        final Set<String> actors = this.tabDetails.getActors();
        final int year = this.tabInfo.getYear();
        final String comment = this.tabNotes.getComment();
        final Quality quality = this.tabInfo.getQuality();
        final Date dateAdded = this.tabNotes.getDateAdded(); // TODO DateAdded can be null if adding new movie; is that a problem?! 
        
        
        final String folderPath = this.tabDetails.getFolderPath();
        final long fileSizeKb = this.tabDetails.getFileSizeKb();
        final String format = this.tabDetails.getFormat();
        final Set<String> files = this.tabDetails.getFiles();
        
        final int duration = this.tabInfo.getDuration().getTotalInMinutes();
        final Resolution resolution = this.tabInfo.getResolution();
        final Set<String> subtitles = this.tabDetails.getSubtitles();
        
        return Movie.newMovie(id, title, seen, rating, coverFile, genres, languages, style, director, actors, year, comment, quality, dateAdded, fileSizeKb, folderPath, format, files, duration, resolution, subtitles);
    }
    
    
    public static void main(String[] args) throws Exception {
        final Movie movie = BeanFactory.getInstance().getMovieDao().getMovie(10);
        final AddEditMovieDialog editDialog = AddEditMovieDialog.newEditDialog(null, movie, new IPrevNextMovieProvider() {
            public int getCountIndices() { return 1; }
            public int getInitialIndex() { return 0; }
            public Movie getMovieAt(int index) { return null;}
        });
        editDialog.setVisible(true);
        System.exit(0);
    }
    
}



