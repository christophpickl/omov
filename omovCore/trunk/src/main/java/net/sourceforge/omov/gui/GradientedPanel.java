package net.sourceforge.omov.gui;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.core.util.SimpleGuiUtil;

public class GradientedPanel extends JPanel {

	private static final long serialVersionUID = 3189367251606845575L;

	private final Color colorTop;
	private final Color colorBottom;
	

	public GradientedPanel(Color colorTop, Color colorBottom) {
		this.colorTop = colorTop;
		this.colorBottom = colorBottom;
		this.setOpaque(false);
	}
	
	public void paint(final Graphics g) {
		SimpleGuiUtil.paintGradient((Graphics2D) g, this.colorTop, this.colorBottom, this.getSize());
		super.paint(g);
	}
	
	

	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		JPanel p = new GradientedPanel(Color.WHITE, Color.GRAY);
		p.add(new JLabel("<html>dies ist ein lagner text<br>er kann<br>ganz<br>ganz<br>ganz<br>viel.</html>"));
		f.getContentPane().add(p);
		f.pack();
		f.setVisible(true);
	}
}
