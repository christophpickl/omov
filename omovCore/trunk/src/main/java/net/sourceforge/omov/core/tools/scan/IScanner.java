package net.sourceforge.omov.core.tools.scan;

import java.io.File;
import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;

public interface IScanner {
    
    List<Movie> process() throws BusinessException;
    
    void shouldStop();
    
    boolean isShouldStop();
    
    File getScanRoot();
}
