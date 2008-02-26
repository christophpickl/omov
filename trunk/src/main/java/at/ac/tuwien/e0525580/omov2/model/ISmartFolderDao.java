package at.ac.tuwien.e0525580.omov2.model;

import java.util.List;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.smartfolder.SmartFolder;

public interface ISmartFolderDao extends IDao {
    
    List<SmartFolder> getAllSmartFolders() throws BusinessException;
    
    SmartFolder insertSmartFolder(SmartFolder smartFolder) throws BusinessException;
    void updateSmartFolder(SmartFolder smartFolder) throws BusinessException;
    void deleteSmartFolder(SmartFolder smartFolder) throws BusinessException;

    void registerMovieDaoListener(ISmartFolderDaoListener listener);
    void unregisterMovieDaoListener(ISmartFolderDaoListener listener);
}
