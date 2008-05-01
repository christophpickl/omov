package net.sourceforge.omov.core.gui;

import java.awt.BorderLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.gui.comp.generic.brushed.BrushedMetalPanel;
import net.sourceforge.omov.core.util.GuiUtil;

/**
 * 
 * @author Christoph Pickl - e0525580@student.tuwien.ac.at
 */
public class SplashScreen extends JWindow {

    private static final long serialVersionUID = -1539009650852919347L;
    
    
    public SplashScreen() {
        this.getContentPane().add(this.initComponents());
        this.pack();
        GuiUtil.setCenterLocation(this, 0, -30);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new BrushedMetalPanel();
        panel.setLayout(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(30, 30, 10, 30));
        
        final JPanel panelSouth = new JPanel();
        panelSouth.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panelSouth.setOpaque(false);
        panelSouth.add(new JLabel("OurMovies v"+BeanFactory.getInstance().getCurrentApplicationVersion().getVersionString()+" is starting up..."));
        
        panel.add(new JLabel(ImageFactory.getInstance().getSplashScreenLogo()), BorderLayout.CENTER);
        panel.add(panelSouth, BorderLayout.SOUTH);

        return panel;
    }
}
