package net.sourceforge.omov.app.gui.preferences;

import javax.swing.ComboBoxModel;

public interface IComboBoxModelX<T> extends ComboBoxModel {
	
	T getTypedElementAt(int index);
	
	int getItemIndex(T item);
	
}