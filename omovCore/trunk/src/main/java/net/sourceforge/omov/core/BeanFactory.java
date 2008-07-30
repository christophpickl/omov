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

package net.sourceforge.omov.core;

import net.sourceforge.omov.core.imodel.IDataVersionDao;
import net.sourceforge.omov.core.imodel.IDatabaseConnection;
import net.sourceforge.omov.core.imodel.IMovieDao;
import net.sourceforge.omov.core.imodel.ISmartFolderDao;

import org.springframework.beans.factory.xml.XmlBeanFactory;
import org.springframework.core.io.ClassPathResource;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class BeanFactory {

//    private static final Log LOG = LogFactory.getLog(BeanFactory.class);
    
    private final String springBeans = "beans.xml";
    private static final BeanFactory instance = new BeanFactory();
    private XmlBeanFactory xbf;
    
    private BeanFactory() {
        this.xbf = new XmlBeanFactory(new ClassPathResource(this.springBeans));
    }
    
    public static BeanFactory getInstance() {
        return instance;
    }

    private Object getBean(final String bean) {
        return this.xbf.getBean(bean);
    }

    
    public IDatabaseConnection getDatabaseConnection() {
        return (IDatabaseConnection) getBean("IDatabaseConnection");
    }
    
    public IMovieDao getMovieDao() {
        return (IMovieDao) getBean("IMovieDao");
    }
    
    public ISmartFolderDao getSmartFolderDao() {
        return (ISmartFolderDao) getBean("ISmartFolderDao");
    }
    
    public IDataVersionDao getDataVersionDao() {
        return (IDataVersionDao) getBean("IDataVersionDao");
    }
    
    public VersionMajorMinor getCurrentApplicationVersion() {
        return (VersionMajorMinor) getBean("CurrentApplicationVersion");
    }
    
    @SuppressWarnings("unused")
    public String getSql(String key) {
//        LOG.debug("Getting bean with key '"+key+"'.");
//        return getBean(key).toString();
        throw new UnsupportedOperationException("OR-mapping sucks! use native OO-db instead!");
    }
}
