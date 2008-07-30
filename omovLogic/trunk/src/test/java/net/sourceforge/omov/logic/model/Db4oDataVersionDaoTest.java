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

import net.sourceforge.omov.core.imodel.IDataVersionDao;
import net.sourceforge.omov.logic.AbstractTestCase;
import net.sourceforge.omov.logic.model.db4o.Db4oConnection;
import net.sourceforge.omov.logic.model.db4o.Db4oDataVersionDao;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Db4oDataVersionDaoTest extends AbstractTestCase {

    private static final IDataVersionDao DAO = new Db4oDataVersionDao(new Db4oConnection("Db4oDataVersionDaoTest.yap"));

//    protected static final ObjectContainer objectContainer = Db4oUtil.getDbConnection("Db4oDataVersionDaoTest.yap");


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
