package at.ac.tuwien.e0525580.omov.gui.movie;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.MovieFolderInfo;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.ButtonMovieFolder;
import at.ac.tuwien.e0525580.omov.gui.comp.ButtonMovieFolder.IButtonFolderListener;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.MultiColTextField;
import at.ac.tuwien.e0525580.omov.gui.comp.suggest.MovieDirectorSuggester;
import at.ac.tuwien.e0525580.omov.gui.comp.suggester.MovieActorsList;
import at.ac.tuwien.e0525580.omov.gui.comp.suggester.MovieLanguagesList;
import at.ac.tuwien.e0525580.omov.gui.comp.suggester.MovieSubtitlesList;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.tools.scan.Scanner;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class MovieTabDetails extends AbstractMovieTab implements IButtonFolderListener {

    private static final Log LOG = LogFactory.getLog(MovieTabDetails.class);
    private static final long serialVersionUID = 1757068592935794813L;
    
    private static final int ACTORS_FIXED_CELL_WIDTH = 67;

    private final MovieLanguagesList inpLanguages;
    private final MovieSubtitlesList inpSubtitles;
    
    private final MovieDirectorSuggester inpDirector = new MovieDirectorSuggester(20);
    private final MovieActorsList inpActors;

    private List<String> files = new LinkedList<String>();
    private long fileSizeKb = 0L;
    private final ButtonMovieFolder btnMovieFolder = new ButtonMovieFolder(this.owner);
    private final MultiColTextField lblPath = new MultiColTextField("", 22);
    private final MultiColTextField lblFiles = new MultiColTextField("", 22);
    private final JLabel lblSize = new JLabel("");
    private final JLabel lblFormat = new JLabel("");

    /**
     * Constructor for adding/editing single movie.
     */
    public MovieTabDetails(AddEditMovieDialog owner, boolean isAddMode, Movie editMovie) {
        super(owner, isAddMode, editMovie);
//        this.lblPath.setBackground(Color.ORANGE);
        
        try {
            this.inpLanguages = new MovieLanguagesList(this.owner);
            this.inpSubtitles = new MovieSubtitlesList(this.owner);
        } catch(BusinessException e) {
            throw new FatalException("Could not open dialog because fetching movie data from database failed!", e);
        }
        
        this.btnMovieFolder.addButtonFolderListener(this);

        try {
            if(isAddMode == false && (editMovie instanceof ScannedMovie)) {
                LOG.info("edit mode && editMovie instanceof ScannedMovie => going to create prefilled MovieActorsList (actors="+editMovie.getActorsString()+").");
                this.inpActors = new MovieActorsList(this.owner, editMovie.getActors(), ACTORS_FIXED_CELL_WIDTH);
            } else {
                this.inpActors = new MovieActorsList(this.owner, ACTORS_FIXED_CELL_WIDTH);
            }
        } catch(BusinessException e) {
            throw new FatalException("Could not open dialog because fetching movie data from database failed!", e);
        }

        if(isAddMode == false) {
            for (String language : editMovie.getLanguages()) {
                this.inpLanguages.setSelectedItem(language);
            }
            for (String subtitle : editMovie.getSubtitles()) {
                this.inpSubtitles.setSelectedItem(subtitle);
            }
            this.inpDirector.setText(editMovie.getDirector());
            
            for (String actor : editMovie.getActors()) {
                this.inpActors.setSelectedItem(actor);
            }
            
            for (String singleMovieFileName : editMovie.getFiles()) {
                this.files.add(singleMovieFileName);
            }
            this.fileSizeKb = editMovie.getFileSizeKb();
            
            this.lblPath.setText(editMovie.getFolderPath());
            this.lblFiles.setText(editMovie.getFilesFormatted());
            this.lblSize.setText(editMovie.getFileSizeFormatted());
            this.lblFormat.setText(editMovie.getFormat());
        }
        
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(this.initComponents());
    }

    
    /**
     * Constructor for editing multiple movies.
     */
    public MovieTabDetails(EditMoviesDialog owner, List<Movie> editMovies) {
        super(owner, editMovies);
        
        try {
            this.inpLanguages = new MovieLanguagesList(this.owner);
            this.inpSubtitles = new MovieSubtitlesList(this.owner);
            this.inpActors = new MovieActorsList(this.owner, ACTORS_FIXED_CELL_WIDTH);
        } catch (BusinessException e) {
            throw new FatalException("Could not open dialog because fetching movie data from database failed!", e);
        }
        
        this.btnMovieFolder.setEnabled(false);
        this.btnMovieFolder.setToolTipText("Setting Moviefolder is for multiple Movies not possible");
        
        this.setLayout(new FlowLayout(FlowLayout.CENTER));
        this.add(this.initComponents());
    }
    
    
    private JPanel initComponents() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);

        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 6, 7, 10); // top left bottom right
        c.gridx = 0;
        c.gridy = 0;
        c.weightx = 0.1; // press as small as possible
        panel.add(this.panelLanguagesSubtitles(), c);

        c.insets = new Insets(0, 0, 7, 6); // top left bottom right
        c.gridx = 1;
        c.weightx = 0.9;
        panel.add(this.panelDirectorActors(), c);
        
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 1;
        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 6, 0, 6); // top left bottom right
        c.weightx = 1.0;
        panel.add(this.panelFolder(), c);
        
//        panel.setBackground(Color.GRAY);
        
        return panel;
    }

    private JPanel panelLanguagesSubtitles() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);
        
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        panel.add(this.newInputComponent(this.inpLanguages, MovieField.LANGUAGES), c);
        c.gridy = 1;
        c.insets = new Insets(10, 0, 0, 0); // top left bottom right
        panel.add(this.newInputComponent(this.inpSubtitles, MovieField.SUBTITLES), c);
        
//        panel.setBackground(Color.RED);
        return panel;
    }
    
    private JPanel panelDirectorActors() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);
        
        c.fill = GridBagConstraints.HORIZONTAL;
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridy = 0;
        panel.add(this.newInputComponent(this.inpDirector, MovieField.DIRECTOR), c);
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(20, 0, 0, 0); // top left bottom right
        c.gridy = 1;
        panel.add(this.newInputComponent(this.inpActors, MovieField.ACTORS), c);
        
//        panel.setBackground(Color.GREEN);
        return panel;
    }
    
    private JPanel panelFolder() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);
        
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.fill = GridBagConstraints.BOTH;
        
        panel.add(this.panelFolderLeft(), c);
        c.gridx = 1;
        panel.add(this.panelFolderRight(), c);
//        panel.setBackground(Color.CYAN);
        return panel;
    }

    
    private JPanel panelFolderLeft() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.add(this.btnMovieFolder);

        return panel;
    }
    
    private JPanel panelFolderRight() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);
        
        final int gapHorizontal = 6;
        final int gapVertical = 3;
        
        c.fill = GridBagConstraints.BOTH;
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        c.insets = new Insets(0, 0, gapVertical, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 0;
        panel.add(lbl("Path"), c);
        c.insets = new Insets(0, gapHorizontal, gapVertical, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblPath, c);

        c.insets = new Insets(0, 0, gapVertical, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 1;
        panel.add(lbl("Files"), c);
        c.insets = new Insets(0, gapHorizontal, gapVertical, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblFiles, c);

        c.insets = new Insets(0, 0, gapVertical, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 2;
        panel.add(lbl("Size"), c);
        c.insets = new Insets(0, gapHorizontal, gapVertical, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblSize, c);

        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 3;
        panel.add(lbl("Format"), c);
        c.insets = new Insets(0, gapHorizontal, 0, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblFormat, c);
        
//        panel.setBackground(Color.YELLOW);

        return panel;
    }
    
    private static JLabel lbl(String text) {
        return GuiUtil.newLabelBold(text);
    }
    
    
    
    @Override
    String getTabTitle() {
        return "Details";
    }
    
    
    public void notifyFolderSelected(File folder) {
        MovieFolderInfo folderInfo = Scanner.scanMovieFolderInfo(folder);

        this.files = folderInfo.getFiles();
        this.fileSizeKb = folderInfo.getFileSizeKB();
        
        this.lblPath.setText(folderInfo.getFolderPath());
        this.lblFiles.setText(Arrays.toString(folderInfo.getFiles().toArray()));
        this.lblSize.setText(FileUtil.formatFileSize(folderInfo.getFileSizeKB()));
        this.lblFormat.setText(folderInfo.getFormat());
    }

    public void notifyFolderCleared() {
        this.files = new LinkedList<String>();
        this.fileSizeKb = 0L;
        
        this.lblPath.setText("");
        this.lblFiles.setText("");
        this.lblSize.setText("");
        this.lblFormat.setText("");
    }
    


    void setMovieLanguages(Set<String> languages) {
        for (String language : languages) {
            this.inpLanguages.setSelectedItem(language);
        }
    }

    void setMovieDirector(String director) {
        this.inpDirector.setText(director);
    }

    void setMovieActors(Set<String> actors) {
        for (String actor : actors) {
            this.inpActors.setSelectedItem(actor);
        }
    }

    void setMovieSubtitles(Set<String> subtitles) {
        for (String subtitle : subtitles) {
            this.inpSubtitles.setSelectedItem(subtitle);
        }
    }

    
    public Set<String> getActors() {
        return this.inpActors.getSelectedItems();
    }
    public String getDirector() {
        return this.inpDirector.getText();
    }
    public Set<String> getLanguages() {
        return this.inpLanguages.getSelectedItems();
    }
    public Set<String> getSubtitles() {
        return this.inpSubtitles.getSelectedItems();
    }
    public List<String> getFiles() {
        return this.files;
    }
    public String getFormat() {
        return this.lblFormat.getText();
    }
    public String getFolderPath() {
        return this.lblPath.getText();
    }
    public long getFileSizeKb() {
        return this.fileSizeKb;
    }

    
}
