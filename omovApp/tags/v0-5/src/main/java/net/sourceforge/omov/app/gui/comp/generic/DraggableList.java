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

package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DraggableList extends MacLikeList {

	private static final long serialVersionUID = -2907146218495589836L;
	private int from;
	
	public DraggableList(final Vector<String> items) {
		super(items);

		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent m) {
				from = getSelectedIndex();
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent m) {
				int to = getSelectedIndex();
				if (to == from)
					return;
				String s = items.remove(from);
				items.add(to, s);
				from = to;
			}
		});
	}
	
	public List<String> getItems() {
		ListModel model = this.getModel();
		List<String> list = new ArrayList<String>(model.getSize());
		for (int i = 0; i < model.getSize(); i++) {
			list.add((String) model.getElementAt(i));
		}
		return list;
	}
}
