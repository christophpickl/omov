package at.ac.tuwien.e0525580.omov.gui.movie;

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.AbstractAddEditDialog;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class EditMoviesDialog extends AbstractAddEditDialog<List<Movie>> {

    private static final long serialVersionUID = 6773818202819376865L;

    final Map<MovieField, JCheckBox> fieldCheckBoxes = new HashMap<MovieField, JCheckBox>();
    {
        for (MovieField field : Movie.getAllFields()) {
            final JCheckBox checkBox = new JCheckBox();
            checkBox.setToolTipText("Change "+field.label()+" for all Movies");
            fieldCheckBoxes.put(field, checkBox);
        }
    }
    private final MovieTabInfo tabInfo;
    private final MovieTabDetails tabDetails;
    private final MovieTabNotes tabNotes;

    public EditMoviesDialog(JFrame owner, List<Movie> editMovies) {
        super(owner, editMovies);
        
        // FEATURE if all editMovies have one attribute in common, set its value and preselect the checkbox
        this.tabInfo = new MovieTabInfo(this, editMovies);
        this.tabDetails = new MovieTabDetails(this, editMovies);
        this.tabNotes = new MovieTabNotes(this, editMovies);
        
        this.setModal(true);
        this.setTitle("Edit Movies");
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    

    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        final JTabbedPane tabbedPane = new JTabbedPane();
        
        tabbedPane.add(" "+this.tabInfo.getTabTitle()+" ", this.tabInfo);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_I);
        tabbedPane.add(" "+this.tabDetails.getTabTitle()+" ", this.tabDetails);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_D);
        tabbedPane.add(" "+this.tabNotes.getTabTitle()+" ", this.tabNotes);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_N);
        
        // tabbedPane.setSelectedIndex(1); // SHORTCUT
        
        panel.add(tabbedPane, BorderLayout.CENTER);
        panel.add(this.newSouthPanel(), BorderLayout.SOUTH);
        
        return panel;
    }

    private JPanel newSouthPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.add(this.newCommandPanel(), BorderLayout.EAST);
        return panel;
    }
    
    
    
    // actual confirmed object is in position one
    @Override
    protected List<Movie> _getConfirmedObject() {
        final int id = -1;
        
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
        final Date dateAdded = this.tabNotes.getDateAdded(); // DateAdded is null if editing multiple movies -> will be anyway set for multiple movies 
        
        
        final String folderPath = this.tabDetails.getFolderPath();
        final long fileSizeKb = this.tabDetails.getFileSizeKb();
        final String format = this.tabDetails.getFormat();
        final Set<String> files = this.tabDetails.getFiles();
        
        final int duration = this.tabInfo.getDuration().getTotalInMinutes();
        final Resolution resolution = this.tabInfo.getResolution();
        final Set<String> subtitles = this.tabDetails.getSubtitles();
        
        final Movie movie = Movie.newMovie(id, title, seen, rating, coverFile, genres, languages, style, director, actors, year, comment, quality, dateAdded, fileSizeKb, folderPath, format, files, duration, resolution, subtitles);
        final List<Movie> movies = new ArrayList<Movie>(1);
        movies.add(movie);
        return Collections.unmodifiableList(movies);
    }

    public boolean isFieldSelected(MovieField field) {
        return this.fieldCheckBoxes.get(field).isSelected();
    }
    
    public boolean isCoverChanged() {
        return this.tabInfo.isCoverChanged();
    }

    public static void main(String[] args) throws Exception {
        final Movie movie1 = BeanFactory.getInstance().getMovieDao().getMovie(2);
        final Movie movie2 = BeanFactory.getInstance().getMovieDao().getMovie(3);
        final List<Movie> movies = new ArrayList<Movie>();
        movies.add(movie1);
        movies.add(movie2);
        final EditMoviesDialog editDialog = new EditMoviesDialog(null, movies);
        editDialog.setVisible(true);
        System.exit(0);
    }
}
