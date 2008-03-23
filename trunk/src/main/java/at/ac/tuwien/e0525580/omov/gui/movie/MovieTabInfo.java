package at.ac.tuwien.e0525580.omov.gui.movie;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.List;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.Configuration;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Quality;
import at.ac.tuwien.e0525580.omov.bo.Resolution;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.CoverSelector;
import at.ac.tuwien.e0525580.omov.gui.comp.DurationPanel;
import at.ac.tuwien.e0525580.omov.gui.comp.QualityField;
import at.ac.tuwien.e0525580.omov.gui.comp.ResolutionPanel;
import at.ac.tuwien.e0525580.omov.gui.comp.YearField;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.IDataList;
import at.ac.tuwien.e0525580.omov.gui.comp.intime.MovieGenresList;
import at.ac.tuwien.e0525580.omov.gui.comp.intime.MovieGenresListFilled;
import at.ac.tuwien.e0525580.omov.gui.comp.rating.RatingField;
import at.ac.tuwien.e0525580.omov.gui.comp.suggest.MovieStyleSuggester;
import at.ac.tuwien.e0525580.omov.gui.comp.suggest.MovieTitleSuggester;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.util.NumberUtil.Duration;

class MovieTabInfo extends AbstractMovieTab {

    private static final Log LOG = LogFactory.getLog(MovieTabInfo.class);
    private static final long serialVersionUID = -4273211406354799248L;
    
    // TODO let inpTitle gain initial focus
    private final JTextField inpTitle = new MovieTitleSuggester(42); // 42 is actually irrelevant because it does not have any effect
    
    private final DurationPanel inpDuration;
    private final ResolutionPanel inpResolution;
    private final JCheckBox inpSeen = new JCheckBox(MovieField.SEEN.label());
    private final CoverSelector inpCoverSelector = new CoverSelector(this);
    private final IDataList inpGenre;
    
    private final RatingField inpRating;
    private final YearField inpYear;
    private final QualityField inpQuality;
    private final JTextField inpStyle = new MovieStyleSuggester(10);

    public MovieTabInfo(AddEditMovieDialog owner, boolean isAddMode, Movie editMovie) {
        super(owner, isAddMode, editMovie);
        
        final int duration = isAddMode ? 0 : editMovie.getDuration(); 
        this.inpDuration = new DurationPanel(Duration.newByTotal(duration));
        
        final Resolution resolution = isAddMode ? Resolution.R0x0 : editMovie.getResolution();
        this.inpResolution = new ResolutionPanel(resolution);
        
        this.inpRating = new RatingField(isAddMode ? 0 : editMovie.getRating(), null, Color.WHITE);
        this.inpQuality = new QualityField((isAddMode ? Quality.UNRATED : editMovie.getQuality()));
        this.inpYear = new YearField(isAddMode ? 0 : editMovie.getYear());
        
//        final int preferredGenreHeight = Constants.COVER_IMAGE_HEIGHT;
        final int fixedCellWidth = 10;
        if(isAddMode == false && (editMovie instanceof ScannedMovie)) {
            LOG.info("edit mode && editMovie instanceof ScannedMovie => going to create prefilled MovieGenresList (genres="+editMovie.getGenresString()+").");
            this.inpGenre = new MovieGenresListFilled(this.owner, editMovie.getGenres(), fixedCellWidth);
        } else {
            this.inpGenre = new MovieGenresList(this.owner, fixedCellWidth);
        }
        
        
        if(isAddMode == false) {
            this.inpTitle.setText(editMovie.getTitle());
            this.inpSeen.setSelected(editMovie.isSeen());
            for (String genre : editMovie.getGenres()) {
                this.inpGenre.setSelectedItem(genre);
            }
            this.inpStyle.setText(editMovie.getStyle());
            
            if(editMovie.isCoverFileSet()) {
                final File coverFile;
                if(editMovie.getCoverFile().startsWith(Configuration.getInstance().getTemporaryFolder().getAbsolutePath())) {
                    LOG.debug("coverFile seems to be in temporary folder (coverFile="+editMovie.getCoverFile()+").");
                    coverFile = new File(editMovie.getCoverFile());
                } else {
                    LOG.debug("coverFile is not in temporary folder, has to be in coverFolder (coverFile="+editMovie.getCoverFile()+").");
                    coverFile = new File(Configuration.getInstance().getCoversFolder(), editMovie.getCoverFile());
                }
                this.inpCoverSelector.setInitialCoverFile(coverFile);
            }
        }

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(this.initComponents());
    }
    
    public MovieTabInfo(EditMoviesDialog owner, List<Movie> editMovies) {
        super(owner, editMovies);
        
        this.inpDuration = new DurationPanel(Duration.newByTotal(0));
        this.inpResolution = new ResolutionPanel(Resolution.R0x0);
        this.inpGenre = new MovieGenresList(this.owner, 10);
        this.inpRating = new RatingField(0, null, Color.WHITE);
        this.inpYear = new YearField(0);
        this.inpQuality = new QualityField(Quality.UNRATED);

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(this.initComponents());
    }
    
    void unregisterFilledListListeners() {
        if(this.inpGenre instanceof MovieGenresListFilled) {
            MovieGenresListFilled filledList = (MovieGenresListFilled) this.inpGenre;
            filledList.unregisterIntimeDatabaseModel();
        }
    }
    
    private JPanel initComponents() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);
        this.inpSeen.setOpaque(false);
        this.inpQuality.setOpaque(false);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 3;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 8, 0); // top left bottom right
        panel.add(this.newInputComponent(this.inpTitle, MovieField.TITLE), c);

        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 8, 6); // top left bottom right
        panel.add(this.newInputComponent(this.inpDuration, MovieField.DURATION), c);
        c.gridx = 1;
        c.anchor = GridBagConstraints.PAGE_START;
        panel.add(this.newInputComponent(this.inpResolution, MovieField.RESOLUTION), c);
        c.gridx = 2;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 0, 8, 0); // top left bottom right
        this.inpSeen.setHorizontalTextPosition(SwingConstants.LEFT);
        panel.add(this.newInputComponent(this.inpSeen, MovieField.SEEN), c); // TODO inpSeen schaut grausig aus

        c.gridx = 0;
        c.gridy = 2;
        c.gridwidth = 1;
        c.gridheight = 4;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 0, 6); // top left bottom right
        panel.add(this.newInputComponent(this.inpCoverSelector, MovieField.COVER_FILE), c);
        c.gridx = 1;
        c.gridy = 2;
        c.gridheight = 4;
        c.anchor = GridBagConstraints.PAGE_START;
        panel.add(this.newInputComponent(this.inpGenre.getPanel(), MovieField.GENRES), c);
        
        c.gridx = 2;
        c.gridy = 2;
        c.gridheight = 1;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
//        panel.add(new LabeledComponent(this.inpRating, "Rating"), c);
        c.insets = new Insets(0, 0, 8, 0); // top left bottom right
        panel.add(this.newInputComponent(this.inpRating, MovieField.RATING), c);
        c.gridx = 2;
        c.gridy = 3;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        panel.add(this.newInputComponent(this.inpYear, MovieField.YEAR), c);
        c.gridx = 2;
        c.gridy = 4;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        panel.add(this.newInputComponent(this.inpQuality, MovieField.QUALITY), c);
        c.gridx = 2;
        c.gridy = 5;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        panel.add(this.newInputComponent(this.inpStyle, MovieField.STYLE), c);
        

        return panel;
    }
    
    @Override
    String getTabTitle() {
        return "Info";
    }
    
    public boolean isCoverChanged() {
        return this.inpCoverSelector.isCoverChanged();
    }

    public String getCoverFile() {
        final String result;
        if(this.inpCoverSelector.isCoverFileSet()) {
            final File originalFile = this.inpCoverSelector.getCoverFile();
            final String originalPath = originalFile.getAbsolutePath();
            final String coversFolder = Configuration.getInstance().getCoversFolder().getAbsolutePath();
            
            if(originalPath.startsWith(coversFolder)) {
                result = originalPath.substring(coversFolder.length() + File.separator.length());
            } else {
                result = originalPath;
            }
        } else {
            result = "";
        }
        LOG.debug("returning coverFile value '"+result+"'");
        return result;
    }
    
    
    public Duration getDuration() {
        return this.inpDuration.getDuration();
    }
    public Set<String> getGenres() {
        return this.inpGenre.getSelectedItems();
    }
    public int getRating() {
        return this.inpRating.getRating();
    }
    public Resolution getResolution() {
        return this.inpResolution.getResolution();
    }
    public boolean getSeen() {
        return this.inpSeen.isSelected();
    }
    public String getStyle() {
        return this.inpStyle.getText();
    }
    public String getTitle() {
        return this.inpTitle.getText();
    }
    public int getYear() {
        return (int) this.inpYear.getNumber();
    }
    public Quality getQuality() {
        return this.inpQuality.getQuality();
    }
}
