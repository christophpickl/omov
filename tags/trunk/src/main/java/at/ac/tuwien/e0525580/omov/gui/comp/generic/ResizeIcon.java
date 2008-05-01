package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.Icon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

/**
 * @deprecated does not work!
 */
public class ResizeIcon extends JPanel implements Icon {

    private static final int ICON_WIDTH = 12;

    private static final int ICON_HEIGHT = 12;

    private static class ResizeIconControler implements MouseListener, MouseMotionListener {
        private final JFrame frame;

        public ResizeIconControler(JFrame frame) {
            this.frame = frame;
            // GuiUtil.enableHandCursor(this); DOES NOT WORK!!!
        }

        private MouseEvent storedMouseEvent;

        private Dimension storedFrameDimension;

        public void mouseClicked(MouseEvent e) {
            System.out.println("clicked");
        }

        public void mousePressed(MouseEvent e) {
            System.out.println("pressed");
            this.storedMouseEvent = e;
            this.storedFrameDimension = this.frame.getSize();
        }

        public void mouseDragged(MouseEvent e) {
            final int widthChange = e.getX() - this.storedMouseEvent.getX();
            final int heightChange = e.getY() - this.storedMouseEvent.getY();
            final int newWidth = ((int) storedFrameDimension.getWidth()) + widthChange;
            final int newHeight = ((int) storedFrameDimension.getHeight()) + heightChange;
            this.frame.setSize(new Dimension(newWidth, newHeight));
        }

        public void mouseEntered(MouseEvent e) { /* nothing to do */ }

        public void mouseExited(MouseEvent e) { /* nothing to do */ }

        public void mouseReleased(MouseEvent e) { /* nothing to do */}

        public void mouseMoved(MouseEvent e) { /* nothing to do */ }
    }

    private static final long serialVersionUID = 2175604196539079422L;

    public int getIconHeight() {
        return ResizeIcon.ICON_HEIGHT + 5;
    }

    public int getIconWidth() {
        return ResizeIcon.ICON_WIDTH + 5;
    }

    private static final Color WHITE_LINE_COLOR = new Color(10, 40, 110);

    private static final Color GRAY_LINE_COLOR = new Color(50, 10, 30);

    public void paintIcon(Component c, Graphics g, int x, int y) {
        g.setColor(WHITE_LINE_COLOR);
        g.drawLine(0, 12, 12, 0);
        g.drawLine(5, 12, 12, 5);
        g.drawLine(10, 12, 12, 10);

        g.setColor(GRAY_LINE_COLOR);
        g.drawLine(1, 12, 12, 1);
        g.drawLine(2, 12, 12, 2);
        g.drawLine(3, 12, 12, 3);

        g.drawLine(6, 12, 12, 6);
        g.drawLine(7, 12, 12, 7);
        g.drawLine(8, 12, 12, 8);

        g.drawLine(11, 12, 12, 11);
        g.drawLine(12, 12, 12, 12);
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.setResizable(false);
        int rowCnt = 100;
        String[][] rows = new String[rowCnt][];
        for (int i = 0; i < rows.length; i++) {
            rows[i] = new String[] { "Row " + (i + 1) };
        }
        JTable tbl = new JTable(rows, new String[] { "Col1" });
        JScrollPane s = new JScrollPane(tbl);

//        ResizeIcon icon = new ResizeIcon();
//        final JLabel lbl = new JLabel(icon); // new JLabel("asdf"); ... funtioniert halbert
        final JPanel lbl = new JPanel();
        lbl.setBackground(Color.RED);

        ResizeIconControler c = new ResizeIconControler(f);
        lbl.addMouseListener(c);
        lbl.addMouseMotionListener(c);

        JPanel p = new JPanel(new BorderLayout());
        p.add(s, BorderLayout.CENTER);

        JPanel panelSouth = new JPanel(new BorderLayout());
        panelSouth.add(lbl, BorderLayout.EAST);
        p.add(panelSouth, BorderLayout.SOUTH);

        f.getContentPane().add(p);

        f.pack();
        f.setVisible(true);
    }
}
