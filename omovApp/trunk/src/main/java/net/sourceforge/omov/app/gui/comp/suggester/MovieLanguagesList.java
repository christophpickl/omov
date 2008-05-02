package net.sourceforge.omov.app.gui.comp.suggester;

import java.awt.Dialog;
import java.util.Collection;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;

public class MovieLanguagesList extends AbstractSuggesterList {
    
    private static final long serialVersionUID = -4512946734145833891L;

    private static final String DEFAULT_ITEM_NAME = "Languages";
    /** length of input-textfield when adding new temporary element to intime-list */
    private static final int TEXTFIELD_COLUMNS = 13;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 2;
    private static final int DEFAULT_CELL_WIDTH = 150;
    
//    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
//        @Override
//        protected List<String> reloadValues() throws BusinessException {
//            return MOVIE_DAO.getMovieLanguages();
//        }
//    };
//    
//    protected IntimeMovieDatabaseList<String> getIntimeModel() {
//        return MovieLanguagesList.model;
//    }
//    
//
//    public MovieLanguagesList(Dialog owner) {
//        this(owner, DEFAULT_CELL_WIDTH);
//    }
//    public MovieLanguagesList(Dialog owner, int fixedCellWidth) {
//        this(owner, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
//    }
//    public MovieLanguagesList(Dialog owner, int fixedCellWidth, int visibleRowCount) {
//        super(owner, true, "Language", TEXTFIELD_COLUMNS, fixedCellWidth, visibleRowCount);
//    }

    public MovieLanguagesList(Dialog owner, Collection<String> additionalItems) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieLanguages(), additionalItems, true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS, DEFAULT_CELL_WIDTH, DEFAULT_VISIBLE_ROWCOUNT);
        
    }
    public MovieLanguagesList(Dialog owner) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieLanguages(), true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS , DEFAULT_CELL_WIDTH, DEFAULT_VISIBLE_ROWCOUNT);
    }
    
}
