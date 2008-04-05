package at.ac.tuwien.e0525580.omov.spielwiese;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JWindow;

public class MoveWindow extends JWindow implements MouseListener, MouseMotionListener
{
    private static final long serialVersionUID = 1L;
    Point location;
    MouseEvent pressed;
    Dimension pressedDimension;
 
    public MoveWindow()
    {
        JPanel panel = new JPanel();
        panel.setPreferredSize( new Dimension(200, 100) );
        panel.setBackground(Color.RED);
        getContentPane().add(panel, BorderLayout.SOUTH);
        getContentPane().add(new JTextField("Hello World"), BorderLayout.NORTH);
        addMouseListener( this );
        addMouseMotionListener( this );
    }
 
    public void mousePressed(MouseEvent me)
    {
        pressed = me;
        pressedDimension = this.getSize();
    }
 
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
 
    public void mouseDragged(MouseEvent me)
    {
        final int widthChange = me.getX() - pressed.getX();
        final int heightChange = me.getY() - pressed.getY();
        
        this.setSize(new Dimension((int) pressedDimension.getWidth() + widthChange, (int) pressedDimension.getHeight() + heightChange));
     }
 
    public void mouseMoved(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
 
    public static void main(String args[])
    {
        MoveWindow window = new MoveWindow();
        window.setSize(300, 300);
        window.setLocationRelativeTo( null );
        window.setVisible(true);
    }
}
