package net.sourceforge.omov.core.gui.comp.suggester;

import java.awt.Dialog;
import java.util.Collection;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;

public class MovieActorsList extends AbstractSuggesterList {
    
    
    private static final long serialVersionUID = -4512946734145833891L;

    private static final String DEFAULT_ITEM_NAME = "Actors";
    private static final int TEXTFIELD_COLUMNS = 20;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 4;
    

    public MovieActorsList(Dialog owner, Collection<String> additionalItems, int fixedCellWidth) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieActors(), additionalItems, true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
        
    }
    
    public MovieActorsList(Dialog owner, int fixedCellWidth) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieActors(), true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS , fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
    }
}
