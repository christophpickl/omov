package net.sourceforge.omov.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuItem;

public class SelectableContextMenuButton extends ContextMenuButton {

	private static final long serialVersionUID = 2844205041492573761L;


	private String recentSelectActionCommand;
	
	private final Map<String, JMenuItem> menuItemByCmd = new HashMap<String, JMenuItem>();
	
	
	public SelectableContextMenuButton(List<JMenuItem> popupItems, ActionListener clickedListener) {
		super(popupItems, clickedListener);
		
		final ActionListener changeListener =  new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				selectItem(menuItemByCmd.get(recentSelectActionCommand), false);
				selectItem(menuItemByCmd.get(event.getActionCommand()), true);
				
				recentSelectActionCommand = event.getActionCommand();
			}
		};
		
		
		
		for (JMenuItem menuItem : popupItems) {
			if(menuItem != null) {
				if(this.recentSelectActionCommand == null && menuItem.isEnabled() == true) {
					this.recentSelectActionCommand = menuItem.getActionCommand(); // default is first entry
					menuItem.setText("-  " + menuItem.getText());
				} else {
					menuItem.setText("  " + menuItem.getText());
				}
				menuItemByCmd.put(menuItem.getActionCommand(), menuItem);
				
				menuItem.addActionListener(changeListener);
			}
		}
	}

	public String getSelectedActionCommand() {
		return this.recentSelectActionCommand;
	}
	
	private void selectItem(JMenuItem item, boolean select) {
		final String newLabel;
		if(select == true) {
			newLabel = "- " + item.getText().substring(2); // MINOR GUI use JComboBox instead for popup menu (because it gots exact some logic like coded here)
		} else {
			newLabel = "  " + item.getText().substring(2);
		}
		
		item.setText(newLabel);
	}

}
