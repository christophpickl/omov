package net.sourceforge.omov.core.gui.main.tablex;

import java.util.List;

import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

public interface IMovieTableModel {

    void doSearch(String searchTerm);
    
    void setSmartFolder(SmartFolder smartFolder);
    
    Movie getMovieAt(int row);
    List<Movie> getMoviesAt(final int[] rowIndices);
}
