package at.ac.tuwien.e0525580.omov.tools.export;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.commons.lang.StringEscapeUtils;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.bo.Movie;
import at.ac.tuwien.e0525580.omov.util.CollectionUtil;
import at.ac.tuwien.e0525580.omov.util.ImageUtil;
import at.ac.tuwien.e0525580.omov.util.ImageUtil.WidthHeight;



public abstract class HtmlColumn {

    public static final HtmlColumn COLUMN_ID = new HtmlColumn("40", "", "id") { // displayed as checkbox list
        String getValue(final Movie movie, IExporterHtml exporter) {
            return "<input type='checkbox' name='inpId' value='"+movie.getId()+"' id='inpCheckbox"+movie.getId()+"' />";
        }
    };
    public static final HtmlColumn COLUMN_TITLE = new HtmlColumn("*", "Title", "title") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return "<a class='title_link' href='javascript:clickTitle(this, "+movie.getId()+");return false;'>"+StringEscapeUtils.escapeHtml(movie.getTitle()) + "</a>";
        }
    };
    public static final HtmlColumn COLUMN_COVER = new HtmlColumn("50", "Cover", "cover") { // cover width passt so?
        String getValue(final Movie movie, IExporterHtml exporter) {
            final String coverFile = movie.getCoverFile();
            if(coverFile.equals("")) {
                return "N/A";
            }
            
            final WidthHeight widthHeight = ImageUtil.recalcMaxWidthHeight(new File(exporter.getCoversFolder(), "big_"+movie.getCoverFile()), Constants.COVER_IMAGE_WIDTH, Constants.COVER_IMAGE_HEIGHT);
            return "<img src='covers/lil_"+coverFile+"' alt='"+StringEscapeUtils.escapeHtml(movie.getTitle())+"'" +
                    " onmouseover=\"Tip('<img src=\\'covers/big_"+movie.getCoverFile()+"\\' width=\\'"+widthHeight.getWidth()+"\\' height=\\'"+widthHeight.getHeight()+"\\' />', BGCOLOR, '', BORDERWIDTH, 0, DELAY, 0, FADEIN, 200, FADEOUT, 100)\" onmouseout='UnTip()' />";
        }
    };
    public static final HtmlColumn COLUMN_GENRE = new HtmlColumn("210", "Genre", "genre") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return limitStringLength(20, movie.getGenresString(), this);
        }
    };
    public static final HtmlColumn COLUMN_ACTORS = new HtmlColumn("210", "Actors", "actors") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return limitStringLength(20, movie.getActorsString(), this);
        }
    };
    public static final HtmlColumn COLUMN_LANGUAGE = new HtmlColumn("130", "Language", "language") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return transformNonBreakingSpace(StringEscapeUtils.escapeHtml(movie.getLanguagesString()));
        }
    };
    public static final HtmlColumn COLUMN_RATING = new HtmlColumn("80", "Rating", "rating") {
        String getValue(final Movie movie, IExporterHtml exporter) {
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

    public static final HtmlColumn COLUMN_STYLE = new HtmlColumn("170", "Style", "style") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return limitStringLength(15, movie.getStyle(), this);
        }
    };

    public static final HtmlColumn COLUMN_DIRECTOR = new HtmlColumn("170", "Director", "director") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return limitStringLength(15, movie.getDirector(), this);
        }
    };

    public static final HtmlColumn COLUMN_YEAR = new HtmlColumn("80", "Year", "year") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return String.valueOf(movie.getYear());
        }
    };

    public static final HtmlColumn COLUMN_QUALITY = new HtmlColumn("130", "Quality", "quality") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return transformNonBreakingSpace(movie.getQualityString());
        }
    };

    public static final HtmlColumn COLUMN_FILE_SIZE = new HtmlColumn("80", "Size", "file_size") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return divisionWidthedWrapped(movie.getFileSizeFormatted(), this);
        }
    };

    public static final HtmlColumn COLUMN_FORMAT = new HtmlColumn("80", "Format", "format") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return movie.getFormat();
        }
    };

    public static final HtmlColumn COLUMN_DURATION = new HtmlColumn("80", "Duration", "duration") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return divisionWidthedWrapped(movie.getDurationFormatted(), this);
        }
    };

    public static final HtmlColumn COLUMN_RESOLUTION = new HtmlColumn("80", "Resolution", "resolution") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return movie.getResolution().getFormattedString();
        }
    };

    public static final HtmlColumn COLUMN_SUBTITLES = new HtmlColumn("130", "Subtitles", "subtitles") {
        String getValue(final Movie movie, IExporterHtml exporter) {
            return limitStringLength(10, movie.getSubtitlesString(), this);
        }
    };

    
    
    
    /**
     * @return all available columns (except of ID and Cover) sorted by label
     */
    public static List<HtmlColumn> getAllColumns() {
        List<HtmlColumn> list = new CollectionUtil<HtmlColumn>().asList(
                COLUMN_GENRE,
                COLUMN_ACTORS,
                COLUMN_LANGUAGE,
                COLUMN_RATING,
                COLUMN_STYLE,
                COLUMN_DIRECTOR,
                COLUMN_YEAR,
                COLUMN_QUALITY,
                COLUMN_FILE_SIZE,
                COLUMN_FORMAT,
                COLUMN_DURATION,
                COLUMN_RESOLUTION,
                COLUMN_SUBTITLES
                );
        Collections.sort(list, new Comparator<HtmlColumn>() {
            public int compare(HtmlColumn o1, HtmlColumn o2) {
                return o1.getLabel().compareTo(o2.getLabel());
            }
        });
        return Collections.unmodifiableList(list);
    }
    
    /**
     * limits given string to maxChars if necessary and html escapes it
     */
    private static String limitStringLength(final int maxChars, final String string, final HtmlColumn column) {
        final String fullString = string;
        if(fullString.length() <= maxChars) {
            return transformNonBreakingSpace(StringEscapeUtils.escapeHtml(fullString));
        }
        final String partString = fullString.substring(0, maxChars) + " ...";
        return "<div  onmouseover=\"Tip('"+fullString+"', BGCOLOR, '#FBFCC8', BORDERWIDTH, 1, BORDERCOLOR, '#000000', DELAY, 0, FADEIN, 200, FADEOUT, 100)\" onmouseout='UnTip()' style='width:"+column.getWidth()+"px;'>" + transformNonBreakingSpace(StringEscapeUtils.escapeHtml(partString)) + "</div>";
    }
    
    private static String divisionWidthedWrapped(final String string, final HtmlColumn column) {
        return "<div style='width:"+column.getWidth()+"px'>"+string+"</div>";
    }
    
    private static String transformNonBreakingSpace(String string) {
        return string.replaceAll(" ", "&nbsp;");
    }
    
    private final String width;
    private final String label;
    private final String styleClass;
    
    private HtmlColumn(final String width, final String label, final String styleClass) {
        this.width = width;
        this.label = label;
        this.styleClass = styleClass;
    }
    
    abstract String getValue(Movie movie, IExporterHtml exporter);
    
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
