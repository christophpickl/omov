package at.ac.tuwien.e0525580.omov2.model.hsqldb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.BeanFactory;
import at.ac.tuwien.e0525580.omov2.BusinessException;
import at.ac.tuwien.e0525580.omov2.FatalException;
import at.ac.tuwien.e0525580.omov2.model.IDao;

abstract class AbstractHsqldbDao implements IDao {

    private static final Log LOG = LogFactory.getLog(AbstractHsqldbDao.class);
    
    private final HsqldbConnection connection;
    
    private final PreparedStatement insertedIdStmt;
    
    
    
    public AbstractHsqldbDao(HsqldbConnection connection) {
        this.connection = connection;

        try {
            this.insertedIdStmt = this.prepareStatement(BeanFactory.getInstance().getSql("LAST_INSERT_ID"));
        } catch (Exception e) {
            throw new FatalException("Could not prepare statement!", e);
        }
    }
    
    final String getSql(String beanName) {
        return BeanFactory.getInstance().getSql(beanName);
    }
    
    abstract void notifyListeners();
    
    final PreparedStatement prepareStatement(String sql) throws SQLException {
        return this.connection.prepareStatement(sql);
    }
    
    final Statement createStatement() throws SQLException {
        return this.connection.createStatement();
    }

    
    final void createTableIfNecessary(final String tableName, final String sqlBeanName) throws SQLException {
        LOG.debug("Trying to create table '"+tableName+"' (bean = "+sqlBeanName+").");
        
        final boolean tableExisting = this.isTableExisting(tableName);
        LOG.debug("Table is existing: " + tableExisting);
        
        if(tableExisting == false) {
            final Statement stmt = createStatement();
            stmt.executeUpdate(BeanFactory.getInstance().getSql(sqlBeanName));
            LOG.info("Successfully created table "+tableName+".");
        }
    }
    
    private boolean isTableExisting(final String tableName) {
        try {
            Statement stmt = createStatement();
            stmt.executeQuery("SELECT * FROM " + tableName);
            return true;
        } catch (SQLException e) {
            LOG.debug("Seems as table already exists: " + e.getMessage());
            return false;
        }
    }

    public final void commit() throws BusinessException {
        LOG.debug("invoked commit");
        try {
            this.connection.commit();
            
            this.notifyListeners();
        } catch (SQLException e) {
            throw new BusinessException("Could not commit!", e);
        }
    }

    public final void rollback() {
        try {
            this.connection.rollback();
        } catch (SQLException e) {
            LOG.error("Could not rollback!", e);
        }
    }
    
    public final void setAutoCommit(boolean autoCommit) {
        try {
            this.connection.setAutoCommit(autoCommit);
        } catch (SQLException e) {
            throw new FatalException("Could not set autocommit to " + autoCommit, e);
        }
    }
    
    final boolean isAutoCommit() throws BusinessException {
        try {
            return this.connection.isAutoCommit();
        } catch (SQLException e) {
            throw new BusinessException("Could not get autocommit state!", e);
        }
    }
    
    
    

    
    final int getLastInsertedMovieId() throws BusinessException {
        try {
            ResultSet s = this.insertedIdStmt.executeQuery();
            s.next();
            return s.getInt(1);
        } catch (SQLException e) {
            throw new BusinessException("Could not get last inserted movie id!", e);
        }
    }
}
