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

package net.sourceforge.omov.core.util;

import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import net.sourceforge.omov.gui.dialogs.ErrorDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
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
    	ErrorDialog.newDialog(title, message).setVisible(true);
    }
    public static void error(String title, String message, Exception exception) {
    	ErrorDialog.newDialog(title, message, exception).setVisible(true);
    }
    public static void error(JDialog owner, String title, String message) {
//      JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    	ErrorDialog.newDialog(owner, title, message).setVisible(true);
    }
    public static void error(JDialog owner, String title, String message, Exception exception) {
    	ErrorDialog.newDialog(owner, title, message, exception).setVisible(true);
    }
    public static void error(JFrame owner, String title, String message) {
    	ErrorDialog.newDialog(owner, title, message).setVisible(true);
    }
    public static void error(JFrame owner, String title, String message, Exception exception) {
    	ErrorDialog.newDialog(owner, title, message, exception).setVisible(true);
    }
    
    


    /**
     * should be used if exceptions was thrown, which forces an application shutdown.
     * use it to surround user invoked methods (within actionPerformed & co).
     */
    public static void handleFatalException(Exception exception) {
        exception.printStackTrace();
        LOG.error("Application error! Shutdown...", exception);
        SimpleGuiUtil.error("Fatal Application Error", "Whups, the application crashed. Sorry for that dude :)<br>" +
                                                 "The evil source is a "+exception.getClass().getSimpleName()+".", exception);
        // MANTIS [25] gui: use swingx panel + collapsable details containing stack trace
        LOG.debug("System.exit(1);");
        System.exit(1);
        
    }

    
    public static void addGlobalKeyListener(final JComponent contentPanel, final IGlobalKeyListener listener) {
    	addGlobalKeyListenerDetails(contentPanel, listener, GlobalKey.ESCAPE);
    	addGlobalKeyListenerDetails(contentPanel, listener, GlobalKey.SPACE);
    }
    
    // WHEN_ANCESTOR_OF_FOCUSED_COMPONENT
    private static void addGlobalKeyListenerDetails(final JComponent contentPanel, final IGlobalKeyListener listener, final GlobalKey key) {
    	contentPanel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(key.getKeyStroke(), key.getActionCommand());
		contentPanel.getActionMap().put(key.getActionCommand(), new AbstractAction() {
			private static final long serialVersionUID = -266823267636545239L;
			public void actionPerformed(ActionEvent event) {
				listener.doKeyPressed(key);
		    }
		});
    }
    
    
    
    public enum GlobalKey {
    	ESCAPE("CMD_GLOBAL_KEY_ESCAPE", KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)),
    	SPACE("CMD_GLOBAL_KEY_SPACE", KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0));
    	
    	private final String actionCommand;
    	private final KeyStroke keyStroke;
    	private GlobalKey(String actionCommand, KeyStroke keyStroke) {
    		this.actionCommand = actionCommand;
    		this.keyStroke = keyStroke;
    	}
    	public String getActionCommand() {
    		return this.actionCommand;
    	}
    	public KeyStroke getKeyStroke() {
    		return this.keyStroke;
    	}
    }
    
    public static interface IGlobalKeyListener {
    	void doKeyPressed(GlobalKey key);
    }

    public static String convertExceptionToString(Exception e) {
    	return convertExceptionToString(e, false);
    }
    public static String convertExceptionToString(Exception e, boolean withCause) {
    	final StringBuffer sb = new StringBuffer();
    	
    	sb.append(convertSingleException(e));
    	
    	if(withCause == true) {
    		Throwable cause = e;
    		while( (cause = cause.getCause()) != null) {
    			sb.append("Caused by:");
    			sb.append(convertSingleException(cause));
    		}
    	}
    	
    	return sb.toString();
    }
    
    private static String convertSingleException(Throwable t) {
    	final StringWriter sw = new StringWriter();
    	final PrintWriter pw = new PrintWriter(sw);
    	t.printStackTrace(pw);
    	return sw.toString();
    }
    

    public static JMenuItem newMenuItem(String label, String actionCmd, List<JMenuItem> list) {
		final JMenuItem item = new JMenuItem(label);
		item.setActionCommand(actionCmd);
		list.add(item);
		return item;
    }
}
