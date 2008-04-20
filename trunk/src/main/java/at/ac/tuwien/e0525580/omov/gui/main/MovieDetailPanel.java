package at.ac.tuwien.e0525580.omov.gui.main;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.bo.CoverFileType;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.CoverImagePanel;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.MultiColTextField;
import at.ac.tuwien.e0525580.omov.gui.comp.rating.RatingSlider;
import at.ac.tuwien.e0525580.omov.model.IMovieDao;
import at.ac.tuwien.e0525580.omov.model.IMovieDaoListener;
import at.ac.tuwien.e0525580.omov.util.ImageUtil;

public class MovieDetailPanel implements IMovieDaoListener {

    private static final Log LOG = LogFactory.getLog(MovieDetailPanel.class);
    private static final int TEXT_FIELD_COLUMNS = 12;
    private static final Font LBL_FONT = new Font(null, Font.BOLD, 12);
    
    private final JPanel panel = new JPanel();
    
    private Movie movie;
    
    private final CoverImagePanel imagePanel = new CoverImagePanel();
    
    private final MultiColTextField txtTitle = new MultiColTextField(TEXT_FIELD_COLUMNS);
    private final RatingSlider txtRating = new RatingSlider(0, Color.BLACK, Color.GRAY);
    private final MultiColTextField txtDuration = new MultiColTextField(TEXT_FIELD_COLUMNS);
    private final MultiColTextField txtGenres = new MultiColTextField(TEXT_FIELD_COLUMNS);

    private final MultiColTextField txtStyle = new MultiColTextField(TEXT_FIELD_COLUMNS);
    private final MultiColTextField txtQuality = new MultiColTextField(TEXT_FIELD_COLUMNS);
    private final MultiColTextField txtDateAdded = new MultiColTextField(TEXT_FIELD_COLUMNS);
    private final MultiColTextField txtLanguages = new MultiColTextField(TEXT_FIELD_COLUMNS);
    
    
    public MovieDetailPanel() {
        this.initComponents();
        this.setMovie(null);
        this.txtRating.setEnabled(false);
        
        BeanFactory.getInstance().getMovieDao().addMovieDaoListener(this);
    }
    
    public void setMovie(Movie movie) {
        LOG.debug("Setting movie to: " + movie);
        this.movie = movie;

        if(movie == null) {
            this.txtTitle.setText("");
            this.txtRating.setValue(0);
            this.txtDuration.setText("");
            this.txtGenres.setText("");

            this.txtStyle.setText("");
            this.txtQuality.setText("");
            this.txtDateAdded.setText("");
            this.txtLanguages.setText("");
            
            this.imagePanel.setImage(null);
            
        } else {
            
            this.txtTitle.setText(movie.getTitle());
            this.txtRating.setValue(movie.getRating());
            this.txtDuration.setText(movie.getDurationFormatted());
            this.txtGenres.setText(movie.getGenresString());

            this.txtStyle.setText(movie.getStyle());
            this.txtQuality.setText(movie.getQualityString());
            this.txtDateAdded.setText(movie.getDateAddedFormattedShort());
            this.txtLanguages.setText(movie.getLanguagesString());
            
            final String coverFile = movie.getOriginalCoverFile();
            if(coverFile.length() > 0) {
                final File cover = new File(PreferencesDao.getInstance().getCoversFolder(), coverFile);
                LOG.debug("Setting image to '"+cover.getAbsolutePath()+"'.");
                if(cover.exists() == false) {
                    LOG.warn("The cover file does not exist '"+cover.getAbsolutePath()+"'!");
                }
                this.imagePanel.setImage(ImageUtil.getResizedCoverImage(cover, this.imagePanel, CoverFileType.NORMAL));
//                this.imagePanel.setImage(CoverUtil.getMovieCoverImage(movie, CoverFileType.NORMAL).getImage());
            } else {
                this.imagePanel.setImage(null);
            }
        }
    }
    
    private void initComponents() {
        final JPanel panelData = new JPanel();
        panelData.setOpaque(false);
//        panelData.setBackground(Color.GREEN);
        
        panelData.add(this.initGridData(
                new GridDataPair(MovieField.TITLE.label(), this.txtTitle),
                new GridDataPair(MovieField.RATING.label(), this.txtRating),
                new GridDataPair(MovieField.DURATION.label(), this.txtDuration),
                new GridDataPair(MovieField.GENRES.label(), this.txtGenres)
            ));
        panelData.add(this.initGridData(
                new GridDataPair(MovieField.STYLE.label(), this.txtStyle),
                new GridDataPair(MovieField.QUALITY.label(), this.txtQuality),
                new GridDataPair(MovieField.DATE_ADDED.label(), this.txtDateAdded),
                new GridDataPair(MovieField.LANGUAGES.label(), this.txtLanguages)
            ));
        

        // this.imagePanel.setBorder(BorderFactory.createLineBorder(Color.BLACK, 2)); // looks ugly
        // this.panel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createEtchedBorder(), "Details", TitledBorder.LEADING, TitledBorder.TOP, new Font(null, Font.BOLD, 12)));
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(this.panel, c);
        this.panel.setLayout(layout);
        this.panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        this.panel.setOpaque(false);

        final JPanel imagePanelWrapper = new JPanel();
        imagePanelWrapper.add(this.imagePanel);
        imagePanelWrapper.setOpaque(false);
        imagePanelWrapper.setPreferredSize(CoverFileType.NORMAL.getDimension());
        imagePanelWrapper.setMaximumSize(CoverFileType.NORMAL.getDimension());
        imagePanelWrapper.setMinimumSize(CoverFileType.NORMAL.getDimension());
        
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        this.panel.add(imagePanelWrapper, c);

        c.gridx = 1;
        this.panel.add(panelData, c);
    }
    
    private JPanel initGridData(GridDataPair... gridData) {
        if(gridData.length == 0) throw new IllegalArgumentException("gridData empty");
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);

        c.ipadx = 10;
        c.insets = new Insets(0, 0, 10, 4); // top, left, bottom, right
        c.gridy = 0;
        c.gridx = 0;
        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.fill = GridBagConstraints.HORIZONTAL;

        for (GridDataPair pair : gridData) {
            addGridDataPair(pair.label, pair.component, panel, c);
        }

        // panel.setBackground(Color.RED);

        final Dimension dimensionPanel = new Dimension(420, CoverFileType.NORMAL.getMaxHeight());
        panel.setMaximumSize(dimensionPanel);
        panel.setPreferredSize(dimensionPanel);
        panel.setMinimumSize(dimensionPanel);

        return panel;
    }
    
    private static JLabel newGridLabel(String txt) {
        final JLabel lbl = new JLabel(txt, JLabel.RIGHT);
        
        lbl.setVerticalAlignment(JLabel.TOP);
        lbl.setOpaque(false);
        lbl.setFont(LBL_FONT);
        
        return lbl;
    }
    
    private static void addGridDataPair(String label, Component component, JPanel panel, GridBagConstraints c) {
        addGridDataComponent(MovieDetailPanel.newGridLabel(label), panel, c);
        addGridDataComponent(component, panel, c);
    }
    
    private static void addGridDataComponent(Component component, JPanel panel, GridBagConstraints c) {
        panel.add(component, c);

        final Dimension dimensionPanel = new Dimension(100, 30);
        component.setMaximumSize(dimensionPanel);
        component.setPreferredSize(dimensionPanel);
        component.setMinimumSize(dimensionPanel);
        
        final boolean isColumnLabel = (c.anchor == GridBagConstraints.FIRST_LINE_END);  
        
        c.gridx = c.gridx == 0 ? 1 : 0;
        if(c.gridx == 0) c.gridy++;
        c.anchor = isColumnLabel ? GridBagConstraints.FIRST_LINE_END : GridBagConstraints.FIRST_LINE_START;
    }
    
    public JPanel getPanel() {
        return this.panel;
    }

    public void movieDataChanged() {
        if(this.movie == null) {
            // nothing to do
            return;
        }
        
        try {
            final IMovieDao dao = BeanFactory.getInstance().getMovieDao();
            Movie newMovie = dao.getMovie(this.movie.getId());
            this.setMovie(newMovie);
        } catch (BusinessException e) {
            throw new FatalException("Could not reload movie data!", e);
        }
        
    }
    
    
    private static class GridDataPair {
        final String label;
        final Component component;
        public GridDataPair(String label, Component component) {
            this.label = label;
            this.component = component;
        }
        
    }
    
}
