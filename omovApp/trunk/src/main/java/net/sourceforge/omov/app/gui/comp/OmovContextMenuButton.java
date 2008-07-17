package net.sourceforge.omov.app.gui.comp;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import net.sourceforge.jpotpourri.jpotface.button.PtContextMenuButton;
import net.sourceforge.omov.app.util.AppImageFactory;

public class OmovContextMenuButton extends PtContextMenuButton {

	private static final long serialVersionUID = 5953463401111293260L;
	
	private static final Icon ICON = AppImageFactory.getInstance().getContextMenuButton();
	
	
	public OmovContextMenuButton(List<JMenuItem> popupItems, ActionListener listener) {
		super(ICON, popupItems, listener);
	}


}
