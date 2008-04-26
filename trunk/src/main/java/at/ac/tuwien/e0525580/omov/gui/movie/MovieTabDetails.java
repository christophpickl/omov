package at.ac.tuwien.e0525580.omov.gui.movie;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.FatalException;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.MovieFolderInfo;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;
import at.ac.tuwien.e0525580.omov.gui.comp.ButtonMovieFolder;
import at.ac.tuwien.e0525580.omov.gui.comp.MovieFilesReordering;
import at.ac.tuwien.e0525580.omov.gui.comp.ButtonMovieFolder.IButtonFolderListener;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.ContextMenuButton;
import at.ac.tuwien.e0525580.omov.gui.comp.generic.MultiColTextField;
import at.ac.tuwien.e0525580.omov.gui.comp.suggest.MovieDirectorSuggester;
import at.ac.tuwien.e0525580.omov.gui.comp.suggester.MovieActorsList;
import at.ac.tuwien.e0525580.omov.gui.comp.suggester.MovieLanguagesList;
import at.ac.tuwien.e0525580.omov.gui.comp.suggester.MovieSubtitlesList;
import at.ac.tuwien.e0525580.omov.tools.scan.ScannedMovie;
import at.ac.tuwien.e0525580.omov.tools.scan.Scanner;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.GuiUtil;

public class MovieTabDetails extends AbstractMovieTab implements IButtonFolderListener, ActionListener {

    private static final Log LOG = LogFactory.getLog(MovieTabDetails.class);
    private static final long serialVersionUID = 1757068592935794813L;

    private static final String CMD_FILES_RESCAN = "CMD_FILES_RESCAN";
    private static final String CMD_FILES_REORDER = "CMD_FILES_REORDER";
    private static final String CMD_FILES_CLEAR = "CMD_FILES_CLEAR";

    private static final int ACTORS_FIXED_CELL_WIDTH = 67;

    private final MovieLanguagesList inpLanguages;
    private final MovieSubtitlesList inpSubtitles;

    private final MovieDirectorSuggester inpDirector = new MovieDirectorSuggester(17); // FIXME GUI - size should not be specified by columns, but layout should decide actual size of this component
    private final MovieActorsList inpActors;

    private List<String> files = new LinkedList<String>();
    private long fileSizeKb = 0L;
    private final ButtonMovieFolder btnMovieFolder = new ButtonMovieFolder(this.owner);
    private final MultiColTextField lblPath = new MultiColTextField(" ", 32);
    private final MultiColTextField lblFiles = new MultiColTextField("", 22);
    private final JLabel lblSize = new JLabel("");
    private final JLabel lblFormat = new JLabel("");

    // popmenu items for folder stuff
    private ContextMenuButton folderContextMenu;
    private final JMenuItem itemRescan = new JMenuItem("Rescan");
    private final JMenuItem itemReorder = new JMenuItem("Reorder Files");
    private final JMenuItem itemClear = new JMenuItem("Clear Files");
    
    private String folderPathOriginally = "";
    
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

            if(editMovie.isFolderPathSet()) {
            	this.lblPath.setText(editMovie.getFolderPath());
            } else {
            	this.lblPath.setText(" ");
            }
            this.lblFiles.setText(editMovie.getFilesFormatted());
            this.lblSize.setText(editMovie.getFileSizeFormatted());
            this.lblFormat.setText(editMovie.getFormat());
            
            this.folderPathOriginally = editMovie.getFolderPath();
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
        // MINOR languages widget is somehow placed below its righthand neighbour director
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        JPanel cmp = this.newInputComponent(this.inpLanguages, MovieField.LANGUAGES);
//        cmp.setOpaque(true);
//        cmp.setBackground(Color.GREEN);
        panel.add(cmp, c);

        c.gridy = 1;
        c.insets = new Insets(10, 0, 0, 0); // top left bottom right
        panel.add(this.newInputComponent(this.inpSubtitles, MovieField.SUBTITLES), c);

//        panel.setOpaque(true);
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
    	/*
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);

        c.anchor = GridBagConstraints.LINE_START;
        c.insets = new Insets(0, 0, 0, 10); // top left bottom right
        c.gridx = 0;
        c.gridy = 0;
        c.fill = GridBagConstraints.VERTICAL;
//        panel.add(this.panelFolderLeft(), c);
        panel.add(this.btnMovieFolder, c);
//        panel.add(new JLabel("xyz"), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridx = 1;
//        c.fill = GridBagConstraints.NONE;
        panel.add(this.panelFolderRight(), c);

        panel.setOpaque(true);
		panel.setBackground(Color.GREEN);

        return panel;
        */

    	
    	final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    	panel.setOpaque(false);
    	panel.add(this.panelFolderLeft());
    	panel.add(this.panelFolderRight());
    	return panel;
    }
    

    private JPanel panelFolderLeft() {
		List<JMenuItem> popupItems = new ArrayList<JMenuItem>();
		itemRescan.setActionCommand(CMD_FILES_RESCAN);
		itemReorder.setActionCommand(CMD_FILES_REORDER);
		itemClear.setActionCommand(CMD_FILES_CLEAR);
		itemRescan.addActionListener(this);
		itemReorder.addActionListener(this);
		itemClear.addActionListener(this);
		popupItems.add(itemRescan);
		popupItems.add(itemReorder);
		popupItems.add(itemClear);
    	this.folderContextMenu = new ContextMenuButton(popupItems);
    	
    	if(this.isAddMode == true || this.editMovies != null || this.editMovie.isFolderPathSet() == false) {
    		this.folderContextMenu.setEnabled(false);
    	}
    	
    	final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);

        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.anchor = GridBagConstraints.LAST_LINE_START;
        c.fill = GridBagConstraints.NONE;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(this.btnMovieFolder, c);

        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.gridy = 1;
        panel.add(this.folderContextMenu, c);

//        panel.setOpaque(true);
//        panel.setBackground(Color.YELLOW);

        return panel;
    }

	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		
		if(cmd.equals(CMD_FILES_CLEAR)) {
			this.doFilesClear();
		} else if(cmd.equals(CMD_FILES_REORDER)) {
			this.doFilesReorder();
		} else if(cmd.equals(CMD_FILES_RESCAN)) {
			this.doFilesRescan();
		} else {
			throw new IllegalArgumentException("unhandled action commad '"+cmd+"'!");
		}
	}
	
	private void doFilesClear() {
		this.folderContextMenu.setEnabled(false);
		
		this.lblPath.setText("");
		this.files = new ArrayList<String>(0);
		this.lblFiles.setText("[]");
		this.lblSize.setText("0.0 KB");
		this.fileSizeKb = 0L;
		this.lblFormat.setText("");
	}
	
	private void doFilesReorder() {
		MovieFilesReordering reordering = new MovieFilesReordering(this.owner, this.files);
		reordering.setVisible(true);
		if(reordering.isConfirmed()) {
			this.files = reordering.getConfirmedList();
			this.lblFiles.setText(Arrays.toString(this.files.toArray()));
		}
	}
	
	private void doFilesRescan() {
		// FIXME implement doFilesRescan
		GuiUtil.warning(this.owner, "Ups", "Not yet implemented!");
	}

    private JPanel panelFolderRight() {
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        panel.setOpaque(false);

        final int gapHorizontal = 6;
        final int gapVertical = 3;

        c.fill = GridBagConstraints.NONE;

        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.insets = new Insets(0, 0, gapVertical, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 0;
        panel.add(lbl("Path"), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, gapHorizontal, gapVertical, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblPath, c);

        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.insets = new Insets(0, 0, gapVertical, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 1;
        panel.add(lbl("Files"), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, gapHorizontal, gapVertical, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblFiles, c);

        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.insets = new Insets(0, 0, gapVertical, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 2;
        panel.add(lbl("Size"), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, gapHorizontal, gapVertical, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblSize, c);

        c.anchor = GridBagConstraints.FIRST_LINE_END;
        c.insets = new Insets(0, 0, 0, 0); // top left bottom right
        c.gridx = 0;
        c.gridy = 3;
        panel.add(lbl("Format"), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, gapHorizontal, 0, 0); // top left bottom right
        c.gridx = 1;
        panel.add(this.lblFormat, c);

//        panel.setOpaque(true);
//        panel.setBackground(Color.CYAN);

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
        
        try {
			List<String> folderPaths = BeanFactory.getInstance().getMovieDao().getMovieFolderPaths();
			
			final String newFolderPath = folderInfo.getFolderPath();
			if(folderPaths.contains(newFolderPath) == true && this.folderPathOriginally.equals(newFolderPath) == false) {
				GuiUtil.warning(this.owner, "Could not set folder path", "The path '"+newFolderPath+"' is already in use!");
				return;
			}
			
		} catch (BusinessException e) {
			LOG.error("Could not check existing folder paths!");
		}
        
        
		this.folderContextMenu.setEnabled(true);
        this.files = folderInfo.getFiles();
        this.fileSizeKb = folderInfo.getFileSizeKB();

        this.lblPath.setText(folderInfo.getFolderPath());
        this.lblFiles.setText(Arrays.toString(folderInfo.getFiles().toArray()));
        this.lblSize.setText(FileUtil.formatFileSize(folderInfo.getFileSizeKB()));
        this.lblFormat.setText(folderInfo.getFormat());
    }

    public void notifyFolderCleared() {
    	this.folderContextMenu.setEnabled(false);
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
        return this.lblPath.getFullText().trim();
    }
    public long getFileSizeKb() {
        return this.fileSizeKb;
    }



}
