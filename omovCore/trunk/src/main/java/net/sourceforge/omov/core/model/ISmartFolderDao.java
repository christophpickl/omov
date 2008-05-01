package net.sourceforge.omov.core.model;

import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

public interface ISmartFolderDao extends IDao {

    List<SmartFolder> getAllSmartFolders() throws BusinessException;
    List<SmartFolder> getAllSmartFoldersSorted() throws BusinessException;
    
    SmartFolder insertSmartFolder(SmartFolder smartFolder) throws BusinessException;
    void updateSmartFolder(SmartFolder smartFolder) throws BusinessException;
    void deleteSmartFolder(SmartFolder smartFolder) throws BusinessException;

    void registerMovieDaoListener(ISmartFolderDaoListener listener);
    void unregisterMovieDaoListener(ISmartFolderDaoListener listener);
}
