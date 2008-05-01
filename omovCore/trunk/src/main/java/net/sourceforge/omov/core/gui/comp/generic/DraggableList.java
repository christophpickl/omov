package net.sourceforge.omov.core.gui.comp.generic;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

public class DraggableList extends MacLikeList {

	private static final long serialVersionUID = -2907146218495589836L;
	private int from;
	
	public DraggableList(final Vector<String> items) {
		super(items);

		this.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		
		this.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent m) {
				from = getSelectedIndex();
			}
		});
		
		this.addMouseMotionListener(new MouseMotionAdapter() {
			public void mouseDragged(MouseEvent m) {
				int to = getSelectedIndex();
				if (to == from)
					return;
				String s = items.remove(from);
				items.add(to, s);
				from = to;
			}
		});
	}
	
	public List<String> getItems() {
		ListModel model = this.getModel();
		List<String> list = new ArrayList<String>(model.getSize());
		for (int i = 0; i < model.getSize(); i++) {
			list.add((String) model.getElementAt(i));
		}
		return list;
	}
}
