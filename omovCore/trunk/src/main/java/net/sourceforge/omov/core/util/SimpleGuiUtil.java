package net.sourceforge.omov.core.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JOptionPane;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class SimpleGuiUtil {

    private static final Log LOG = LogFactory.getLog(SimpleGuiUtil.class);
    
    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    
    
    protected SimpleGuiUtil() {
        // no instantiation
    }

    public static void setCenterLocation(final Component component) {
    	SimpleGuiUtil.setCenterLocation(component, 0, 0);
    }

    /**
     * sets the position of given component to the center of the screen
     *
     * @param component
     *            which should be placed in the center of screen
     */
    public static void setCenterLocation(final Component component, int xOffset, int yOffset) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (screenSize.width - component.getWidth()) / 2;
        int y = (screenSize.height - component.getHeight()) / 2;

        component.setLocation(x + xOffset, y + yOffset);
    }

    public static void enableHandCursor(final Component component) {
        component.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent event) {
                component.setCursor(SimpleGuiUtil.HAND_CURSOR);
            }
            public void mouseExited(MouseEvent event) {
                component.setCursor(SimpleGuiUtil.DEFAULT_CURSOR);
            }
            public void mouseClicked(MouseEvent event) { /* nothing to do */ }
            public void mousePressed(MouseEvent event) { /* nothing to do */ }
            public void mouseReleased(MouseEvent event) { /* nothing to do */ }
        });
    }


    public static void info(String title, String message) {
        info(null, title, message);
    }
    public static void info(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }

    public static void warning(String title, String message) {
        warning(null, title, message);
    }
    public static void warning(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void error(String title, String message) {
        error(null, title, message);
    }
    public static void error(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    
    


    /**
     * should be used if exceptions was thrown, which forces an application shutdown.
     * use it to surround user invoked methods (within actionPerformed & co).
     */
    static void handleFatalException(Throwable e) {
        e.printStackTrace();
        LOG.error("Application error! Shutdown...", e);
        SimpleGuiUtil.error("Fatal Application Error", "Whups, the application crashed. Sorry for that dude :)\n" +
                                                 "The evil source is a "+e.getClass().getSimpleName()+".");
        // MANTIS [25] gui: use swingx panel + collapsable details containing stack trace
        System.exit(1);
    }

}
