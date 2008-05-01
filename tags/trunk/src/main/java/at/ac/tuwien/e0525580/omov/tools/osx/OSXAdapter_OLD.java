package at.ac.tuwien.e0525580.omov.tools.osx;


public class OSXAdapter_OLD { // extends ApplicationAdapter {

//    static Logger log = Logger.getRootLogger();
//
//    // pseudo-singleton model; no point in making multiple instances
//    // of the EAWT application or our adapter
//    private static OSXAdapter_OLD adapter;
//
//    private static Application application;
//
//    // reference to the app where the existing quit, about, prefs code is
//    private final JFrame mainApp;
//
//    private final JMenuItem aboutButton;
//
//    private final JMenuItem prefsButton;
//
//    private final JMenuItem quitButton;
//
//    private OSXAdapter_OLD(MainWindow mainWindow) {
//        this.mainApp = mainWindow;
//
////        final MenuBar menuBar = mainWindow.getOmovMenuBar();
//        
//        this.aboutButton = this.prefsButton = this.quitButton = null;
////        this.aboutButton = menuBar.getAboutItem();
////        this.aboutButton.setVisible(false);
////        
////        this.prefsButton = menuBar.getPreferencesItem();
////        this.prefsButton.setVisible(false);
////        
////        this.quitButton = menuBar.getQuitItem();
////        this.quitButton.setVisible(false);
//    }
//
//    /**
//     * The main entry-point for this functionality. This is the only method that
//     * needs to be called at runtime.
//     */
//    public static void registerMacOSXApplication(MainWindow mainWindow) {
//        if (OSXAdapter_OLD.application == null) {
//            OSXAdapter_OLD.application = new com.apple.eawt.Application();
//        }
//
//        if (OSXAdapter_OLD.adapter == null) {
//            OSXAdapter_OLD.adapter = new OSXAdapter_OLD(mainWindow);
//        }
//        OSXAdapter_OLD.application.addApplicationListener(OSXAdapter_OLD.adapter);
//    }
//
//    /**
//     * Implemented handler methods. These are basically hooks into existing
//     * functionality from the main app, as if it came over from another
//     * platform.
//     */
//    public void handleAbout(ApplicationEvent event) {
//        if (this.mainApp != null) {
//            this.aboutButton.doClick();
//            event.setHandled(true);
//        } else {
//            throw new IllegalStateException("handleAbout: instance detached from listener");
//        }
//    }
//
//    public void handlePreferences(ApplicationEvent event) {
//        prefsButton.doClick();
//        event.setHandled(true);
//    }
//
//    /**
//     * Another static entry point for EAWT functionality. Enables the
//     * "Preferences..." menu item in the application menu.
//     */
//    public static void enablePrefs(boolean enabled) {
//        if (OSXAdapter_OLD.application == null) {
//            OSXAdapter_OLD.application = new Application();
//        }
//        OSXAdapter_OLD.application.setEnabledPreferencesMenu(enabled);
//    }
//
//    public void handleQuit(ApplicationEvent event) {
//        /*
//         * You MUST setHandled(false) if you want to delay or cancel the quit.
//         * This is important for cross-platform development -- have a universal
//         * quit routine that chooses whether or not to quit, so the
//         * functionality is identical on all platforms. This example simply
//         * cancels the AppleEvent-based quit and defers to that universal
//         * method.
//         */
//        event.setHandled(false);
//        this.quitButton.doClick();
//    }
}
