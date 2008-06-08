package net.sourceforge.omov.gui;

import java.awt.Image;

import javax.swing.ImageIcon;

import net.sourceforge.omov.core.AbstractImageFactory;

public class CoreImageFactory extends AbstractImageFactory {

    private static final CoreImageFactory INSTANCE = new CoreImageFactory();

    public static CoreImageFactory getInstance() {
        return INSTANCE;
    }
    
    
    public Image getImgBrushed() {
        return this.getImage("brushed.gif").getImage();
    }


    public ImageIcon getDialogWarning() {
        return this.getImage("dialog/warning.png");
    }
    
    public ImageIcon getDialogError() {
        return this.getImage("dialog/error.png");
    }
}
