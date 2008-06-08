package net.sourceforge.omov.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import net.sourceforge.omov.core.util.GuiAction;

// MANTIS 000xxx GUI - for all gui classes: use this handy trick as a shortcut for safe gui-actions! 
public abstract class GuiActionListener implements ActionListener {
	protected abstract void action(final ActionEvent event);

	public void actionPerformed(final ActionEvent event) {
		new GuiAction() {
			@Override
			protected void _action() {
				action(event);
			}
			
		}.doAction();
	}
	
}