package net.sourceforge.omov.core.gui.movie;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.util.List;

import javax.swing.JPanel;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.gui.comp.generic.CheckedComponent;
import net.sourceforge.omov.core.gui.comp.generic.LabeledCheckedComponent;
import net.sourceforge.omov.core.gui.comp.generic.LabeledComponent;

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
