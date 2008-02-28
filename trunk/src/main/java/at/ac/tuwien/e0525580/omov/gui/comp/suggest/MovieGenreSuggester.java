package at.ac.tuwien.e0525580.omov.gui.comp.suggest;

import java.util.List;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.model.IntimeMovieDatabaseList;


public class MovieGenreSuggester extends AbstractTextSuggester {

    private static final long serialVersionUID = -163599779199212347L;

    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieGenres();
        }
    };
    
    
    public MovieGenreSuggester(final int columns) {
        super(columns);
    }

    @Override
    protected List<String> getValues() {
        return MovieGenreSuggester.model.getValues();
    }

}
