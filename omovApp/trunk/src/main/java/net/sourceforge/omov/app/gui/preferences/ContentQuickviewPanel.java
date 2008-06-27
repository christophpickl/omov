package net.sourceforge.omov.app.gui.preferences;

import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.app.gui.preferences.PreferencesWindowController.AbstractPreferencesContent;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.util.OmovGuiUtil;
import net.sourceforge.omov.qtjApi.IQtjSessionManager;
import net.sourceforge.omov.qtjApi.QtjFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ContentQuickviewPanel extends AbstractPreferencesContent {

    private static final Log LOG = LogFactory.getLog(ContentQuickviewPanel.class);
	private static final long serialVersionUID = -2114513454360496570L;
	
	
	

	public ContentQuickviewPanel(PreferencesWindow owner, PreferencesWindowController preferencesController) {
		super(owner, preferencesController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setOpaque(false);
		
		final boolean qtjAvailable = QtjFactory.isQtjAvailable();
		
		final JPanel panel = new JPanel();
		panel.setOpaque(false);
		final GridBagLayout layout = new GridBagLayout();
		final GridBagConstraints c = new GridBagConstraints();
		layout.setConstraints(panel, c);
		panel.setLayout(layout);

		c.gridx = 0;
		c.gridy = 0;
		c.anchor = GridBagConstraints.FIRST_LINE_START;
		final String availableString = qtjAvailable ? "Yes" : "No";
		panel.add(new JLabel("QuickTime is available: " + availableString), c);

		final String qtVersion;
		final String qtJavaVersion;
		
		
		if(qtjAvailable) {
			IQtjSessionManager qtjSessionManager;
			String _qtVersion;
			String _qtJavaVersion;
			try {
				qtjSessionManager = QtjFactory.getQtjSessionManager();
				qtjSessionManager.openSession();
				_qtVersion = qtjSessionManager.getQuicktimeVersion().getVersionString();
				_qtJavaVersion = String.valueOf(qtjSessionManager.getJavaVersion());
			} catch (BusinessException e) {
				LOG.error("Could not get QtjSessionManager!", e);
				OmovGuiUtil.error("QuickTime Error", "Some internal error occured while communicating with QuickTime!");
				_qtVersion = "-";
				_qtJavaVersion = "-";
			}
			qtVersion = _qtVersion;
			qtJavaVersion = _qtJavaVersion;
		} else {
			qtVersion = "-";
			qtJavaVersion = "-";
		}

		
		c.insets = new Insets(10, 0, 0, 0);
		c.gridy++;
		panel.add(new JLabel("QuickTime version: " + qtVersion), c);
		c.gridy++;
		panel.add(new JLabel("Java API version: " + qtJavaVersion), c);
		
		this.add(panel);
	}
	
//	@Override
//	public void paint(Graphics g) {
//		super.paint(g);
//		GuiUtil.paintCenteredBackgroundImage(g, this, QUICKTIME_BG_LOGO);
//	}
	
}
