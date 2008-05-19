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

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class Fullscreen extends JWindow {
    
    private static final long serialVersionUID = 3666763820929922832L;
    
    public Fullscreen() {
        
        this.getContentPane().add(this.initComponents());
        this.pack();
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();

        JButton btnExit = new JButton("exit");
        btnExit.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });
        JButton btnNoFullScreen = new JButton("leave full screen");
        btnNoFullScreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterFullScreen(false);
            }
        });
        JButton btnFullScreen = new JButton("enter full screen");
        btnFullScreen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                enterFullScreen(true);
            }
        });

        panel.add(btnExit);
        panel.add(btnNoFullScreen);
        panel.add(btnFullScreen);

        return panel;
    }
    
    private void enterFullScreen(boolean b) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        GraphicsDevice d = devices[0];
        d.setFullScreenWindow(b ? this : null);
        
//        this.invalidate();
//        this.repaint();
        if(b == false) {
        	this.setSize(300, 200);
        	this.setLocation(200, 200);
        }
    }
    
    public static void main(String[] args) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        GraphicsDevice d = devices[0];
        if(d.isFullScreenSupported()) {
//            d.setFullScreenWindow(new Fullscreen());
        	Fullscreen f = new Fullscreen();
        	f.setVisible(true);
        } else {
            System.err.println("Fullscreen mode not supported!");
        }
//        d.setDisplayMode(new DisplayMode());
    }
}
