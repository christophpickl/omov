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

package net.sourceforge.omov.app.gui.comp.generic;

import java.io.File;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.omov.core.util.CollectionUtil;
import net.sourceforge.omov.core.util.FileUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class FileChooser extends AbstractFileDirectoryChooser {

//    private static final Log LOG = LogFactory.getLog(DirectoryChooser.class);
    
    private static final long serialVersionUID = 4870536517103469362L;

    private final FileFilter fileFilter;
    
    
    /** simplest constructor */
    public FileChooser(String dialogTitle, String... validExtensions) {
        this(dialogTitle, AbstractFileDirectoryChooser.DEFAULT_BUTTON_LABEL, null, AbstractFileDirectoryChooser.DEFAULT_BUTTON_POSITION, validExtensions);
    }
    
    /** adds button position */
    public FileChooser(String dialogTitle, ButtonPosition position, String... validExtensions) {
        this(dialogTitle, AbstractFileDirectoryChooser.DEFAULT_BUTTON_LABEL, null, position, validExtensions);
    }
    
    /** adds default path */
    public FileChooser(String dialogTitle, File defaultPath, ButtonPosition position, String... validExtensions) {
        this(dialogTitle, AbstractFileDirectoryChooser.DEFAULT_BUTTON_LABEL, defaultPath, position, validExtensions);
    }
    
    /** finally: adds button label*/
    public FileChooser(String dialogTitle, String buttonLabel, File defaultPath, ButtonPosition position, String... validExtensions) {
        super(dialogTitle, defaultPath, position, buttonLabel);
        
        final Set<String> validExtensionsSet = new LinkedHashSet<String>(Arrays.asList(validExtensions));
        final String validExtensionsString = CollectionUtil.toString(validExtensionsSet);
        
        this.fileFilter = new FileFilter() {
            @Override
            public boolean accept(File file) {
                if(file.isDirectory() == true) return true;
                
                final String extension = FileUtil.extractExtension(file);
                if(extension == null) {
                    return false;
                }
                
                return validExtensionsSet.contains(extension);
            }
            @Override
            public String getDescription() {
                return validExtensionsString;
            }
        };
    }

    /**
     * @return can be null
     */
    public File getSelectedFile() {
        return this.getFileOrDir();
    }

    @Override
    int getSelectionMode() {
        return JFileChooser.FILES_ONLY;
    }

    @Override
    FileFilter getFileFilter() {
        return this.fileFilter;
    }

    @Override
    boolean getIsRightFileOrDir(final File file) {
        return file.isFile();
    }
    
    public final void setFile(final File file) {
        this.setFileOrDir(file);
    }
}
