package net.sourceforge.omov.core.gui;

import net.sourceforge.omov.core.bo.Movie;

public interface IPrevNextMovieProvider {
    
    int getCountIndices();
    
    int getInitialIndex();
    
    Movie getMovieAt(int index);
}
