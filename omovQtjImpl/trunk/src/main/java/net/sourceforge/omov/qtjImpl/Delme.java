package net.sourceforge.omov.qtjImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.gui.PressableButton;
import net.sourceforge.omov.qtjImpl.QtjImageFactory.ButtonSmallScreenIcon;

public class Delme {
	
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		
		JPanel p = new JPanel();
		
		p.add(new JLabel("tut: "));
		ButtonSmallScreenIcon icon = ButtonSmallScreenIcon.PAUSE;
		JButton btn = new PressableButton(
				QtjImageFactory.getInstance().getButtonSmallScreen(icon),
				QtjImageFactory.getInstance().getButtonSmallScreenPressed(icon));
		btn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("pressed");
			}
		});
		p.add(btn);
		
		f.getContentPane().add(p);
		
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.pack();
		f.setLocationRelativeTo(null);
		f.setVisible(true);
	}
	
}
