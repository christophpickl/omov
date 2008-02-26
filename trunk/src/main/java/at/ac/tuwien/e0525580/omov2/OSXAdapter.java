package at.ac.tuwien.e0525580.omov2;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import org.apache.log4j.Logger;

import com.apple.eawt.ApplicationAdapter;
import com.apple.eawt.ApplicationEvent;

public class OSXAdapter extends ApplicationAdapter {


    static Logger log = Logger.getRootLogger();
    
	// pseudo-singleton model; no point in making multiple instances
	// of the EAWT application or our adapter
	private static OSXAdapter						theAdapter;
	private static com.apple.eawt.Application		theApplication;

	// reference to the app where the existing quit, about, prefs code is
	private JFrame mainApp;
	private JMenuItem aboutButton;
	private JMenuItem prefsButton;
	private JMenuItem quitButton;
	
	private OSXAdapter (JFrame inApp) {
		mainApp = inApp;
	
		JMenu helpMenu = mainApp.getJMenuBar().getMenu(4);
		this.aboutButton = helpMenu.getItem(7);
		aboutButton.setVisible(false);
		helpMenu.getItem(0).setVisible(false);
		helpMenu.remove(4);
		helpMenu.add(new JPanel(), 4);
		helpMenu.remove(6);
		helpMenu.add(new JPanel(), 6);
	
		JMenu toolsMenu = mainApp.getJMenuBar().getMenu(2);
		this.prefsButton = toolsMenu.getItem(0);
		prefsButton.setVisible(false);
		toolsMenu.remove(1);
		toolsMenu.add(new JPanel(), 1);
		
		JMenu fileMenu = mainApp.getJMenuBar().getMenu(0);
		this.quitButton = fileMenu.getItem(9);
		quitButton.setVisible(false);
		fileMenu.remove(8);
		fileMenu.add(new JPanel(), 8);
	}

	  // The main entry-point for this functionality.  This is the only method
	  // that needs to be called at runtime, and it can easily be done using
	  // reflection (see MyApp.java) 
	  public static void registerMacOSXApplication(JFrame inApp) {
	    if (theApplication == null) {
	      theApplication = new com.apple.eawt.Application();
	    }      
	    
	    if (theAdapter == null) {
	      theAdapter = new OSXAdapter(inApp);
	    }
	    theApplication.addApplicationListener(theAdapter);
	  }

	// implemented handler methods.  These are basically hooks into existing 
	// functionality from the main app, as if it came over from another platform.
	public void handleAbout(ApplicationEvent ae) {
		if (mainApp != null) {
			aboutButton.doClick();
			ae.setHandled(true);			
		} else {
			throw new IllegalStateException("handleAbout: instance detached from listener");
		}
	}
	
	public void handlePreferences(ApplicationEvent ae) {
		if (mainApp != null) {
			prefsButton.doClick();
			ae.setHandled(true);
		} else {
			throw new IllegalStateException("handlePreferences: instance detached from listener");
		}
	}
	
	// Another static entry point for EAWT functionality.  Enables the 
	// "Preferences..." menu item in the application menu. 
	public static void enablePrefs(boolean enabled) {
	  if (theApplication == null) {
	    theApplication = new com.apple.eawt.Application();
	  }
	  theApplication.setEnabledPreferencesMenu(enabled);
	}
	
	public void handleQuit(ApplicationEvent ae) {
		if (mainApp != null) {
			/*	
			/	You MUST setHandled(false) if you want to delay or cancel the quit.
			/	This is important for cross-platform development -- have a universal quit
			/	routine that chooses whether or not to quit, so the functionality is identical
			/	on all platforms.  This example simply cancels the AppleEvent-based quit and
			/	defers to that universal method.
			*/
			ae.setHandled(false);
			quitButton.doClick();
		} else {
			throw new IllegalStateException("handleQuit: instance detached from listener");
		}
	}
}
