package net.sourceforge.omov.webImpl;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.Set;

import net.sourceforge.jpotpourri.util.PtCollectionUtil;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.ProxyEnabledConnectionFactory;
import net.sourceforge.omov.core.bo.Movie;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.tags.TableRow;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;
import org.htmlparser.visitors.NodeVisitor;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class ImdbDetailPage extends NodeVisitor {

    private final ImdbMovieData movieData = new ImdbMovieData();
    private static final Log LOG = LogFactory.getLog(ImdbDetailPage.class);
    
    private static void debug(String msg) {
        if(ImdbWebDataFetcher.DEBUG) System.out.println("ImdbDetailPage: " + msg);
    }
    
    private final boolean fetchCover;
    
    public ImdbDetailPage(boolean fetchCover) {
        this.fetchCover = fetchCover;
    }

    @Override
	public void visitTag(Tag tag) {
        if(this.fetchTitle(tag) == true) {
            return;
        }
        if(this.fetchActors(tag) == true) {
            return;
        }
        if(this.fetchCover == true && this.fetchCover(tag) == true) {
            return;
        }
        
        if(tag.getTagName().equalsIgnoreCase("div") == false) return;
        if(tag.getAttribute("class") == null || tag.getAttribute("class").equals("info") == false) return;
        
        this.processInfoTag(tag);
    }

    private boolean fetchCover(Tag tag) {
        if(tag.getTagName().equalsIgnoreCase("div") && tag.getAttribute("id") != null && tag.getAttribute("id").equals("tn15lhs")) {
            Node divPhoto = tag.getChildren().elementAt(1);
            if((divPhoto instanceof Div) == false) return false;
            
            LinkTag linkTag = (LinkTag) divPhoto.getChildren().elementAt(1);
            
            final String href = linkTag.getAttribute("href");
            LOG.debug("Fetching cover from href '"+href+"'.");
            if(href.startsWith("/media/") == false) {
                LOG.info("Found href '"+href+"' does not start with /media/; skipping it.");
                return false;
            }
            final String url = ImdbWebDataFetcher.HOST + href;

        	URLConnection connection;
    		try {
    			connection = ProxyEnabledConnectionFactory.openConnection(new URL(url));
    		} catch (MalformedURLException e) { // MalformedURLException, IOException
    			throw new FatalException("Malformed url given '"+url+"'!", e);
    		} catch (IOException e) {
    			throw new FatalException("Could not connect to website by url '"+url+"'!", e);
    		}
            
            try {
                Parser parser = new Parser(connection);
                final ImdbCoverFetcher visitor = new ImdbCoverFetcher();
                parser.visitAllNodesWith(visitor);
                
                if(visitor.isCoverDownloaded() == true) {
                    final String newCoverFile = visitor.getDownloadedFile().getAbsolutePath();
                    LOG.debug("Setting coverfile form fetched image to '"+newCoverFile+"'.");
                    this.movieData.setCoverFile(newCoverFile);
                }
            } catch (ParserException e) {
                throw new FatalException("Could not fetch cover for url '"+url+"'!", e);
            }
            return true;
        }
        return false;
    }
    
    private boolean fetchTitle(Tag tag) {
        if(tag.getTagName().equalsIgnoreCase("div") && tag.getAttribute("id") != null && tag.getAttribute("id").equals("tn15title")) {
            // tag (Div) <- [1] HeadingTag <- [0] TextNode == "Title"
            final String title = tag.getChildren().elementAt(1).getChildren().elementAt(0).getText().trim();
            debug("setting title to '"+title+"'.");
            this.movieData.setTitle(title);
            return true;
        }
        return false;
    }
    
    private static final int MAX_ACTORS = 3;
    private boolean fetchActors(Tag tag) {
        if(tag.getTagName().equalsIgnoreCase("table") && tag.getAttribute("class") != null && tag.getAttribute("class").equals("cast")) {
            
            final Set<String> actors = new HashSet<String>();
            final NodeList tableRows = tag.getChildren();
            for (int i = 1, actorsFetched = 0, n = tableRows.size(); i < n && actorsFetched < MAX_ACTORS; i++) { // skip first, its a TextNode (header); only add top 3 actors
                final Node trNode = tableRows.elementAt(i);
                if((trNode instanceof TableRow) == false) { // some line between with text only (eg: http://imdb.com/title/tt0997157/)
                    debug("Ignoring table row at index i="+i+" (not a TableRow, but "+trNode.getClass().getSimpleName()+"): " + trNode);
                    continue;
                }
                final TableRow row = (TableRow) trNode;
                final int childrenSize = row.getChildren().size();
                if(childrenSize < 2) {
                    debug("Ignoring table row at index i="+i+" (children size != 2, but "+childrenSize+"): " + row);
                    continue;
                }
                final TableColumn secondTd = (TableColumn) row.getChildren().elementAt(1);
                final LinkTag linkTag = (LinkTag) secondTd.getChildren().elementAt(0);
                final TextNode textNode = (TextNode) linkTag.getChildren().elementAt(0);
                // row <- [1] TableColumn <-  [0] LinkTag <- [0] TextNode = actor
                final String actorName = textNode.getText().trim();
                debug("Adding actorName '"+actorName+"'.");
                actors.add(actorName);
                actorsFetched++;
            }
            
            LOG.debug("Setting movie actors to '"+PtCollectionUtil.toString(actors)+"'.");
            this.movieData.setActors(actors);
            return true;
        }
        return false;
    }
    
    private void processInfoTag(Tag tag) {
//        debug("processing info row ...");
        final Node tagHeader5 = tag.getChildren().elementAt(1); // maybe a HeadingTag object
        if(tagHeader5 == null) {
            debug("there was no tagHeader5 present; skipping.");
            return;
        }
        if((tagHeader5 instanceof TextNode) == true) {
            debug("tagHeader5 is not a tagHeader5 because its a textnode");
            return;
        }
        
        if(tagHeader5.getChildren() == null) {
            System.out.println("ups");
        }
        final Node maybeLabelTag = tagHeader5.getChildren().elementAt(0);
        if((maybeLabelTag instanceof TextNode) == false) {
            debug("not a TextNode but '"+maybeLabelTag.getClass().getSimpleName()+"'.");
            return;
        }
        
        final TextNode labelTag = (TextNode) maybeLabelTag;
        final String lbl = labelTag.getText();
        debug("parsing TextNode containing label '"+lbl+"'");
        
        final Node infoTag = labelTag.getParent().getParent(); // <div class="info">  <h5>LABEL</h5> VALUE </div>
        
        if(lbl.equals("Director:")) {
            // labelTag -> HeadingTag ->  Div <- [3] LinkTag <- [0] TextNode
            final String director = infoTag.getChildren().elementAt(3).getChildren().elementAt(0).getText();
            LOG.debug("setting director to '"+director+"'.");
            this.movieData.setDirector(director);
        } else if(lbl.equals("Directors:")) {
            
            NodeList directorsChildren = infoTag.getChildren();
            for (int i = 0; i < directorsChildren.size(); i++) {
                if((directorsChildren.elementAt(i) instanceof LinkTag) == false) {
                    continue;
                }
                final String singleDirector = directorsChildren.elementAt(i).getChildren().elementAt(0).getText().trim();
                debug("Found one of many director '"+singleDirector+"'.");
                if(this.movieData.getDirector() != null) {
                    this.movieData.setDirector(this.movieData.getDirector() + ", " + singleDirector);
                } else {
                    this.movieData.setDirector(singleDirector);
                }
                LOG.debug("new moviedirector is now '"+this.movieData.getDirector()+"'.");
            }
            
        } else if(lbl.equals("Release Date:")) {
            final TextNode textNode = (TextNode) infoTag.getChildren().elementAt(2); // TextNode
            final String releaseString = textNode.getText().trim();
            Integer releaseYear = parseYear(releaseString);
            if(releaseYear != null) {
                LOG.debug("setting year to '"+releaseYear+"'.");
                this.movieData.setYear(releaseYear.intValue());
            }
        } else if(lbl.equals("Genre:")) { // wenn in "Addtion Details" darunter: lbl.equals("Genres:")
            final Set<String> genres = new HashSet<String>();
            NodeList maybeLinks = tag.getChildren();
            
            for (int i = 0; i < maybeLinks.size(); i++) {
                Node maybeLink = maybeLinks.elementAt(i);
                if((maybeLink instanceof LinkTag) == false) {
                    debug("not a link");
                    continue;
                }
                
                LinkTag link = (LinkTag) maybeLink;
                if(link.getAttribute("href").startsWith("/Sections/Genres") == false) {
                    debug("genre is link to 'more'.");
                    continue;
                }
                
                final String genre = ((TextNode) link.getChild(0)).getText();
                debug("Adding genre '"+genre+"'.");
                genres.add(genre);
            }
            LOG.debug("Setting movie genres to '"+PtCollectionUtil.toString(genres)+"'.");
            this.movieData.setGenres(genres);

        } else if(lbl.equals("Plot Outline:") || lbl.equals("Plot:")) {
            final String comment = infoTag.getChildren().elementAt(2).getText().trim();
            LOG.debug("setting comment to '"+comment+"'.");
            this.movieData.setComment(comment);

        } else if(lbl.equals("Runtime:")) {
            final TextNode textNode = (TextNode) infoTag.getChildren().elementAt(2); // TextNode
            final String runtimeString = textNode.getText().trim();
            Integer runtimeMin = parseRuntime(runtimeString);
            if(runtimeMin != null) {
                LOG.debug("setting duration (in min) to '"+runtimeMin+"'.");
                this.movieData.setDuration(runtimeMin.intValue());
            }
            
        } else {
            debug("unhandled label: '" + lbl+"'");
        }
    }
    
    /**
     * @param releaseDateString e.g.: "18 May 2001 (USA)" OR "1990 (UK)" OR "3 September 1969 (South Korea)"
     * @return null if could not parse;
     */
    private static Integer parseYear(String releaseDateString) { 
        debug("parsing release year string '"+releaseDateString+"'");
        
        String[] parts = releaseDateString.split(" ");
        try {
            final String yearPart;
            if(parts.length == 2) {
                yearPart = parts[0];
            } else if(parts.length >= 4) {
                yearPart = parts[2];
            } else {
                throw new IllegalArgumentException("Could not parse release date '"+releaseDateString+"'!");
            }
            debug("yearPart: '"+yearPart+"'.");
            return new Integer(Integer.parseInt(yearPart));
        } catch(NumberFormatException e) {
            LOG.warn("Could not parse release year '"+releaseDateString+"'!");
            return null;
        }
    }

    /**
     * @param string e.g.: "145 min  / USA:153 min (special edition)"
     * @return null if was not parsable
     */
    private static Integer parseRuntime(String string) {
        debug("parsing release year string '"+string+"'");
        
        String[] parts = string.split(" ");
        try {
            final String runtimePart = parts[0];
            debug("runtimePart: '"+runtimePart+"'.");
            return new Integer(Integer.parseInt(runtimePart));
        } catch(NumberFormatException e) {
            LOG.warn("Could not parse runtime '"+string+"'!");
            return null;
        }
    }
    
    public Movie getMovie() {
        return this.movieData.getMovie();
    }
    
}