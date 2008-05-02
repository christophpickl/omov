package net.sourceforge.omov.app.gui.comp.suggester;

import java.awt.Dialog;
import java.util.Collection;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;

public class MovieSubtitlesList extends AbstractSuggesterList {
    
    private static final long serialVersionUID = 7512647499462269400L;

    private static final String DEFAULT_ITEM_NAME = "Subtitles";
    /** length of input-textfield when adding new temporary element to intime-list */
    private static final int TEXTFIELD_COLUMNS = 13;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 2;
    private static final int DEFAULT_CELL_WIDTH = 150;
    

    public MovieSubtitlesList(Dialog owner, Collection<String> additionalItems) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieSubtitles(), additionalItems, true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS, DEFAULT_CELL_WIDTH, DEFAULT_VISIBLE_ROWCOUNT);
        
    }
    public MovieSubtitlesList(Dialog owner) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieSubtitles(), true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS , DEFAULT_CELL_WIDTH, DEFAULT_VISIBLE_ROWCOUNT);
    }
}
