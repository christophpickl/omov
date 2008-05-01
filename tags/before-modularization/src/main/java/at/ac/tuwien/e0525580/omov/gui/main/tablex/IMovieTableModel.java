package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.util.List;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.smartfolder.SmartFolder;

public interface IMovieTableModel {

    void doSearch(String searchTerm);
    
    void setSmartFolder(SmartFolder smartFolder);
    
    Movie getMovieAt(int row);
    List<Movie> getMoviesAt(final int[] rowIndices);
}
