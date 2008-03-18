package at.ac.tuwien.e0525580.omov.bo;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.util.CollectionUtil;
import at.ac.tuwien.e0525580.omov.util.FileUtil;
import at.ac.tuwien.e0525580.omov.util.NumberUtil;
import at.ac.tuwien.e0525580.omov.util.StringUtil;

/**
 * Immutable value class.
 * 
 * Attributes:
 * ----------
 * actors
 * comment
 * coverFile
 * dateAdded
 * director
 * duration
 * files
 * fileSizeKb
 * folderPath
 * format
 * genres
 * id
 * languages
 * quality
 * rating
 * resolution
 * seen
 * style
 * subtitles
 * title
 * year
 * 
 * @author Christoph Pickl - e0525580@student.tuwien.ac.at
 */
public class Movie implements Serializable {

    private static final long serialVersionUID = -1005281123869400266L;
    private static final Log LOG = LogFactory.getLog(Movie.class);

    public static final int DATA_VERSION = 1;
    public static final SimpleDateFormat DATE_ADDED_FORMAT_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_ADDED_FORMAT_SHORT = new SimpleDateFormat("yyyy-MM-dd");
    
    private final int id;

    private static List<MovieField> ALL_FIELDS = new ArrayList<MovieField>();
    public static enum MovieField {
        ID("ID", "id", Integer.class), // Long.class
        TITLE("Title", "title", String.class), 
        SEEN("Seen", "seen", Boolean.class),
        RATING("Rating", "rating", Integer.class), // Rating.class
        COVER_FILE("Cover", "coverFile", String.class),
        GENRES("Genres", "genres", Set.class),
        LANGUAGES("Languages", "languages", Set.class),
        STYLE("Style", "style", String.class),
        DIRECTOR("Director", "director", String.class),
        ACTORS("Actors", "actors", Set.class),
        YEAR("Year", "year", Integer.class),
        COMMENT("Comment", "comment", String.class),
        QUALITY("Quality", "quality", Quality.class),
        DATE_ADDED("Date Added", "dateAdded", Date.class),
        FILE_SIZE_KB("Size", "fileSizeKb", Long.class),
        FOLDER_PATH("Folder", "folderPath", String.class),
        FORMAT("Format", "format", String.class), 
        FILES("Files", "files", Set.class),
        DURATION("Duration", "duration", Integer.class),
        RESOLUTION("Resolution", "resolution", Resolution.class),
        SUBTITLES("Subtitles", "subtitles", Set.class);
        
        private final String label;
        private final String column; // DB-column, Db4o-column
        private final Class fieldClass;
        private MovieField(String label, String column, Class fieldClass) {
            this.label = label;
            this.column = column;
            this.fieldClass = fieldClass;
            ALL_FIELDS.add(this);
        }
        public String label() {
            return this.label;
        }
        public String column() {
            return this.column;
        }
        public Class getFieldClass() {
            return this.fieldClass;
        }
    }

    public static List<MovieField> getAllFields() {
        MovieField.ID.name(); // load class hack :)
        return ALL_FIELDS;
    }
    
    // ----------------------------------------------- GENERAL DATA
    private final String title;
    
    private final boolean seen;
    
    /** 0, 1-5 */
    private final int rating;
    
    /** either "" or absolute path to image file (including extension) */
    private final String coverFile;
    
    private final Set<String> genres;
    private final String genresString;
    
    private final Set<String> languages;
    private final String languagesString;
    
    /** e.g.: Color, Black/White, Cartoon, 3D-Animation*/
    private final String style;
    
    // ----------------------------------------------- DETAIL DATA

    private final String director;

    private final Set<String> actors;
    private final String actorsString;
    
    private final int year;
    
    private final String comment;
    
    /** IDs: [0-4] ... 0 = unrated; 1 = cinema bad; 2 = cinema, bad rip; 3 = good; 4 = dvd best */
    private final Quality quality;
    
    
    private final Date dateAdded;
    
    // ----------------------------------------------- TECHNICAL DATA
    
    /** sum of all movie files */
    private final long fileSizeKb;
    
    private final String folderPath;

    public static final String FORMAT_DELIMITER = "/";
    
    private final String format;
    
    private final Set<String> files;
    
    /** in minuten */
    private final int duration;
    
    /**
     * can be null
     */
    private final Resolution resolution;
    
    private final Set<String> subtitles;
    
    // -----------------------------------------------
    
    public static Comparator<Movie> TITLE_COMPARATOR = new Comparator<Movie>() {
        public int compare(Movie m1, Movie m2) {
            
            int result = m1.getTitle().compareTo(m2.getTitle());
            if(result != 0) {
                return result;
            }
            return m1.id - m2.id;   
        }
    };
    
    

    Movie(int id, Movie m) {
            this(id, m.dateAdded, m);
    }

    Movie(int id, Date dateAdded, Movie m) {
            this(id, m.title, m.seen, m.rating, m.coverFile, m.genres, m.languages, m.style,
            m.director, m.actors, m.year, m.comment, m.quality, dateAdded,
            m.fileSizeKb, m.folderPath, m.format, m.files, m.duration, m.resolution, m.subtitles);
    }
    
    Movie(final int id, 
            final String title, final boolean seen, final int rating, final String coverFile, final Set<String> genres, final Set<String> languages, final String style,
            final String director, final Set<String> actors, final int year, final String comment, Quality quality, final Date dateAdded, 
            final long fileSizeKb, final String folderPath, final String format, final Set<String> files, final int duration, final Resolution resolution, final Set<String> subtitles) {
        this.id = id;

        if(id < -1)            throw new IllegalArgumentException("id: " + id);
        if(title == null)      throw new NullPointerException("title");
        if(rating<0||rating>5) throw new IllegalArgumentException("rating: " + rating);
        if(coverFile == null)  throw new NullPointerException("coverFile");
        if(genres == null)     throw new NullPointerException("genres");
        if(languages == null)  throw new NullPointerException("languages");
        if(style == null)      throw new NullPointerException("style");
        if(director == null)   throw new NullPointerException("director");
        if(actors == null)     throw new NullPointerException("actors");
        if(year < 0)           throw new IllegalArgumentException("year: " + year);
        if(comment == null)    throw new NullPointerException("comment");
        if(quality == null)    throw new NullPointerException("quality");
        if(dateAdded == null)  LOG.info("dateAdded is null; seems as we are adding a new movie.");
        if(fileSizeKb < 0)     throw new NullPointerException("fileSizeKb: " + fileSizeKb);
        if(folderPath == null) throw new NullPointerException("folderPath");
        if(format == null)     throw new NullPointerException("format");
        if(files == null)      throw new NullPointerException("files");
        if(duration < 0)       throw new IllegalArgumentException("duration: " + duration);
        if(resolution == null) throw new NullPointerException("resolution");
        if(subtitles == null)  throw new NullPointerException("subtitles");
        
        // general
        this.title = title;
        this.seen = seen;
        this.rating = rating;
        this.coverFile = coverFile;
        this.genres = CollectionUtil.immutableSet(genres);
        this.languages = CollectionUtil.immutableSet(languages);
        this.style = style;

        this.genresString = CollectionUtil.toString(this.genres);
        this.languagesString = CollectionUtil.toString(this.languages);
        
        // detail
        this.director = director;
        this.actors = CollectionUtil.immutableSet(actors);
        this.year = year;
        this.comment = comment;
        this.quality = quality;
        this.dateAdded = dateAdded;
        
        this.actorsString = CollectionUtil.toString(this.actors);
        
        // technical
        this.fileSizeKb = fileSizeKb;
        this.folderPath = folderPath;
        this.format = format;
        this.files = CollectionUtil.immutableSet(files);
        this.duration = duration;
        this.resolution = resolution;
        this.subtitles = subtitles;
    }
    
    public static MovieCreator create(int id) {
        return new MovieCreator(id);
    }
    
    /** actually same as full constructor */
    public static Movie newMovie(final int id, 
            final String title, final boolean seen, final int rating, final String coverFile, final Set<String> genres, final Set<String> languages, final String style,
            final String director, final Set<String> actors, final int year, final String comment, Quality quality, final Date dateAdded, 
            final long fileSizeKb, final String folderPath, final String format, final Set<String> files, final int duration, final Resolution resolution, final Set<String> subtitles) {
        return new Movie(id, title, seen, rating, coverFile, genres, languages, style, director, actors, year, comment, quality, dateAdded, fileSizeKb, folderPath, format, files, duration, resolution, subtitles);
    }
    public static Movie newByMultipleEdited(final Movie m, 
            final String title, final boolean seen, final int rating, final String coverFile, final Set<String> genres, final Set<String> languages, final String style,
            final String director, final Set<String> actors, final int year, final String comment, Quality quality, 
            final int duration, final Resolution resolution, final Set<String> subtitles) {
        return new Movie(m.getId(),
            title, seen, rating, coverFile, genres, languages, style,
            director, actors, year, comment, quality, m.getDateAdded(),
            m.getFileSizeKb(), m.getFolderPath(), m.getFormat(), m.getFiles(), duration, resolution, subtitles);
    }
    public static Movie newByOtherMovie(int id, Movie m) {
        return new Movie(id, m.dateAdded, m);
    }

    public static Movie newByOtherMovieSetDateAdded(int id, Date dateAdded, Movie m) {
        return new Movie(id, dateAdded, m);
    }

    public static Movie newByOtherMovieSetCoverFile(Movie m, String newCoverFile) {
        LOG.debug("Creating movie by other and setting new coverfile to '"+newCoverFile+"'.");
        
        return new Movie(m.id,
                m.title, m.seen, m.rating, newCoverFile, m.genres, m.languages, m.style,
                m.director, m.actors, m.year, m.comment, m.quality, m.dateAdded,
                m.fileSizeKb, m.folderPath, m.format, m.files, m.duration, m.resolution, m.subtitles);
    }

    /**
     * always keep updatedfields in sync with ImdbMovieData:
     * @param metadata updated fields: title, director, comment, year, duration, genres, actors, coverFile
     */
    public static Movie updateByMetadataMovie(Movie original, Movie metadata) {
        final Movie newMovie = Movie.create(original.getId())
            .actors(metadata.getActors())
            .comment(metadata.getComment())
            .coverFile(metadata.getCoverFile())
            .director(metadata.getDirector())
            .duration(metadata.getDuration())
            .genres(metadata.getGenres())
            .title(metadata.getTitle())
            .year(metadata.getYear())
            
            .dateAdded(original.getDateAdded())
            .files(original.getFiles())
            .fileSizeKb(original.getFileSizeKb())
            .folderPath(original.getFolderPath())
            .format(original.getFormat())
            .languages(original.getLanguages())
            .quality(original.getQuality())
            .rating(original.getRating())
            .resolution(original.getResolution())
            .seen(original.isSeen())
            .style(original.getStyle())
            .subtitles(original.getSubtitles())
            
            .get();
        
        return newMovie;
    }
    
    public static Movie getDummy(String title) {
//        return new Movie(1,
//                new MovieGeneralData("Matrix", true, 3, "/img/coverFile.jpg", genres, langs, "Color"),
//                new MovieDetailData("Stanley Kubrick", actors, 1998, "comment"),
//                new MovieTechnicalData(123889, "/path/to/movie", "mp4", files, 142320, new Resolution(712, 568), subtitles));
        return Movie.create(-1).title(title).get();
    }
    
//    public static Movie newByScan(String title, long fileSizeKb, String filePath) {
//        Collection<String> x = new ArrayList<String>(); x.add("DE"); x.add("EN");
//        return new Movie(-1, title, "", fileSizeKb, x, filePath, "", false, 0);
//    }
    
//    public static Movie newByDialog(int id, String title, String genre, long fileSize, Collection<String> languages, String filePath, String coverFile, boolean seen, int rating) {
//        return new Movie(id, title, genre, fileSize, languages, filePath, coverFile, seen, rating);
//    }


    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        
        sb.append("Movie[");
          sb.append("id=").append(id).append(";");
          
          sb.append("title=").append(this.getTitle()).append(";");
          sb.append("seen=").append(this.isSeen()).append(";");
          sb.append("rating=").append(this.getRating()).append(";");
          sb.append("coverFileString=").append(this.getCoverFile()).append(";");
          sb.append("genres.size=").append(this.getGenres().size()).append(";");
          sb.append("languages.size=").append(this.getLanguagesString()).append(";");
          sb.append("style=").append(this.getStyle()).append(";");

          sb.append("director=").append(this.getDirector()).append(";");
          sb.append("actors.size=").append(this.getActors().size()).append(";");
          sb.append("year=").append(this.getYear()).append(";");
          sb.append("comment=").append(StringUtil.escapeLineFeeds(this.getComment())).append(";");
          sb.append("quality=").append(this.getQuality()).append(";");
          sb.append("dateAdded=").append(this.getDateAddedFormattedLong()).append(";");

          sb.append("fileSizeKb=").append(this.getFileSizeKb()).append(";");
          sb.append("folderPath=").append(this.getFolderPath()).append(";");
          sb.append("format=").append(this.getFormat()).append(";");
          sb.append("files=").append(Arrays.toString(this.getFiles().toArray())).append(";");
          sb.append("duration=").append(this.getDuration()).append(";");
          sb.append("resolution=").append(this.getResolution()).append(";");
          sb.append("subtitles.size=").append(this.getSubtitles().size()).append(";");
          
        sb.append("]");
        
        return sb.toString();
    }
    
    @Override
    public boolean equals(final Object object) {
        if((object instanceof Movie) == false) return false;
        final Movie that = (Movie) object;
        return that.getId() == this.getId()
        
            && this.getTitle().equals(that.getTitle())
            && this.isSeen() == that.isSeen()
            && this.getRating() == that.getRating()
            && this.getCoverFile().equals(that.getCoverFile())
            && this.getGenresString().equals(that.getGenresString())
            && this.getLanguagesString().equals(that.getLanguagesString())
            && this.getStyle().equals(that.getStyle())
            
            && this.getActors().equals(that.getActors())
            && this.getYear() == that.getYear()
            && this.getComment().equals(that.getComment())
            && this.getQuality() == that.getQuality()
            
            && this.getFileSizeKb() == that.getFileSizeKb()
            && this.getFolderPath().equals(that.getFolderPath())
            && this.getFormat().equals(that.getFormat())
            && this.getFiles().equals(that.getFiles())
            && this.getDuration() == that.getDuration()
            && this.getResolution().equals(that.getResolution())
            && this.getSubtitles().equals(that.getSubtitles());
    }

    @Override
    public int hashCode() {
        return this.getId() + 
            this.getTitle().hashCode() + this.getRating() + this.getCoverFile().hashCode() + this.getGenres().hashCode() + this.getLanguages().hashCode() + this.getStyle().hashCode() +
            this.getActors().hashCode() + this.getYear() + this.comment.hashCode() + this.getQuality().label().hashCode() +
            ((int)this.getFileSizeKb()) + this.getFolderPath().hashCode() + this.getFormat().hashCode() + this.getFormat().hashCode() + 
            this.getFiles().hashCode() + this.getDuration() + this.getResolution().hashCode() + this.getSubtitles().hashCode() +
            73;
    }
    
    
    
    public int getId() {
        return this.id;
    }

    
    

    // ----------------------------------------------- GENERAL DATA

    public String getTitle() {
        return this.title;
    }

    public int getRating() {
        return this.rating;
    }

    public boolean isSeen() {
        return this.seen;
    }

    public Set<String> getGenres() {
        return this.genres;
    }

    public String getGenresString() {
        return this.genresString;
    }
    

    public Set<String> getLanguages() {
        return this.languages;
    }

    public String getLanguagesString() {
        return this.languagesString;
    }
//    private static Collection<String> convertLanguages(String string) {
//        if(string.length() == 0) {
//            return new HashSet<String>(0);
//        }
//        
//        return new HashSet<String>(Arrays.asList(string.split(",")));
//    }

    public String getCoverFile() {
        return this.coverFile;
    }
    
    /**
     * handy method
     */
    public boolean isCoverFileSet() {
        return this.coverFile.length() > 0;
    }
    
    public String getStyle() {
        return this.style;
    }
    


    // ----------------------------------------------- DETAIL DATA
    
    public String getDirector() {
        return this.director;
    }
    
    public Set<String> getActors() {
        return this.actors;
    }

    public String getComment() {
        return this.comment;
    }

    public int getYear() {
        return this.year;
    }

    public Quality getQuality() {
        return this.quality;
    }
    
    public String getQualityString() {
        return this.quality.label();
    }

    public Date getDateAdded() {
        return new Date(this.dateAdded.getTime());
    }

    public String getDateAddedFormattedLong() {
        if(this.dateAdded == null) return "null";
        return Movie.DATE_ADDED_FORMAT_LONG.format(this.dateAdded);
    }
    public String getDateAddedFormattedShort() {
        if(this.dateAdded == null) return "null";
        return Movie.DATE_ADDED_FORMAT_SHORT.format(this.dateAdded);
    }



    public String getActorsString() {
        return this.actorsString;
    }


    // ----------------------------------------------- TECHNICAL DATA

    public int getDuration() {
        return this.duration;
    }
    
    /**
     * @return something like "1h 23min"
     */
    public String getDurationFormatted() {
        int minutes = this.duration % 60;
        int hours = (int) Math.floor(this.duration / 60.0);
        
        StringBuilder sb = new StringBuilder(10);
        sb.append(hours).append("h ").append(minutes).append("min");
        return sb.toString();
    }

    /**
     * @return something like "1:23"
     */
    public String getDurationFormattedShort() {
        return NumberUtil.formatDurationShort(this.duration);
    }
    

    public Set<String> getFiles() {
        return this.files;
    }

    public String getFilesFormatted() {
        return Arrays.toString(this.getFiles().toArray());
    }

    public long getFileSizeKb() {
        return this.fileSizeKb;
    }

    public String getFileSizeFormatted() {
        return FileUtil.formatFileSize(this.getFileSizeKb());
    }
    
    public boolean isFolderPathSet() {
        return this.folderPath.length() > 0;
    }
    
    public String getFolderPath() {
        return this.folderPath;
    }

    public String getFormat() {
        return this.format;
    }

    public Resolution getResolution() {
        return this.resolution;
    }

    public Set<String> getSubtitles() {
        return this.subtitles;
    }
    
}
