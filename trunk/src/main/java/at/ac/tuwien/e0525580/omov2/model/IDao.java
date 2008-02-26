package at.ac.tuwien.e0525580.omov2.model;

import at.ac.tuwien.e0525580.omov2.BusinessException;

public interface IDao {
    
    void commit() throws BusinessException;
    void rollback();
    void setAutoCommit(boolean autoCommit);
    
}
