package net.sourceforge.omov.core.model;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;

import org.apache.log4j.Logger;


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
