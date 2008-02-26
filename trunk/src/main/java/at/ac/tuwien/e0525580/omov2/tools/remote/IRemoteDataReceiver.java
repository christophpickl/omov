package at.ac.tuwien.e0525580.omov2.tools.remote;

import at.ac.tuwien.e0525580.omov2.bo.movie.VersionedMovies;

public interface IRemoteDataReceiver {
    
    boolean acceptTransmission(String hostAddress);
    
    void dataReceived(VersionedMovies movies);
    
}
