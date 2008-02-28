package at.ac.tuwien.e0525580.omov.tools.export;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.util.CollectionUtil;



public abstract class HtmlColumn {

    public static final HtmlColumn COLUMN_ID = new HtmlColumn("40", "ID", "id") {
        String getValue(final Movie movie) {
            return "<input type='checkbox' name='inpId' value='"+movie.getId()+"' />";
        }
    };
    public static final HtmlColumn COLUMN_TITLE = new HtmlColumn("*", "Title", "title") {
        String getValue(final Movie movie) {
            return StringEscapeUtils.escapeHtml(movie.getTitle());
        }
    };
    public static final HtmlColumn COLUMN_COVER = new HtmlColumn("50", "Cover", "cover") { // cover width passt so?
        String getValue(final Movie movie) {
            final String coverFile = movie.getCoverFile();
            if(coverFile.equals("")) {
                return "N/A";
            }
            return "<img src='covers/"+coverFile+"' alt='"+StringEscapeUtils.escapeHtml(movie.getTitle())+"'/>"; 
        }
    };
    public static final HtmlColumn COLUMN_GENRE = new HtmlColumn("230", "Genre", "genre") {
        String getValue(final Movie movie) {
            return StringEscapeUtils.escapeHtml(String.valueOf(movie.getGenresString()));
        }
    };
    public static final HtmlColumn COLUMN_ACTORS = new HtmlColumn("180", "Actors", "actors") {
        String getValue(final Movie movie) {
            return StringEscapeUtils.escapeHtml(String.valueOf(movie.getActorsString()));
        }
    };
    public static final HtmlColumn COLUMN_LANGUAGE = new HtmlColumn("120", "Language", "language") {
        String getValue(final Movie movie) {
            return StringEscapeUtils.escapeHtml(String.valueOf(movie.getLanguagesString()));
        }
    };
    public static final HtmlColumn COLUMN_RATING = new HtmlColumn("80", "Rating", "rating") {
        String getValue(final Movie movie) {
            return String.valueOf(movie.getRating());
        }
    };
    // FEATURE fuer html export mehr columns zur verfuegung stellen
    
    public static List<HtmlColumn> getAllColumns() {
        return new CollectionUtil<HtmlColumn>().asImmutableList(COLUMN_GENRE, COLUMN_ACTORS, COLUMN_LANGUAGE, COLUMN_RATING);
    }
    
    
    
    private final String width;
    private final String label;
    private final String styleClass;
    abstract String getValue(Movie movie);
    private HtmlColumn(final String width, final String label, final String styleClass) {
        this.width = width;
        this.label = label;
        this.styleClass = styleClass;
    }
    public String toString() {
        return "HtmlColumn[" + this.label + "]";
    }
    public String getLabel() {
        return this.label;
    }
    public String getStyleClass() {
        return this.styleClass;
    }
    public String getWidth() {
        return this.width;
    }
}
