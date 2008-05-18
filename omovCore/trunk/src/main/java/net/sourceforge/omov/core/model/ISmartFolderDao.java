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

package net.sourceforge.omov.core.model;

import java.util.List;

import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.smartfolder.SmartFolder;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public interface ISmartFolderDao extends IDao {

    List<SmartFolder> getAllSmartFolders() throws BusinessException;
    List<SmartFolder> getAllSmartFoldersSorted() throws BusinessException;
    
    SmartFolder insertSmartFolder(SmartFolder smartFolder) throws BusinessException;
    void updateSmartFolder(SmartFolder smartFolder) throws BusinessException;
    void deleteSmartFolder(SmartFolder smartFolder) throws BusinessException;

    void registerMovieDaoListener(ISmartFolderDaoListener listener);
    void unregisterMovieDaoListener(ISmartFolderDaoListener listener);
}
