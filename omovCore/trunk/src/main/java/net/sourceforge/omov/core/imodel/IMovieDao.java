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

package net.sourceforge.omov.core.imodel;

import java.util.List;
import java.util.Set;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public interface IMovieDao extends IDao {
    
    Movie getMovie(long id) throws BusinessException;
    Set<Movie> getMovies() throws BusinessException;
    List<Movie> getMoviesSorted() throws BusinessException;

    List<String> getMovieTitles() throws BusinessException; // distinct
    List<String> getMovieGenres() throws BusinessException; // distinct
    List<String> getMovieStyles() throws BusinessException; // distinct
    List<String> getMovieLanguages() throws BusinessException; // distinct
    List<String> getMovieSubtitles() throws BusinessException; // distinct
    List<String> getMovieDirectors() throws BusinessException; // distinct
    List<String> getMovieActors() throws BusinessException; // distinct
    List<String> getMovieFolderPaths() throws BusinessException; // distinct
    
    
    List<Movie> getMoviesBySmartFolder(SmartFolder smartFolder) throws BusinessException;

    Movie insertMovie(Movie insertMovie) throws BusinessException;
    List<Movie> insertMovies(List<Movie> insertMovies) throws BusinessException;
    void updateMovie(Movie movie) throws BusinessException;
    void deleteMovie(Movie movie) throws BusinessException;
    
    

    // happens within insertMovie/updateMovie
//    void insertLanguage(String language) throws BusinessException;
//    void insertMovie2Language(String language) throws BusinessException;
//    ... and so forth ...
    
    
    
    
    void addMovieDaoListener(IMovieDaoListener listener);
    void unregisterMovieDaoListener(IMovieDaoListener listener);
    
    
//    void setAutocommit(boolean autoCommit) throws BusinessException;
//    void commit() throws BusinessException;
//    void rollback();
//    
//    void close() throws BusinessException;
//    boolean isConnected();
}
