package at.ac.tuwien.e0525580.omov.model;

import at.ac.tuwien.e0525580.omov.BusinessException;

public interface IDao {
    
    void commit() throws BusinessException;
    
    void rollback();
    
    boolean isAutoCommit();
    void setAutoCommit(boolean autoCommit);
    
}