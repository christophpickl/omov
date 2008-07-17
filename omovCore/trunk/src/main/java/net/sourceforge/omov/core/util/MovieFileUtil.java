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

package net.sourceforge.omov.core.util;

import java.io.File;
import java.util.Set;

import net.sourceforge.jpotpourri.util.PtCollectionUtil;
import net.sourceforge.jpotpourri.util.PtFileUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieFileUtil {

    private static final Set<String> KNOWN_MOVIE_FILE_EXTENSIONS = PtCollectionUtil.immutableSet(
            "mpg", "mpeg", "mp4", "avi", "ogm", "mkv", "divx", "wmv", "flv", "mov",
            "bin", "cue", 
            "mdf", "mds", 
            "bup", "ifo", "vob"); // DVDs
    
    public static boolean isMovieFileExtension(String extension) {
        return KNOWN_MOVIE_FILE_EXTENSIONS.contains(extension);
    }

    public static boolean isMovieFile(File file) {
        return MovieFileUtil.isMovieFileExtension(PtFileUtil.extractExtension(file));
    }
    
}
