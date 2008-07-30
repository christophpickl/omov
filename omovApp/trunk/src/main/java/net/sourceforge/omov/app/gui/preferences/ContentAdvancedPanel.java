package net.sourceforge.omov.app.gui.preferences;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.app.gui.preferences.PreferencesWindowController.AbstractPreferencesContent;
import net.sourceforge.omov.logic.prefs.PreferencesDao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ContentAdvancedPanel extends AbstractPreferencesContent {

    private static final Log LOG = LogFactory.getLog(ContentAdvancedPanel.class);
	private static final long serialVersionUID = 8253576868147647312L;


    private final AbstractPreferencesBooleanFieldX inpStartupVersion;
    private final AbstractPreferencesBooleanFieldX inpStartupFileSystem;

    private final AbstractPreferencesBooleanFieldX inpProxyEnabled;
    private final AbstractPreferencesIntFieldX inpProxyPort;
    private final AbstractPreferencesStringFieldX inpProxyHost; // TODO check if proper url entered by user for proxy host
    
    
    
    
	public ContentAdvancedPanel(PreferencesWindow owner, PreferencesWindowController preferencesController) {
		super(owner, preferencesController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setOpaque(false);
		

        // ---------------------------------------------------------------------------

        this.inpStartupVersion = new AbstractPreferencesBooleanFieldX(owner, PreferencesDao.getInstance().isStartupVersionCheck(), "Check at startup") {
			@Override
			void saveData() {
				if(getOwner().isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		ContentAdvancedPanel.this.inpStartupVersion.setVisibleData(CONF.isStartupVersionCheck() ? Boolean.TRUE : Boolean.FALSE); // reset value
            		return;
            	}
            	CONF.setStartupVersionCheck(this.getData().booleanValue());
			}
        };
        this.inpStartupVersion.getComponent().setToolTipText("Whenever you start OurMovies a version check will be performed");

        // ---------------------------------------------------------------------------
        
        this.inpStartupFileSystem = new AbstractPreferencesBooleanFieldX(owner, PreferencesDao.getInstance().isStartupFilesystemCheck(), "Check at startup") {
			@Override
			void saveData() {
				if(getOwner().isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		ContentAdvancedPanel.this.inpStartupFileSystem.setVisibleData(CONF.isStartupFilesystemCheck() ? Boolean.TRUE : Boolean.FALSE); // reset value
            		return;
            	}
            	CONF.setStartupFilesystemCheck(this.getData().booleanValue());
			}
        };
        this.inpStartupFileSystem.getComponent().setToolTipText("Whenever you start OurMovies a filesystem check will be performed");

        
        // ---------------------------------------------------------------------------
        
        this.inpProxyHost = new AbstractPreferencesStringFieldX(owner, PreferencesDao.getInstance().getProxyHost(), 10) {
			@Override
			void saveData() {
				if(getOwner().isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		ContentAdvancedPanel.this.inpProxyHost.setVisibleData(CONF.getProxyHost()); // reset value
            		return;
            	}
            	CONF.setProxyHost(this.getData());
            }
        };

        // ---------------------------------------------------------------------------
        
        this.inpProxyPort = new AbstractPreferencesIntFieldX(owner, PreferencesDao.getInstance().getProxyPort(), 0L, 65535L, 4) {
			@Override
			void saveData() {
				if(getOwner().isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		ContentAdvancedPanel.this.inpProxyPort.setVisibleData(new Integer(CONF.getProxyPort())); // reset value
            		return;
            	}
            	CONF.setProxyPort(this.getData().intValue());
			}
        	
        };

        // ---------------------------------------------------------------------------
        
        this.inpProxyEnabled = new AbstractPreferencesBooleanFieldX(owner, PreferencesDao.getInstance().isProxyEnabled(), null) {
			@Override
			void saveData() {
				if(getOwner().isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		ContentAdvancedPanel.this.inpProxyEnabled.setVisibleData(CONF.isProxyEnabled() ? Boolean.TRUE : Boolean.FALSE); // reset value
            		return;
            	}
            	CONF.setProxyEnabled(this.getData().booleanValue());
			}
        };
        this.inpProxyEnabled.getComponent().setToolTipText("Select this if you are using a proxy to access the internet");

        // ---------------------------------------------------------------------------

        this.add(this.initComponents());
	}

	private JPanel initComponents() {

        final JButton btnClearPrefs = new JButton("Clear & Shutdown");
        btnClearPrefs.setActionCommand(PreferencesWindowController.CMD_CLEAR_PREFERENCES);
        btnClearPrefs.addActionListener(this.getPreferencesController());
        
        final JButton btnCheckVersion = new JButton("Check Now");
        btnCheckVersion.setActionCommand(PreferencesWindowController.CMD_CHECK_VERSION_NOW);
        btnCheckVersion.addActionListener(this.getPreferencesController());
        
        final JButton btnCheckFileSystem = new JButton("Check Now");
        btnCheckFileSystem.setActionCommand(PreferencesWindowController.CMD_CHECK_FILESYSTEM_NOW);
        btnCheckFileSystem.addActionListener(this.getPreferencesController());
        
        btnClearPrefs.setOpaque(false);
        btnCheckVersion.setOpaque(false);
        btnCheckFileSystem.setOpaque(false);
        
        
        
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        final Insets insetLeft = new Insets(0, 0, 15, 0);
        final Insets insetRight = new Insets(0, 10, 15, 0);

        

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = insetLeft;
        panel.add(new JLabel("Clear Preferences"), c);
        
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(btnClearPrefs, c);

        // ----------------------------

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy++;
        c.insets = insetLeft;
        panel.add(new JLabel("Software Update"), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        final JPanel panelSoftwareUpdate = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelSoftwareUpdate.setOpaque(false);
        panelSoftwareUpdate.add(this.inpStartupVersion.getComponent());
        panelSoftwareUpdate.add(btnCheckVersion);
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(panelSoftwareUpdate, c);

        // ----------------------------

        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy++;
        c.insets = insetLeft;
        panel.add(new JLabel("FileSystem Check"), c);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        final JPanel panelFileSystemCheck = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        panelFileSystemCheck.setOpaque(false);
        panelFileSystemCheck.add(this.inpStartupFileSystem.getComponent());
        panelFileSystemCheck.add(btnCheckFileSystem);
        c.gridx = 1;
        c.insets = insetRight;
        panel.add(panelFileSystemCheck, c);

        // ----------------------------

        c.anchor = GridBagConstraints.LINE_START;
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
        c.anchor = GridBagConstraints.FIRST_LINE_START;
		c.gridx = 1;
        c.insets = insetRight;
		panel.add(proxyPanel, c);
		
		
        return panel;
	}
}
