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

package net.sourceforge.omov.app.playground;

import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MacSearchField {

	public static void main(String[] args) {
		JFrame f = new JFrame();
		
		SearchField s = new SearchField();
		
		f.getContentPane().add(s);
		
		f.pack();
		f.setVisible(true);
	}
	
	private static class SearchField extends JTextField {
		private static final long serialVersionUID = 1L;

		public SearchField() {
			this.putClientProperty("JTextField.variant", "search");
		}
	}
	
}
