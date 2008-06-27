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

package net.sourceforge.omov.app.gui.preferences;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JPanel;

import net.sourceforge.omov.app.gui.FileSystemCheckDialog;
import net.sourceforge.omov.app.gui.main.MainWindowController;
import net.sourceforge.omov.app.util.AppImageFactory.PrefToolBarIcon;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.tools.FileSystemChecker;
import net.sourceforge.omov.core.tools.FileSystemChecker.FileSystemCheckResult;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.core.util.OmovGuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreferencesWindowController implements ActionListener {

    private static final Log LOG = LogFactory.getLog(PreferencesWindowController.class);

    static final String CMD_PANE_GENERAL = "CMD_PANE_GENERAL";
    static final String CMD_PANE_QUICKVIEW = "CMD_PANE_QTJ";
    static final String CMD_PANE_ADVANCED = "CMD_PANE_ADVANCED";

    static final String CMD_CLEAR_PREFERENCES = "CMD_CLEAR_PREFERENCES";
    static final String CMD_CHECK_VERSION_NOW = "CMD_CHECK_VERSION_NOW";
    static final String CMD_CHECK_FILESYSTEM_NOW = "CMD_CHECK_FILESYSTEM_NOW";
    
    
    static final String CMD_CLOSE = "CMD_CLOSE";
    
//    private static final String CMD_SERVER_START = "CMD_SERVER_START";
//    private static final String CMD_SERVER_STOP = "CMD_SERVER_STOP";
    
    private final PreferencesWindow window;

    final PrefToolBarItem prefItemGeneral;
    final PrefToolBarItem prefItemQuickView;
    final PrefToolBarItem prefItemAdvanced;

    private final MainWindowController mainController;
    
    public PreferencesWindowController(PreferencesWindow window, MainWindowController mainController) {
        this.window = window;
        this.mainController = mainController;
        
        this.prefItemGeneral = new PrefToolBarItem("General", PreferencesWindowController.CMD_PANE_GENERAL, PrefToolBarIcon.GENERAL, new ContentGeneralPanel(window, this));
        this.prefItemQuickView = new PrefToolBarItem("QuickView", PreferencesWindowController.CMD_PANE_QUICKVIEW, PrefToolBarIcon.QUICKVIEW, new ContentQuickviewPanel(window, this));
        this.prefItemAdvanced = new PrefToolBarItem("Advanced", PreferencesWindowController.CMD_PANE_ADVANCED, PrefToolBarIcon.ADVANCED, new ContentAdvancedPanel(window, this));
    }
    
    public void doClearPreferences() throws BusinessException {
        PreferencesDao.clearPreferences();
    }
    
    public void doCheckApplicationVersion() {
        final VersionCheckDialog dialog = new VersionCheckDialog(this.window);
        dialog.startCheck();
        dialog.setVisible(true);
    }
    
    public void doCheckFileSystem() {
        LOG.info("User running filesystem check manually.");
        
        try {
            final FileSystemCheckResult result = FileSystemChecker.process();
            if(result.isEverythingOkay() == true) {
                OmovGuiUtil.info(this.window, "File System Check", "Each and every file is at its the expected location.");
                
            } else { // (result.isEverythingOkay() == false)
                new FileSystemCheckDialog(result, this.window).setVisible(true);
            }
            
        } catch (BusinessException e) {
            LOG.error("File System Check failed!", e);
            OmovGuiUtil.error("Filesystem Check failed", "Sorry, but could not perform the check because of an internal error.");
        }
    }

	public void actionPerformed(final ActionEvent event) {
		new GuiAction() {
			@Override
			protected void _action() {
				final String cmd = event.getActionCommand();
				LOG.debug("Action performed, cmd='"+cmd+"'.");
				
				if(cmd.equals(CMD_PANE_GENERAL)) {
					window.switchContent(prefItemGeneral);

				} else if(cmd.equals(CMD_PANE_QUICKVIEW)) {
					window.switchContent(prefItemQuickView);

				} else if(cmd.equals(CMD_PANE_ADVANCED)) {
					window.switchContent(prefItemAdvanced);
					

				} else if(cmd.equals(CMD_CLEAR_PREFERENCES)) {
					if(OmovGuiUtil.getYesNoAnswer(window, "Clear Preferences", "Do you really want to clear every perferences you set\nand shutdown OurMovies immediately?") == false) {
                        return;
                    }
                    
                    try {
                        doClearPreferences();
                        mainController.doQuit(); // do only quit, if clearing preferences was successfull
                        
                    } catch (BusinessException e) {
                        LOG.error("Could not clear preferences!", e);
                        OmovGuiUtil.error(window, "Error", "Could not clear preferences!");
                    }
                    
                } else  if(cmd.equals(CMD_CHECK_VERSION_NOW)) {
                    doCheckApplicationVersion();
                    
                } else  if(cmd.equals(CMD_CHECK_FILESYSTEM_NOW)) {
                    doCheckFileSystem();
                    
                } else  if(cmd.equals(CMD_CLOSE)) {
                    doClose();
                    
                    
				} else {
					throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
				}
			}
		}.doAction();
	}

    void doClose() {
        this.window.setVisible(false);
    }
    
//    public boolean doStartServer(int port) {
//        try {
//            RemoteServer.getInstance().startUp(port);
//            return true;
//        } catch (BusinessException e) {
//            LOG.warn("Server startup at port '"+port+"' failed!", e);
//            JOptionPane.showMessageDialog(this.window, e.getMessage(), "Server startup failed!", JOptionPane.ERROR_MESSAGE);
//            return false;
//        }
//    }
//    
//    public boolean doStopServer() {
//        try {
//            RemoteServer.getInstance().shutDown();
//            return true;
//        } catch (BusinessException e) {
//            GuiUtil.error(this.window, "Server shutdown failed", e.getMessage());
//            return false;
//        }
//    }
	
	
    static abstract class AbstractPreferencesContent extends JPanel {
    	private final PreferencesWindow owner;
    	private final PreferencesWindowController preferencesController;
    
    	public AbstractPreferencesContent(PreferencesWindow owner, PreferencesWindowController preferencesController) {
    		this.owner = owner;
    		this.preferencesController = preferencesController;
    	}

		protected PreferencesWindow getOwner() {
			return this.owner;
		}

		protected PreferencesWindowController getPreferencesController() {
			return this.preferencesController;
		}
    }

    static final class PrefToolBarItem {
    	private final String label;
    	private final String cmd;
    	private final PrefToolBarIcon icon;
    	private final AbstractPreferencesContent content;
    	
    	private PrefToolBarItem(String label, String cmd, PrefToolBarIcon icon, AbstractPreferencesContent content) {
    		this.label = label;
    		this.cmd = cmd;
    		this.icon = icon;
    		this.content = content;
    	}
    	
    	String getLabel() {
    		return this.label;
    	}
    	String getActionCommand() {
    		return this.cmd;
    	}
    	PrefToolBarIcon getIcon() {
    		return this.icon;
    	}
    	AbstractPreferencesContent getContent() {
    		return this.content;
    	}
    }
    
}
