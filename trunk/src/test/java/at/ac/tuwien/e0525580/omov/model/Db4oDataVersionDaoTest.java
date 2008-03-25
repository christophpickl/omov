package at.ac.tuwien.e0525580.omov.model;

import at.ac.tuwien.e0525580.omov.AbstractTestCase;
import at.ac.tuwien.e0525580.omov.model.db4o.Db4oConnection;
import at.ac.tuwien.e0525580.omov.model.db4o.Db4oDataVersionDao;

public class Db4oDataVersionDaoTest extends AbstractTestCase {

    private static final IDataVersionDao DAO = new Db4oDataVersionDao(new Db4oConnection("Db4oDataVersionDaoTest.db4"));
    
//    protected static final ObjectContainer objectContainer = Db4oUtil.getDbConnection("Db4oDataVersionDaoTest.db4");
    
    public Db4oDataVersionDaoTest() {
        
    }
    
    public void test1() {
        final int expectedVersion = 1;
        DAO.storeDataVersion(expectedVersion);
        assertEquals(expectedVersion, DAO.getDataVersion());
    }
    
    public void test2() {
        final int expectedVersion = 2;
        DAO.storeDataVersion(expectedVersion);
        assertEquals(expectedVersion, DAO.getDataVersion());
    }
    
    public void test3() {
        final int expectedVersion = 3;
        DAO.storeDataVersion(expectedVersion);
        DAO.storeDataVersion(expectedVersion);
        DAO.storeDataVersion(expectedVersion);
        assertEquals(expectedVersion, DAO.getDataVersion());
        assertEquals(expectedVersion, DAO.getDataVersion());
        assertEquals(expectedVersion, DAO.getDataVersion());
    }
}
