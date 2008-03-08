package at.ac.tuwien.e0525580.omov.bo;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.util.CollectionUtil;

public class MovieCreator {

    private static final Log LOG = LogFactory.getLog(MovieCreator.class);
    
//    private static final int INITIAL_ID = -42;
//    private int id = INITIAL_ID;
    private final int id;
    
    // general
    private String title;
    private boolean seen;
    private int rating;
    private String coverFile;
    private Set<String> genres;
    private Set<String> languages;
    private String style;

    // detail
    private String director;
    private Set<String> actors;
    private int year;
    private String comment;
    private Quality quality;
    private Date dateAdded;
    
    // technical
    private long fileSizeKb;
    private String folderPath;
    private String format;
    private Set<String> files;
    private int duration;
    private Resolution resolution; // may be null
    private Set<String> subtitles;
    
    
    
    MovieCreator(int id) {
        if(id < -1) throw new IllegalArgumentException("id: " + id);
        this.id = id;
    }
    
    public Movie get() {
        // general
        if(this.title == null) {
            LOG.warn("title was null, setting to empty string.");
            this.title = "";
        }
        if(this.coverFile == null) {
            LOG.debug("coverFile was null, setting to empty string.");
            this.coverFile = "";
        }
        if(this.style == null) {
            LOG.debug("style was null, setting to empty string.");
            this.style = "";
        }
        if(this.genres == null) {
            LOG.debug("genres was null, setting to empty set.");
            this.genres = new HashSet<String>();
        }
        if(this.languages == null) {
            LOG.debug("languages was null, setting to empty set.");
            this.languages = new HashSet<String>();
        }
        if(this.quality == null) {
            LOG.debug("quality was null, setting to unrated.");
            this.quality = Quality.UNRATED;
        }
        
        // detail
        if(this.director == null) {
            LOG.debug("director was null, setting to empty string.");
            this.director = "";
        }
        if(this.actors == null) {
            LOG.debug("actors was null, setting to empty set.");
            this.actors = new HashSet<String>();
        }
        if(this.comment == null) {
            LOG.debug("comment was null, setting to empty string.");
            this.comment = "";
        }
        if(this.dateAdded == null) {
            LOG.debug("dateAdded was null, setting to 0000-00-00 00:00:00!");
            this.dateAdded = new Date(0);
        }
        
        // technical
        if(this.folderPath == null) {
            LOG.debug("folderPath was null, setting to empty string.");
            this.folderPath = "";
        }
        if(this.format == null) {
            LOG.debug("format was null, setting to empty string.");
            this.format = "";
        }
        if(this.files == null) {
            LOG.debug("files were null, setting to empty set.");
            this.files = new HashSet<String>();
        }
        if(this.subtitles == null) {
            LOG.debug("subtitles were null, setting to empty set.");
            this.subtitles = new HashSet<String>();
        }
        if(this.resolution == null) {
            LOG.debug("resolution were null, setting to 0x0 resolution.");
            this.resolution = Resolution.R0x0;
        }
        
        
        return new Movie(id,
                title, seen, rating, coverFile, genres, languages, style,
                director, actors, year, comment, quality, dateAdded,
                fileSizeKb, folderPath, format, files, duration, resolution, subtitles);
    }

    // TODO erneutes setzen unmoeglich machen: if(this.format != null) throw new IllegalStateOperation("The attribute 'format' was already set!");
    
    // general
    public MovieCreator title(String input) {
        if(input == null) throw new NullPointerException("title");
        this.title = input; return this;
    }
    public MovieCreator seen(boolean input) {
        this.seen = input; return this;
    }
    public MovieCreator rating(int input) {
        if(input < 0 || input > 5) throw new IllegalArgumentException("rating: " + input);
        this.rating = input; return this;
    }
    public MovieCreator coverFile(String input) {
        LOG.debug("setting coverFile to '"+input+"'");
        if(input == null) throw new NullPointerException("coverFile");
        this.coverFile = input; return this;
    }
    public MovieCreator genres(String... input) {
        if(input == null) throw new NullPointerException("genres");
        this.genres = CollectionUtil.asStringSet(input); return this;
    }
    public MovieCreator genres(Set<String> input) {
        if(input == null) throw new NullPointerException("genres");
        this.genres = input; return this;
    }
    public MovieCreator languages(String... input) {
        if(input == null) throw new NullPointerException("languages");
        this.languages = CollectionUtil.asStringSet(input); return this;
    }
    public MovieCreator languages(Set<String> input) {
        if(input == null) throw new NullPointerException("languages");
        this.languages = input; return this;
    }
    public MovieCreator style(String input) {
        if(input == null) throw new NullPointerException("style");
        this.style = input; return this;
    }
    
    
    // detail

    public MovieCreator director(String input) {
        if(input == null) throw new NullPointerException("director");
        this.director = input; return this;
    }
    public MovieCreator actors(Set<String> input) {
        if(input == null) throw new NullPointerException("actors");
        this.actors = input; return this;
    }
    public MovieCreator actors(String... input) {
        if(input == null) throw new NullPointerException("actors");
        this.actors = CollectionUtil.asStringSet(input); return this;
    }
    public MovieCreator year(int input) {
        if(input < 0 || input >= 3000) throw new IllegalArgumentException("year: " + input);
        this.year = input; return this;
    }
    public MovieCreator comment(String input) {
        if(input == null) throw new NullPointerException("comment");
        this.comment = input; return this;
    }
    public MovieCreator quality(Quality input) {
        this.quality = input; return this;
    }
    public MovieCreator dateAdded(Date input) {
        if(input == null) throw new NullPointerException("dateAdded");
        this.dateAdded= input; return this;
    }
    
    // technical
    
    public MovieCreator fileSizeKb(long input) {
        if(input < 0) throw new IllegalArgumentException("fileSizeKb: " + input);
        this.fileSizeKb = input; return this;
    }
    public MovieCreator folderPath(String input) {
        if(input == null) throw new NullPointerException("folderPath");
        this.folderPath = input; return this;
    }
    public MovieCreator format(String input) {
        if(input == null) throw new NullPointerException("format");
        this.format = input; return this;
    }
    public MovieCreator files(String... input) {
        if(input == null) throw new NullPointerException("files");
        this.files = CollectionUtil.asStringSet(input); return this;
    }
    public MovieCreator files(Set<String> input) {
        if(input == null) throw new NullPointerException("files");
        this.files = input; return this;
    }
    public MovieCreator duration(int input) {
        if(input < 0) throw new IllegalArgumentException("duration: " + input);
        this.duration = input; return this;
    }
    public MovieCreator resolution(Resolution input) {
        if(input == null) throw new NullPointerException("resolution");
        this.resolution = input; return this;
    }
    public MovieCreator subtitles(String... input) {
        if(input == null) throw new NullPointerException("subtitles");
        this.subtitles = CollectionUtil.asStringSet(input); return this;
    }
    public MovieCreator subtitles(Set<String> input) {
        if(input == null) throw new NullPointerException("subtitles");
        this.subtitles = input; return this;
    }
}
