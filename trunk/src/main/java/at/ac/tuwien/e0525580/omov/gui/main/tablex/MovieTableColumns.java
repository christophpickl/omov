package at.ac.tuwien.e0525580.omov.gui.main.tablex;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.bo.Movie.MovieField;

public class MovieTableColumns {

    private static final List<String> COLUMN_NAMES;
    private static final List<MovieTableColumn> COLUMNS;
    static {
        final List<MovieTableColumn> columns = new ArrayList<MovieTableColumn>();

        // FEATURE add coverFile as tablecolumn (mantis: 7)
        
        columns.add(new MovieTableColumn(MovieField.TITLE, 100)        { public Object getValue(Movie movie) { return movie.getTitle(); } });
        columns.add(new MovieTableColumn(MovieField.RATING, 60)        { public Object getValue(Movie movie) { return movie.getRating(); } });
        columns.add(new MovieTableColumn(MovieField.QUALITY, 60)       { public Object getValue(Movie movie) { return movie.getQuality(); } });
        columns.add(new MovieTableColumn(MovieField.DURATION, 40)      { public Object getValue(Movie movie) { return movie.getDuration(); }});
        columns.add(new MovieTableColumn(MovieField.GENRES, 100)       { public Object getValue(Movie movie) { return movie.getGenresString(); }
                                                                    public Class getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieTableColumn(MovieField.DATE_ADDED, 100)   { public Object getValue(Movie movie) { return movie.getDateAdded(); }});
        columns.add(new MovieTableColumn(MovieField.FILE_SIZE_KB, 60)  { public Object getValue(Movie movie) { return movie.getFileSizeFormatted(); }
                                                                    public Class getColumnClass() { return String.class;} }); // override Set.class with String.class
        columns.add(new MovieTableColumn(MovieField.FOLDER_PATH, 100)  { public Object getValue(Movie movie) { return movie.getFolderPath(); }});
        columns.add(new MovieTableColumn(MovieField.FORMAT, 80)        { public Object getValue(Movie movie) { return movie.getFormat(); }});
        columns.add(new MovieTableColumn(MovieField.RESOLUTION, 40)    { public Object getValue(Movie movie) { return movie.getResolution(); }});
        columns.add(new MovieTableColumn(MovieField.YEAR, 40)          { public Object getValue(Movie movie) { return movie.getYear(); }});
        columns.add(new MovieTableColumn(MovieField.STYLE, 80)         { public Object getValue(Movie movie) { return movie.getStyle(); }});
        columns.add(new MovieTableColumn(MovieField.LANGUAGES, 100)    { public Object getValue(Movie movie) { return movie.getLanguagesString(); }
                                                                    public Class getColumnClass() { return String.class;} }); // override Set.class with String.class
        
        final List<String> columnNames = new ArrayList<String>(columns.size());
        for (MovieTableColumn column : columns) {
            columnNames.add(column.getLabel());
        }
        
        COLUMNS = Collections.unmodifiableList(columns);
        COLUMN_NAMES = Collections.unmodifiableList(columnNames);
    }
    


    static List<MovieTableColumn> getColumns() {
        return COLUMNS;
    }

    public static List<String> getColumnNames() {
        return COLUMN_NAMES;
    }
    
    
    
    abstract static class MovieTableColumn {
        private final MovieField field;
        private final int prefWidth;
        private MovieTableColumn(MovieField field, int prefWidth) {
            this.field = field;
            this.prefWidth = prefWidth;
        }
        public String getLabel() {
            return this.field.label();
        }
        public Class getColumnClass() {
            return this.field.getFieldClass();
        }
        public int getPrefWidth() {
            return this.prefWidth;
        }
        public abstract Object getValue(Movie movie);
    }
}