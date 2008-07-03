/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.gui.table;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;

import net.sourceforge.jpotpourri.gui.IMacColors;
import net.sourceforge.jpotpourri.tools.UserSniffer;
import net.sourceforge.omov.core.Constants;

import org.jdesktop.swingx.JXTable;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MacLikeTable extends JXTable implements IMacColors {

    private static final long serialVersionUID = -4690492128203374606L;

    // TODO GUI - vertikalen linien gehen nicht ganz bis hinunter durch, wenn tabelle noch nicht ganz gefuellt ist!


    public static void main(String[] args) {
        final MacLikeTable table = new MacLikeTable(new DefaultTableModel() {
            private static final long serialVersionUID = 1L;
            public int getRowCount() {
                return 2;
            }
            public String getColumnName(int col) {
                return col == 0 ? "Text" : "Bool";
            }
            public Class<?> getColumnClass(int col) {
                return String.class;
            }
            public int getColumnCount() {
                return 2;
            }
            public Object getValueAt(int row, int col) {
                if(col == 0) {
                    return "row " + row;
                }
                return row == 0 ? "das ist mein ganz langer text -- jaja, das ist er" : "hubert franz von goisner";
            }
        });

        final JFrame frame = new JFrame();
        frame.getContentPane().add(new JScrollPane(table));
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }

    public MacLikeTable(TableModel model) {
        super(model);

        this.setShowGrid(false);
        this.setShowHorizontalLines(false);
        this.setShowVerticalLines(true);
//        this.setGridColor(gridColor)
        this.setIntercellSpacing(new Dimension());
        // Work-around for Apple 4352937.
//        JLabel.class.cast(getTableHeader().getDefaultRenderer()).setHorizontalAlignment(SwingConstants.LEADING); // cast exception: org.jdesktop.swingx.table.ColumnHeaderRenderer
    }

    /******************************************************************************************************************/
    /** RENDERER
    /******************************************************************************************************************/

//    public Component prepareEditor(TableCellEditor editor, int row, int column) {
//        final Component c = super.prepareEditor(editor, row, column);
//
//        if (c instanceof JComponent) {
//            JComponent jc = (JComponent) c;
//            // jc.setOpaque(true);
//            if (getCellSelectionEnabled() == false) {
//                fixMacOsCellRendererBorder(jc, false, false);
//            }
//        }
//        return c;
//    }

    public Component prepareRenderer(TableCellRenderer renderer, int row, int column) {
        Component c = super.prepareRenderer(renderer, row, column);

        boolean focused = hasFocus();
        boolean selected = isCellSelected(row, column);
        if (selected) {
            if (focused == false) {
            	// selected, but got no focus
                c.setBackground(Constants.getColorSelectedBackgroundNoFocus());
                c.setForeground(Color.BLACK); 
            } else {
                c.setBackground(Constants.getColorSelectedBackground());
                c.setForeground(Constants.getColorSelectedForeground());
            }
        } else {
            c.setBackground(colorForRow(row));
            c.setForeground(UIManager.getColor("Table.foreground"));
        }
//        System.out.print("... class "+c.getClass().getSimpleName()+"; selected "+selected+"; focused "+focused+"; getCellSelectionEnabled "+getCellSelectionEnabled()+"; isEditing "+isEditing()+" ... ");
        if (c instanceof JComponent) {
            JComponent jc = (JComponent) c;
            // jc.setOpaque(true);

            if (getCellSelectionEnabled() == false) { // && isEditing() == false) {
                fixMacOsCellRendererBorder(jc, selected, focused, isEditing());
//                System.out.print("FIXED");
            }

            initToolTip(jc, row, column);
        }
//        System.out.println();

        return c;
    }

    // MINOR GUI - does not work properly (also, isEditing parameter is not used)
    @SuppressWarnings("unused")
    protected void fixMacOsCellRendererBorder(JComponent renderer, boolean selected, boolean focused, boolean isEditing) {
        Border border;
        if (selected) {
            border = BorderFactory.createMatteBorder(0, 0, 1, 0, focused ? MAC_COLOR_FOCUSED_SELECTED_CELL_HORIZONTAL_LINE : MAC_COLOR_UNFOCUSED_SELECTED_CELL_HORIZONTAL_LINE);
        } else {
            border = BorderFactory.createEmptyBorder(0, 0, 1, 0);
        }

        if (getShowVerticalLines()) {
            final Color verticalLineColor;
            if (focused) {
                verticalLineColor = selected ? MAC_COLOR_FOCUSED_SELECTED_VERTICAL_LINE : MAC_COLOR_FOCUSED_UNSELECTED_VERTICAL_LINE;
            } else {
//                if(isEditing && row == selectedRow) {
//                    verticalLineColor = dark grey thing: MAC_FOCUSED_SELECTED_VERTICAL_LINE_COLOR?
//                } else {
                    verticalLineColor = selected ? MAC_COLOR_UNFOCUSED_SELECTED_VERTICAL_LINE : MAC_COLOR_UNFOCUSED_UNSELECTED_VERTICAL_LINE;
//                }
            }
            Border verticalBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, verticalLineColor);
            border = BorderFactory.createCompoundBorder(border, verticalBorder);
        }

        renderer.setBorder(border);
    }

    // TODO this next section was already outsourced to jpotpourri (potface?!) -> class: TableFillEmptyRowsPainter
    /******************************************************************************************************************/
    /** PAINT EMPTY ROWS
    /******************************************************************************************************************/

    public void paint(Graphics g) {
        super.paint(g);
        this.paintEmptyRows(g);
    }

    protected void paintEmptyRows(Graphics g) {
        final int rowCount = getRowCount();
        final Rectangle clip = g.getClipBounds();
        final int height = clip.y + clip.height;
        if (rowCount * rowHeight < height) {
            for (int i = rowCount; i <= height/rowHeight; ++i) {
                g.setColor(colorForRow(i));
                g.fillRect(clip.x, i * rowHeight, clip.width, rowHeight);
            }

            // Mac OS' Aqua LAF never draws vertical grid lines, so we have to draw them ourselves.
            if (UserSniffer.isMacOSX() && getShowVerticalLines()) {
                g.setColor(MAC_COLOR_UNFOCUSED_UNSELECTED_VERTICAL_LINE);
                TableColumnModel model = getColumnModel();
                int x = 0;
                for (int i = 0; i < model.getColumnCount(); ++i) {
                    TableColumn column = model.getColumn(i);
                    x += column.getWidth();
                    g.drawLine(x - 1, rowCount * rowHeight, x - 1, height);
                }
            }
        }
    }

    protected Color colorForRow(int row) {
        return (row % 2 == 0) ? Constants.getColorRowBackgroundOdd() : getBackground();
    }

    /******************************************************************************************************************/
    /** POSITION TOOLTIP
    /******************************************************************************************************************/

    private void initToolTip(JComponent c, int row, int column) {
        String toolTipText = null;
        if (c.getPreferredSize().width > getCellRect(row, column, false).width) {
//            toolTipText = getValueAt(row, column).toString();
            toolTipText = c.getToolTipText();
        }
        c.setToolTipText(toolTipText);
    }

    @Override
    public Point getToolTipLocation(MouseEvent e) {
        if (getToolTipText(e) == null) {
            return null;
        }
        final int row = rowAtPoint(e.getPoint());
        final int column = columnAtPoint(e.getPoint());
        if (row == -1 || column == -1) {
            return null;
        }
        return getCellRect(row, column, false).getLocation();
    }

}
