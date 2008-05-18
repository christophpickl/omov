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

package net.sourceforge.omov.core.tools.webdata;

import java.util.HashSet;
import java.util.Set;

import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.MovieCreator;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class ImdbMovieData {

    private static final Log LOG = LogFactory.getLog(ImdbMovieData.class);
    
    private String title;
    private String director;
    private String comment; // anhand "Plot Outline"
    private int year;
    private int duration;
    private Set<String> genres;
    private Set<String> actors; // beschraenkt auf ersten 3
    private String coverFile; // absolute path to image in temporary folder
    
    ImdbMovieData() {
        // nothing to do
    }
    
    Movie getMovie() {
        if(comment == null) {
            LOG.warn("Could not find any comment (Plot Outline).");
            comment = "";
        }
        if(director == null) {
            LOG.warn("Could not find director.");
            director = "";
        }
        if(actors == null) {
            LOG.warn("Could not find actors.");
            actors = new HashSet<String>();
        }
        if(genres == null) {
            LOG.warn("Could not find genres.");
            genres = new HashSet<String>();
        }
        
        final MovieCreator c = Movie.create(-1).title(title).genres(genres).director(director).year(year).comment(comment).actors(actors).duration(duration);
        
        if(coverFile != null) {
            c.coverFile(coverFile);
        } else {
            LOG.warn("Seems as cover file was not found/downloaded... setting coverFile to empty string.");
            c.coverFile("");
        }
        
        return c.get();
    }
    
    public String getDirector() {
        return this.director;
    }

    public void setActors(Set<String> actors) {
        this.actors = new HashSet<String>();
        for (String actor : actors) {
            this.actors.add(StringEscapeUtils.unescapeHtml(actor));
        }
    }

    public void setComment(String comment) {
        this.comment = StringEscapeUtils.unescapeHtml(comment);
    }

    public void setCoverFile(String coverFile) {
        this.coverFile = coverFile;
    }

    public void setDirector(String director) {
        this.director = StringEscapeUtils.unescapeHtml(director);
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setGenres(Set<String> genres) {
        this.genres = new HashSet<String>();
        for (String genre : genres) {
            this.genres.add(StringEscapeUtils.unescapeHtml(genre));
        }
    }

    public void setTitle(String title) {
        this.title = StringEscapeUtils.unescapeHtml(title);
    }

    public void setYear(int year) {
        this.year = year;
    }
    
    

}
