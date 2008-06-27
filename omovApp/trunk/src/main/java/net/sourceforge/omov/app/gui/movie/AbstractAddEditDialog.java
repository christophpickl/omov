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

package net.sourceforge.omov.app.gui.movie;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;

import net.sourceforge.omov.gui.GuiActionListener;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.jlib.gui.EscapeDisposer;
import at.ac.tuwien.e0525580.jlib.gui.IEscapeDisposeReceiver;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public abstract class AbstractAddEditDialog<T> extends JDialog implements IEscapeDisposeReceiver {

    private static final Log LOG = LogFactory.getLog(AbstractAddEditDialog.class);
    
    private T editItem;
    
    private final boolean isAddMode;
    
    private boolean actionConfirmed = false;
    

    public AbstractAddEditDialog(JFrame owner, T editObject) {
        super(owner, true);
        
        this.editItem = editObject;
        this.isAddMode = editObject == null;

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });
        EscapeDisposer.enableEscape(this.getRootPane(), this);
        
        this.setResizable(false);
    }
    

    
    protected final JPanel newCommandPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        
        final JButton btnCancel = new JButton("Cancel");
        final JButton btnConfirm = new JButton(isAddMode() ? "Create" : "Update");
        this.rootPane.setDefaultButton(btnConfirm);
        
        btnCancel.setOpaque(false);
        btnConfirm.setOpaque(false);
        
        btnCancel.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
                doCancel();
        }});
        btnConfirm.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
                doConfirm();
        }});
        
        panel.add(btnCancel);
        panel.add(btnConfirm);
        
        return panel;
    }
    
    
    

    protected abstract T _getConfirmedObject();
    
    public final T getConfirmedObject() {
        assert(this.isActionConfirmed() == true);
        
        final T confirmedObject = this._getConfirmedObject();
        LOG.debug("Returning confirmed object: " + confirmedObject);
        return confirmedObject;
    }
    
    protected final boolean isAddMode() {
        return this.isAddMode;
    }
    
    protected T getEditItem() {
        assert(this.isAddMode == false);
        return this.editItem;
    }
    protected void setEditItem(T editItem) {
        LOG.debug("setting edit item to: " + editItem);
        this.editItem = editItem;
    }
    

    protected final void doConfirm() {
        LOG.debug("doConfirm()");
        this.actionConfirmed = true;
        this.dispose();
    }
    /**
     * used by prev/next buttons in movie dialog
     */
    protected final void doConfirmWithoutDispose() {
        LOG.debug("doConfirmWithoutDispose()");
        this.actionConfirmed = true;
    }
    
    public final boolean isActionConfirmed() {
        return this.actionConfirmed;
    }

    protected final void doCancel() {
        LOG.debug("doCancel()");
        this.dispose();
    }
    
    


	public void doEscape() {
		LOG.debug("doEscape()");
		this.doCancel();
	}
}
