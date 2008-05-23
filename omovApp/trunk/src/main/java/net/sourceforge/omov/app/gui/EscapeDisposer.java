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

package net.sourceforge.omov.app.gui;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JTable;

import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.core.util.SimpleGuiUtil.GlobalKey;
import net.sourceforge.omov.core.util.SimpleGuiUtil.IGlobalKeyListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public final class EscapeDisposer {
	
	private static final Log LOG = LogFactory.getLog(EscapeDisposer.class);
	
	
	private EscapeDisposer() {
		// no instantiation
	}
	
	public static void enableEscapeOnDialogWithoutFocusableComponents(JDialog dialog, final IEscapeDisposeReceiver receiver) {
		LOG.debug("Enabling dialog-without-focusable-components escape notification on receiver with class '"+receiver.getClass().getName()+"'.");
    	
		dialog.addKeyListener(new KeyAdapter() {
    		public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					LOG.debug("Broadcasting dialog-without-focusable-components escape event to listener with class '"+receiver.getClass().getName()+"'.");
					receiver.doEscape();
				}
			}
    	});
	}
    
    public static void enableEscape(JTable table, final IEscapeDisposeReceiver receiver) {
    	LOG.debug("Enabling table escape notification on receiver with class '"+receiver.getClass().getName()+"'.");
    	
    	table.addKeyListener(new KeyAdapter() {
    		public void keyPressed(KeyEvent e) {
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
					LOG.debug("Broadcasting table escape event to listener with class '"+receiver.getClass().getName()+"'.");
					receiver.doEscape();
				}
			}
    	});
    	
    }
	//  JComponent JRootPane
    public static void enableEscape(JComponent rootPane, final IEscapeDisposeReceiver receiver) {
    	LOG.debug("Enabling rootpane escape notification on receiver with class '"+receiver.getClass().getName()+"'.");
    	
		SimpleGuiUtil.addGlobalKeyListener(rootPane, new IGlobalKeyListener() {
			public void doKeyPressed(GlobalKey key) {
				if(key == GlobalKey.ESCAPE) {
					LOG.debug("Broadcasting rootpane escape event to listener with class '"+receiver.getClass().getName()+"'.");
					receiver.doEscape();
				}
			}
		});
    }
    
    public static interface IEscapeDisposeReceiver {
        void doEscape();
    }
}

