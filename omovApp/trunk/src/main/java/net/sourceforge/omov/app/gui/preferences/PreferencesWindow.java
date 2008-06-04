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

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ComboBoxModel;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;

import net.sourceforge.omov.app.App;
import net.sourceforge.omov.app.gui.main.MainWindowController;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.core.util.LanguageUtil;
import net.sourceforge.omov.core.util.LanguageUtil.LanguageCode;
import net.sourceforge.omov.gui.EscapeDisposer;
import net.sourceforge.omov.gui.GuiActionListener;
import net.sourceforge.omov.gui.EscapeDisposer.IEscapeDisposeReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreferencesWindow extends JDialog implements ActionListener, IEscapeDisposeReceiver {

    private static final long serialVersionUID = -3157582134656737177L;
    private static final Log LOG = LogFactory.getLog(PreferencesWindow.class);

    private final PreferencesWindowController preferencesController = new PreferencesWindowController(this);

    private static final String CMD_CLEAR_PREFERENCES = "CMD_CLEAR_PREFERENCES";
    private static final String CMD_CHECK_VERSION_NOW = "CMD_CHECK_VERSION_NOW";
    private static final String CMD_CHECK_FILESYSTEM_NOW = "CMD_CHECK_FILESYSTEM_NOW";
    
    
    private static final String CMD_CLOSE = "CMD_CLOSE";
    
//    private static final String CMD_SERVER_START = "CMD_SERVER_START";
//    private static final String CMD_SERVER_STOP = "CMD_SERVER_STOP";

    
    private boolean escapeHit;
    

    private final MainWindowController mainController;
    
    
    private final AbstractPreferencesStringFieldX inpUsername;
    
    private final AbstractPreferencesBooleanFieldX inpStartupVersion;
    private final AbstractPreferencesBooleanFieldX inpStartupFileSystem;
    
//    private final PreferencesNumber inpServerPort;
//    private final JButton btnStartStopServer = new JButton("Start");

    private final AbstractPreferencesBooleanFieldX inpProxyEnabled;
    private final AbstractPreferencesIntFieldX inpProxyPort;
    private final AbstractPreferencesStringFieldX inpProxyHost; // TODO check if proper url entered by user for proxy host
    private final AbstractPreferencesComboBoxFieldX<LanguageCode> inpLanguageBox;

    public static interface IComboBoxModelX<T> extends ComboBoxModel {
    	T getTypedElementAt(int index);
    	int getItemIndex(T item);
    }
    
    public PreferencesWindow(JFrame owner, MainWindowController mainController) {
        super(owner, true);
        this.setTitle("Preferences");
        this.mainController = mainController;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        EscapeDisposer.enableEscape(this.getRootPane(), this);
        
        
        // ---------------------------------------------------------------------------
        
        this.inpUsername = new AbstractPreferencesStringFieldX(this, PreferencesDao.getInstance().getUsername(), 20) {
            void saveData() throws BusinessException {
            	if(isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpUsername.setVisibleData(CONF.getUsername()); // reset value
            		return;
            	}
            	CONF.setUsername(this.getData());
            }
        };

        // ---------------------------------------------------------------------------

        this.inpStartupVersion = new AbstractPreferencesBooleanFieldX(this, PreferencesDao.getInstance().isStartupVersionCheck(), "Check at startup") {
			@Override
			void saveData() throws BusinessException {
				if(isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpStartupVersion.setVisibleData(CONF.isStartupVersionCheck()); // reset value
            		return;
            	}
            	CONF.setStartupVersionCheck(this.getData());
			}
        };
        this.inpStartupVersion.getComponent().setToolTipText("Whenever you start OurMovies a version check will be performed");

        // ---------------------------------------------------------------------------
        
        this.inpStartupFileSystem = new AbstractPreferencesBooleanFieldX(this, PreferencesDao.getInstance().isStartupFilesystemCheck(), "Check at startup") {
			@Override
			void saveData() throws BusinessException {
				if(isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpStartupFileSystem.setVisibleData(CONF.isStartupFilesystemCheck()); // reset value
            		return;
            	}
            	CONF.setStartupFilesystemCheck(this.getData());
			}
        };
        this.inpStartupFileSystem.getComponent().setToolTipText("Whenever you start OurMovies a filesystem check will be performed");

        // ---------------------------------------------------------------------------
        
        this.inpProxyHost = new AbstractPreferencesStringFieldX(this, PreferencesDao.getInstance().getProxyHost(), 10) {
			void saveData() throws BusinessException {
				if(isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpProxyHost.setVisibleData(CONF.getProxyHost()); // reset value
            		return;
            	}
            	CONF.setProxyHost(this.getData());
            }
        };

        // ---------------------------------------------------------------------------
        
        this.inpProxyPort = new AbstractPreferencesIntFieldX(this, PreferencesDao.getInstance().getProxyPort(), 0L, 65535L, 4) {
			@Override
			void saveData() throws BusinessException {
				if(isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpProxyPort.setVisibleData(CONF.getProxyPort()); // reset value
            		return;
            	}
            	CONF.setProxyPort(this.getData());
			}
        	
        };

        // ---------------------------------------------------------------------------
        
        this.inpProxyEnabled = new AbstractPreferencesBooleanFieldX(this, PreferencesDao.getInstance().isProxyEnabled(), null) {
			@Override
			void saveData() throws BusinessException {
				if(isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpProxyEnabled.setVisibleData(CONF.isProxyEnabled()); // reset value
            		return;
            	}
            	CONF.setProxyEnabled(this.getData());
			}
        };
        this.inpProxyEnabled.getComponent().setToolTipText("Select this if you are using a proxy to access the internet");

        // ---------------------------------------------------------------------------
        

        class LanguageBoxModel extends DefaultComboBoxModel implements IComboBoxModelX<LanguageCode> {
    		private static final long serialVersionUID = 6862102176774378797L;
    		private final List<LanguageCode> data = LanguageUtil.getLanguagesSorted();
        	public LanguageBoxModel() {
        		// nothing to do
        	}
        	@Override
        	public int getSize() {
        		return this.data.size();
        	}
        	@Override
        	public Object getElementAt(int index) {
        		return this.data.get(index).getLocale().getDisplayLanguage();
        	}
        	public LanguageCode getTypedElementAt(int index) {
        		return this.data.get(index);
        	}
			public int getItemIndex(LanguageCode item) {
				for (int i = 0; i < this.getSize(); i++) {
					if(this.data.get(i) == item) {
						return i;
					}
				}
				throw new IllegalArgumentException("Could not get index of item: " + item);
			}
        }
        
        this.inpLanguageBox = new AbstractPreferencesComboBoxFieldX<LanguageCode>(this, new LanguageBoxModel(), PreferencesDao.getInstance().getLanguage()) {
			@Override
			void saveData() throws BusinessException {
				if(isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpLanguageBox.setVisibleData(CONF.getLanguage()); // reset value
            		return;
            	}
            	CONF.setLanguage(this.getData());
			}
        };
        this.inpLanguageBox.getComponent().setToolTipText("Select Language and restart program to take effect");
        // TODO add flags
        this.inpLanguageBox.getComponent().addActionListener(new GuiActionListener() {
			@Override
			protected void action(ActionEvent event) {
				if(GuiUtil.getYesNoAnswer(PreferencesWindow.this, "Language changed",
						"<html>Changing the language takes effect after a restart.<br>" +
						"Do you want to restart the application now?</html>")
						== true) {
					App.restartApplication();
				}
			}
        });
        // ---------------------------------------------------------------------------
        
        
        
        GuiUtil.macSmallWindow(this.getRootPane());
        
//        this.inpServerPort = new PreferencesNumber(this, Configuration.getInstance().getServerPort(), 4) {
//            private static final long serialVersionUID = -2125603612020028656L;
//            void saveData() throws BusinessException {
//                CONF.setServerPort(this.getData()); }
//            String getInvalidInputString() {
//                return "The server port must only consists of digits!"; } };

//          this.btnStartStopServer.setActionCommand(CMD_SERVER_START);
//          this.btnStartStopServer.addActionListener(new ActionListener() {
//              public void actionPerformed(ActionEvent e) {
//                  final String cmd = btnStartStopServer.getActionCommand();
//                  if(cmd.equals(CMD_SERVER_START)) {
//                      doStartServer();
//                  } else {
//                      assert(cmd.equals(CMD_SERVER_STOP));
//                      doStopServer();
//                  }
//          }});
                
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        
        final JButton btnClearPrefs = new JButton("Clear & Shutdown");
        btnClearPrefs.setActionCommand(CMD_CLEAR_PREFERENCES);
        btnClearPrefs.addActionListener(this);
        
        final JButton btnCheckVersion = new JButton("Check Now");
        btnCheckVersion.setActionCommand(CMD_CHECK_VERSION_NOW);
        btnCheckVersion.addActionListener(this);
        
        final JButton btnCheckFileSystem = new JButton("Check Now");
        btnCheckFileSystem.setActionCommand(CMD_CHECK_FILESYSTEM_NOW);
        btnCheckFileSystem.addActionListener(this);
        
        btnClearPrefs.setOpaque(false);
        btnCheckVersion.setOpaque(false);
        btnCheckFileSystem.setOpaque(false);
        
        

        final JPanel panel = new JPanel();
        panel.setBackground(Constants.getColorWindowBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        
        c.anchor = GridBagConstraints.LINE_START;
        final Insets insetLeft = new Insets(0, 0, 15, 0);
        final Insets insetRight = new Insets(0, 10, 15, 0);
        
        // ----------------------------

        c.gridx = 0;
        c.gridy = 0;
        c.insets = insetLeft;
        panel.add(new JLabel("Username"), c);
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(this.inpUsername.getComponent(), c);

        // ----------------------------
        
        c.gridx = 0;
        c.gridy++;
        c.insets = insetLeft;
        panel.add(new JLabel("Clear Preferences"), c);
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(btnClearPrefs, c);

        // ----------------------------

        c.gridx = 0;
        c.gridy++;
        c.insets = insetLeft;
        panel.add(new JLabel("Software Update"), c);
        
        final JPanel panelSoftwareUpdate = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSoftwareUpdate.setOpaque(false);
        panelSoftwareUpdate.add(btnCheckVersion);
        panelSoftwareUpdate.add(this.inpStartupVersion.getComponent());
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(panelSoftwareUpdate, c);

        // ----------------------------

        c.gridx = 0;
        c.gridy++;
        c.insets = insetLeft;
        panel.add(new JLabel("FileSystem Check"), c);
        
        final JPanel panelFileSystemCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelFileSystemCheck.setOpaque(false);
        panelFileSystemCheck.add(btnCheckFileSystem);
        panelFileSystemCheck.add(this.inpStartupFileSystem.getComponent());
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(panelFileSystemCheck, c);

        // ----------------------------
        
//        panel.add(new JLabel("Server port"));
//        panel.add(this.inpServerPort);
//
//        panel.add(new JLabel("Start/Stop Server"));
//        panel.add(this.btnStartStopServer);
        // ----------------------------

		c.gridx = 0;
        c.gridy++;
        c.insets = insetLeft;
		panel.add(new JLabel("Proxy"), c);
		
		
		JPanel proxyPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
		proxyPanel.setOpaque(false);
		proxyPanel.add(this.inpProxyEnabled.getComponent());
		proxyPanel.add(new JLabel(" Host"));
		proxyPanel.add(this.inpProxyHost.getComponent());
		proxyPanel.add(new JLabel(" Port"));
		proxyPanel.add(this.inpProxyPort.getComponent());
		c.gridx = 1;
        c.insets = insetRight;
		panel.add(proxyPanel, c);

        // ----------------------------

        c.gridx = 0;
        c.gridy++;
        c.insets = insetLeft;
        panel.add(new JLabel("Language"), c);
        
        final JPanel panelLanguage = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelLanguage.setOpaque(false);
        panelLanguage.add(this.inpLanguageBox.getComponent());
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(panelLanguage, c);

        // ----------------------------------------------------------------

        c.gridx = 0;
        c.gridwidth = 2;
        c.gridy++;
        c.anchor = GridBagConstraints.LAST_LINE_END;
        final JButton btnClose = new JButton("Close");
        this.getRootPane().setDefaultButton(btnClose);
        btnClose.setOpaque(false);
        btnClose.setActionCommand(CMD_CLOSE);
        btnClose.addActionListener(this);
        c.insets = new Insets(0, 0, 0, 0);
        panel.add(btnClose, c);

        // ----------------------------

		
		
        return panel;
    }
    
//    private void doStartServer() {
//        LOG.debug("User clicked start server.");
//        boolean startupSuccessfull = false;
//        this.btnStartStopServer.setEnabled(false);
//        try {
//            int port = this.inpServerPort.getData();
//            startupSuccessfull = this.controller.doStartServer(port);
//        } catch (BusinessException e) {
//            JOptionPane.showMessageDialog(this, "Invalid port entered '"+this.inpServerPort.getText()+"'!", "Server startup failed", JOptionPane.WARNING_MESSAGE);
//        }
//        
//        if(startupSuccessfull == true) {
//            this.btnStartStopServer.setText("Stop");
//            this.btnStartStopServer.setActionCommand(CMD_SERVER_STOP);
//        }
//        this.btnStartStopServer.setEnabled(true);
//    }

//    private void doStopServer() {
//        LOG.debug("User clicked stop server.");
//        this.btnStartStopServer.setEnabled(false);
//        
//        this.controller.doStopServer();
//        
//        this.btnStartStopServer.setText("Start");
//        this.btnStartStopServer.setActionCommand(CMD_SERVER_START);
//        this.btnStartStopServer.setEnabled(true);
//    }
    
    private void doClose() {
        this.setVisible(false);
    }

    public void actionPerformed(final ActionEvent event) {
        new GuiAction() {
            @Override
            protected void _action() {
                final String cmd = event.getActionCommand();
                LOG.debug("Action performed, cmd='"+cmd+"'.");
                
                if(cmd.equals(CMD_CLEAR_PREFERENCES)) {
                    if(GuiUtil.getYesNoAnswer(PreferencesWindow.this, "Clear Preferences", "Do you really want to clear every perferences you set\nand shutdown OurMovies immediately?") == false) {
                        return;
                    }
                    
                    try {
                        preferencesController.doClearPreferences();
                        mainController.doQuit(); // do only quit, if clearing preferences was successfull
                        
                    } catch (BusinessException e) {
                        LOG.error("Could not clear preferences!", e);
                        GuiUtil.error(PreferencesWindow.this, "Error", "Could not clear preferences!");
                    }
                } else  if(cmd.equals(CMD_CHECK_VERSION_NOW)) {
                    preferencesController.doCheckApplicationVersion();
                    
                } else  if(cmd.equals(CMD_CHECK_FILESYSTEM_NOW)) {
                    preferencesController.doCheckFileSystem();
                    
                } else  if(cmd.equals(CMD_CLOSE)) {
                    doClose();
                    
                } else {
                    throw new IllegalArgumentException("Unhandled command '"+cmd+"'!");
                }
            }
        }.doAction();
    }
    
    
    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception ex) {
            LOG.error("Unable to set system look&feel!", ex);
        }
		new PreferencesWindow(null, null).setVisible(true);
	}

	public void doEscape() {
		this.escapeHit = true;
		this.doClose();
	}
	
	public boolean isEscapeHit() {
		return this.escapeHit;
	}
	
	public void setVisible(boolean visible) {
		if(visible == true) {
			// reset visible-lifetime data
			this.escapeHit = false;
		}
		super.setVisible(visible);
	}
}
