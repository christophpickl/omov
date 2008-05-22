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

package net.sourceforge.omov.gui;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DirectoryChooser extends AbstractFileDirectoryChooser {

//    private static final Log LOG = LogFactory.getLog(DirectoryChooser.class);
    
    private static final long serialVersionUID = -3831195688364368872L;
    
    

    public DirectoryChooser(String dialogTitle) {
        super(dialogTitle);
    }
    
    public DirectoryChooser(String dialogTitle, ButtonPosition position) {
        super(dialogTitle, position);
    }
    
    public DirectoryChooser(String dialogTitle, File defaultPath, ButtonPosition position) {
        super(dialogTitle, defaultPath, position);
    }
    
    public DirectoryChooser(String dialogTitle, File defaultPath, ButtonPosition position, String buttonLabel) {
        super(dialogTitle, defaultPath, position, buttonLabel);
    }


    /**
     * @return can be null
     */
    public File getSelectedDirectory() {
        return this.getFileOrDir();
    }
    
    @Override
    int getSelectionMode() {
        return JFileChooser.DIRECTORIES_ONLY;
    }

    @Override
    FileFilter getFileFilter() {
        return null;
    }

    @Override
    boolean getIsRightFileOrDir(final File file) {
        return file.isDirectory();
    }
    
    public final void setDirectory(final File directory) {
        this.setFileOrDir(directory);
    }
}
