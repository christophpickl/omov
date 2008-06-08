package net.sourceforge.omov.gui;

import javax.swing.Icon;
import javax.swing.JButton;

public class PressableButton extends JButton {

	private static final long serialVersionUID = 1784844960026759904L;

	public PressableButton(Icon iconNormal, Icon iconPressed) {
		super(iconNormal);
		this.setPressedIcon(iconPressed);

//		this.setRolloverEnabled(true);
//		this.setRolloverIcon(iconPressed);

		this.setBorderPainted(false);
		this.setFocusPainted(false);
		this.setContentAreaFilled(false);
	}

}
