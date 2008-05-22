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
import java.awt.Component;
import java.awt.Dialog;
import java.util.List;

import javax.swing.JPanel;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.gui.CheckedComponent;
import net.sourceforge.omov.gui.LabeledCheckedComponent;
import net.sourceforge.omov.gui.LabeledComponent;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
abstract class AbstractMovieTab extends JPanel {

    final Dialog owner;
    final boolean isAddMode;
    final Movie editMovie;
    final List<Movie> editMovies;


    public AbstractMovieTab(AddEditMovieDialog owner, boolean isAddMode, Movie editMovie) {
        this.owner = owner;
        this.isAddMode = isAddMode;
        this.editMovie = editMovie;
        this.editMovies = null;

        this.pseudeConstructor();
    }

    public AbstractMovieTab(EditMoviesDialog owner, List<Movie> editMovies) {
        this.owner = owner;
        this.editMoviesDialog = owner;
        this.isAddMode = true;
        this.editMovie = null;
        this.editMovies = editMovies;

        this.pseudeConstructor();
    }

    private void pseudeConstructor() {
        this.setBackground(Constants.getColorWindowBackground());
    }

    abstract String getTabTitle();

    private EditMoviesDialog editMoviesDialog;
    final JPanel newInputComponent(Component component, MovieField movieField) {
        final JPanel result;

        if(this.editMoviesDialog == null) { // dialog for single movie
            if(movieField == MovieField.SEEN) {
                result = new JPanel(new BorderLayout());
                result.add(component, BorderLayout.WEST);
            } else if(movieField == MovieField.DURATION) {
                result = new LabeledComponent(component, movieField.label(), BorderLayout.WEST);
            } else {
                result = new LabeledComponent(component, movieField.label());
            }

        } else { // is editing multiple movies
            if(movieField == MovieField.SEEN) {
                result = new CheckedComponent(component, this.editMoviesDialog.fieldCheckBoxes.get(movieField));
            } else {
                result = new LabeledCheckedComponent(component, movieField.label(), this.editMoviesDialog.fieldCheckBoxes.get(movieField));
            }
        }
        result.setOpaque(false);
        return result;
    }
}
