package net.sourceforge.omov.core.model;

import java.util.Set;

import junit.framework.TestCase;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;

public class Db4oMovieTest extends TestCase {
    
    private final IMovieDao dao;
    
    public Db4oMovieTest() {
        this.dao = BeanFactory.getInstance().getMovieDao();
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        Set<Movie> movies = this.dao.getMovies();
        for (Movie movie : movies) {
            this.dao.deleteMovie(movie);
        }
    }
    
    public void testInsert() throws BusinessException {
        final int expectedSize = this.dao.getMovies().size() + 1;
        this.dao.insertMovie(Movie.getDummy("Das ist ein dummy movie in Db4oMovieTest (insert)"));
        
        assertEquals(expectedSize, this.dao.getMovies().size());
    }
    
    public void testInsertId() throws BusinessException {
        final Movie insertedMovie1 = this.dao.insertMovie(Movie.getDummy("Das ist ein dummy movie in Db4oMovieTest (insertid1)"));
        final long expectedNextId = insertedMovie1.getId() + 1;
        final Movie insertedMovie2 = this.dao.insertMovie(Movie.getDummy("Das ist ein dummy movie in Db4oMovieTest (insertid2)"));
        
        assertEquals(expectedNextId, insertedMovie2.getId());
    }
    
//    public void testDelete() throws BusinessException {
//        final Movie insertMovie = Movie.newByScan("junit test", 12, "path");
//        
//    }
}