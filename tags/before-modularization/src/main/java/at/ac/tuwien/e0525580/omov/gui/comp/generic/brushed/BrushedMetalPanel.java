package at.ac.tuwien.e0525580.omov.gui.comp.generic.brushed;

import java.awt.Image;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.UIManager;

import at.ac.tuwien.e0525580.omov.gui.ImageFactory;

/**
 * 
 * @author http://blog.elevenworks.com/?p=10
 */
public class BrushedMetalPanel extends TiledImagePanel {

    private static final long serialVersionUID = -8717131766395076544L;

    private static Image image = ImageFactory.getInstance().getImgBrushed();

    public static void main(String[] args) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception exc) {
            // Do nothing...
        }

        final JFrame frame = new JFrame("Brushed Metal Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 300);
        final JPanel panel = new BrushedMetalPanel();
        frame.getContentPane().add(panel);
        frame.setVisible(true);
    }

    public BrushedMetalPanel() {
        super(image);
    }

    protected void initializeUI() {
        super.setUI(HighlightedImagePanelUI.createUI(this));
    }

    public void setImage(Image aImage) {
        if (image == aImage) {
            super.setImage(aImage);
        }
    }

}
