package at.ac.tuwien.e0525580.omov;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;

public class Db4oUtil {

    private static final Log LOG = LogFactory.getLog(Db4oUtil.class);
    
    public static ObjectContainer getDbConnection(final String dbFileName) {
        final File existingDbFile = new File(dbFileName);
        
        if(existingDbFile.exists()) {
            LOG.debug("Deleting existing DB file at '"+existingDbFile.getAbsolutePath()+"'.");
            if(existingDbFile.delete() == false) {
                throw new RuntimeException("Could not delete DB file '"+existingDbFile.getAbsolutePath()+"'!");
            }
        }
        
        LOG.info("Opening connection to file '"+dbFileName+"'.");
        return Db4o.openFile(dbFileName);
    }
}
