package at.ac.tuwien.e0525580.omov.model.db4o;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.BusinessException;
import at.ac.tuwien.e0525580.omov.PreferencesDao;
import at.ac.tuwien.e0525580.omov.model.IDatabaseConnection;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

// http://developer.db4o.com/resources/api/db4o-java/
public class Db4oConnection implements IDatabaseConnection {

    private static final Log LOG = LogFactory.getLog(Db4oConnection.class);
    private ObjectContainer connection;
    
    private boolean autoCommit = true;
    
    public boolean isAutoCommit() {
        return this.autoCommit;
    }

    public void setAutoCommit(boolean autoCommit) {
        this.autoCommit = autoCommit;
    }

    public Db4oConnection(String dbFileName) {
        final boolean isRunningJunitTest = System.getProperty("omovTestRunning") != null;
        
        if(isRunningJunitTest == false) {
            dbFileName = new File(PreferencesDao.getInstance().getDataFolder(), dbFileName).getAbsolutePath();
        }
        
        LOG.info("Opening database file '"+dbFileName+"'.");
        this.connection = Db4o.openFile(dbFileName);
    }

    public void close() throws BusinessException {
        LOG.info("Closing connection.");
        try {
            if(this.connection.close() == false) {
                throw new BusinessException("Could not close DB connection!");
            }
        } finally {
            this.connection = null;
        }
    }
    
    public ObjectContainer getObjectContainer() {
        return this.connection;
    }

    public boolean isConnected() {
        return this.connection != null;
    }
}
