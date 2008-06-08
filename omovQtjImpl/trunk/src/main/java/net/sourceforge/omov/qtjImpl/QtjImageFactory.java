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
    public ImageIcon getButtonSmallScreenPressed(ButtonSmallScreenIcon iconEnum) {
    	return this.getImage("buttons_smallscreen/" + iconEnum.fileNamePressed);
    }

    public ImageIcon getCloseMini() {
    	return this.getImage("close_mini.png");
    }
    
    public Image getTransparentPixelImage() {
    	return this.getImage("transparent_pixel.gif").getImage();
    }
    
    
    public enum ButtonSmallScreenIcon {
    	PLAY("play"),
    	PAUSE("pause"),
    	BACK("back"),
    	FULLSCREEN("fullscreen"),
    	FORWARD("forward"),
    	BACKWARD("backward");

        final String fileName;
        final String fileNamePressed;
        private ButtonSmallScreenIcon(String fileName) {
            this.fileName = fileName + ".png";
            this.fileNamePressed = fileName + "_pressed.png";
        }
    	
    }
    
}
