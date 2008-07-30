package net.sourceforge.omov.app.gui.preferences;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;

import net.sourceforge.omov.app.App;
import net.sourceforge.omov.app.gui.preferences.PreferencesWindowController.AbstractPreferencesContent;
import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.LanguageCode;
import net.sourceforge.omov.guicore.GuiActionListener;
import net.sourceforge.omov.guicore.OmovGuiUtil;
import net.sourceforge.omov.logic.prefs.PreferencesDao;
import net.sourceforge.omov.logic.util.LanguageUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

class ContentGeneralPanel extends AbstractPreferencesContent {

    private static final Log LOG = LogFactory.getLog(ContentGeneralPanel.class);
	private static final long serialVersionUID = 1282229908827436636L;

	
    
    private final AbstractPreferencesStringFieldX inpUsername;
    
    
//    private final PreferencesNumber inpServerPort;
//    private final JButton btnStartStopServer = new JButton("Start");

    private final AbstractPreferencesComboBoxFieldX<LanguageCode> inpLanguageBox;
	
    
	public ContentGeneralPanel(PreferencesWindow owner, PreferencesWindowController preferencesController) {
		super(owner, preferencesController);
		this.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
		this.setOpaque(false);

        // ---------------------------------------------------------------------------
        
        this.inpUsername = new AbstractPreferencesStringFieldX(owner, PreferencesDao.getInstance().getUsername(), 20) {
            @Override
			void saveData() {
            	if(getOwner().isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpUsername.setVisibleData(CONF.getUsername()); // reset value
            		return;
            	}
            	CONF.setUsername(this.getData());
            }
        };

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
        		return this.data.get(index);
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
			void saveData() {
				if(getOwner().isEscapeHit() == true) {
            		LOG.info("Not going to store value '"+this.getData()+"' because user hit escape.");
            		inpLanguageBox.setVisibleData(CONF.getLanguage()); // reset value
            		return;
            	}
            	CONF.setLanguage(this.getData());
			}
        };
        this.inpLanguageBox.getComponent().setToolTipText("Select Language and restart program to take effect");
        this.inpLanguageBox.setRenderer(new DefaultListCellRenderer() {

			private static final long serialVersionUID = -4269358333115253401L;

			@Override
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				if (!(value instanceof LanguageCode))
					throw new IllegalArgumentException("Argument value must be instance of LanguageCode but was '"+value.getClass().getName()+"'!");

				final LanguageCode languageCode = (LanguageCode) value;
				final JLabel label = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				label.setText(languageCode.getLocale().getDisplayLanguage(PreferencesDao.getInstance().getLanguage().getLocale()));
				label.setIcon(AppImageFactory.getInstance().getFlag(languageCode));
				
				return label;
			}
        });
        this.inpLanguageBox.getComponent().addActionListener(new GuiActionListener() {
			@Override
			protected void action(ActionEvent event) {
				if(PreferencesDao.getInstance().getLanguage() == inpLanguageBox.getData()) {
					LOG.debug("Ignoring ActionEvent because selected value is already stored in preferences dao.");
					return;
				}
				
				if(OmovGuiUtil.getYesNoAnswer(getOwner(), "Language changed",
						"<html>Changing the language takes effect after a restart.<br>" +
						"Do you want to restart the application now?</html>")
						== true) {
					App.restartApplication();
				}
			}
        });
        // ---------------------------------------------------------------------------
        
        this.add(this.initComponents());
	}
	
	private JPanel initComponents() {

        

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
        
        
//        panel.add(new JLabel("Server port"));
//        panel.add(this.inpServerPort);
//
//        panel.add(new JLabel("Start/Stop Server"));
//        panel.add(this.btnStartStopServer);
        // ----------------------------


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
