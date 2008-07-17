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
import java.awt.FlowLayout;
import java.util.Date;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.gui.OmovGuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieTabNotes extends AbstractMovieTab {

//    private static final Log LOG = LogFactory.getLog(MovieTabNotes.class);
    private static final long serialVersionUID = -310795245521329242L;

    private final JTextArea inpComment = new JTextArea();
    private final JLabel lblDateAdded = new JLabel();


    public MovieTabNotes(AddEditMovieDialog owner, boolean isAddMode, Movie editMovie) {
        super(owner, isAddMode, editMovie);

//        this.inpComment.setRows(13);
//        this.inpComment.setColumns(31);
        this.inpComment.setLineWrap(true);
        this.inpComment.setWrapStyleWord(true);

        if(isAddMode == true) {
            // nothing to do
        } else {
            final String dateString;
            if(editMovie.getDateAdded().getTime() == 0) {
                dateString = "-";
            } else {
                dateString = editMovie.getDateAddedFormattedLong();
            }
            this.lblDateAdded.setText(dateString);
            this.inpComment.setText(editMovie.getComment());
        }

        this.initComponents();
    }

    public MovieTabNotes(EditMoviesDialog owner, List<Movie> editMovies) {
        super(owner, editMovies);

        this.lblDateAdded.setText("-");

        this.initComponents();
    }

    private void initComponents() {
        this.setOpaque(false);
        this.setLayout(new BorderLayout());
        this.setBorder(BorderFactory.createEmptyBorder(5, 11, 0, 11)); // top left bottom right

        this.add(this.panelNotes(), BorderLayout.CENTER);
        this.add(this.panelDateAdded(), BorderLayout.SOUTH);
    }

    private JPanel panelNotes() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        final JScrollPane scrollComment = new JScrollPane(this.inpComment);
        scrollComment.setWheelScrollingEnabled(true);
        scrollComment.setViewportView(this.inpComment);

        panel.add(this.newInputComponent(scrollComment, MovieField.COMMENT), BorderLayout.CENTER);

        return panel;
    }

    private JPanel panelDateAdded() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        panel.add(OmovGuiUtil.newLabelBold("Date Added "));
        panel.add(this.lblDateAdded);

        return panel;
    }

    @Override
    String getTabTitle() {
        return "Notes";
    }


    void setMovieComment(String comment) {
        this.inpComment.setText(comment);
    }

    public String getComment() {
        return this.inpComment.getText();
    }

    public Date getDateAdded() {
        if(this.isAddMode) return null; // if editing multiple movies, isAddMode == true

        return this.editMovie.getDateAdded();
    }
}
