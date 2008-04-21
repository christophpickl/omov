package at.ac.tuwien.e0525580.omov.gui.qtjava;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import quicktime.QTException;
import quicktime.QTSession;

public class SessionInitializer {
	
    private static final Log LOG = LogFactory.getLog(SessionInitializer.class);

	private static boolean isOpened = false;
	
	public static void openSession() throws QTException {
		
		if(isOpened == false) {
			LOG.info("Opening quicktime session...");
			QTSession.open();
			
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
