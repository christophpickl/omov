package net.sourceforge.omov.app.playground;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

// TODO instead of generic "Yes/No" confirmation dialogs, use own label, e.g. "Delete/Cancel"
public class ConfirmDialog {
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.setVisible(true);
		
		System.out.println("opt pane");
		JOptionPane p = new JOptionPane();
		p.setMessage("Das ist message");
		p.setWantsInput(false);
		p.setOptionType(JOptionPane.INFORMATION_MESSAGE);
		p.setName("name");
		p.setVisible(true);
		System.out.println("finished");
		
		f.getContentPane().add(p);
	}
}
