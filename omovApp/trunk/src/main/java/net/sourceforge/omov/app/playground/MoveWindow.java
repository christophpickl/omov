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

package net.sourceforge.omov.app.playground;

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

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class MoveWindow extends JWindow implements MouseListener, MouseMotionListener {
    private static final long serialVersionUID = 1L;
    Point location;
    MouseEvent pressed;
    Dimension pressedDimension;

    public MoveWindow() {
        JPanel panel = new JPanel();
        panel.setPreferredSize( new Dimension(200, 100) );
        panel.setBackground(Color.RED);
        getContentPane().add(panel, BorderLayout.SOUTH);
        getContentPane().add(new JTextField("Hello World"), BorderLayout.NORTH);
        addMouseListener( this );
        addMouseMotionListener( this );
    }

    public void mousePressed(MouseEvent me) {
        pressed = me;
        pressedDimension = this.getSize();
    }

    public void mouseClicked(MouseEvent e) { /* nothing to do */ }
    public void mouseReleased(MouseEvent e) { /* nothing to do */ }

    public void mouseDragged(MouseEvent me) {
        final int widthChange = me.getX() - pressed.getX();
        final int heightChange = me.getY() - pressed.getY();

        this.setSize(new Dimension((int) pressedDimension.getWidth() + widthChange, (int) pressedDimension.getHeight() + heightChange));
     }

    public void mouseMoved(MouseEvent e) { /* nothing to do */}
    public void mouseEntered(MouseEvent e) { /* nothing to do */ }
    public void mouseExited(MouseEvent e) { /* nothing to do */ }

    public static void main(String args[]) {
        MoveWindow window = new MoveWindow();
        window.setSize(300, 300);
        window.setLocationRelativeTo( null );
        window.setVisible(true);
    }
}
