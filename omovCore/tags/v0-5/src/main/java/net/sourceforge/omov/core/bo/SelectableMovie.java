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

package net.sourceforge.omov.core.bo;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SelectableMovie extends Movie {
    
    private static final long serialVersionUID = -7997056179880553697L;
    private boolean selected;
    
    public SelectableMovie(long id, Movie movie, boolean selected) {
        super(id, movie);
        this.selected = selected;
    }

    public SelectableMovie(Movie movie, boolean selected) {
        this(movie.getId(), movie, selected);
    }

//    public SelectableMovie(Movie movie) {
//        this(movie, true); // DEFAULT == selected: true
//    }

    public boolean isSelected() {
        return this.selected;
    }

    public SelectableMovie setSelected(boolean selected) {
        this.selected = selected;
        return this;
    }
}
