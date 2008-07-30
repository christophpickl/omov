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

package net.sourceforge.omov.core;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.ImageIcon;

import net.sourceforge.omov.core.bo.CoverFileType;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.bo.Movie.MovieField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieTableColumns {

    public static String COVER_COLUMN_LABEL = "Cover";

    private static final List<String> COLUMN_NAMES;
    private static final List<IMovieTableColumn> COLUMNS;
    static {
        final List<IMovieTableColumn> columns = new ArrayList<IMovieTableColumn>();

        // this order will be preserved and displayed to the user!

        columns.add(new MovieCoverColumn(/* MovieField.COVER_FILE */)  { @Override
		public Object getValue(Movie movie) { return ColumnsCoverFactory.getInstance().getImage(movie); } });
        columns.add(new MovieTableColumn(MovieField.TITLE, 100)        { public Object getValue(Movie movie) { return movie.getTitle(); } });

        columns.add(new MovieTableColumn(MovieField.RATING, 60)        { public Object getValue(Movie movie) { return new Integer(movie.getRating()); } });
        columns.add(new MovieTableColumn(MovieField.QUALITY, 60)       { public Object getValue(Movie movie) { return movie.getQuality(); } });

        columns.add(new MovieTableColumn(MovieField.YEAR, 40)          { public Object getValue(Movie movie) { return new Integer(movie.getYear()); }});
        columns.add(new MovieTableColumn(MovieField.STYLE, 80)         { public Object getValue(Movie movie) { return movie.getStyle(); }});
        columns.add(new MovieTableColumn(MovieField.GENRES, 100)       { public Object getValue(Movie movie) { return movie.getGenresString(); }
                                                                         @Override
																		public Class<?> getColumnClass() { return String.class;} }); // override Set.class with String.class

        columns.add(new MovieTableColumn(MovieField.ACTORS, 100)       { public Object getValue(Movie movie) { return movie.getActorsString(); }
                                                                         @Override
																		public Class<?> getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieTableColumn(MovieField.DIRECTOR, 80)      { public Object getValue(Movie movie) { return movie.getDirector(); }});
        columns.add(new MovieTableColumn(MovieField.LANGUAGES, 100)    { public Object getValue(Movie movie) { return movie.getLanguagesString(); }
                                                                         @Override
																		public Class<?> getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieTableColumn(MovieField.SUBTITLES, 100)    { public Object getValue(Movie movie) { return movie.getSubtitlesString(); }
                                                                         @Override
																		public Class<?> getColumnClass() { return String.class;} }); // override Set.class with String.class

        columns.add(new MovieTableColumn(MovieField.DURATION, 40)      { public Object getValue(Movie movie) { return new Integer(movie.getDuration()); }});

        // -------

        columns.add(new MovieTableColumn(MovieField.FOLDER_PATH, 100)  { public Object getValue(Movie movie) { return movie.getFolderPath(); }});
        columns.add(new MovieTableColumn(MovieField.FILES, 100)        { public Object getValue(Movie movie) { return movie.getFilesString(); }
                                                                         @Override
																		public Class<?> getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieTableColumn(MovieField.FILE_SIZE_KB, 60)  { public Object getValue(Movie movie) { return movie.getFileSizeFormatted(); }
                                                                         @Override
																		public Class<?> getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieTableColumn(MovieField.RESOLUTION, 40)    { public Object getValue(Movie movie) { return movie.getResolution(); }});
        columns.add(new MovieTableColumn(MovieField.FORMAT, 80)        { public Object getValue(Movie movie) { return movie.getFormat(); }});

        columns.add(new MovieTableColumn(MovieField.DATE_ADDED, 100)   { public Object getValue(Movie movie) { return movie.getDateAdded(); }});



        final List<String> columnNames = new ArrayList<String>(columns.size());
        for (IMovieTableColumn column : columns) {
            columnNames.add(column.getLabel());
        }

        COLUMNS = Collections.unmodifiableList(columns);
        COLUMN_NAMES = Collections.unmodifiableList(columnNames);
    }



    public static List<IMovieTableColumn> getColumns() {
        return COLUMNS;
    }

    public static List<String> getColumnNames() {
        return COLUMN_NAMES;
    }



    public static interface IMovieTableColumn {
        String getLabel();
        Class<?> getColumnClass();
        int getPrefWidth();
        Object getValue(Movie movie);
    }

    private abstract static class MovieTableColumn implements IMovieTableColumn {
        private final MovieField field;
        private final int prefWidth;
        private MovieTableColumn(MovieField field, int prefWidth) {
            this.field = field;
            this.prefWidth = prefWidth;
        }
        public String getLabel() {
            return this.field.label();
        }
        public Class<?> getColumnClass() {
            return this.field.getFieldClass();
        }
        public int getPrefWidth() {
            return this.prefWidth;
        }
    }

    /** used in table displaying the thumbnail image (loaded by ColumnsCoverFactory) */
    private abstract static class MovieCoverColumn implements IMovieTableColumn {
        private MovieCoverColumn() {
            /* no instantiation */
        }
        public String getLabel() {
            return COVER_COLUMN_LABEL;
        }
        public Class<?> getColumnClass() {
            return ImageIcon.class;
        }
        public int getPrefWidth() {
            return CoverFileType.THUMBNAIL.getMaxWidth();
        }
        public abstract Object getValue(Movie movie);
    }
}
