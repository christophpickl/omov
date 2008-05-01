package at.ac.tuwien.e0525580.omov.tools.remote;

import at.ac.tuwien.e0525580.omov.bo.VersionedMovies;

public interface IRemoteDataReceiver {
    
    boolean acceptTransmission(String hostAddress);
    
    void dataReceived(VersionedMovies movies);
    
}
