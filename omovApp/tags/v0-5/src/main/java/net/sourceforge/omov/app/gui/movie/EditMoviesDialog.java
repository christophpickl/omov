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

import java.awt.BorderLayout;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import net.sourceforge.omov.app.gui.comp.generic.AbstractAddEditDialog;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Quality;
import net.sourceforge.omov.core.bo.Resolution;
import net.sourceforge.omov.core.bo.Movie.MovieField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class EditMoviesDialog extends AbstractAddEditDialog<List<Movie>> {

    private static final long serialVersionUID = 6773818202819376865L;

    private static final Set<MovieField> MULTIPLE_EDIT_FIELDS;
    static {
        final Set<MovieField> tmp = new HashSet<MovieField>();
        tmp.add(MovieField.TITLE);
        tmp.add(MovieField.SEEN);
        tmp.add(MovieField.RATING);
        tmp.add(MovieField.COVER_FILE);
        tmp.add(MovieField.GENRES);
        tmp.add(MovieField.LANGUAGES);
        tmp.add(MovieField.STYLE);
        tmp.add(MovieField.DIRECTOR);
        tmp.add(MovieField.ACTORS);
        tmp.add(MovieField.YEAR);
        tmp.add(MovieField.COMMENT);
        tmp.add(MovieField.QUALITY);
        tmp.add(MovieField.DURATION);
        tmp.add(MovieField.RESOLUTION);
        tmp.add(MovieField.SUBTITLES);
        
        MULTIPLE_EDIT_FIELDS = Collections.unmodifiableSet(tmp);
    }
    
    final Map<MovieField, JCheckBox> fieldCheckBoxes = new HashMap<MovieField, JCheckBox>();
    {
        for (MovieField field : MULTIPLE_EDIT_FIELDS) {
            final JCheckBox checkBox = new JCheckBox();
            checkBox.putClientProperty("JComponent.sizeVariant", "mini");
            checkBox.setBackground(Constants.getColorWindowBackground());
            checkBox.setToolTipText("Change "+field.label()+" for all Movies");
            this.fieldCheckBoxes.put(field, checkBox);
        }
    }
    private final MovieTabInfo tabInfo;
    private final MovieTabDetails tabDetails;
    private final MovieTabNotes tabNotes;

    public EditMoviesDialog(JFrame owner, List<Movie> editMovies) {
        super(owner, editMovies);
        
        this.tabInfo = new MovieTabInfo(this, editMovies);
        this.tabDetails = new MovieTabDetails(this, editMovies);
        this.tabNotes = new MovieTabNotes(this, editMovies);
        
        this.setModal(true);
        this.setTitle("Edit Movies");
        
        this.getContentPane().add(this.initComponents());
        this.preselectCheckboxes();
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private void preselectCheckboxes() {
        for (MovieField field : MULTIPLE_EDIT_FIELDS) {
            
            boolean allMoviesEqualField = true;
            Movie prevMovie = null;
            for (Movie curMovie : this.getEditItem()) {
                if(prevMovie != null) {
                    if(curMovie.getValueByField(field).equals(prevMovie.getValueByField(field)) == false) {
                        allMoviesEqualField = false;
                        break;
                    }
                }
                
                prevMovie = curMovie;
            }
            
            if(allMoviesEqualField == true) {
                this.fieldCheckBoxes.get(field).setSelected(true);
                if(field == MovieField.TITLE) {
                    this.tabInfo.setMovieTitle(prevMovie.getTitle());
                } else if(field == MovieField.SEEN) {
                    this.tabInfo.setMovieSeen(prevMovie.isSeen());
                } else if(field == MovieField.RATING) {
                    this.tabInfo.setMovieRating(prevMovie.getRating());
                } else if(field == MovieField.COVER_FILE) {
                    // either they are all different, or are all empty (in this case, doing nothing is just fine)
                } else if(field == MovieField.GENRES) {
                    this.tabInfo.setMovieGenres(prevMovie.getGenres());
                } else if(field == MovieField.LANGUAGES) {
                    this.tabDetails.setMovieLanguages(prevMovie.getLanguages());
                } else if(field == MovieField.STYLE) {
                    this.tabInfo.setMovieStyle(prevMovie.getStyle());
                } else if(field == MovieField.DIRECTOR) {
                    this.tabDetails.setMovieDirector(prevMovie.getDirector());
                } else if(field == MovieField.ACTORS) {
                    this.tabDetails.setMovieActors(prevMovie.getActors());
                } else if(field == MovieField.YEAR) {
                    this.tabInfo.setMovieYear(prevMovie.getYear());
                } else if(field == MovieField.COMMENT) {
                    this.tabNotes.setMovieComment(prevMovie.getComment());
                } else if(field == MovieField.QUALITY) {
                    this.tabInfo.setMovieQuality(prevMovie.getQuality());
                } else if(field == MovieField.DURATION) {
                    this.tabInfo.setMovieDuration(prevMovie.getDuration());
                } else if(field == MovieField.RESOLUTION) {
                    this.tabInfo.setMovieResolution(prevMovie.getResolution());
                } else if(field == MovieField.SUBTITLES) {
                    this.tabDetails.setMovieSubtitles(prevMovie.getSubtitles());
                } else {
                    throw new FatalException("Unhandled movie field '"+field+"'!");
                }
            }
        }
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        final JTabbedPane tabbedPane = new JTabbedPane();

        panel.setBackground(Constants.getColorWindowBackground());
        tabbedPane.setBackground(Constants.getColorWindowBackground());
        
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
        panel.setBackground(Constants.getColorWindowBackground());
        
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
        final List<String> files = this.tabDetails.getFiles();
        
        final int duration = this.tabInfo.getDuration().getTotalInMinutes();
        final Resolution resolution = this.tabInfo.getResolution();
        final Set<String> subtitles = this.tabDetails.getSubtitles();
        
        final Movie movie = Movie.newMovie(id, title, seen, rating, coverFile, genres, languages, style, director, actors, year, comment, quality, dateAdded, fileSizeKb, folderPath, format, files, duration, resolution, subtitles);
        final List<Movie> movies = new ArrayList<Movie>(1);
        movies.add(movie);
        return Collections.unmodifiableList(movies);
    }

    public boolean isFieldSelected(MovieField field) {
        assert(MULTIPLE_EDIT_FIELDS.contains(field) == true);
        
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
