package net.sourceforge.omov.app.gui;

import net.sourceforge.omov.core.bo.Movie;

public interface IPrevNextMovieProvider {
    
    int getCountIndices();
    
    int getInitialIndex();
    
    Movie getMovieAt(int index);
}
