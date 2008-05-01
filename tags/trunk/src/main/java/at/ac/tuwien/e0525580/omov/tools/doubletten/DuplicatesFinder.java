package at.ac.tuwien.e0525580.omov.tools.doubletten;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.bo.Movie;

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
