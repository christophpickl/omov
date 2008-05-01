package at.ac.tuwien.e0525580.omov.gui;

import at.ac.tuwien.e0525580.omov.bo.Movie;

public interface IPrevNextMovieProvider {
    
    int getCountIndices();
    
    int getInitialIndex();
    
    Movie getMovieAt(int index);
}
