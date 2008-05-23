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

package net.sourceforge.omov.app.gui.smartfolder;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sourceforge.omov.app.gui.EscapeDisposer;
import net.sourceforge.omov.app.gui.EscapeDisposer.IEscapeDisposeReceiver;
import net.sourceforge.omov.app.help.HelpEntry;
import net.sourceforge.omov.app.help.HelpSystem;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.model.ISmartFolderDao;
import net.sourceforge.omov.core.model.ISmartFolderDaoListener;
import net.sourceforge.omov.core.smartfolder.SmartFolder;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.gui.MacLikeList;
import net.sourceforge.omov.gui.MacLikeListCellRenderer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SmartFolderManageDialog extends JDialog implements ActionListener, IEscapeDisposeReceiver {
    
    private static final long serialVersionUID = -6096464702194946519L;
    private static final Log LOG = LogFactory.getLog(SmartFolderManageDialog.class);

    private static final String CMD_ADD = "Add";
    private static final String CMD_EDIT = "Edit";
    private static final String CMD_DELETE = "Delete";
     
    
    private final JList smartFolderList = new MacLikeList();
    private final FolderModel listModel = new FolderModel();
    private final SmartFolderManageController controller;
    
    public SmartFolderManageDialog(JFrame owner) {
        super(owner, true);
        
        this.controller = new SmartFolderManageController(this, owner);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        EscapeDisposer.enableEscape(this.getRootPane(), this);
        
        
        this.setTitle("SmartFolders");
        GuiUtil.macSmallWindow(this.getRootPane());
        
        this.initComponents();
        this.setSize(300, 180);
//        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private void initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());

        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        this.smartFolderList.setVisibleRowCount(5);
        this.smartFolderList.setModel(this.listModel);
        this.smartFolderList.setCellRenderer(new MacLikeListCellRenderer());
        
        
        // doubleclick on smartfolder list opens the edit dialog
        this.smartFolderList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent event) {
                LOG.debug("mouseClicked on moviesTable: event.getButton()="+event.getButton()+"; clickCount="+event.getClickCount()+"");
                int row = smartFolderList.getSelectedIndex();
                if (row > -1 && event.getClickCount() >= 2) {
                    LOG.debug("Double clicked on table row "+row+"; displaying editDialog.");
                    controller.doEditSmartFolder(listModel.getSmartFolderAt(row));
                }
                
            }
        });
        
        
        final JScrollPane scroll = new JScrollPane(this.smartFolderList);
        scroll.setWheelScrollingEnabled(true);
        panel.add(scroll, BorderLayout.CENTER);
        panel.add(this.newPanelButtons(), BorderLayout.EAST);
        
        
        this.getContentPane().add(panel);
    }
    
    private JPanel newPanelButtons() {
        final JButton btnAdd = new JButton("Add");
        final JButton btnEdit = new JButton("Edit");
        final JButton btnDelete = new JButton("Delete");
        this.getRootPane().setDefaultButton(btnAdd);

        btnAdd.setOpaque(false);
        btnEdit.setOpaque(false);
        btnDelete.setOpaque(false);
        
        btnAdd.setActionCommand(CMD_ADD);
        btnEdit.setActionCommand(CMD_EDIT);
        btnDelete.setActionCommand(CMD_DELETE);
        btnAdd.addActionListener(this);
        btnEdit.addActionListener(this);
        btnDelete.addActionListener(this);
        
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        c.anchor = GridBagConstraints.CENTER;
        c.insets = new Insets(0, 10, 4, 0);
        c.fill = GridBagConstraints.HORIZONTAL;
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(btnAdd, c);
        c.gridy++;
        panel.add(btnEdit, c);
        c.gridy++;
        c.insets = new Insets(0, 10, 14, 0); // more margin between delete button and help icon
        panel.add(btnDelete, c);
        c.gridy++;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(0, 10, 0, 0);
        panel.add(HelpSystem.newButton(HelpEntry.SMARTFOLDER, "What are SmartFolders?"), c);

        return panel;
    }

    // buttons add, edit, delete clicked
    public void actionPerformed(final ActionEvent event) {
        new GuiAction() { protected void _action() {
            final String cmd = event.getActionCommand();
            LOG.info("button with command '"+cmd+"' clicked.");
            
            if(cmd.equals(CMD_ADD)) {
                controller.doAddSmartFolder();
                
            } else if(cmd.equals(CMD_EDIT)) {
                final int index = smartFolderList.getSelectedIndex();
                if(index < 0) return; // nothing selected
                
                
                final SmartFolder folder = listModel.getSmartFolderAt(index);
                LOG.debug("edit on position: " + index + "; folder: " + folder);
                controller.doEditSmartFolder(folder);
                
            } else if(cmd.equals(CMD_DELETE)) {
                final int index = smartFolderList.getSelectedIndex();
                if(index < 0) {
                    LOG.debug("nothing selected, nothing deleted.");
                    return;
                }
                
                final SmartFolder folder = listModel.getSmartFolderAt(index);
                LOG.debug("deleting on position: " + index + "; folder: " + folder);
                controller.doDeleteSmartFolder(folder);
                
            } else {
                throw new IllegalArgumentException("unhandled command '"+cmd+"'!");
            }
        }}.doAction();
    }
    
    public JFrame getOwner() {
        return (JFrame) super.getOwner();
    }
    

    public void escapeEntered() {
        this.dispose();
    }

    
    
    private static class FolderModel extends AbstractListModel implements ISmartFolderDaoListener {
        
        private static final long serialVersionUID = -3911623931659548495L;
        private static final ISmartFolderDao dao = BeanFactory.getInstance().getSmartFolderDao();
        private List<SmartFolder> folders;
        
        public FolderModel() {
            dao.registerMovieDaoListener(this);
            this.reloadData();
        }
        
        public String getElementAt(int index) {
            return this.folders.get(index).getName();
        }
        public SmartFolder getSmartFolderAt(int index) {
            return this.folders.get(index);
        }

        public int getSize() {
            return this.folders.size();
        }
        
        public void smartFolderDataChanged() {
            this.reloadData();
        }
        
        private void reloadData() {
            LOG.info("Reloading data...");
            try {
                this.folders = dao.getAllSmartFoldersSorted();
                LOG.debug("Reloaded "+this.folders.size()+" smartfolders.");
                this.fireContentsChanged(this, 0, this.folders.size()-1);
            } catch (BusinessException e) {
                throw new FatalException("Could not reload data for smart folders!", e);
            }
        }
        
    }

    private void doClose() {
    	this.dispose();
    }

	public void doEscape() {
		this.doClose();
	}
    
}
