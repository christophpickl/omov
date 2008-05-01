package net.sourceforge.omov.core.model;

import net.sourceforge.omov.core.BusinessException;

public interface IDao {
    
    void commit() throws BusinessException;
    
    void rollback();
    
    boolean isAutoCommit();
    void setAutoCommit(boolean autoCommit);
    
}
