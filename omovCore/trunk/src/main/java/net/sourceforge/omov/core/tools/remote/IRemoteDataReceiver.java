package net.sourceforge.omov.core.tools.remote;

import net.sourceforge.omov.core.bo.VersionedMovies;

public interface IRemoteDataReceiver {
    
    boolean acceptTransmission(String hostAddress);
    
    void dataReceived(VersionedMovies movies);
    
}
