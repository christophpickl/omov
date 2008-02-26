package at.ac.tuwien.e0525580.omov2.tools.export;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BeanFactory;
import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.Configuration;
import at.ac.tuwien.e0525580.omov2.Constants;
import at.ac.tuwien.e0525580.omov2.bo.movie.Movie;
import at.ac.tuwien.e0525580.omov2.gui.comp.generic.ImagePanel;
import at.ac.tuwien.e0525580.omov2.util.FileUtil;
import at.ac.tuwien.e0525580.omov2.util.ImageUtil;

public class ExporterHtml {

    private static final Log LOG = LogFactory.getLog(ExporterHtml.class);
    
    private final List<HtmlColumn> columns;

    public static void main(String[] args) throws BusinessException {
//        List<Movie> movies = new LinkedList<Movie>();
//        movies.add(Movie.getDummy());
        List<Movie> movies = BeanFactory.getInstance().getMovieDao().getMoviesSorted();
//        new ExporterHtml().process(movies, new OutputStreamWriter(System.out));
        try {
            List<HtmlColumn> columns = new ArrayList<HtmlColumn>();
            columns.add(HtmlColumn.COLUMN_ACTORS);
            new ExporterHtml(columns).process(movies, new File("/tut.html"));
        } catch (BusinessException e) {
            e.printStackTrace();
        }
    }
    


    public ExporterHtml(List<HtmlColumn> columns) {
        final List<HtmlColumn> list = new ArrayList<HtmlColumn>();
        list.add(HtmlColumn.COLUMN_ID);
        list.add(HtmlColumn.COLUMN_TITLE);
        
        for (HtmlColumn column : columns) {
            if(column == HtmlColumn.COLUMN_ID) {
                LOG.warn("Setting column ID is not necessary!");
                continue;
            }
            if(column == HtmlColumn.COLUMN_TITLE) {
                LOG.warn("Setting column Title is not necessary!");
                continue;
            }
            list.add(column);
        }
        this.columns = Collections.unmodifiableList(list);
        LOG.info("Exporting with columns: " + Arrays.toString(columns.toArray()));
    }

    private static final SimpleDateFormat FOLDER_DATE_FORMAT = new SimpleDateFormat("yyyy_MM_dd");
    private File copyCoverFiles(List<Movie> movies, File targetFile) throws BusinessException {
        final File targetDirectory = this.getAvailableTargetDirectory(targetFile);
        final File targetCoverDirectory = new File(targetDirectory, "covers");
        if(targetCoverDirectory.mkdirs() == false) {
            throw new BusinessException("Could not create cover folder '"+targetCoverDirectory.getAbsolutePath()+"'!");
        }
        
        final File coverFolder = Configuration.getInstance().getCoversFolder();
        for (Movie movie : movies) {
            if(movie.isCoverFileSet()) {
                final File originalCoverFile = new File(coverFolder, movie.getCoverFile());
                final File newCoverFile = new File(targetCoverDirectory, movie.getCoverFile());
                
                
//                final String urlPath = "file://" + originalCoverFile.getAbsolutePath();
//                URL url;
//                try {
//                    url = new URL(urlPath);
//                } catch (MalformedURLException e) {
//                    throw new BusinessException("Invalid image cover url '"+urlPath+"'!", e);
//                }
//                Image coverImage;
//                try {
//                    coverImage = ImageIO.read(url);
//                } catch (IOException e) {
//                    throw new BusinessException("Could not read image by file '"+urlPath+"'!", e);
//                }
                ImagePanel imagePanel = new ImagePanel(Constants.COVER_IMAGE_WIDTH, Constants.COVER_IMAGE_HEIGHT);
                final Image coverImage = ImageUtil.getResizedCoverImage(originalCoverFile, imagePanel, Constants.COVER_IMAGE_WIDTH, Constants.COVER_IMAGE_HEIGHT);
                
                int w = coverImage.getWidth(null);
                int h = coverImage.getHeight(null);
                
//                final WidthHeight widthHeight = ImageUtil.recalcMaxWidthHeight(w, h, Constants.COVER_IMAGE_WIDTH, Constants.COVER_IMAGE_HEIGHT);
//                w = widthHeight.getWidth();
//                h = widthHeight.getHeight();
                
                BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
                Graphics2D g2 = bi.createGraphics();
                g2.drawImage(coverImage, 0, 0, null);
                g2.dispose();
                
                try {
                    ImageIO.write(bi, FileUtil.extractExtension(originalCoverFile), newCoverFile);
                } catch (Exception e) {
                    throw new BusinessException("Could not save coverFile '"+originalCoverFile.getAbsolutePath()+"' to '"+newCoverFile.getAbsolutePath()+"'!", e);
                }
                
//                FileUtil.copyFile(new File(coverFolder, movie.getCoverFile()), new File(targetCoverDirectory, movie.getCoverFile()));
            }
        }
        
        targetFile = new File(targetDirectory, targetFile.getName());
        return targetFile;
    }
    
    private File getAvailableTargetDirectory(final File targetFile) {
        final String folderNameDatePart = FOLDER_DATE_FORMAT.format(new Date());
        final File parent = targetFile.getParentFile();
        
        File dir = new File(parent, folderNameDatePart);
        int i = 1;
        while(dir.exists() == true) {
            dir = new File(parent, folderNameDatePart + "-"+i);
            i++;
        }
        return dir;
    }
    
    // Monday, 11 February - 23:53:59
    private static final SimpleDateFormat CURRENT_DATE_FORMAT = new SimpleDateFormat("EEEE, dd MMMM - HH:mm:ss");
    public void process(List<Movie> movies, File target) throws BusinessException {
        LOG.info("Going to export " + movies.size() + " movies.");

        final boolean coversEnabled = columns.contains(HtmlColumn.COLUMN_COVER);
        if(coversEnabled) {
            target = this.copyCoverFiles(movies, target);
            // FIXME wenn process abstuerzt, den erstellten ordner loeschen!
        }
        
        final String currentDate = CURRENT_DATE_FORMAT.format(new Date());
        
        // FEATURE den topbanner + die columnheader ganz oben absolut positionieren, damit man die nicht wegscrollen kann (also immer visible sind)
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(target));
            writer.write(
        "<html>\n" +
        "<head>\n" +
        "<title>OurMovies2 - HTML Report</title>\n" +
        "<script type='text/javascript'>\n" +
        "function doGenerateIds() {\n" +
        "   form = document.getElementById('data_form');\n" +
        "   output = '';\n" +
        "   for(i=0; i < form.inpId.length; i++) {\n" +
        "       currentInpId = form.inpId[i];\n" +
        "       if(currentInpId.checked == true) {\n" +
        "           if(output != '') output += '|';\n" +
        "           output += currentInpId.value;\n" +
        "       }\n" +
        "   }\n" +
        "   if(output == '') {\n" +
        "       alert('Not any movie was selected.');\n" +
        "   } else {\n" +
        "       document.getElementById('outputIds').innerHTML = '[[' + output + ']]';\n" +
        "   }\n" +
        "}\n" +
        "</script>\n" +
        "<style type='text/css'>\n" +
        "body {\n" +
        "   background-color:#EDEEEE;\n" +
        "   margin:10px 0px 0px 0px; /* top, x, x, left */\n" +
        "}\n" +
        "body, h1, #date {\n" +
        "   font-family: verdana, sans-serif;\n" +
        "}\n" +
        "h1 {\n" +
        "   font-size:19pt;\n" +
        "   font-weight:bold;\n" +
        "   margin-bottom:8px;\n" +
        "}\n" +
        "h1, #date, #btnGenerate {\n" +
        "   margin-top:10px;\n" +
        "   margin-left:25px;\n" +
        "   margin-right:25px;\n" +
        "}\n" +
        "#date {\n" +
        "   font-size:12pt;\n" +
        "   margin-bottom:0px;\n" +
        "}\n" +
        "#data_wrapper {\n" +
        "   margin-top:30px;\n" +
        "   margin-bottom:15px; /* ... button needs to be closer */\n" +
        "}\n" +
        "#data_header {\n" +
        "   border-bottom:2px solid black;\n" +
        "}\n" +
        ".tr_even {\n" +
        "   background-color:#FFFFFF;\n" +
        "}\n" +
        ".tr_odd {\n" +
        "   background-color:#EDEEEE;\n" +
        "}\n" +
        ".tr_even, .tr_odd {\n" +
        "   height:50px;\n" +
        "}\n" +
        ".td_id {\n" +
        "   text-align:center;\n" +
        "}\n" +
        ".td_id, .td_cover, .td_title, .td_genre, .td_actors, .td_language, .td_rating {\n" +
        "   border-bottom:1px solid #999999;\n" +
        "   empty-cells:show;\n" +
        "}\n" +
        "#outputIds {\n" +
        "   width:500px;\n" +
        "   height:140px;\n" +
        "}\n" +
        "#outputIdsWrapper {\n" +
        "   text-align:center;\n" +
        "}\n" +
        "#btnGenerate {\n" +
        "   \n" +
        "}\n" +
        "#tbl_data {\n" +
        "   width:100%;\n" +
        "   margin-top:20px\n;" +
        "   margin-bottom:40px;\n" +
        "}\n" +
        "</style>\n" +
        "</head>\n" +
        "\n" +
        "<body>\n" +
        "<h1>OurMovies2 - HTML Report</h1>\n" +
        "<div id='date'>Created on "+currentDate+"</div>\n" +
        "\n" +
        "<form id='data_form'>\n" +
        "\n" +
        "<table id='tbl_data' cellspacing='0' cellpadding='12'>\n" +
        "   <colgroup>\n");
        
        for (HtmlColumn column : this.columns) {
            writer.write("       <col width='"+column.getWidth()+"' valign='middle' />\n");
        }
        
        writer.write("   </colgroup>\n" +
        "   <tr>\n");
        
        for (HtmlColumn column : this.columns) {
            writer.write("       <th>"+column.getLabel()+"</th>\n");
        }
        
        writer.write("   </tr>\n");
            

            boolean isEven = true;
            for (final Movie movie : movies) {
                writer.write(this.getHtmlCodeForMovie(movie, isEven));
                isEven = !isEven;
            }
            
        writer.write("</table>" +
        "\n" +
        "<div id='outputIdsWrapper'>\n" +
        "   <textarea id='outputIds' readonly='readonly'></textarea>\n" +
        "   <div id='btnGenerate'><input type='submit' value='Generate ID Request' onclick='doGenerateIds();return false' /></div>\n" +
        "</div>\n" +
        "\n" +
        "</form>\n" +
        "\n" +
        "</body>\n" +
        "</html>\n");
        } catch(IOException e) {
            throw new BusinessException("Could not generate HTML report!", e);
        } finally {
            if(writer != null) try { writer.close(); } catch(IOException e) {};
        }
        LOG.info("exporting HTML finished.");
    }
    
    private String getHtmlCodeForMovie(Movie movie, boolean isEven) {
        final StringBuilder sb = new StringBuilder();

        sb.append("   <tr class='tr_" + (isEven ? "even" : "odd") + "'>");
        for (HtmlColumn column : this.columns) {
            sb.append("<td class='td_"+column.getStyleClass()+"'>");
            sb.append(column.getValue(movie));
            sb.append("</td>");
        }
        sb.append("   </tr>\n");
        
        return sb.toString();
    }
    

}
