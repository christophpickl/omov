package at.ac.tuwien.e0525580.omov.model;

import at.ac.tuwien.e0525580.omov.BusinessException;

public interface IDatabaseConnection {
    
    void close() throws BusinessException;
    
    boolean isConnected();
    
}
