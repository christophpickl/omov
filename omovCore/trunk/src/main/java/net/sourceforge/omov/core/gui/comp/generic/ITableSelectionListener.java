package net.sourceforge.omov.core.gui.comp.generic;

import java.util.List;

import net.sourceforge.omov.core.bo.Movie;

public interface ITableSelectionListener {

    void selectionEmptyChanged();
    
    void selectionSingleChanged(final Movie newSelectedMovie);
    
    void selectionMultipleChanged(final List<Movie> newSelectedMovies);
    
}
