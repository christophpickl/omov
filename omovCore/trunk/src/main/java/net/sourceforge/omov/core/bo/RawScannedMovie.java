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

import java.util.Comparator;
import java.util.List;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class RawScannedMovie extends MovieFolderInfo {

    private final String title;
    
//    private boolean selected = true;

    public static Comparator<RawScannedMovie> COMPARATOR = new Comparator<RawScannedMovie>() {
        public int compare(RawScannedMovie m1, RawScannedMovie m2) {
            return m1.title.compareTo(m2.title);
        }
    };
    
    public Movie toMovie() {
        return Movie.create(-1).title(title).seen(false).rating(0).coverFile("").
            year(0).comment("").
            fileSizeKb(this.getFileSizeKB()).folderPath(this.getFolderPath()).format(this.getFormat()).files(this.getFiles()).duration(0).get();
    }
    
    @Override
	public String toString() {
        return "ScannedMovie[title="+title+"]";
    }
    
    public RawScannedMovie(String title, String folderPath, long fileSizeKb, String format, List<String> files) {
        super(folderPath, files, fileSizeKb, format);
        this.title = title;
    }
    
}
