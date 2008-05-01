package net.sourceforge.omov.core.tools.scan;

import java.util.List;

public interface IScanListener {
    
    void doDirectoryCount(int count);
    
    void doNextPhase(String phaseName);
    
    void doNextFinished();
    
    void doScanFinished(List<ScannedMovie> scannedMovies, List<ScanHint> hints);
    
}
