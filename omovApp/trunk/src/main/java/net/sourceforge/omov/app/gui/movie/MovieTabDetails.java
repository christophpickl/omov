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

package net.sourceforge.omov.app.gui.movie;

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

import net.sourceforge.omov.app.gui.comp.FolderChooseButton;
import net.sourceforge.omov.app.gui.comp.IFolderChooseListener;
import net.sourceforge.omov.app.gui.comp.MovieFilesReordering;
import net.sourceforge.omov.app.gui.comp.suggester.MovieActorsListSuggester;
import net.sourceforge.omov.app.gui.comp.suggester.MovieDirectorTextSuggester;
import net.sourceforge.omov.app.gui.comp.suggester.MovieLanguagesListSuggester;
import net.sourceforge.omov.app.gui.comp.suggester.MovieSubtitlesListSuggester;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.MovieFolderInfo;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.tools.scan.ScannedMovie;
import net.sourceforge.omov.core.tools.scan.Scanner;
import net.sourceforge.omov.core.util.FileUtil;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.gui.ContextMenuButton;
import net.sourceforge.omov.gui.inputfields.MultiColTextField;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * got two constructors: one for add/edit single, and one for edit multiple movies. 
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieTabDetails extends AbstractMovieTab implements IFolderChooseListener, ActionListener {

    private static final Log LOG = LogFactory.getLog(MovieTabDetails.class);
    private static final long serialVersionUID = 1757068592935794813L;

    private static final String CMD_SELECT_FOLDER = "CMD_SELECT_FOLDER";
    private static final String CMD_FILES_RESCAN = "CMD_FILES_RESCAN";
    private static final String CMD_FILES_REORDER = "CMD_FILES_REORDER";
    private static final String CMD_FILES_CLEAR = "CMD_FILES_CLEAR";

    private static final int ACTORS_FIXED_CELL_WIDTH = 67;

    private final MovieLanguagesListSuggester inpLanguages;
    private final MovieSubtitlesListSuggester inpSubtitles;

    private final MovieDirectorTextSuggester inpDirector = new MovieDirectorTextSuggester(17); // TODO GUI - size should not be specified by columns, but layout should decide actual size of this component
    private final MovieActorsListSuggester inpActors;

    private List<String> files = new LinkedList<String>();
    private long fileSizeKb = 0L;
    private final FolderChooseButton btnMovieFolder = new FolderChooseButton(this.owner);
    private final MultiColTextField lblPath = new MultiColTextField(" ", 32);
    private final MultiColTextField lblFiles = new MultiColTextField("", 32);
    private final JLabel lblSize = new JLabel("");
    private final JLabel lblFormat = new JLabel("");

    // popmenu items for folder stuff
    private ContextMenuButton folderContextMenu;
    private final JMenuItem itemSelectFolder = new JMenuItem("Select Folder");
    private final JMenuItem itemRescan = new JMenuItem("Rescan Folder");
    private final JMenuItem itemReorder = new JMenuItem("Reorder Files");
    private final JMenuItem itemClear = new JMenuItem("Clear");
    
    private String folderPathOriginally = "";
    
    /**
     * Constructor for adding/editing single movie.
     */
    public MovieTabDetails(AddEditMovieDialog owner, boolean isAddMode, Movie editMovie) {
        super(owner, isAddMode, editMovie);
//        this.lblPath.setBackground(Color.ORANGE);

        try {
            this.inpLanguages = new MovieLanguagesListSuggester(this.owner);
            this.inpSubtitles = new MovieSubtitlesListSuggester(this.owner);
        } catch(BusinessException e) {
            throw new FatalException("Could not open dialog because fetching movie data from database failed!", e);
        }

        this.btnMovieFolder.addFolderChooseListener(this);
        this.btnMovieFolder.setInitialPath(PreferencesDao.getInstance().getRecentMovieFolderPath());

        try {
            if(isAddMode == false && (editMovie instanceof ScannedMovie)) {
                LOG.info("edit mode && editMovie instanceof ScannedMovie => going to create prefilled MovieActorsList (actors="+editMovie.getActorsString()+").");
                this.inpActors = new MovieActorsListSuggester(this.owner, editMovie.getActors(), ACTORS_FIXED_CELL_WIDTH);
            } else {
                this.inpActors = new MovieActorsListSuggester(this.owner, ACTORS_FIXED_CELL_WIDTH);
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
            this.inpLanguages = new MovieLanguagesListSuggester(this.owner);
            this.inpSubtitles = new MovieSubtitlesListSuggester(this.owner);
            this.inpActors = new MovieActorsListSuggester(this.owner, ACTORS_FIXED_CELL_WIDTH);
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
        panel.add(this.panelMovieFolder(), c);

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

    private JPanel panelMovieFolder() {
    	final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 10));
    	panel.setOpaque(false);
    	panel.add(this.panelMovieFolderControl());
    	panel.add(this.panelMovieFolderInfo());
    	return panel;
    }
    

    private JPanel panelMovieFolderControl() {
		List<JMenuItem> popupItems = new ArrayList<JMenuItem>();
		
		this.itemSelectFolder.setActionCommand(CMD_SELECT_FOLDER);
		this.itemRescan.setActionCommand(CMD_FILES_RESCAN);
		this.itemReorder.setActionCommand(CMD_FILES_REORDER);
		this.itemClear.setActionCommand(CMD_FILES_CLEAR);
		
		if(this.isAddMode == true || this.editMovie.isFolderPathSet() == false) {
			this.itemRescan.setEnabled(false);
		}
		
		if(this.isAddMode == true || this.editMovie.getFiles().size() <= 1) {
			this.itemReorder.setEnabled(false);
		}
		
		if(this.isAddMode == true || this.editMovie.isFolderPathSet() == false) {
			this.itemClear.setEnabled(false);
		}
		
		popupItems.add(this.itemSelectFolder);
		popupItems.add(this.itemRescan);
		popupItems.add(this.itemReorder);
		popupItems.add(this.itemClear);
		
    	this.folderContextMenu = new ContextMenuButton(popupItems, this);
    	this.folderContextMenu.setOpaque(false);
    	
    	
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

	public void actionPerformed(final ActionEvent e) {
		new GuiAction() {
			@Override
			protected void _action() {
				final String cmd = e.getActionCommand();
				
				if(cmd.equals(CMD_FILES_CLEAR)) {
					doFilesClear();
				} else if(cmd.equals(CMD_FILES_REORDER)) {
					doFilesReorder();
				} else if(cmd.equals(CMD_FILES_RESCAN)) {
					doFilesRescan();
				} else if(cmd.equals(CMD_SELECT_FOLDER)) {
					btnMovieFolder.doClicked(); // invoke button programatically
				} else {
					throw new IllegalArgumentException("unhandled action commad '"+cmd+"'!");
				}
			}
		}.doAction();
	}
	
	/**
	 * Clears folderpath/files/size/format attribute (manipulating visible gui components only).
	 */
	private void doFilesClear() {
		LOG.debug("doFilesClear()");
		this.itemRescan.setEnabled(false);
		this.itemReorder.setEnabled(false);
		this.itemClear.setEnabled(false);

		this.fileSizeKb = 0L;
		this.files = new ArrayList<String>(0);
		
		this.lblPath.setText("");
		this.lblFiles.setText("[]");
		this.lblSize.setText("0.0 KB");
		this.lblFormat.setText("");
	}
	
	private void doFilesReorder() {
		LOG.debug("doFilesReorder()");
		MovieFilesReordering reordering = new MovieFilesReordering(this.owner, this.files);
		reordering.setVisible(true);
		if(reordering.isConfirmed()) {
			this.files = reordering.getConfirmedList();
			this.lblFiles.setText(Arrays.toString(this.files.toArray()));
		}
	}
	
	private void doFilesRescan() {
		LOG.debug("doFilesRescan()");
		
		final String path = this.lblPath.getFullText();
		// do NOT use this.editMovie, because it could be out-of-date
		// -> lblPath stores the most recent data (renewed by rescan action)
		
		assert(path.length() > 0);
		this.doScanFolder(new File(path));
	}

    private JPanel panelMovieFolderInfo() {
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

    
    private void doScanFolder(File folder) {
        MovieFolderInfo folderInfo = Scanner.scanMovieFolderInfo(folder);
        
        try {
			List<String> folderPaths = BeanFactory.getInstance().getMovieDao().getMovieFolderPaths();
			
			final String newFolderPath = folderInfo.getFolderPath();
			if(folderPaths.contains(newFolderPath) == true && this.folderPathOriginally.equals(newFolderPath) == false) {
				GuiUtil.warning(this.owner, "Scan Folder Error", "The path '"+newFolderPath+"' is already in use!"); // MINOR display which movie has occupied this folderpath
				return;
			}
		} catch (BusinessException e) {
			LOG.error("Could not check existing folder paths!");
			GuiUtil.error(this.owner, "Scan Folder Error", "Could not scan folder due to an interal error!");
			return;
		}
        
		this.folderContextMenu.setEnabled(true);
        this.files = folderInfo.getFiles();
        this.fileSizeKb = folderInfo.getFileSizeKB();

        this.lblPath.setText(folderInfo.getFolderPath());
        this.lblFiles.setText(Arrays.toString(folderInfo.getFiles().toArray()));
        this.lblSize.setText(FileUtil.formatFileSize(folderInfo.getFileSizeKB()));
        this.lblFormat.setText(folderInfo.getFormat());
        
        this.itemRescan.setEnabled(true);
        this.itemClear.setEnabled(true);
        
        if(this.files.size() == 0) {
        	this.itemReorder.setEnabled(false);
        } else if(this.files.size() == 1) {
        	this.itemReorder.setEnabled(false);
        } else {
        	assert(this.files.size() > 1);
        	this.itemReorder.setEnabled(true);
        	
        }
    }

    public void notifyFolderCleared() {
        this.doFilesClear();
    }

    public void notifyFolderSelected(File folder) {
    	// update recently stored folder
    	PreferencesDao.getInstance().setRecentMovieFolderPath(folder.getParent());
    	
    	this.doScanFolder(folder);
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
