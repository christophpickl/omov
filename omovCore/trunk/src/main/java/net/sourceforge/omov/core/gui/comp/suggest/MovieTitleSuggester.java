package net.sourceforge.omov.core.gui.comp.suggest;

import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.model.IntimeMovieDatabaseList;

public class MovieTitleSuggester extends AbstractTextSuggester {

    private static final long serialVersionUID = -8003497356259924525L;
    
    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieTitles();
        }
    };

    public MovieTitleSuggester(final int columns) {
        super(columns);
    }

    @Override
    protected List<String> getValues() {
        return MovieTitleSuggester.model.getValues();
    }

}
