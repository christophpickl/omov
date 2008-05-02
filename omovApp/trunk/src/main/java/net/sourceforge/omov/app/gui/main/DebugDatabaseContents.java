package net.sourceforge.omov.app.gui.main;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.DefaultTableModel;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.bo.Movie;

class DebugDatabaseContents extends JFrame {

	private static final long serialVersionUID = -4684617117221230671L;
	
	private final JTable table = new JTable();
	
	public DebugDatabaseContents() {
		super("Database Contents");
		this.setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		this.setResizable(true);
		this.getContentPane().add(this.initComponents());
		this.pack();
		GuiUtil.setCenterLocation(this);
		this.showMovieContents();
	}
	
	private JPanel initComponents() {
		final JPanel panel = new JPanel(new BorderLayout());
		
		panel.add(new JScrollPane(this.table), BorderLayout.CENTER);
		
		JButton btnMovie = new JButton("Show Movie Contents");
		btnMovie.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private static class TableModel extends DefaultTableModel {

		private static final long serialVersionUID = 123097421428534313L;
		private final List<?> data;
		private final List<String> columns = new ArrayList<String>();
		
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
