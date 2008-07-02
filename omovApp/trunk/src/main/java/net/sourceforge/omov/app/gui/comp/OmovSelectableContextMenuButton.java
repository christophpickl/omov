package net.sourceforge.omov.app.gui.comp;

import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.Icon;
import javax.swing.JMenuItem;

import net.sourceforge.jpotpourri.gui.widget.SelectableContextMenuButton;
import net.sourceforge.omov.app.util.AppImageFactory;

public class OmovSelectableContextMenuButton extends SelectableContextMenuButton {

	private static final long serialVersionUID = 3800794998784546573L;

	private static final Icon ICON = AppImageFactory.getInstance().getContextMenuButton();
	
	public OmovSelectableContextMenuButton(List<JMenuItem> popupItems, ActionListener clickedListener) {
		super(ICON, popupItems, clickedListener);
	}


}
