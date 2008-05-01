package net.sourceforge.omov.core.gui.movie;

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

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.gui.comp.CoverSelector;
import net.sourceforge.omov.core.gui.comp.DurationPanel;
import net.sourceforge.omov.core.gui.comp.QualityField;
import net.sourceforge.omov.core.gui.comp.ResolutionPanel;
import net.sourceforge.omov.core.gui.comp.YearField;
import net.sourceforge.omov.core.gui.comp.generic.IDataList;
import net.sourceforge.omov.core.gui.comp.rating.RatingSlider;
import net.sourceforge.omov.core.gui.comp.suggest.MovieStyleSuggester;
import net.sourceforge.omov.core.gui.comp.suggest.MovieTitleSuggester;
import net.sourceforge.omov.core.gui.comp.suggester.MovieGenresList;
import net.sourceforge.omov.core.tools.scan.ScannedMovie;
import net.sourceforge.omov.core.util.NumberUtil.Duration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class MovieTabInfo extends AbstractMovieTab {

    private static final Log LOG = LogFactory.getLog(MovieTabInfo.class);
    private static final long serialVersionUID = -4273211406354799248L;
    
    public final JTextField inpTitle = new MovieTitleSuggester(33); // TODO GUI - tab info ist wiedermal zu gross
    
    private final DurationPanel inpDuration;
    private final ResolutionPanel inpResolution;
    private final JCheckBox inpSeen = new JCheckBox("Yet Seen");
    private final CoverSelector inpCoverSelector = new CoverSelector(this);
    private final IDataList inpGenre;
    
    private final RatingSlider inpRating;
    private final YearField inpYear;
    private final QualityField inpQuality;
    private final JTextField inpStyle = new MovieStyleSuggester(10);

    void requestInitialFocus() {
        this.inpTitle.requestFocusInWindow();
    }
    
    public MovieTabInfo(AddEditMovieDialog owner, boolean isAddMode, Movie editMovie) {
        super(owner, isAddMode, editMovie);
        
        final int duration = isAddMode ? 0 : editMovie.getDuration(); 
        this.inpDuration = new DurationPanel(Duration.newByTotal(duration));
        
        final Resolution resolution = isAddMode ? Resolution.R0x0 : editMovie.getResolution();
        this.inpResolution = new ResolutionPanel(resolution);
        
        this.inpRating = new RatingSlider(isAddMode ? 0 : editMovie.getRating(), null, Color.WHITE);
        this.inpQuality = new QualityField((isAddMode ? Quality.UNRATED : editMovie.getQuality()));
        this.inpYear = new YearField(isAddMode ? 0 : editMovie.getYear());
        
//        final int preferredGenreHeight = Constants.COVER_IMAGE_HEIGHT;
        final int fixedCellWidth = 10;
        try {
            if(isAddMode == false && (editMovie instanceof ScannedMovie)) {
                LOG.info("edit mode && editMovie instanceof ScannedMovie => going to create prefilled MovieGenresList (genres="+editMovie.getGenresString()+").");
                this.inpGenre = new MovieGenresList(this.owner, editMovie.getGenres(), fixedCellWidth);
            } else {
                this.inpGenre = new MovieGenresList(this.owner, fixedCellWidth);
            }
        } catch(BusinessException e) {
            throw new FatalException("Could not open dialog because fetching movie data from database failed!", e);
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
                if(editMovie.getOriginalCoverFile().startsWith(PreferencesDao.getInstance().getTemporaryFolder().getAbsolutePath())) {
                    LOG.debug("coverFile seems to be in temporary folder (coverFile="+editMovie.getOriginalCoverFile()+").");
                    coverFile = new File(editMovie.getOriginalCoverFile());
                } else {
                    LOG.debug("coverFile is not in temporary folder, has to be in coverFolder (coverFile="+editMovie.getOriginalCoverFile()+").");
                    coverFile = new File(PreferencesDao.getInstance().getCoversFolder(), editMovie.getOriginalCoverFile());
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
        try {
            this.inpGenre = new MovieGenresList(this.owner, 10);
        } catch (BusinessException e) {
            throw new FatalException("Could not open dialog because fetching movie data from database failed!", e);
        }
        this.inpRating = new RatingSlider(0, null, Color.WHITE);
        this.inpYear = new YearField(0);
        this.inpQuality = new QualityField(Quality.UNRATED);

        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(this.initComponents());
    }
    
    private JPanel initComponents() {
        this.inpDuration.setFocusSelection(true);
        this.inpResolution.setFocusSelection(true);
        this.inpYear.setFocusSelection(true);
        
        this.inpGenre.setVisibleRowCount(8);
        
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
        c.insets = new Insets(0, 0, 4, 0); // top left bottom right
        this.inpSeen.setHorizontalTextPosition(SwingConstants.LEFT);
        panel.add(this.newInputComponent(this.inpSeen, MovieField.SEEN), c); // MINOR gui: inpSeen looks ugly

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
            final String coversFolder = PreferencesDao.getInstance().getCoversFolder().getAbsolutePath();
            
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

    void setMovieTitle(String title) {
        this.inpTitle.setText(title);
    }

    void setMovieSeen(boolean seen) {
        this.inpSeen.setSelected(seen);
    }

    void setMovieRating(int rating) {
        this.inpRating.setValue(rating);
    }

    void setMovieGenres(Set<String> genres) {
        for (String genre : genres) {
            this.inpGenre.setSelectedItem(genre);
        }
    }

    void setMovieStyle(String style) {
        this.inpStyle.setText(style);
    }

    void setMovieYear(int year) {
        this.inpYear.setNumber(year);
    }

    void setMovieQuality(Quality quality) {
        this.inpQuality.setQuality(quality);
    }

    void setMovieDuration(int durationInMin) {
        this.inpDuration.setDuration(Duration.newByTotal(durationInMin));
    }

    void setMovieResolution(Resolution resolution) {
        this.inpResolution.setResolution(resolution);
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
