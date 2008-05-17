package net.sourceforge.omov.qtjImpl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import quicktime.QTException;
import quicktime.QTSession;


public class QtjSessionInitializer {
	
    private static final Log LOG = LogFactory.getLog(QtjSessionInitializer.class);

	private static boolean isOpened = false;
	
	public static void openSession() throws QTException {
		
		if(isOpened == false) {
			LOG.info("Opening quicktime session...");
			QTSession.open();
			LOG.info("QT version: " + QTSession.getMajorVersion() + "." + QTSession.getMinorVersion());
			// TODO check for qt version > 7 ?
			
			Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
				public void run() {
					LOG.info("Closing QuickTime session...");
					try {
						QTSession.close();
					} catch(Exception e) {
						LOG.error("Could not close QuickTime session!", e);
	                    e.printStackTrace();
					}
				}
			}));
			
			isOpened = true;
		}
	}
	
}
