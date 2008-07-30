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

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class VersionedMovies implements Serializable {
    
    private static final long serialVersionUID = 4552162987116801481L;

    private final List<Movie> movies;
    
    private final int dataVersion = Movie.DATA_VERSION;
    
    public VersionedMovies(List<Movie> movies) {
        this.movies = Collections.unmodifiableList(movies);
    }
    
    @Override
	public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("VersionedMovies[length="+movies.size() + "]");
        for (Movie movie : this.movies) {
            sb.append("\n  ").append(movie);
        }
        return sb.toString();
    }
    
    public List<Movie> getMovies() {
        return this.movies;
    }
    
    public int getDataVersion() {
        return this.dataVersion;
    }
}
