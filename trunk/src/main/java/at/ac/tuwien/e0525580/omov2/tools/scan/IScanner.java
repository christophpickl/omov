package at.ac.tuwien.e0525580.omov2.tools.scan;

import java.io.File;
import java.util.List;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;

public interface IScanner {
    
    List<Movie> process() throws BusinessException;
    
    void shouldStop();
    
    boolean isShouldStop();
    
    File getScanRoot();
}
