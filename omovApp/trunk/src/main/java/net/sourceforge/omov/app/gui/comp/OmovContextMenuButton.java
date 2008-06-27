package net.sourceforge.omov.app.gui.comp;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import net.sourceforge.omov.app.util.AppImageFactory;
import at.ac.tuwien.e0525580.jlib.gui.widget.ContextMenuButton;

public class OmovContextMenuButton extends ContextMenuButton {

	private static final long serialVersionUID = 5953463401111293260L;
	
	private static final Icon ICON = AppImageFactory.getInstance().getContextMenuButton();
	
	
	public OmovContextMenuButton(List<JMenuItem> popupItems, ActionListener listener) {
		super(ICON, popupItems, listener);
	}


}
