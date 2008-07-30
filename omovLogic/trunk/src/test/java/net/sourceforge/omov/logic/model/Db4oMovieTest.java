
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

package net.sourceforge.omov.logic.model;

import java.util.Set;

import junit.framework.TestCase;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.imodel.IMovieDao;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oMovieTest extends TestCase {
    
    private final IMovieDao dao;
    
    public Db4oMovieTest() {
        this.dao = BeanFactory.getInstance().getMovieDao();
    }
    
    @Override
	protected void setUp() throws Exception {
        super.setUp();
        
        Set<Movie> movies = this.dao.getMovies();
        for (Movie movie : movies) {
            this.dao.deleteMovie(movie);
        }
    }
    
    public void testInsert() throws BusinessException {
        final int expectedSize = this.dao.getMovies().size() + 1;
        this.dao.insertMovie(newDummy("Das ist ein dummy movie in Db4oMovieTest (insert)"));
        
        assertEquals(expectedSize, this.dao.getMovies().size());
    }
    
    public void testInsertId() throws BusinessException {
        final Movie insertedMovie1 = this.dao.insertMovie(newDummy("Das ist ein dummy movie in Db4oMovieTest (insertid1)"));
        final long expectedNextId = insertedMovie1.getId() + 1;
        final Movie insertedMovie2 = this.dao.insertMovie(newDummy("Das ist ein dummy movie in Db4oMovieTest (insertid2)"));
        
        assertEquals(expectedNextId, insertedMovie2.getId());
    }
    
//    public void testDelete() throws BusinessException {
//        final Movie insertMovie = Movie.newByScan("junit test", 12, "path");
//        
//    }
    

    private static Movie newDummy(String title) {
        return Movie.create(-1).title(title).year(2008).get();
    }
}
