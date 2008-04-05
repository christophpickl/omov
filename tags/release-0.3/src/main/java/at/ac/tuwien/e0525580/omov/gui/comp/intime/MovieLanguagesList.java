package at.ac.tuwien.e0525580.omov.gui.comp.intime;

import java.awt.Dialog;
import java.util.List;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.model.IntimeMovieDatabaseList;

public class MovieLanguagesList extends AbstractIntimeList {
    
    private static final long serialVersionUID = -4512946734145833891L;

    /** length of input-textfield when adding new temporary element to intime-list */
    private static final int TEXTFIELD_COLUMNS = 13;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 2;
    private static final int DEFAULT_CELL_WIDTH = 150;
    
    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieLanguages();
        }
    };
    
    protected IntimeMovieDatabaseList<String> getIntimeModel() {
        return MovieLanguagesList.model;
    }
    

    public MovieLanguagesList(Dialog owner) {
        this(owner, DEFAULT_CELL_WIDTH);
    }
    public MovieLanguagesList(Dialog owner, int fixedCellWidth) {
        this(owner, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
    }
    public MovieLanguagesList(Dialog owner, int fixedCellWidth, int visibleRowCount) {
        super(owner, true, "Language", TEXTFIELD_COLUMNS, fixedCellWidth, visibleRowCount);
    }
    
}
