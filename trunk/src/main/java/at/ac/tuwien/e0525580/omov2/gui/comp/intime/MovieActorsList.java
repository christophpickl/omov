package at.ac.tuwien.e0525580.omov2.gui.comp.intime;

import java.awt.Dialog;
import java.util.List;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.model.IntimeMovieDatabaseList;

public class MovieActorsList extends AbstractIntimeList {
    
    private static final long serialVersionUID = -4512946734145833891L;

    private static final int TEXTFIELD_COLUMNS = 20;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 4;
    
    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieActors();
        }
    };
    
    protected IntimeMovieDatabaseList<String> getIntimeModel() {
        return MovieActorsList.model;
    }
    

    public MovieActorsList(Dialog owner, int fixedCellWidth) {
        this(owner, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
    }
    public MovieActorsList(Dialog owner, int fixedCellWidth, int visibleRowCount) {
        super(owner, true, "Actors", TEXTFIELD_COLUMNS, fixedCellWidth, visibleRowCount);
    }
    
}
