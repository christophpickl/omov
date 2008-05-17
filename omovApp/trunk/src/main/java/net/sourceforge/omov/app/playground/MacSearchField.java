package net.sourceforge.omov.app.playground;

import javax.swing.JFrame;
import javax.swing.JTextField;

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
