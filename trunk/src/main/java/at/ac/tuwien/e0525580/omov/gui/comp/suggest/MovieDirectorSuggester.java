package at.ac.tuwien.e0525580.omov.gui.comp.suggest;

import java.util.List;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.model.IntimeMovieDatabaseList;

public class MovieDirectorSuggester extends AbstractTextSuggester {

    private static final long serialVersionUID = 6715184514254982852L;
    
    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieDirectors();
        }
    };
    
    
    public MovieDirectorSuggester(final int columns) {
        super(columns);
    }

    @Override
    protected List<String> getValues() {
        return MovieDirectorSuggester.model.getValues();
    }

}
