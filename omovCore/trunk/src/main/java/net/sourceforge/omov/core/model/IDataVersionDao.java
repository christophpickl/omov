package net.sourceforge.omov.core.model;

public interface IDataVersionDao {

    public void storeDataVersions(final int movieVersion, final int smartfolderVersion);

    int getMovieDataVersion();
    int getSmartfolderDataVersion();
    
}
