/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.app.gui.comp.suggester;

import java.awt.Dialog;
import java.util.Collection;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieLanguagesListSuggester extends AbstractListSuggester {
    
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

    public MovieLanguagesListSuggester(Dialog owner, Collection<String> additionalItems) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieLanguages(), additionalItems, true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS, DEFAULT_CELL_WIDTH, DEFAULT_VISIBLE_ROWCOUNT);
        
    }
    public MovieLanguagesListSuggester(Dialog owner) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieLanguages(), true, DEFAULT_ITEM_NAME, TEXTFIELD_COLUMNS , DEFAULT_CELL_WIDTH, DEFAULT_VISIBLE_ROWCOUNT);
    }
    
}
