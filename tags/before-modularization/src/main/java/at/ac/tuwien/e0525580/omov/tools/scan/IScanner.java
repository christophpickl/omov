package at.ac.tuwien.e0525580.omov.tools.scan;

import java.io.File;
import java.util.List;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;

public interface IScanner {
    
    List<Movie> process() throws BusinessException;
    
    void shouldStop();
    
    boolean isShouldStop();
    
    File getScanRoot();
}
