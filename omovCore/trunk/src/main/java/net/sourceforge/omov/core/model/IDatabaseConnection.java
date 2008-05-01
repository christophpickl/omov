package net.sourceforge.omov.core.model;

import net.sourceforge.omov.core.BusinessException;

public interface IDatabaseConnection {
    
    void close() throws BusinessException;
    
    boolean isConnected();
    
}
