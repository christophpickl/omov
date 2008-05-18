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

package net.sourceforge.omov.core.model.db4o;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.ObjectContainer;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class AbstractDb4oDao {

    private static final Log LOG = LogFactory.getLog(AbstractDb4oDao.class);
    
    final ObjectContainer objectContainer;
    
    private final Db4oConnection connection;
    
    
    public AbstractDb4oDao(Db4oConnection connection) {
        this.connection = connection;
        this.objectContainer = connection.getObjectContainer();
    }

    public void commit() {
        LOG.debug("Transaction commit.");
        this.objectContainer.commit();
    }

    public void rollback() {
        LOG.debug("Transaction rollback.");
        this.objectContainer.rollback();
    }
    
    public boolean isAutoCommit() {
        return this.connection.isAutoCommit();
    }
    
    public void setAutoCommit(boolean autoCommit) {
        LOG.debug("Setting auto commit to "+autoCommit+".");
        this.connection.setAutoCommit(autoCommit);
    }

    
//    static final class StringWrapperTransformer<T extends StringWrapper> {
//        public List<String> transformList(ObjectSet<T> os) {
//            final List<String> result = new LinkedList<String>();
//            while(os.hasNext()) {
//                result.add(os.next().getString());
//            }
//            return Collections.unmodifiableList(result);
//        }
//    }
//
//    
//    static interface StringWrapper {
//        String getString();
//    }
}
