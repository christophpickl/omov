package net.sourceforge.omov.app.model;

import net.sourceforge.omov.app.AbstractTestCase;
import net.sourceforge.omov.core.model.IDataVersionDao;
import net.sourceforge.omov.core.model.db4o.Db4oConnection;
import net.sourceforge.omov.core.model.db4o.Db4oDataVersionDao;

public class Db4oDataVersionDaoTest extends AbstractTestCase {

    private static final IDataVersionDao DAO = new Db4oDataVersionDao(new Db4oConnection("Db4oDataVersionDaoTest.db4"));

//    protected static final ObjectContainer objectContainer = Db4oUtil.getDbConnection("Db4oDataVersionDaoTest.db4");


    public void testInitialStore() {
        final int expectedMovieVersion = 1;
        final int expectedSmartFolderVersion = 1;

        DAO.storeDataVersions(expectedMovieVersion, expectedSmartFolderVersion);

        assertEquals(expectedMovieVersion, DAO.getMovieDataVersion());
        assertEquals(expectedSmartFolderVersion, DAO.getSmartfolderDataVersion());
    }

    public void testMultipleStoring() {
        final int expectedVersion = 3;

        DAO.storeDataVersions(expectedVersion, expectedVersion);
        DAO.storeDataVersions(expectedVersion, expectedVersion);
        DAO.storeDataVersions(expectedVersion, expectedVersion);

        assertEquals(expectedVersion, DAO.getMovieDataVersion());
        assertEquals(expectedVersion, DAO.getSmartfolderDataVersion());
        assertEquals(expectedVersion, DAO.getMovieDataVersion());
        assertEquals(expectedVersion, DAO.getSmartfolderDataVersion());
        assertEquals(expectedVersion, DAO.getMovieDataVersion());
        assertEquals(expectedVersion, DAO.getSmartfolderDataVersion());
    }
}
