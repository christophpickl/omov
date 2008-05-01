package at.ac.tuwien.e0525580.omov.tools.scan;

import java.util.List;

public interface IScanListener {
    
    void doDirectoryCount(int count);
    
    void doNextPhase(String phaseName);
    
    void doNextFinished();
    
    void doScanFinished(List<ScannedMovie> scannedMovies, List<ScanHint> hints);
    
}
