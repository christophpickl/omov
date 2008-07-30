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
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.Graphics2D;
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

import net.sourceforge.jpotpourri.jpotface.IPtEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.jpotface.PtEscapeDisposer;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.app.gui.main.MainWindowController;
import net.sourceforge.omov.app.gui.preferences.PreferencesWindowController.AbstractPreferencesContent;
import net.sourceforge.omov.app.gui.preferences.PreferencesWindowController.PrefToolBarItem;
import net.sourceforge.omov.app.util.AppImageFactory;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.gui.OmovGuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class PreferencesWindow extends JDialog implements IPtEscapeDisposeReceiver {

    private static final long serialVersionUID = -3157582134656737177L;
    private static final Log LOG = LogFactory.getLog(PreferencesWindow.class);

    private final PreferencesWindowController preferencesController;
        
    private boolean escapeHit;
    
    private final JPanel wrapPanel = new JPanel();
    
    private final List<PrefToolBarItem> toolBarItems;

    private Map<PrefToolBarItem, JButton> prefToolBarButtons = new HashMap<PrefToolBarItem, JButton>();
    
    private PrefToolBarItem recentPrefToolBarItem;
    
    
    
    public PreferencesWindow(JFrame owner, MainWindowController mainController) {
        super(owner, true);
        this.setTitle("Preferences");
        this.preferencesController = new PreferencesWindowController(this, mainController);
        
        {
	        List<PrefToolBarItem> tmp = new ArrayList<PrefToolBarItem>(2);
	    	tmp.add(this.preferencesController.prefItemGeneral);
	    	tmp.add(this.preferencesController.prefItemQuickView);
	    	tmp.add(this.preferencesController.prefItemAdvanced);
	    	toolBarItems = Collections.unmodifiableList(tmp);
        }

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(final WindowEvent event) {
                preferencesController.doClose();
            }
        });
        PtEscapeDisposer.enableEscape(this.getRootPane(), this);
        
        
        OmovGuiUtil.macSmallWindow(this.getRootPane());
        
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
        
        this.adjustSize();
        
        this.setResizable(false);
        PtGuiUtil.setCenterLocation(this);
    }
    
    private void adjustSize() {
    	int minWidth = -1;
    	int minHeight = -1;
    	
    	for (PrefToolBarItem item : this.toolBarItems) {
			final AbstractPreferencesContent content = item.getContent();
			final Dimension size = content.getPreferredSize();
			LOG.debug("PrefContent preferred size for " + item.getLabel() + ": " + size.width+"x"+size.height);
			if(size.width > minWidth) {
				minWidth = size.width;
			}
			if(size.height > minHeight) {
				minHeight = size.height;
			}
		}
    	// TODO GUI hack :)
    	minWidth += 40; // adjusting does not work properly; advanced content got not enough space to draw whole content
    	
    	LOG.debug("Adjusting wrap panel size to: " + minWidth + "x" + minHeight);
    	final Dimension minDimension = new Dimension(minWidth, minHeight);
    	this.wrapPanel.setSize(minDimension);
    	this.wrapPanel.setMinimumSize(minDimension);
    	this.wrapPanel.setPreferredSize(minDimension);
    	this.pack();
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
    
    void switchContent(PrefToolBarItem item) {
    	LOG.debug("Switching content to new toolbar item '"+item.getLabel()+"'.");
    	if(this.recentPrefToolBarItem != null) {
    		this.prefToolBarButtons.get(this.recentPrefToolBarItem).setEnabled(true);
    	}
    	this.prefToolBarButtons.get(item).setEnabled(false);
    	this.wrapPanel.removeAll();
    	this.wrapPanel.add(item.getContent());
    	this.wrapPanel.revalidate();
    	this.wrapPanel.repaint();
    	this.recentPrefToolBarItem = item;
    }
    
    private JToolBar initToolBar() {
    	final JToolBar bar = new JToolBar() {
			private static final long serialVersionUID = 1L;
			@Override
    		public void paint(final Graphics g) {
				PtGuiUtil.paintGradient((Graphics2D) g, 
    					Constants.getColorLightGray(), Constants.getColorWindowBackground(), this.getSize());
    			super.paint(g);
    		}
    	};

    	bar.setOpaque(false);
    	bar.setFloatable(false);
    	bar.setOrientation(SwingConstants.HORIZONTAL);
//    	bar.setBorderPainted(false);
//    	bar.setRollover(true);
//    	bar.setLayout(int)
//    	bar.setMargin(Insets)
    	

    	for (PrefToolBarItem item : toolBarItems) {
    		final JButton btn = newToolBarButton(item.getLabel(), item.getActionCommand(), AppImageFactory.getInstance().getIconPrefToolBar(item.getIcon()), this.preferencesController);
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
    	PtGuiUtil.enableHandCursor(button);
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
	
	@Override
	public void setVisible(boolean visible) {
		if(visible == true) {
			// reset visible-lifetime data
			this.escapeHit = false;
		}
		super.setVisible(visible);
	}
}
