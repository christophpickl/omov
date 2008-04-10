package at.ac.tuwien.e0525580.omov.tools;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BeanFactory;
import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.bo.Movie;

public class FileSystemChecker {

    private static final Log LOG = LogFactory.getLog(FileSystemChecker.class);



    public static FileSystemCheckResult process() throws BusinessException {
        final FileSystemCheckResult result = new FileSystemCheckResult();

        final List<Movie> movies = BeanFactory.getInstance().getMovieDao().getMoviesSorted();
        for (final Movie movie : movies) {
            LOG.debug("Checking movie: " + movie);

            if(movie.isFolderPathSet() == false) {
                LOG.debug("No folder path is set for movie.");
                continue;
            }

            LOG.debug("Checking movie folder '"+movie.getFolderPath()+"'.");
            final File folder = new File(movie.getFolderPath());

            if(folder.exists() == false) {
                LOG.debug("Adding missing folder '"+folder.getAbsolutePath()+"'.");
                result.addMissingMovieFolder(movie, folder.getAbsolutePath());

            } else {
                // MANTIS [2] filecheck: maybe also check if other moviefiles were added; maybe also recalculate size if files changed

                // MANTIS [2] filecheck: also check if subdirectories are existing

                // check subfiles if folder is existing
                for (String subFileRelativePath : movie.getFiles()) {
                    final File subFile = new File(folder, subFileRelativePath);
                    if(subFile.exists() == false) {
                        LOG.debug("Adding missing movie file '"+subFile.getAbsolutePath()+"'.");
                        result.addMissingMovieFile(movie, subFile.getAbsolutePath());
                    }
                } // for each movie file
            }
        }


        return result;
    }

    public static class FileSystemCheckResult {
        private final Map<Movie, List<String>> missingFiles = new LinkedHashMap<Movie, List<String>>();
        private final Map<Movie, List<String>> missingFolders = new LinkedHashMap<Movie, List<String>>();

        FileSystemCheckResult() {
            /* nothing to do */
        }

        void addMissingMovieFile(Movie movie, String path) {
            this.addMissingMovieFileOrFolder(this.missingFiles, movie, path);
        }

        void addMissingMovieFolder(Movie movie, String path) {
            this.addMissingMovieFileOrFolder(this.missingFolders, movie, path);
        }

        private void addMissingMovieFileOrFolder(final Map<Movie, List<String>> map, Movie movie, String path) {
            List<String> paths = map.get(movie);
            if(paths == null) {
                paths = new LinkedList<String>();
                map.put(movie, paths);
            }

            paths.add(path);
        }

        public boolean isEverythingOkay() {
            return this.missingFiles.size() == 0 && this.missingFolders.size() == 0;
        }

        public Map<Movie, List<String>> getMissingFiles() {
            return this.missingFiles;
        }

        public Map<Movie, List<String>> getMissingFolders() {
            return this.missingFolders;
        }
    }
}
