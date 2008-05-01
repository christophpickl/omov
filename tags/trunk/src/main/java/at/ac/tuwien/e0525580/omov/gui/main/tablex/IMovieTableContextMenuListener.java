package at.ac.tuwien.e0525580.omov.gui.main.tablex;

public interface IMovieTableContextMenuListener {
    
    void doEditMovie(int tableRowSelected);
    
    void doEditMovies(int[] tableRowSelected);
    
    void doDeleteMovie(int tableRowSelected);
    
    void doDeleteMovies(int[] tableRowSelected);
    
    void doFetchMetaData(int tableRowSelected);
    
    void doRevealMovie(int tableRowSelected);
    
    void doPlayVlc(int tableRowSelected);
}
