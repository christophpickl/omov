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

package net.sourceforge.omov.core.tools.scan;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

import net.sourceforge.omov.core.util.MovieFileUtil;
import net.sourceforge.omov.core.util.FileUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class RepositoryPreparer {

    private static final Log LOG = LogFactory.getLog(RepositoryPreparer.class);

    private final List<PreparerHint> hints = new LinkedList<PreparerHint>();

    public static void main(String[] args) {

//        final File directory = GuiUtil.getDirectory(null, null);
//        if(directory == null) {
//            return;
//        }


        final File directory = new File("/Volumes/MEGADISK/Movies_Holy/_not_yet_seen");

        RepositoryPreparer p = new RepositoryPreparer();
        PreparerResult r = p.process(directory);
        System.out.println("moved files: " + r.getCntMovedFiles());
        for (PreparerHint hint : r.getHints()) {
            System.out.println(hint);
        }
    }

    public RepositoryPreparer() {
        /* nothing to do */
    }

    private void success(String msg) {
        this.hints.add(PreparerHint.success(msg));
    }
    private void info(String msg) {
        this.hints.add(PreparerHint.info(msg));
    }
    private void warning(String msg) {
        this.hints.add(PreparerHint.warning(msg));
    }
    private void error(String msg) {
        this.hints.add(PreparerHint.error(msg));
    }

    public PreparerResult process(final File directory) {
        if(directory.exists() == false || directory.isDirectory() == false) {
            throw new IllegalArgumentException("Invalid directory '"+directory.getAbsolutePath()+"'!");
        }

        this.hints.clear();
        int cntMovedFiles = 0;
        int cntIgnoredDirs = 0;
        int cntIgnoredFiles = 0;

        for (File movieFile : directory.listFiles()) {
            LOG.debug("Processing file/directory '"+movieFile.getAbsolutePath()+"' ...");

            if(movieFile.isDirectory() == true) {
                final String msg = "Ignoring existing directory: " + movieFile.getAbsolutePath();
                LOG.debug(msg);
                this.info(msg);
                cntIgnoredDirs++;
                continue;
            }

            if(MovieFileUtil.isMovieFile(movieFile) == false) {
                if(FileUtil.isHiddenFile(movieFile) == true) {
                    LOG.debug("Ignoring hidden file '"+movieFile.getName()+"'.");
                } else {
                    this.info("Ignoring non movie file: " + movieFile.getAbsolutePath());
                    cntIgnoredFiles++;
                }
                continue;
            }
            final String fileName = movieFile.getName();
            final String targetDirName = fileName.substring(0, fileName.length() - FileUtil.extractExtension(movieFile).length() - 1);
            final File targetDir = new File(movieFile.getParentFile(), targetDirName);
            final String targetDirPath = targetDir.getAbsolutePath();
            LOG.info("Creating target directory '"+targetDirPath+"' for movie file '"+fileName+"'.");
            if(targetDir.exists() == true) {
                final String msg = "Directory '"+targetDirPath+"' already exists; leaving file '"+fileName+"' where it is.";
                LOG.info(msg);
                this.warning(msg);
                continue;
            }
            if(targetDir.mkdir() == false) {
                final String msg = "Could not create directory '"+targetDirPath+"'!";
                LOG.error(msg);
                this.error(msg);
                continue;
            }


            final boolean couldMoveFile = movieFile.renameTo(new File(targetDir, fileName));
            if (couldMoveFile == false) {
                final String msg = "Could not move file '"+fileName+"' to directory '"+targetDirPath+"'!";
                LOG.error(msg);
                this.error(msg);
                continue;
            }

            final String msg = "Moved movie file '"+fileName+"' to directory '"+targetDirName+"'.";
            LOG.info(msg);
            this.success(msg);
            cntMovedFiles++;
        } // for each file

        return new PreparerResult(this.hints, cntMovedFiles, cntIgnoredDirs, cntIgnoredFiles);
    }






    public static class PreparerResult {
        private final List<PreparerHint> hints;
        private final int cntMovedFiles;
        private final int cntIgnoredDirs;
        private final int cntIgnoredFiles;
        private PreparerResult(final List<PreparerHint> hints, final int cntMovedFiles, int cntIgnoredDirs, int cntIgnoredFiles) {
            this.hints = hints;
            this.cntMovedFiles = cntMovedFiles;
            this.cntIgnoredDirs = cntIgnoredDirs;
            this.cntIgnoredFiles = cntIgnoredFiles;
        }
        public List<PreparerHint> getHints() {
            return this.hints;
        }
        public int getCntMovedFiles() {
            return this.cntMovedFiles;
        }
        public int getCntIgnoredDirs() {
            return this.cntIgnoredDirs;
        }
        public int getCntIgnoredFiles() {
            return this.cntIgnoredFiles;
        }
    }
}
