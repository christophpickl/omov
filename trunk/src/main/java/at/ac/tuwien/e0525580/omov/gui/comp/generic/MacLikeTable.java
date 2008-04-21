package at.ac.tuwien.e0525580.omov.gui.comp.generic;

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

import org.jdesktop.swingx.JXTable;

import at.ac.tuwien.e0525580.omov.Constants;
import at.ac.tuwien.e0525580.omov.util.UserSniffer;

public class MacLikeTable extends JXTable implements IMacColors {

    private static final long serialVersionUID = -4690492128203374606L;

    // FIXME vertikalen linien gehen nicht ganz bis hinunter durch, wenn tabelle noch nicht ganz gefuellt ist!


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
                c. setBackground(Constants.getColorSelectedBackground());
                c.setForeground(Constants.getColorLightGray()); // selected, but got no focus
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

    protected void fixMacOsCellRendererBorder(JComponent renderer, boolean selected, boolean focused, boolean isEditing) {
    	// FIXME does not work properly (also, isEditing parameter is not used)
        Border border;
        if (selected) {
            border = BorderFactory.createMatteBorder(0, 0, 1, 0, focused ? MAC_FOCUSED_SELECTED_CELL_HORIZONTAL_LINE_COLOR : MAC_UNFOCUSED_SELECTED_CELL_HORIZONTAL_LINE_COLOR);
        } else {
            border = BorderFactory.createEmptyBorder(0, 0, 1, 0);
        }

        if (getShowVerticalLines()) {
            final Color verticalLineColor;
            if (focused) {
                verticalLineColor = selected ? MAC_FOCUSED_SELECTED_VERTICAL_LINE_COLOR : MAC_FOCUSED_UNSELECTED_VERTICAL_LINE_COLOR;
            } else {
//                if(isEditing && row == selectedRow) {
//                    verticalLineColor = dark grey thing: MAC_FOCUSED_SELECTED_VERTICAL_LINE_COLOR?
//                } else {
                    verticalLineColor = selected ? MAC_UNFOCUSED_SELECTED_VERTICAL_LINE_COLOR : MAC_UNFOCUSED_UNSELECTED_VERTICAL_LINE_COLOR;
//                }
            }
            Border verticalBorder = BorderFactory.createMatteBorder(0, 0, 0, 1, verticalLineColor);
            border = BorderFactory.createCompoundBorder(border, verticalBorder);
        }

        renderer.setBorder(border);
    }

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
                g.setColor(MAC_UNFOCUSED_UNSELECTED_VERTICAL_LINE_COLOR);
                TableColumnModel columnModel = getColumnModel();
                int x = 0;
                for (int i = 0; i < columnModel.getColumnCount(); ++i) {
                    TableColumn column = columnModel.getColumn(i);
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
