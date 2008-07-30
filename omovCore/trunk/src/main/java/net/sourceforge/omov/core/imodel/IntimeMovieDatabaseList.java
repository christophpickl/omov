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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;

import org.apache.log4j.Logger;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class IntimeMovieDatabaseList<T> implements IMovieDaoListener {

    private static final Logger LOG = Logger.getLogger(IntimeMovieDatabaseList.class);

    protected static final IMovieDao MOVIE_DAO = BeanFactory.getInstance().getMovieDao();
    
    private final Set<IIntimeDatabaseListener> listeners = new HashSet<IIntimeDatabaseListener>();
    
    private List<T> values;
    

    public IntimeMovieDatabaseList() {
        MOVIE_DAO.addMovieDaoListener(this);
    }
    
    public void unregisterFromMovieDao() {
        MOVIE_DAO.unregisterMovieDaoListener(this);
    }
    
    protected abstract List<T> reloadValues() throws BusinessException;

    public final void movieDataChanged() {
        try {
            LOG.debug("Reloading suggestions for "+this.getClass()+".");
            this.values = this.reloadValues();
            for (IIntimeDatabaseListener listener : this.listeners) {
                listener.valuesChanged(); // broadcast
            }
        } catch (BusinessException e) {
            throw new FatalException("Could not reload data for intime database listener!", e);
        }
    }
    
    public final void registerListener(IIntimeDatabaseListener listener) {
        this.listeners.add(listener);
    }
    
    public final void unregisterListener(IIntimeDatabaseListener listener) {
        this.listeners.remove(listener);
    }
    
    public final List<T> getValues() {
        if(this.values == null) {
            try {
                this.values = this.reloadValues();
            } catch (BusinessException e) {
                throw new FatalException("Could not reload values for intime database listener!", e);
            }
        }
        return this.values;
    }
    
    public static interface IIntimeDatabaseListener {
        void valuesChanged();
    }
}
