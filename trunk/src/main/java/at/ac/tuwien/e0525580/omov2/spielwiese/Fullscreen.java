package at.ac.tuwien.e0525580.omov2.spielwiese;

import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JWindow;

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
        
        
    }
    
    public static void main(String[] args) {
        GraphicsEnvironment env = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice[] devices = env.getScreenDevices();
        GraphicsDevice d = devices[0];
        if(d.isFullScreenSupported()) {
            d.setFullScreenWindow(new Fullscreen());
        } else {
            System.err.println("Fullscreen mode not supported!");
        }
//        d.setDisplayMode(new DisplayMode());
    }
}
