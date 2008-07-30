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

package net.sourceforge.omov.logic.tools.doubletten;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DuplicatesFinder {

    private static final Log LOG = LogFactory.getLog(DuplicatesFinder.class);

    private boolean aborted = false;

    private Exception thrownException = null;

    /** cleaned (trimmed&lowered) title, list of movies with this title*/
    private Map<String, List<Movie>> foundDuplicates = null;

    public DuplicatesFinder() {
        /* nothing to do */
    }

    public void findDuplicates(IDuplicatesFinderListener listener) {
        LOG.info("Start finding duplicates...");
        this.foundDuplicates = new HashMap<String, List<Movie>>();

        try {

//            // --- delete me
//            for (int i = 0; i < 50 && this.aborted == false; i++) {
//                try { System.out.println("Thread sleeping...");
//                Thread.sleep(100);
//                } catch (InterruptedException e) { e.printStackTrace(); }
//            }
//            // ---

            final Set<Movie> movies = BeanFactory.getInstance().getMovieDao().getMovies();

            for (Movie movie : movies) {
                final String cleanedTitle = movie.getTitle().trim().toLowerCase();
                List<Movie> list = this.foundDuplicates.get(cleanedTitle);
                if(list == null) {
                    list = new LinkedList<Movie>();
                    this.foundDuplicates.put(cleanedTitle, list);
                }
                list.add(movie);
            }

            LOG.info("findDuplicates end (aborted="+aborted+")");

            listener.doProgressFinished(this.aborted == false);
        } catch(Exception e) {
            this.thrownException = e;
            this.foundDuplicates = null;
            listener.doProgressFinished(false);
        }
    }

    public DoublettenSet getFoundDuplicates() {
        if(this.foundDuplicates == null) {
            LOG.warn("Tried to get found duplicates but was null!");
            return null;
        }
        final DoublettenSet duplicates = new DoublettenSet();

        // Map<String, List<Movie>> foundDuplicates
        for (final String cleanedTitle : this.foundDuplicates.keySet()) {
            final List<Movie> movieDuplicateList = this.foundDuplicates.get(cleanedTitle);
            if(movieDuplicateList.size() == 1) { // only one movie exists with this cleaned title
                continue;
            }
            LOG.debug("Adding "+movieDuplicateList.size()+" duplicates for title '"+cleanedTitle+"'.");

            duplicates.add(movieDuplicateList);
        }
        return duplicates;
    }

    public Exception getThrownException() {
        return this.thrownException;
    }

    public void doAbort() {
        this.aborted = true;
    }

    public static interface IDuplicatesFinderListener {
        /**
         * @param successfullyFinished false if: user aborted or some exception was thrown; true otherwise
         */
        void doProgressFinished(boolean successfullyFinished);
    }
}
