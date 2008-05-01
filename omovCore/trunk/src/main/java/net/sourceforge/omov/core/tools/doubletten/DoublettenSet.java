package net.sourceforge.omov.core.tools.doubletten;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
