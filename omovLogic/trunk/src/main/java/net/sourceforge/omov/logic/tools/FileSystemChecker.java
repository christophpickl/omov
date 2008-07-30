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

package net.sourceforge.omov.logic.tools;

import java.io.File;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
