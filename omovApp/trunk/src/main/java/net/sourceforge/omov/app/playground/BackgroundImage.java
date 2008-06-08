package net.sourceforge.omov.app.playground;

import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.app.util.AppImageFactory;

public class BackgroundImage {
	
	public static void main(String[] args) {
		JFrame f = new JFrame();
		f.getContentPane().add(new PanelX());
		f.setVisible(true);
	}
	
	private static class PanelX extends JPanel {
		private static final long serialVersionUID = 1L;
		private final Image image = AppImageFactory.getInstance().getAboutLogo().getImage();
		public PanelX() {
			this.add(new JLabel("PanelX"));
		}
		public void paint(Graphics g) {
			super.paint(g);
			GuiUtil.paintCenteredBackgroundImage(g, this, image);
		}
	}
	
}
