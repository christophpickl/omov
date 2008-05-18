package net.sourceforge.omov.webImpl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import net.sourceforge.omov.webApi.WebSearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.TableColumn;
import org.htmlparser.visitors.NodeVisitor;


/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class ImdbStartPage extends NodeVisitor {

    private static final Log LOG = LogFactory.getLog(ImdbStartPage.class);
    
    // eigentlich wirds nicht gebraucht; aber evtl .... im title-label von website suchen... oder so.
    private final String search;
    private final List<WebSearchResult> targetLinks = new LinkedList<WebSearchResult>();

    private static void debug(String msg) {
        if(ImdbWebDataFetcher.DEBUG) System.out.println("ImdbStartPage: " + msg);
    }
    
    public ImdbStartPage(String search) {
        this.search = search;
        LOG.debug("Searching start page with term '"+this.search+"'.");
    }
    
    // MINOR FEATURe websearch: some display "Popular Titles" different, because the probability is higher to be the right match
    public void visitTag(final Tag link) {
        
        
//        debug("visiting tag '"+tag.getTagName()+"'.");
        if(link.getTagName ().equalsIgnoreCase("a") == false) {
            return;
        }
        debug("");
        
        final String href = link.getAttribute("href");
        final Node linkChild = link.getChildren().elementAt(0);
        debug("FOUND <A> tag with href '"+href+"'.");
        
        if(href == null) {
            debug("  - SKIPPING because no href-attribute");
            return;
        }
        if(href.startsWith("/title/") == false) {
            debug("  - SKIPPING link because its href does not starts with /title/");
            return;
        }
        
        if((linkChild instanceof TextNode) == false) {
            debug("  - SKIPPING node because its not a text node (was: '"+linkChild.getClass().getSimpleName()+"').");
            return;
        }
        final TextNode textNode = (TextNode) linkChild;
        
        if(this.checkParentTitle(textNode) == false) {
            return;
        }
        
        final String linkLabel = textNode.getText();
        final WebSearchResult searchResult = new WebSearchResult(linkLabel, ImdbWebDataFetcher.HOST + href);
        if(this.alreadyAdded(searchResult) == true) {
            LOG.debug("Skipping duplicate searchresult entry '"+searchResult+"'.");
        } else {
            LOG.info("adding new target link with label '"+linkLabel+"' and href '" + href + "'");
            this.targetLinks.add(searchResult);
        }
    }
    
    private static final Set<String> HEADER_TITLES = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(new String[] 
              { "Popular Titles", "Titles (Exact Matches)", "Titles (Partial Matches)" })));
    
    /**
     * @return true, if the parent tag somewhere contains one of the valid header titles.
     */
    private boolean checkParentTitle(final TextNode textNode) {
//      textNode.getParent() textNode -> LinkTag -> ParagraphTag <- [1] TextNode == "Media from&nbsp;"
//      parent of text == link node; that parent == td (if its paragraph, the we are in "Media from" at top)
        if ((textNode.getParent().getParent() instanceof TableColumn) == false) {
            debug("seems as we are in 'Media from' part; skipping.");
            return false;
        }
        final TableColumn td = (TableColumn) textNode.getParent().getParent();

        Node maybeHeaderNode = td.getParent().getParent().getParent().getChildren().elementAt(1);
        if((maybeHeaderNode instanceof TextNode) == false) {
            LOG.debug("Parsing parent title failed because node is not a TextNode but '"+maybeHeaderNode.getClass().getSimpleName()+"'!");
            return false;
        }
        // td -> TableRow -> TableTag -> ParagraphTag <- [1] TextNode == "Popular Titles"
        final TextNode headerNode = (TextNode) maybeHeaderNode;
        final String headerTitle = headerNode.getText();
        
        final boolean result = HEADER_TITLES.contains(headerTitle); 
        debug("header title '"+headerTitle+"' is valid: " + result);
        return result;
    }

    private boolean alreadyAdded(WebSearchResult x) {
        return this.targetLinks.contains(x);
    }
    
    public List<WebSearchResult> getFoundTargetLinks() {
        return Collections.unmodifiableList(this.targetLinks);
    }
}
