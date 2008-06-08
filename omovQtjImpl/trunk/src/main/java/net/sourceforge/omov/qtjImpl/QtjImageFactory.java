package net.sourceforge.omov.qtjImpl;

import java.awt.Image;

import javax.swing.ImageIcon;

import net.sourceforge.omov.gui.CoreImageFactory;

public class QtjImageFactory extends CoreImageFactory {

    private static final QtjImageFactory INSTANCE = new QtjImageFactory();

    public static QtjImageFactory getInstance() {
        return INSTANCE;
    }
    
    
    


    public ImageIcon getButtonSmallScreen(ButtonSmallScreenIcon iconEnum) {
        return this.getImage("buttons_smallscreen/" + iconEnum.fileName);
    }

    public ImageIcon getCloseMini() {
    	return this.getImage("buttons_smallscreen/close_mini.png");
    }
    
    public Image getTransparentPixelImage() {
    	return this.getImage("transparent_pixel.gif").getImage();
    }
    
    
    public enum ButtonSmallScreenIcon {
    	PLAY("play.png"),
    	PAUSE("pause.png"),
    	BACK("back.png"),
    	FULLSCREEN("fullscreen.png"),
    	FORWARD("forward.png"),
    	BACKWARD("backward.png");
    	
        final String fileName;
        private ButtonSmallScreenIcon(String fileName) {
            this.fileName = fileName;
        }
    	
    }
    
}
