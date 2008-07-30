package net.sourceforge.omov.guicore;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public abstract class AbstractGuiKeyAdapter implements KeyListener {

	public abstract void keyPressedAction(final KeyEvent event);
	public abstract void keyReleasedAction(final KeyEvent event);
	public abstract void keyTypedAction(final KeyEvent event);
	
	public final void keyPressed(final KeyEvent event) {
		new GuiAction() {
			@Override
			protected void _action() {
				keyPressedAction(event);
			}
		}.doAction();
	}

	public final void keyReleased(final KeyEvent event) {
		new GuiAction() {
			@Override
			protected void _action() {
				keyReleasedAction(event);
			}
		}.doAction();
	}

	public final void keyTyped(final KeyEvent event) {
		new GuiAction() {
			@Override
			protected void _action() {
				keyTypedAction(event);
			}
		}.doAction();
	}
}
