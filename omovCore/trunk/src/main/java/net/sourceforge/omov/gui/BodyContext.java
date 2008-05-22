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

package net.sourceforge.omov.gui;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import net.sourceforge.omov.core.util.GuiAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class BodyContext extends MouseAdapter implements ActionListener, KeyListener {

    private static final Log LOG = LogFactory.getLog(BodyContext.class);
    
    private final JTable table;
    private final JPopupMenu popupSingle = new JPopupMenu();
    private final JPopupMenu popupMultiple = new JPopupMenu();
    private final TableContextMenuListener listener;
    
    private int tableRowSelected = -1;
    private boolean isKeyDown = false;
    private boolean wasPopupShownSingle = false;
    
    public BodyContext(JTable table, List<JMenuItem> popupItemsSingle, List<JMenuItem> popupItemsMultiple, TableContextMenuListener listener) {
        this.table = table;
        this.listener = listener;
        this.table.addMouseListener(this);
        this.table.addKeyListener(this);

        for (JMenuItem item : popupItemsSingle) {
            if(item == null) {
                this.popupSingle.addSeparator();
            } else {
                item.addActionListener(this);
    //            item.addMouseListener(this);
                this.popupSingle.add(item);
            }
        }
        
        if(popupItemsMultiple != null) {
            for (JMenuItem item : popupItemsMultiple) {
                if(item == null) {
                    this.popupMultiple.addSeparator();
                } else {
                    item.addActionListener(this);
    //                item.addMouseListener(this);
                    this.popupMultiple.add(item);
                }
            }
        }
    }

    public static JMenuItem newJMenuItem(List<JMenuItem> items, String label, String actionCommand) {
        return newJMenuItem(items, label, actionCommand, null);
    }
    
    /**
     * utility method
     */
    public static JMenuItem newJMenuItem(List<JMenuItem> items, String label, String actionCommand, Icon icon) {
        JMenuItem item = (icon != null) ? new JMenuItem(label, icon) : new JMenuItem(label);
        item.setActionCommand(actionCommand);
        items.add(item);
        return item;
    }
    
    public static void newJMenuSeparator(List<JMenuItem> items) {
        items.add(null);
    }
    
    @Override
    public void mousePressed(MouseEvent event) {
        LOG.debug("mousePressed(); button()="+event.getButton()+"; isPopupTrigger="+event.isPopupTrigger());
        
//        final boolean isRightButton = event.isPopupTrigger(); // !!! does not work on windows (but on osx)
//        System.out.println("isPopupTrigger="+event.isPopupTrigger()); // event.consume();
        
        final boolean isRightButton = event.getButton() == MouseEvent.BUTTON3;
//        System.out.println("getButton = " + event.getButton() + "; b1 = " + MouseEvent.BUTTON1 + "; b2 = " + MouseEvent.BUTTON2 + "; b3 = " + MouseEvent.BUTTON3 + "; ");
        	
        if(isRightButton) {
//            System.out.println("event.isPopupTrigger() =>" + event.isPopupTrigger());
//            System.out.println("event.isConsumed()     => " + event.isConsumed());
//            System.out.println("event.isMetaDown()     => " + event.isMetaDown());
            
            if(this.isKeyDown == true) { // is this really necessary anymore ???
                LOG.debug("SwingUtilities says right button, but actual only (meta-)key is down :/");
                return;
            }
            
            final Point pointRightClick = SwingUtilities.convertPoint(event.getComponent(), event.getPoint(), table);
            this.tableRowSelected = this.table.rowAtPoint(pointRightClick);
            this.table.requestFocus();
            
            final int selectedRows = this.table.getSelectedRows().length;
            final JPopupMenu popup;
            if(selectedRows == 1) {
                this.wasPopupShownSingle = true;
                popup = this.popupSingle;
            } else if(selectedRows > 1){
                this.wasPopupShownSingle = false;
                popup = this.popupMultiple;
            } else {
                LOG.debug("selected rows: " + selectedRows +"; ignore right click");
                return;
            }
            popup.show(this.table, pointRightClick.x, pointRightClick.y);
        }
    }
    
    public void actionPerformed(final ActionEvent event) {
    	new GuiAction() {
			@Override
			protected void _action() {
				JMenuItem item = (JMenuItem) event.getSource();
		        LOG.debug("actionPerformed(cmd="+item.getActionCommand()+"; row="+tableRowSelected+"; selection=" + (wasPopupShownSingle?"single":"multiple")+")");
		        
		        if(event.getModifiers() == ActionEvent.META_MASK) {
		            LOG.debug("Ignoring performed action because modifier indicates that right button was clicked.");
		            return;
		        }
		        
		        if(wasPopupShownSingle == true) {
		            listener.contextMenuClicked(item, tableRowSelected);
		        } else {
		            listener.contextMenuClickedMultiple(item, table.getSelectedRows());
		        }
			}
    	}.doAction();
    }
    

    public void keyPressed(KeyEvent event) {
        this.isKeyDown = true;
    }

    public void keyReleased(KeyEvent event) {
        this.isKeyDown = false;
    }

    public void keyTyped(KeyEvent event) {
        // nothing to do
    }

    
}
