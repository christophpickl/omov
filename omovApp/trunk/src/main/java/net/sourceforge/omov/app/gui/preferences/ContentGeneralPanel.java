package net.sourceforge.omov.app.gui.preferences;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.app.App;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.util.LanguageUtil;
import net.sourceforge.omov.core.util.LanguageUtil.LanguageCode;
import net.sourceforge.omov.gui.GuiActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ContentGeneralPanel extends JPanel {

    private static final Log LOG = LogFactory.getLog(ContentGeneralPanel.class);
	private static final long serialVersionUID = 1282229908827436636L;

	
    
    private final AbstractPreferencesStringFieldX inpUsername;
    
    private final AbstractPreferencesBooleanFieldX inpStartupVersion;
    private final AbstractPreferencesBooleanFieldX inpStartupFileSystem;
    
//    private final PreferencesNumber inpServerPort;
//    private final JButton btnStartStopServer = new JButton("Start");

    private final AbstractPreferencesBooleanFieldX inpProxyEnabled;
    private final AbstractPreferencesIntFieldX inpProxyPort;
    private final AbstractPreferencesStringFieldX inpProxyHost; // TODO check if proper url entered by user for proxy host
    private final AbstractPreferencesComboBoxFieldX<LanguageCode> inpLanguageBox;
	
    private final PreferencesWindowController preferencesController;
	
	public ContentGeneralPanel(final PreferencesWindow owner, PreferencesWindowController preferencesController) {
		this.preferencesController = preferencesController;

        // ---------------------------------------------------------------------------
        
        this.inpUsername = new AbstractPreferencesStringFieldX(owner, PreferencesDao.getInstance().getUsername(), 20) {
            void saveData() throws BusinessException {
            	if(owner.isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpUsername.setVisibleData(CONF.getUsername()); // reset value
            		return;
            	}
            	CONF.setUsername(this.getData());
            }
        };

        // ---------------------------------------------------------------------------

        this.inpStartupVersion = new AbstractPreferencesBooleanFieldX(owner, PreferencesDao.getInstance().isStartupVersionCheck(), "Check at startup") {
			@Override
			void saveData() throws BusinessException {
				if(owner.isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpStartupVersion.setVisibleData(CONF.isStartupVersionCheck()); // reset value
            		return;
            	}
            	CONF.setStartupVersionCheck(this.getData());
			}
        };
        this.inpStartupVersion.getComponent().setToolTipText("Whenever you start OurMovies a version check will be performed");

        // ---------------------------------------------------------------------------
        
        this.inpStartupFileSystem = new AbstractPreferencesBooleanFieldX(owner, PreferencesDao.getInstance().isStartupFilesystemCheck(), "Check at startup") {
			@Override
			void saveData() throws BusinessException {
				if(owner.isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpStartupFileSystem.setVisibleData(CONF.isStartupFilesystemCheck()); // reset value
            		return;
            	}
            	CONF.setStartupFilesystemCheck(this.getData());
			}
        };
        this.inpStartupFileSystem.getComponent().setToolTipText("Whenever you start OurMovies a filesystem check will be performed");

        // ---------------------------------------------------------------------------
        
        this.inpProxyHost = new AbstractPreferencesStringFieldX(owner, PreferencesDao.getInstance().getProxyHost(), 10) {
			void saveData() throws BusinessException {
				if(owner.isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpProxyHost.setVisibleData(CONF.getProxyHost()); // reset value
            		return;
            	}
            	CONF.setProxyHost(this.getData());
            }
        };

        // ---------------------------------------------------------------------------
        
        this.inpProxyPort = new AbstractPreferencesIntFieldX(owner, PreferencesDao.getInstance().getProxyPort(), 0L, 65535L, 4) {
			@Override
			void saveData() throws BusinessException {
				if(owner.isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpProxyPort.setVisibleData(CONF.getProxyPort()); // reset value
            		return;
            	}
            	CONF.setProxyPort(this.getData());
			}
        	
        };

        // ---------------------------------------------------------------------------
        
        this.inpProxyEnabled = new AbstractPreferencesBooleanFieldX(owner, PreferencesDao.getInstance().isProxyEnabled(), null) {
			@Override
			void saveData() throws BusinessException {
				if(owner.isEscapeHit() == true) {
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
        
        this.inpLanguageBox = new AbstractPreferencesComboBoxFieldX<LanguageCode>(owner, new LanguageBoxModel(), PreferencesDao.getInstance().getLanguage()) {
			@Override
			void saveData() throws BusinessException {
				if(owner.isEscapeHit() == true) {
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
				if(PreferencesDao.getInstance().getLanguage() == inpLanguageBox.getData()) {
					LOG.debug("Ignoring ActionEvent because selected value is already stored in preferences dao.");
					return;
				}
				
				if(GuiUtil.getYesNoAnswer(owner, "Language changed",
						"<html>Changing the language takes effect after a restart.<br>" +
						"Do you want to restart the application now?</html>")
						== true) {
					App.restartApplication();
				}
			}
        });
        // ---------------------------------------------------------------------------
        
        this.setOpaque(false);
        this.add(this.initComponents());
	}
	
	private JPanel initComponents() {

        final JButton btnClearPrefs = new JButton("Clear & Shutdown");
        btnClearPrefs.setActionCommand(PreferencesWindowController.CMD_CLEAR_PREFERENCES);
        btnClearPrefs.addActionListener(this.preferencesController);
        
        final JButton btnCheckVersion = new JButton("Check Now");
        btnCheckVersion.setActionCommand(PreferencesWindowController.CMD_CHECK_VERSION_NOW);
        btnCheckVersion.addActionListener(this.preferencesController);
        
        final JButton btnCheckFileSystem = new JButton("Check Now");
        btnCheckFileSystem.setActionCommand(PreferencesWindowController.CMD_CHECK_FILESYSTEM_NOW);
        btnCheckFileSystem.addActionListener(this.preferencesController);
        
        btnClearPrefs.setOpaque(false);
        btnCheckVersion.setOpaque(false);
        btnCheckFileSystem.setOpaque(false);
        
        

        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        
        c.anchor = GridBagConstraints.FIRST_LINE_START;
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
        
        return panel;
	}
	
}
