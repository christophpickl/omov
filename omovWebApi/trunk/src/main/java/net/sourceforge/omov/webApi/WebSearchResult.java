package net.sourceforge.omov.webApi;

import org.apache.commons.lang.StringEscapeUtils;


/**
 * Value class, storing results of websearch (by movie title).
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class WebSearchResult { // TODO rename class to something like "WebSearchTarget"
    
    private final String label;
    
    private final String url;
    
    
    public WebSearchResult(final String label, final String url) {
        this.label = StringEscapeUtils.unescapeHtml(label);
        this.url = url;
    }
    
    @Override
	public String toString() {
        return "WebSearchResult[label="+label+";url="+url+"]";
    }
    
    @Override
	public boolean equals(Object object) {
        if((object instanceof WebSearchResult) == false) return false;
        WebSearchResult that = (WebSearchResult) object;
        return this.url.equals(that.url);
    }
    
    @Override
	public int hashCode() {
        return this.url.hashCode();
    }
    
    public String getLabel() {
        return this.label;
    }
    
    public String getUrl() {
        return this.url;
    }
    
    
}
