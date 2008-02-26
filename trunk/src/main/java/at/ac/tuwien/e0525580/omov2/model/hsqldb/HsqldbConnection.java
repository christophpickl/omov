package at.ac.tuwien.e0525580.omov2.model.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.FatalException;
import at.ac.tuwien.e0525580.omov2.model.IDatabaseConnection;

public class HsqldbConnection implements IDatabaseConnection {

    private static final Log LOG = LogFactory.getLog(HsqldbMovieDao.class);
    
    private Connection connection;
    
    private boolean connected = false;

    public HsqldbConnection() {
        try {
            Class.forName("org.hsqldb.jdbcDriver");
            final String dbFilePath = "db/hsqldb_data";
            this.connection = DriverManager.getConnection("jdbc:hsqldb:" + dbFilePath, "sa", "");
            this.setAutoCommit(true);

            this.connected = true;
        } catch (Exception e) {
            throw new FatalException("Could not initial hsqldb connection!");
        }
    }
    
    public PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }

    public Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }
    
    public boolean isAutoCommit() throws SQLException {
        return this.connection.getAutoCommit();
    }
    
    public void setAutoCommit(boolean autoCommit) throws SQLException {
        LOG.debug("Setting autocommit to " + autoCommit);
        this.connection.setAutoCommit(autoCommit);
    }

    public void commit() throws SQLException {
        this.connection.commit();
    }

    public void rollback() throws SQLException {
        this.connection.rollback();
    }

    public void close() throws BusinessException {
        LOG.info("closing db connection.");
        assert(isConnected() == true);
        this.connected = false;
        
        try {
            this.createStatement().executeUpdate("SHUTDOWN");
            this.connection.close();
        } catch (SQLException e) {
            throw new BusinessException("Could not close connection!", e);
        }
    }

    public boolean isConnected() {
        return this.connected;
    }
}
