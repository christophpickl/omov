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

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JToolBar;
import javax.swing.SwingConstants;
import javax.swing.UIManager;

import net.sourceforge.omov.app.gui.main.MainWindowController;
import net.sourceforge.omov.app.gui.preferences.PreferencesWindowController.PrefToolBarItem;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.ImageFactory;
import net.sourceforge.omov.gui.EscapeDisposer;
import net.sourceforge.omov.gui.EscapeDisposer.IEscapeDisposeReceiver;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreferencesWindow extends JDialog implements IEscapeDisposeReceiver {

    private static final long serialVersionUID = -3157582134656737177L;
    private static final Log LOG = LogFactory.getLog(PreferencesWindow.class);

    private final PreferencesWindowController preferencesController;
        
    private boolean escapeHit;
    
    private final JPanel wrapPanel = new JPanel();
    
    
    private final List<PrefToolBarItem> toolBarItems;
    
    
    
    
    public PreferencesWindow(JFrame owner, MainWindowController mainController) {
        super(owner, true);
        this.setTitle("Preferences");
        this.preferencesController = new PreferencesWindowController(this, mainController);
        
        {
	        List<PrefToolBarItem> tmp = new ArrayList<PrefToolBarItem>(2);
	    	tmp.add(this.preferencesController.prefItemGeneral);
	    	tmp.add(this.preferencesController.prefItemQuickView);
	    	toolBarItems = Collections.unmodifiableList(tmp);
        }

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                preferencesController.doClose();
            }
        });
        EscapeDisposer.enableEscape(this.getRootPane(), this);
        
        
        
        
        
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
        
        this.getContentPane().setLayout(new BorderLayout());
        this.getContentPane().add(this.initToolBar(), BorderLayout.NORTH);
        this.getContentPane().setBackground(Constants.getColorWindowBackground());
        this.wrapPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 0, 0));
        this.switchContent(this.preferencesController.prefItemGeneral);
        this.wrapPanel.setOpaque(false);
        this.wrapPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        this.getContentPane().add(this.wrapPanel, BorderLayout.CENTER);
        this.getContentPane().add(this.newSouthPanel(), BorderLayout.SOUTH);
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel newSouthPanel() {
    	JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));
    	panel.setOpaque(false);
        
        final JButton btnClose = new JButton("Close");
        this.getRootPane().setDefaultButton(btnClose);
        btnClose.setOpaque(false);
        btnClose.setActionCommand(PreferencesWindowController.CMD_CLOSE);
        btnClose.addActionListener(this.preferencesController);
        
        panel.add(btnClose, BorderLayout.EAST);
        return panel;
    }
    
    private Map<PrefToolBarItem, JButton> prefToolBarButtons = new HashMap<PrefToolBarItem, JButton>();
    
    private PrefToolBarItem recentPrefToolBarItem;
    void switchContent(PrefToolBarItem item) {
    	LOG.debug("Switching content to new toolbar item '"+item.getLabel()+"'.");
    	if(this.recentPrefToolBarItem != null) {
    		this.prefToolBarButtons.get(this.recentPrefToolBarItem).setEnabled(true);
    	}
    	this.prefToolBarButtons.get(item).setEnabled(false);
    	this.wrapPanel.removeAll();
    	this.wrapPanel.add(item.getPanel());
    	this.wrapPanel.revalidate();
    	this.wrapPanel.repaint();
    	this.recentPrefToolBarItem = item;
    }
    
    private JToolBar initToolBar() {
    	final JToolBar bar = new JToolBar();
    	
    	bar.setFloatable(false);
    	bar.setOrientation(JToolBar.HORIZONTAL);
//    	bar.setBorderPainted(false);
//    	bar.setRollover(true);
//    	bar.setLayout(int)
//    	bar.setMargin(Insets)
    	
    	bar.setOpaque(true);
    	bar.setBackground(Constants.getColorDarkGray()); // MINOR GUI - draw background gradient at top of window

    	for (PrefToolBarItem item : toolBarItems) {
    		final JButton btn = newToolBarButton(item.getLabel(), item.getActionCommand(), ImageFactory.getInstance().getIconPrefToolBar(item.getIcon()), this.preferencesController);
    		prefToolBarButtons.put(item, btn);
			bar.add(btn);
		}
    	
    	return bar;
    }
    
    private static JButton newToolBarButton(final String label, final String cmd, Icon icon, ActionListener listener) {
    	final JButton button = new JButton();
    	button.setActionCommand(cmd);
    	button.addActionListener(listener);
    	button.setText(label);
    	button.setIcon(icon);
    	button.setVerticalTextPosition(SwingConstants.BOTTOM);
    	button.setHorizontalTextPosition(SwingConstants.CENTER);
    	
    	button.setBorderPainted(false);
    	GuiUtil.enableHandCursor(button);
    	return button;
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
		this.preferencesController.doClose();
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
