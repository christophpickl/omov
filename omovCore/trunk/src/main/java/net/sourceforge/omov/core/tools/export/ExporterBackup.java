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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import net.sourceforge.jpotpourri.util.CloseUtil;
import net.sourceforge.jpotpourri.util.FileUtilException;
import net.sourceforge.jpotpourri.util.ZipUtil;
import net.sourceforge.jpotpourri.util.ZipUtilException;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.util.FileUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.thoughtworks.xstream.XStream;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ExporterBackup implements ImportExportConstants {

    private static final Log LOG = LogFactory.getLog(ExporterBackup.class);

    public static void main(String[] args) throws BusinessException {
//        List<Movie> m = new LinkedList<Movie>();
//        m.add(Movie.getDummy());
//        new ExporterXml(m, new OutputStreamWriter(System.out)).process();

//        for (Movie m : ExporterBackup.encodeMovies(new File("/movie.xml"))) {
//            System.out.println(m);
//        }

        List<Movie> movies = BeanFactory.getInstance().getMovieDao().getMoviesSorted();
        ExporterBackup.process(movies, new File("/data.omo"));
    }


    private static DateFormat ZIP_FILE_NAME = new SimpleDateFormat("yyyy_MM_dd");
    private static DateFormat TEMP_FOLDER_NAME = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private ExporterBackup() {
        /* no instantiation */
    }

    public static File process(List<Movie> movies, File targetDir) throws BusinessException {
        assert(targetDir.exists() && targetDir.isDirectory());

        final File backupTempDir = new File(PreferencesDao.getInstance().getTemporaryFolder(), "exportBackup_" + TEMP_FOLDER_NAME.format(new Date()));
        try {
            if(backupTempDir.mkdirs() == false) {
                throw new BusinessException("Could not create folder '"+backupTempDir.getAbsolutePath()+"'!");
            }

            writeXml(movies, new File(backupTempDir, FILE_MOVIES_XML));
            copyCoverFiles(backupTempDir, movies);
            writeDataVersion(backupTempDir, Movie.DATA_VERSION);
            File backupFile;
			try {
				backupFile = zipContents(backupTempDir, targetDir);
			} catch (ZipUtilException e) {
				throw new BusinessException("Could not zip file!", e);
			}

            return backupFile;
        } finally {
            // cleanup
            if(backupTempDir.exists() == true) {
                try {
                    FileUtil.deleteDirectoryRecursive(backupTempDir);
                } catch(FileUtilException e) {
                    LOG.error("Could not clean up temporary directory at '"+backupTempDir.getAbsolutePath()+"'!", e);
                }
            }
        }
    }

    private static File zipContents(File backupTempDir, File targetDir) throws ZipUtilException {
        LOG.debug("Zipping contents to single file.");

        final File targetZipFile = getAvailableZipFile(targetDir);
        final File temporaryZipFile = new File(PreferencesDao.getInstance().getTemporaryFolder(), backupTempDir.getName() + ".zip");

        ZipUtil.zipDirectory(backupTempDir, temporaryZipFile);

        LOG.debug("Moving zip file '"+temporaryZipFile.getAbsolutePath()+"' to '"+targetZipFile.getAbsolutePath()+"'.");
        temporaryZipFile.renameTo(targetZipFile);
        return targetZipFile;

    }

    /**
     * copies only original cover file (when importing, resizing all images to all types and save them).
     */
    private static void copyCoverFiles(File backupTempDir, List<Movie> movies) throws BusinessException {
        LOG.debug("Copying cover files");
        final File coverDir = new File(backupTempDir, FOLDER_COVERS);
        if(coverDir.mkdir() == false) {
            throw new BusinessException("Could not create folder '"+coverDir.getAbsolutePath()+"'!");
        }
        for (final Movie movie : movies) {
            if(movie.isCoverFileSet()) {
                final File coverFile = new File(PreferencesDao.getInstance().getCoversFolder(), movie.getOriginalCoverFile());
                if(coverFile.exists()) {
                    try {
						FileUtil.copyFile(coverFile, new File(coverDir, coverFile.getName()));
					} catch (FileUtilException e) {
						throw new BusinessException("Could not copy cover file!", e);
					}
                } else {
                    LOG.warn("Could not copy cover for movie '"+movie.getTitle()+"' (ID="+movie.getId()+") because its cover file at '"+coverFile.getAbsolutePath()+"' does not exist anymore!");
                }
            }
        }
    }

    private static void writeDataVersion(File directory, int version) throws BusinessException {
        BufferedWriter writer = null;

        final File versionFile = new File(directory, FILE_DATA_VERSION);
        try {
            writer = new BufferedWriter(new FileWriter(versionFile));
            writer.write(String.valueOf(version));
        } catch(IOException e) {
            throw new BusinessException("Could not write movie data version to file '"+versionFile.getAbsolutePath()+"'!", e);
        } finally {
            if(writer != null) try { writer.close(); } catch(IOException e) { LOG.warn("Could not close buffered writer for file '"+versionFile.getAbsolutePath()+"'", e); }
        }
    }

    private static File getAvailableZipFile(File directory) {
        File file = null;
        int i = 0;
        do {
            final String fileName;
            if(i == 0) {
                fileName = "omov_backup-" + ZIP_FILE_NAME.format(new Date()) + "." + BACKUP_FILE_EXTENSION;
            } else {
                fileName = "omov_backup-" + ZIP_FILE_NAME.format(new Date()) + "-" + i + "." + BACKUP_FILE_EXTENSION;
            }
            file = new File(directory, fileName);
            i++;
        } while(file.exists() == true);
        return file;
    }

    private static void writeXml(List<Movie> movies, File xmlFile) throws BusinessException {
        LOG.debug("Writing movie data as XML file to '"+xmlFile.getAbsolutePath()+"'.");
        XStream xstream = new XStream();
        String xml = xstream.toXML(movies);

        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(xmlFile));
            writer.write(xml);
        } catch(IOException e) {
            throw new BusinessException("Could not generate HTML report!", e);
        } finally {
            CloseUtil.close(writer);
        }
    }

    @SuppressWarnings("unchecked")
    public static List<Movie> encodeMovies(File xmlFile) throws BusinessException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(xmlFile));
            StringBuilder sb = new StringBuilder();
            String line = null;
            while( (line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            XStream xstream = new XStream();
            return (List<Movie>) xstream.fromXML(sb.toString());
        } catch(IOException e) {
            throw new BusinessException("Could not encode movies from: " + xmlFile.getAbsolutePath(), e);
        } finally {
            CloseUtil.close(reader);
        }
    }

}
