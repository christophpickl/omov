package at.ac.tuwien.e0525580.omov.gui.comp.intime;

import java.awt.Dialog;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.model.IntimeMovieDatabaseList;

public class MovieActorsListFilled extends MovieActorsList implements IPrefilledIntimeList { // implements IDataList {

    private static final long serialVersionUID = -4512946734145833891L;

    private Set<String> filledActors;

    private IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            List<String> result = new LinkedList<String>(MOVIE_DAO.getMovieActors());
            result.addAll(filledActors);
            return result;
        }
    };
    
    
    public MovieActorsListFilled(Dialog owner, Set<String> filledActors, int preferredHeight) {
        super(owner, preferredHeight);
        
        this.filledActors = filledActors;
        
        this.setListData(); // TODO dirty workaround; superclass constructor invokes getIntimeModel before this instance could be initialized fully
    }
    
    @Override
    protected IntimeMovieDatabaseList<String> getIntimeModel() {
        return this.model;
    }

    public void unregisterIntimeDatabaseModel() {
        this.model.unregisterFromMovieDao();
    }
    
}
