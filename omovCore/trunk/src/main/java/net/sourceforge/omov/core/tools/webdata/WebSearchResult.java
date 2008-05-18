/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.core.tools.webdata;

import org.apache.commons.lang.StringEscapeUtils;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
