package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.awt.Color;

public interface IMacColors {
	
	/**
	 * if editting value in cell, this will be background color of all other
	 * (unfocused) cells in that row
	 */
	// private static final Color MAC_UNFOCUSED_SELECTED_CELL_BACKGROUND_COLOR = new Color(0xc0c0c0);
	
	
	// for renderer
	public static final Color MAC_FOCUSED_SELECTED_CELL_HORIZONTAL_LINE_COLOR = new Color(0x7daaea);
	public static final Color MAC_UNFOCUSED_SELECTED_CELL_HORIZONTAL_LINE_COLOR = new Color(0xe0e0e0);
	public static final Color MAC_FOCUSED_UNSELECTED_VERTICAL_LINE_COLOR = new Color(0xd9d9d9);
	public static final Color MAC_FOCUSED_SELECTED_VERTICAL_LINE_COLOR = new Color(0x346dbe);
	public static final Color MAC_UNFOCUSED_SELECTED_VERTICAL_LINE_COLOR = new Color(0xacacac);

	public static final Color MAC_UNFOCUSED_UNSELECTED_VERTICAL_LINE_COLOR = new Color(0xd9d9d9);

}
