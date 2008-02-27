package at.ac.tuwien.e0525580.omov2.gui.comp.generic.brushed;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicPanelUI;

public class TiledImagePanelUI extends BasicPanelUI implements PropertyChangeListener {
    public static Image image;

    public TiledImagePanelUI() {
    }

    public TiledImagePanelUI(Image aImage) {
        image = aImage;
    }

    public static ComponentUI createUI(JComponent c) {
        return new TiledImagePanelUI();
    }

    public void installUI(JComponent c) {
        super.installUI(c);

        if (image == null) {
            if (c instanceof TiledImagePanel) {
                image = ((TiledImagePanel) c).getImage();
                c.addPropertyChangeListener("image", this);
            } else {
                throw new RuntimeException("TiledImagePanelUI must be used with a TiledImagePanel or the image must be set explicitly");
            }
        }
    }

    protected void uninstallDefaults(JPanel p) {
        p.removePropertyChangeListener("image", this);
        super.uninstallDefaults(p);
    }

    // ------------------------------------------------------------------------------------------------------------------
    //  Custom painting methods
    // ------------------------------------------------------------------------------------------------------------------

    public void paint(Graphics g, JComponent c) {
        if (image == null)
            return;

        Dimension vSize = c.getSize();

        Graphics2D g2d = (Graphics2D) g;

        for (int x = 0; x < vSize.width; x += 200) {
            for (int y = 0; y < vSize.height; y += 200) {
                g2d.drawImage(image, x, y, null);
            }
        }
    }

    // ------------------------------------------------------------------------------------------------------------------
    //  Implementation of PropertyChangeListener interface
    // ------------------------------------------------------------------------------------------------------------------

    public void propertyChange(PropertyChangeEvent evt) {
        if ("image".equals(evt.getPropertyName())) {
            image = (Image) evt.getNewValue();
        }
    }
}
