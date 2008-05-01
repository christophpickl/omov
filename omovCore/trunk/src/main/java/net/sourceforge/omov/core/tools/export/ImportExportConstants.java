package net.sourceforge.omov.core.tools.export;

import java.io.File;

import javax.swing.filechooser.FileFilter;

import net.sourceforge.omov.core.util.FileUtil;

public interface ImportExportConstants {

    public static final String BACKUP_FILE_EXTENSION = "omo";
    
    public static final FileFilter BACKUP_FILE_FILTER = new FileFilter() {
        @Override
        public boolean accept(final File file) {
            if(file.isDirectory() == true) return true;
            
            final String extension = FileUtil.extractExtension(file);
            if(extension == null) return false;
            
            return (extension.equalsIgnoreCase(ImportExportConstants.BACKUP_FILE_EXTENSION));
        }
        @Override
        public String getDescription() {
            return "*." + ImportExportConstants.BACKUP_FILE_EXTENSION;
        }
    };
    
    static final String FILE_DATA_VERSION = "movie_data.version";
    
    static final String FILE_MOVIES_XML = "movies.xml";
    
    static final String FOLDER_COVERS = "covers";
    
}
