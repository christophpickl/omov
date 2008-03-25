package at.ac.tuwien.e0525580.omov.model;

public interface IDataVersionDao {

    void storeDataVersion(int version);
    
    int getDataVersion();
    
}
