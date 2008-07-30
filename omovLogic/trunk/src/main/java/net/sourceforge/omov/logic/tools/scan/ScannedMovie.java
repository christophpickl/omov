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

package net.sourceforge.omov.logic.tools.scan;

import java.io.File;
import java.util.HashSet;

import net.sourceforge.omov.core.bo.CheckedMovie;
import net.sourceforge.omov.core.bo.Movie;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ScannedMovie extends CheckedMovie {

    private static final long serialVersionUID = 3374310280498510658L;
    
    // MINOR scanner: add boolean:metadataFetched indicating that this scanned movie got fetched metadata? -> is this even useful?

    private ScannedMovie(Movie movie, boolean checked) {
        super(movie, checked);
    }


    public static ScannedMovie newByMovie(Movie movie, boolean selected) {
        return new ScannedMovie(movie, selected);
    }

    public static ScannedMovie updateByMovie(Movie movie, ScannedMovie scanned) {
        return new ScannedMovie(movie, scanned.isChecked());
    }
    
    public static ScannedMovie updateByMetadataMovie(ScannedMovie scanned, Movie metadata) {            
        return new ScannedMovie(Movie.updateByMetadataMovie(scanned, metadata), scanned.isChecked());
    }
    
    public static ScannedMovie clearMetadataMovie(ScannedMovie scanned) {
        final String folderName = new File(scanned.getFolderPath()).getName();
        
        final Movie metadataCleared = Movie.create(scanned.getId())
            .actors(new HashSet<String>()).comment("").coverFile("").director("").duration(0).genres(new HashSet<String>()).title(folderName).year(0)
            .get();
        return new ScannedMovie(Movie.updateByMetadataMovie(scanned, metadataCleared), scanned.isChecked());
    }
}
