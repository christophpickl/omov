package net.sourceforge.omov.app.gui.comp.suggester;

import java.awt.Dialog;
import java.util.Collection;

import javax.swing.JPanel;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.app.gui.comp.generic.IDataList;

public class MovieGenresList extends AbstractSuggesterList implements IDataList {

    private static final long serialVersionUID = -4512946734145833891L;

    private static final String DEFAULT_ITEM_NAME = "Genres";
    private static final int COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG = 20;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 4;
    

    public MovieGenresList(Dialog owner, Collection<String> additionalItems, int fixedCellWidth) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieGenres(), additionalItems, true, DEFAULT_ITEM_NAME, COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
        
    }
    public MovieGenresList(Dialog owner, int fixedCellWidth) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieGenres(), true, DEFAULT_ITEM_NAME, COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG , fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
    }

    public JPanel getPanel() {
        return this;
    }
}
