package at.ac.tuwien.e0525580.omov2.gui.comp.intime;

import java.awt.Dialog;
import java.util.List;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.model.IntimeMovieDatabaseList;

public class MovieSubtitlesList extends AbstractIntimeList {
    
    private static final long serialVersionUID = 7512647499462269400L;
    
    /** length of input-textfield when adding new temporary element to intime-list */
    private static final int TEXTFIELD_COLUMNS = 13;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 2;
    private static final int DEFAULT_CELL_WIDTH = 150;
    
    private static final IntimeMovieDatabaseList<String> model = new IntimeMovieDatabaseList<String>() {
        @Override
        protected List<String> reloadValues() throws BusinessException {
            return MOVIE_DAO.getMovieSubtitles();
        }
    };
    
    protected IntimeMovieDatabaseList<String> getIntimeModel() {
        return MovieSubtitlesList.model;
    }
    

    public MovieSubtitlesList(Dialog owner) {
        this(owner, DEFAULT_CELL_WIDTH);
    }
    public MovieSubtitlesList(Dialog owner, int fixedCellWidth) {
        this(owner, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
    }
    public MovieSubtitlesList(Dialog owner, int fixedCellWidth, int visibleRowCount) {
        super(owner, true, "Subtitle", TEXTFIELD_COLUMNS, fixedCellWidth, visibleRowCount);
    }
    
}
