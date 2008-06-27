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

package net.sourceforge.omov.app.gui.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.core.util.OmovGuiUtil;
import net.sourceforge.omov.gui.GuiActionListener;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class DebugDatabaseContents extends JFrame {

	private static final long serialVersionUID = -4684617117221230671L;
	
	private final JTable table = new JTable();
	// TODO DebugDatabaseContents work in progress -> implement me
	public DebugDatabaseContents() {
		super("Database Contents");
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.getContentPane().add(this.initComponents());
		this.pack();
		OmovGuiUtil.setCenterLocation(this);
		this.showMovieContents();
	}
	
	private JPanel initComponents() {
		final JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(new JScrollPane(this.table), BorderLayout.CENTER);
		
		JButton btnMovie = new JButton("Show Movie Contents");
		btnMovie.addActionListener(new GuiActionListener() {
			public void action(ActionEvent e) {
				showMovieContents();
			}
		});
		panel.add(btnMovie, BorderLayout.SOUTH);
		
		return panel;
	}
	
	private void showMovieContents() {
		try {
			this.table.setModel(new TableModel(BeanFactory.getInstance().getMovieDao().getMoviesSorted(), Movie.class));
			this.table.repaint();
		} catch (BusinessException e) {
			throw new FatalException("Could not get database contents!", e);
		}
	}
	
	private static class TableModel extends DefaultTableModel {

		private static final long serialVersionUID = 123097421428534313L;
		private final List<?> data;
//		private final List<String> columns = new ArrayList<String>();
		
		public TableModel(List<?> data, Class<?> dataClass) {
			this.data = data;
			
			for (Field field : dataClass.getDeclaredFields()) {
				if( (field.getModifiers() & Modifier.STATIC) == Modifier.STATIC) {
					continue;
				}
				if(field.getName().endsWith("String")) {
					continue;
				}
				
				System.out.println(field.getName());
			}
		}
		
	    public Object getValueAt(int row, int col) {
	    	return "data";
	    }
	    
	    public int getRowCount() {
	    	if(this.data == null) return 0;
	    	return this.data.size();
	    }

	    public int getColumnCount() {
	    	return 1;
	    }

	    public String getColumnName(final int col) {
	    	return "x";
	    }

	    public boolean isCellEditable(int rowIndex, int columnIndex) {
	        return false;
	    }
	}
	
	public static void main(String[] args) {
		new DebugDatabaseContents().setVisible(true);
	}
}
