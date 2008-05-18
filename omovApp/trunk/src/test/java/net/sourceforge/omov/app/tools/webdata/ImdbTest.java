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

package net.sourceforge.omov.app.tools.webdata;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.util.CollectionUtil;
import net.sourceforge.omov.webApi.IWebDataFetcher;
import net.sourceforge.omov.webApi.WebDataFetcherFactory;
import net.sourceforge.omov.webApi.WebSearchResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ImdbTest extends TestCase {
    
    private static final Log LOG = LogFactory.getLog(ImdbTest.class);
    
    // TODO write more unit tests for webextractor
    
    public void xtestSearchResult() throws Exception {
        final String search = "independence day";
        
        final List<String> expectedUrls = new ArrayList<String>();
        expectedUrls.add("http://imdb.com/title/tt0116629/");
        expectedUrls.add("http://imdb.com/title/tt0208227/");
        expectedUrls.add("http://imdb.com/title/tt0085724/");
        expectedUrls.add("http://imdb.com/title/tt0292014/");
        expectedUrls.add("http://imdb.com/title/tt0073165/");
        expectedUrls.add("http://imdb.com/title/tt0879821/");
        expectedUrls.add("http://imdb.com/title/tt0361872/");
        expectedUrls.add("http://imdb.com/title/tt0327949/");
        expectedUrls.add("http://imdb.com/title/tt0358533/");
        expectedUrls.add("http://imdb.com/title/tt0357791/");
        expectedUrls.add("http://imdb.com/title/tt0818604/");
        
        IWebDataFetcher ex = WebDataFetcherFactory.newWebDataFetcher();
        List<WebSearchResult> result = ex.search(search);
        
        final List<String> actualUrls = new LinkedList<String>();
        for (WebSearchResult r : result) {
            actualUrls.add(r.getUrl());
        }
        
        assertEquals(expectedUrls.size(), actualUrls.size());
        for (int i = 0; i < expectedUrls.size(); i++) {
            assertEquals(expectedUrls.get(i), actualUrls.get(i));
        }
    }
    
    private void abstractTestDetail(final String detailPageUrl,
            String title, Set<String> genres, String director, String comment, int year, Set<String> actors, int duration) throws Exception {
        LOG.info("Creating expected movie instance.");
        
        final Movie expected = Movie.create(-1).title(title).genres(genres).director(director).comment(comment).year(year).actors(actors).duration(duration).get();
        
        final IWebDataFetcher ex = WebDataFetcherFactory.newWebDataFetcher();
        LOG.info("Fetching details for independence day.");
        final Movie actual = ex.getDetails(new WebSearchResult("", detailPageUrl), false);
        
        LOG.info("Comparing expected with actual movie.");
        assertEquals(expected, actual);
    }
    
    public void testDetailIndependenceDay() throws Exception {
        final String comment = "The aliens are coming and their goal is to invade and destroy. Fighting superior technology, Man's best weapon is the will to survive.";
        Set<String> genres = CollectionUtil.asStringSet("Action", "Sci-Fi", "Thriller");
        Set<String> actors = CollectionUtil.asStringSet("Will Smith", "Bill Pullman", "Jeff Goldblum");
        
        this.abstractTestDetail("http://imdb.com/title/tt0116629/", "Independence Day", genres, "Roland Emmerich", comment, 1996, actors, 145);
    }
}
