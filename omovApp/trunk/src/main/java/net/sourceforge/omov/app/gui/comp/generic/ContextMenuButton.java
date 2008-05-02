package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.WindowConstants;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.ImageFactory;

public class ContextMenuButton extends JButton {
	
	private static final long serialVersionUID = -2658781971359830733L;

	public static void main(String[] args) {
		
		JFrame f = new JFrame();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		ActionListener al = new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.out.println("cmb clicked: " + e.getActionCommand());
			}
		};
		
		List<JMenuItem> popupItems = new ArrayList<JMenuItem>();
		JMenuItem item1 = new JMenuItem("Open");
		JMenuItem item2 = new JMenuItem("Save");
		item1.addActionListener(al);
		item2.addActionListener(al);
		popupItems.add(item1);
		popupItems.add(item2);
		
		JPanel p = new JPanel();
		p.add(new JLabel("press me"));
		ContextMenuButton cmb = new ContextMenuButton(popupItems);
		p.add(cmb);
		
		
		f.add(p);
		f.setSize(300, 200);
		GuiUtil.setCenterLocation(f);
		f.setVisible(true);
	}
	
	private final JPopupMenu popupMenu = new JPopupMenu();
	
	public ContextMenuButton(List<JMenuItem> popupItems) {
		super(ImageFactory.getInstance().getContextMenuButton());
		this.setBorderPainted(false);
		GuiUtil.enableHandCursor(this);
		
		for (JMenuItem menuItem : popupItems) {
			if(menuItem == null) {
				this.popupMenu.addSeparator();
			} else {
				this.popupMenu.add(menuItem);
			}
		}
		
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Point p = ContextMenuButton.this.getMousePosition();
				popupMenu.show(ContextMenuButton.this, p.x, p.y);
			}
		});
		// popup.show(this.table, pointRightClick.x, pointRightClick.y);
	}
}
