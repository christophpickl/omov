package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.util.Vector;

import javax.swing.JList;
import javax.swing.ListCellRenderer;

import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.app.gui.OmovListCellRenderer;

// http://nadeausoftware.com/articles/2008/01/java_tip_how_add_zebra_background_stripes_jlist
public class MacLikeList extends JList {

	private static final long serialVersionUID = 7467016349119119051L;

	public MacLikeList() {
		this.pseudoConstructor();
	}

	public MacLikeList(final Vector<String> items) {
		super(items);
		
		this.pseudoConstructor();
	}
	
	public MacLikeList(Object[] data) {
		super(data);

		this.pseudoConstructor();
	}
	
	private void pseudoConstructor() {
		this.setCellRenderer(new OmovListCellRenderer());
	}

	/******************************************************************************************************************/
	/** PAINT EMPTY ROWS
	/******************************************************************************************************************/
	
	private Color rowColors[] = new Color[] { Constants.getColorRowBackgroundEven(), Constants.getColorRowBackgroundOdd() };
	private boolean drawStripes = false;
	private RendererWrapper wrapper = null;
	
	public void paintComponent(Graphics g) {
		drawStripes = (getLayoutOrientation() == VERTICAL) && isOpaque();
		if (!drawStripes) {
			super.paintComponent(g);
			return;
		}

		final Insets insets = getInsets();
		final int w = getWidth() - insets.left - insets.right;
		final int h = getHeight() - insets.top - insets.bottom;
		final int x = insets.left;
		int y = insets.top;
		int nRows = 0;
		int startRow = 0;
		int rowHeight = getFixedCellHeight();
		if (rowHeight > 0) {
			nRows = h / rowHeight;
		} else {
			// Paint non-uniform height rows first
			final int nItems = getModel().getSize();
			rowHeight = 17; // A default for empty lists
			for (int i = 0; i < nItems; i++, y += rowHeight) {
				rowHeight = getCellBounds(i, i).height;
				g.setColor(rowColors[i & 1]);
				g.fillRect(x, y, w, rowHeight);
			}
			// Use last row height for remainder of list area
			nRows = nItems + (insets.top + h - y) / rowHeight;
			startRow = nItems;
		}
		for (int i = startRow; i < nRows; i++, y += rowHeight) {
			g.setColor(rowColors[i & 1]);
			g.fillRect(x, y, w, rowHeight);
		}
		final int remainder = insets.top + h - y;
		if (remainder > 0) {
			g.setColor(rowColors[nRows & 1]);
			g.fillRect(x, y, w, remainder);
		}

		// Paint component
		setOpaque(false);
		super.paintComponent(g);
		setOpaque(true);
	}

	/** Wrap a cell renderer to add zebra stripes behind list cells. */
	private class RendererWrapper implements javax.swing.ListCellRenderer {
		public ListCellRenderer ren = null;

		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
			final Component c = ren.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
			if (!isSelected && drawStripes) {
				c.setBackground(rowColors[index & 1]);
			}
			return c;
		}
	}


	/** Return the wrapped cell renderer. */
	public javax.swing.ListCellRenderer getCellRenderer() {
		final javax.swing.ListCellRenderer ren = super.getCellRenderer();
		if (ren == null) return null;
		
		if (wrapper == null) {
			wrapper = new RendererWrapper();
		}
		wrapper.ren = ren;
		return wrapper;
	}
}