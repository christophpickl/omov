package net.sourceforge.omov.core.gui.comp.suggest;

import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.model.IntimeMovieDatabaseList;

public class MovieStyleSuggester extends AbstractTextSuggester {

    private static final long serialVersionUID = -163599779199212347L;

    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieStyles();
        }
    };

    public MovieStyleSuggester(final int columns) {
        super(columns);
    }

    @Override
    protected List<String> getValues() {
        return MovieStyleSuggester.model.getValues();
    }

}