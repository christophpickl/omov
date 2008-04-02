package at.ac.tuwien.e0525580.omov.model;

public interface IDataVersionDao {

    public void storeDataVersions(final int movieVersion, final int smartfolderVersion);

    int getMovieDataVersion();
    int getSmartfolderDataVersion();
    
}
