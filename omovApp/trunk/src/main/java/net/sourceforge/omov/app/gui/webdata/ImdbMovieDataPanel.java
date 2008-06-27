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

package net.sourceforge.omov.app.gui.webdata;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

import net.sourceforge.omov.app.gui.comp.CoverImagePanel;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.CoverFileType;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.util.ImageUtil;
import net.sourceforge.omov.core.util.OmovGuiUtil;
import net.sourceforge.omov.gui.LabeledComponent;
import at.ac.tuwien.e0525580.jlib.gui.inputfield.MultiColTextField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class ImdbMovieDataPanel extends JPanel {

    private static final long serialVersionUID = -1312777627946425901L;

    private final Movie movie;

    private final MultiColTextField txtTitle = new MultiColTextField(20);

    private final JLabel txtYear = new JLabel(" ");
    private final JLabel txtDuration = new JLabel(" ");
    
    private final MultiColTextField txtDirector = new MultiColTextField(10);
    private final MultiColTextField txtActors = new MultiColTextField(20);
    
    private final CoverImagePanel imagePanel = new CoverImagePanel(); // MINOR draw background color only for this panel different (if no cover is set, looks ugly)
    private final JTextArea txtComment = new JTextArea(4, 20);
    private final MultiColTextField txtGenres = new MultiColTextField(20);
    
    public static final ImdbMovieDataPanel EMPTY_PANEL = new ImdbMovieDataPanel(null); 
    
    public ImdbMovieDataPanel(Movie movie) {
        this.movie = movie;
        this.setOpaque(false);

        this.txtTitle.setForeground(Constants.getColorDarkGray());
        this.txtYear.setForeground(Constants.getColorDarkGray());
        this.txtDuration.setForeground(Constants.getColorDarkGray());
        this.txtDirector.setForeground(Constants.getColorDarkGray());
        this.txtActors.setForeground(Constants.getColorDarkGray());
        this.txtGenres.setForeground(Constants.getColorDarkGray());
        this.txtComment.setForeground(Constants.getColorDarkGray());
        
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(this, c);
        this.setLayout(layout);

        this.txtComment.setEditable(false);
        this.txtComment.setLineWrap(true);
        this.txtComment.setWrapStyleWord(true);
        
        if(movie != null) {
            this.txtTitle.setText(movie.getTitle());
            
            this.txtYear.setText(String.valueOf(movie.getYear()));
            this.txtDuration.setText(movie.getDurationFormatted());

            this.txtDirector.setText(movie.getDirector());
            this.txtActors.setText(movie.getActorsString());
            
            this.txtComment.setText(movie.getComment());
            this.txtGenres.setText(movie.getGenresString());
            
            if(movie.isCoverFileSet()) {
                this.imagePanel.setImage(ImageUtil.getResizedCoverImage(new File(movie.getOriginalCoverFile()), this.imagePanel, CoverFileType.NORMAL));
            }
        }

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 4, 0);

//        c.weightx = 1.0;
        c.gridwidth = 2;
        c.gridx = 0;
        c.gridy = 0;
        this.add(new LabeledComponent(this.txtTitle, "Title"), c);

//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 4, 10);
        this.add(new LabeledComponent(this.txtYear, "Year"), c);
//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 1;
        c.insets = new Insets(0, 0, 4, 0);
        this.add(new LabeledComponent(this.txtDuration, "Duration"), c);

//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 4, 10);
        this.add(new LabeledComponent(this.txtDirector, "Director"), c);
//        c.weightx = 0.5;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        c.insets = new Insets(0, 0, 4, 0);
        this.add(new LabeledComponent(this.txtActors, "Actors"), c);
        
//        c.weightx = 0.3;
        c.gridwidth = 1;
        c.gridheight = 2;
        c.gridx = 0;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 0, 10);
        this.add(new LabeledComponent(this.imagePanel, "Cover"), c);
        
//        c.weightx = 0.7;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.gridx = 1;
        c.gridy = 3;
        c.insets = new Insets(0, 0, 4, 0);
        this.add(new LabeledComponent(this.txtGenres, "Genres"), c);
        c.gridy = 4;
        c.insets = new Insets(0, 0, 0, 0);
        final int commentWidth = (int) this.txtComment.getPreferredSize().getWidth();
        final int commentHeight = 128;
        this.add(new LabeledComponent(OmovGuiUtil.wrapScroll(this.txtComment, commentWidth, commentHeight), "Comment"), c);
        
    }
    
    public Movie getMovie() {
        return this.movie;
    }
    
    @Override
    public String toString() {
        return "ImdbMovieDataPanel=["+(movie == null ? "null" : "movie.title="+movie.getTitle())+"]";
    }
    
}
