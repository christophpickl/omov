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

package net.sourceforge.omov.core.tools.export;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.omov.core.bo.Movie;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ImportProcessResult {

    private boolean succeeded = false;

    private String errorMessage = "";

    /** movies which were not imported because movie folderpath is already in use */
    private final List<Movie> skippedMovies = new LinkedList<Movie>();

    private final List<Movie> insertedMovies = new LinkedList<Movie>();


    ImportProcessResult() {
        /* nothing to do */
    }

    void succeeded() {
        this.succeeded = true;
    }

    void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    void addSkippedMovie(final Movie movie) {
        this.skippedMovies.add(movie);
    }

    void setInsertedMovie(final List<Movie> insertedMovies) {
        if(this.insertedMovies == null) throw new NullPointerException("insertedMovies");
        this.insertedMovies.addAll(insertedMovies);
    }

    /**
     * @return movies which were not imported because movie folderpath is already in use.
     */
    public List<Movie> getSkippedMovies() {
        return Collections.unmodifiableList(this.skippedMovies);
    }
    public List<Movie> getInsertedMovies() {
        return Collections.unmodifiableList(this.insertedMovies);
    }

    public boolean isSucceeded() {
        return this.succeeded;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}
