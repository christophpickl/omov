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

package net.sourceforge.omov.core.tools.doubletten;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DoublettenSet {

    private static final Log LOG = LogFactory.getLog(DoublettenSet.class);
    public static final DoublettenSet EMPTY_SET = new DoublettenSet();


    private final List<Movie> flatList = new ArrayList<Movie>();

    private final List<List<Movie>> setOfDoubletten = new LinkedList<List<Movie>>();

    DoublettenSet() {
        /* nothing to do */
    }

    public int size() {
        return this.flatList.size();
    }

    /**
     * @return list of indices of similar movies (including the given movie)
     */
    public int[] getSimilarMovieIndices(Movie movie) {
        List<Integer> indices = new LinkedList<Integer>();

        for (List<Movie> doublettenList : this.setOfDoubletten) {
            // for each doubletten list

            if(doublettenList.contains(movie)) { // if movie we are looking for, is in this doubletten list
                for (Movie listMovie : doublettenList) { // go through doubletten list
                    indices.add(this.flatList.indexOf(listMovie));
                }
                break; // assume that one movie can only occure once in a doubletten list
            }
        }

        final int[] result = new int[indices.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = indices.get(i);
        }
        return result;
    }

    public void add(List<Movie> doubletten) {
        this.flatList.addAll(doubletten);
        this.setOfDoubletten.add(doubletten);
    }

    public void remove(int row, Movie movie) {
        LOG.info("Removing duplicate movie at row "+row+": " + movie);
        assert(this.flatList.get(row).equals(movie));

        // remove from list
        {
            int index = -1;
            while((index = this.flatList.indexOf(movie)) != -1) {
                Movie removedMovie = this.flatList.remove(index);
                LOG.debug("Removed duplicate movie from flat list: " + removedMovie);
            }
        }

        // remove from set
        for (List<Movie> doublettenList : this.setOfDoubletten) {
            int index = -1;
            while((index = doublettenList.indexOf(movie)) != -1) {
                Movie removedMovie = doublettenList.remove(index);
                LOG.debug("Removed duplicate movie from doubletten set: " + removedMovie);
            }
        }

        // adjust set (if only one doublette left)
        final List<Integer> listsToRemove = new LinkedList<Integer>(); // index of them in setOfDoubletten
//        for (List<Movie> doublettenList : this.setOfDoubletten) {
        for (int i = 0; i < this.setOfDoubletten.size(); i++) {
            List<Movie> doublettenList = this.setOfDoubletten.get(i);

            if(doublettenList.size() == 1) {
                final Movie singleLeftDoublette = doublettenList.get(0);
                LOG.debug("Automatically removing single left doublette: " + singleLeftDoublette);
                this.flatList.remove(singleLeftDoublette);

                listsToRemove.add(i);
            }
            assert(doublettenList.size() != 0);
        }

        for (int listIndex : listsToRemove) {
            List<Movie> removedList = this.setOfDoubletten.remove(listIndex);
            assert(removedList != null) : "Could not remove list containing of single left movie: " + removedList;
        }
    }

    public Movie get(int index) {
        return this.flatList.get(index);
    }
}
