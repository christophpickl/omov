package net.sourceforge.omov.core.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public final class FatalExceptionHandler {

    private static final Log LOG = LogFactory.getLog(FatalExceptionHandler.class);
    
	private FatalExceptionHandler() {
		// no instantiation
	}
	
    /**
     * should be used if exceptions was thrown, which forces an application shutdown.
     * use it to surround user invoked methods (within actionPerformed & co).
     */
    public static void handle(Exception exception) {
        exception.printStackTrace();
        LOG.error("Handling fatal exception", exception);
        OmovGuiUtil.error("Fatal Application Error", "Whups, the application crashed. Sorry for that dude :)<br>" +
                                                 "The evil source is a "+exception.getClass().getSimpleName()+".", exception);
        
        LOG.info("System.exit(1);");
        System.exit(1);
        
    }
    
}
