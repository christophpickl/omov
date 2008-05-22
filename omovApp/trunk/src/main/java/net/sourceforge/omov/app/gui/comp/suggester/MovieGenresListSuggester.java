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

import javax.swing.JPanel;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.gui.IDataList;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieGenresListSuggester extends AbstractListSuggester implements IDataList {

    private static final long serialVersionUID = -4512946734145833891L;

    private static final String DEFAULT_ITEM_NAME = "Genres";
    private static final int COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG = 20;
    private static final int DEFAULT_VISIBLE_ROWCOUNT = 4;
    

    public MovieGenresListSuggester(Dialog owner, Collection<String> additionalItems, int fixedCellWidth) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieGenres(), additionalItems, true, DEFAULT_ITEM_NAME, COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG, fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
        
    }
    public MovieGenresListSuggester(Dialog owner, int fixedCellWidth) throws BusinessException {
        super(owner, BeanFactory.getInstance().getMovieDao().getMovieGenres(), true, DEFAULT_ITEM_NAME, COLUMNSIZE_OF_TEXTFIELD_IN_ADD_DIALOG , fixedCellWidth, DEFAULT_VISIBLE_ROWCOUNT);
    }

    public JPanel getPanel() {
        return this;
    }
}
