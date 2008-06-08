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
import java.util.List;

import javax.swing.Icon;
import javax.swing.JButton;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import net.sourceforge.omov.core.util.SimpleGuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ContextMenuButton extends JButton {
	
	private static final long serialVersionUID = -2658781971359830733L;
	
	private final JPopupMenu popupMenu = new JPopupMenu();
	
	/**
	 * 
	 * @param popupItems if added a null-value in list -> will add a separator line
	 * @param listener attache dto every menu item; can be null
	 */
	public ContextMenuButton(Icon icon, List<JMenuItem> popupItems, ActionListener listener) { // TODO check if guisafe actionlistener
		super(icon);
		
		this.setBorderPainted(false);
		SimpleGuiUtil.enableHandCursor(this);
		
		for (JMenuItem menuItem : popupItems) {
			if(menuItem == null) {
				this.popupMenu.addSeparator();
			} else {
				this.popupMenu.add(menuItem);
				if(listener != null) {
					menuItem.addActionListener(listener);
				}
			}
		}
		
		this.addActionListener(new GuiActionListener() {
			@Override
			protected void action(final ActionEvent event) {
				final Point p = ContextMenuButton.this.getMousePosition();
				
				// would be dirty hack to get mouse position just because of existing GlassPane
				if(p == null) {
//					System.out.println("Could not get mouse position, therefore going to use position relative to glass pane.");
//					p = ContextMenuButton.this.getRootPane().getGlassPane().getMousePosition(); ... does NOT return correct position!
					throw new NullPointerException("Could not get mouse position! (maybe an existing GlassPane overlays content)");
				}
				popupMenu.show(ContextMenuButton.this, p.x, p.y);
			}
		});
	}
		
	
	


//	public static void main(String[] args) {
//		
//		JFrame f = new JFrame();
//		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
//
//		GuiActionListener al = new GuiActionListener() {
//			public void action(ActionEvent e) {
//				System.out.println("cmb clicked: " + e.getActionCommand());
//			}
//		};
//		
//		List<JMenuItem> popupItems = new ArrayList<JMenuItem>();
//		JMenuItem item1 = new JMenuItem("Open");
//		JMenuItem item2 = new JMenuItem("Save");
//		item1.addActionListener(al);
//		item2.addActionListener(al);
//		popupItems.add(item1);
//		popupItems.add(item2);
//		
//		JPanel p = new JPanel();
//		p.add(new JLabel("press me"));
//		ContextMenuButton cmb = new ContextMenuButton(xyz, popupItems, null);
//		p.add(cmb);
//		
//		
//		f.add(p);
//		f.setSize(300, 200);
//		SimpleGuiUtil.setCenterLocation(f);
//		f.setVisible(true);
//	}
}
