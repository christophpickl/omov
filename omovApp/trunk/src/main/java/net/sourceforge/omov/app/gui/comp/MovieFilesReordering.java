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

package net.sourceforge.omov.app.gui.comp;

import java.awt.BorderLayout;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import net.sourceforge.omov.app.gui.comp.generic.DraggableList;
import net.sourceforge.omov.app.util.GuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MovieFilesReordering extends JDialog implements ActionListener {

	private static final long serialVersionUID = 6755261514670084157L;

	private static final String CMD_CANCEL = "CMD_CANCEL";
	private static final String CMD_CONFIRM = "CMD_CONFIRM";

	
	private boolean confirmed = false;
	
	private final DraggableList list;
	
	public MovieFilesReordering(Dialog owner, List<String> files) {
		super(owner, "Reorder Files", true);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		
		this.list = new DraggableList(new Vector<String>(files));
		this.list.setVisibleRowCount(4);
		
		JPanel panel = new JPanel(new BorderLayout());
		panel.add(new JScrollPane(this.list), BorderLayout.CENTER);
		panel.add(this.panelSouth(), BorderLayout.SOUTH);
		GuiUtil.macSmallWindow(this.getRootPane());
		this.getContentPane().add(panel);
		this.pack();
		this.setResizable(false);
		GuiUtil.setCenterLocation(this);
	}
	
	private JPanel panelSouth() {
		final JPanel panel = new JPanel();

		JButton btnCancel = new JButton("Cancel");
		btnCancel.setActionCommand(CMD_CANCEL);
		JButton btnConfirm = new JButton("Update");
		btnConfirm.setActionCommand(CMD_CONFIRM);
		btnCancel.addActionListener(this);
		btnConfirm.addActionListener(this);
		panel.add(btnCancel);
		panel.add(btnConfirm);
		this.getRootPane().setDefaultButton(btnConfirm);
		
		return panel;
	}

	public void actionPerformed(ActionEvent e) {
		final String cmd = e.getActionCommand();
		if(cmd.equals(CMD_CONFIRM)) {
			this.confirmed = true;
			this.dispose();
		} else if(cmd.equals(CMD_CANCEL)) {
			this.dispose();
		} else {
			throw new IllegalArgumentException("Unhandled action command '"+cmd+"'!");
		}
	}
	
	public List<String> getConfirmedList() {
		assert(this.isConfirmed());
		return this.list.getItems();
	}
	
	public boolean isConfirmed() {
		return this.confirmed;
	}
	
}
