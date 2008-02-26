package at.ac.tuwien.e0525580.omov2.gui;

import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;

public interface IPrevNextMovieProvider {
    
    int getCountIndices();
    
    int getInitialIndex();
    
    Movie getMovieAt(int index);
}
