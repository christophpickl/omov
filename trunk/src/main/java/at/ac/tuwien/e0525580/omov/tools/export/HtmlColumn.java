package at.ac.tuwien.e0525580.omov.tools.export;

import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.util.CollectionUtil;



public abstract class HtmlColumn {

    public static final HtmlColumn COLUMN_ID = new HtmlColumn("40", "", "id") { // displayed as checkbox list
        String getValue(final Movie movie) {
            return "<input type='checkbox' name='inpId' value='"+movie.getId()+"' id='inpCheckbox"+movie.getId()+"' />";
        }
    };
    public static final HtmlColumn COLUMN_TITLE = new HtmlColumn("*", "Title", "title") {
        String getValue(final Movie movie) {
            return "<a class='title_link' href='javascript:clickTitle(this, "+movie.getId()+");return false;'>"+StringEscapeUtils.escapeHtml(movie.getTitle()) + "</a>";
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
    public static final HtmlColumn COLUMN_ACTORS = new HtmlColumn("270", "Actors", "actors") {
        String getValue(final Movie movie) {
            return limitStringLength(20, movie.getActorsString(), this);
        }
    };
    public static final HtmlColumn COLUMN_LANGUAGE = new HtmlColumn("120", "Language", "language") {
        String getValue(final Movie movie) {
            return StringEscapeUtils.escapeHtml(String.valueOf(movie.getLanguagesString()));
        }
    };
    public static final HtmlColumn COLUMN_RATING = new HtmlColumn("80", "Rating", "rating") {
        String getValue(final Movie movie) {
            final StringBuilder sb = new StringBuilder();
            sb.append("<span class='ratingYes'>");
            for (int i = 0; i < movie.getRating(); i++) {
                sb.append("&times;");
            }
            sb.append("</span>");
            sb.append("<span class='ratingNo'>");
            for (int i = 0; i < 5 - movie.getRating(); i++) {
                sb.append("&times;");
            }
            sb.append("</span>");
            return sb.toString();
        }
    };
    // FEATURE fuer html export mehr columns zur verfuegung stellen (mantis: 10)
    
    public static List<HtmlColumn> getAllColumns() {
        return new CollectionUtil<HtmlColumn>().asImmutableList(COLUMN_GENRE, COLUMN_ACTORS, COLUMN_LANGUAGE, COLUMN_RATING);
    }
    
    private static String limitStringLength(final int maxChars, final String string, final HtmlColumn column) {
        final String fullString = string;
        if(fullString.length() < maxChars) {
            return StringEscapeUtils.escapeHtml(fullString);
        }
        final String partString = fullString.substring(0, maxChars) + " ...";
        return "<div title='"+fullString+"' style='width:"+column.getWidth()+"px;'>" + StringEscapeUtils.escapeHtml(partString) + "</div>";
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
