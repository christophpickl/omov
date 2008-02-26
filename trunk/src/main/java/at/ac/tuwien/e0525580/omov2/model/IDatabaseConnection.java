package at.ac.tuwien.e0525580.omov2.model;

import at.ac.tuwien.e0525580.omov2.BusinessException;

public interface IDatabaseConnection {
    void close() throws BusinessException;
    boolean isConnected();
}
