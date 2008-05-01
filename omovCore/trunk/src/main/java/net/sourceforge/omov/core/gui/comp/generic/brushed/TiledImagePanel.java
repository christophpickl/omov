package net.sourceforge.omov.core.gui.comp.generic.brushed;

import java.awt.Image;

import javax.swing.JPanel;
import javax.swing.plaf.PanelUI;

public class TiledImagePanel extends JPanel {
    
    private static final long serialVersionUID = -3588314729894479207L;

    private Image image;

    public TiledImagePanel() {
        initializeUI();
    }

    protected void initializeUI() {
        super.setUI(TiledImagePanelUI.createUI(this));
    }

    public TiledImagePanel(Image aImage) {
        this();
        setImage(aImage);
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image aImage) {
        Image oldImage = image;
        image = aImage;
        firePropertyChange("image", oldImage, image);
        repaint();
    }

    public void setUI(PanelUI ui) {
        if (ui instanceof TiledImagePanelUI) {
            super.setUI(ui);
        }
    }

}
