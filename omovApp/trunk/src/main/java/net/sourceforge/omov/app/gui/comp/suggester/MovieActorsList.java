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
