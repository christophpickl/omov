package at.ac.tuwien.e0525580.omov.tools.webdata;

import org.apache.commons.lang.StringEscapeUtils;

public class WebSearchResult {
    
    private final String label;
    
    private final String url;
    
    
    public WebSearchResult(final String label, final String url) {
        this.label = StringEscapeUtils.unescapeHtml(label);
        this.url = url;
    }
    
    public String toString() {
        return "WebSearchResult[label="+label+";url="+url+"]";
    }
    
    public boolean equals(Object object) {
        if((object instanceof WebSearchResult) == false) return false;
        WebSearchResult that = (WebSearchResult) object;
        return this.url.equals(that.url);
    }
    
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
