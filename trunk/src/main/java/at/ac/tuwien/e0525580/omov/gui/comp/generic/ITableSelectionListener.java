package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.util.List;

import at.ac.tuwien.e0525580.omov.bo.Movie;

public interface ITableSelectionListener {

    void selectionEmptyChanged();
    
    void selectionSingleChanged(final Movie newSelectedMovie);
    
    void selectionMultipleChanged(final List<Movie> newSelectedMovies);
    
}
