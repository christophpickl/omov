package at.ac.tuwien.e0525580.omov2.gui.comp.generic.brushed;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Dimension;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Paint;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;

public class HighlightedImagePanelUI extends TiledImagePanelUI {
    
    public static ComponentUI createUI(JComponent c) {
        return new HighlightedImagePanelUI();
    }

    public void paint(Graphics g, JComponent c) {
        super.paint(g, c);
        Dimension vSize = c.getSize();
        Graphics2D g2d = (Graphics2D) g;
        // Create the paint for the second layer of the button
        Color vGradientStartColor = Color.GRAY;
        Color vGradientEndColor = Color.WHITE;
        int vHorizontalCenter = vSize.width / 2;
        int vOffset = (int) (vSize.width * .1);
        Composite vPreviousComposite = g2d.getComposite();
        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, .3f));
        g2d.setColor(Color.WHITE);
        g2d.fillRect(vHorizontalCenter - vOffset, 0, vOffset * 2, vSize.height);
        Paint vPaint = new GradientPaint(0, 0, vGradientStartColor, vHorizontalCenter - vOffset, 0, vGradientEndColor, false);
        g2d.setPaint(vPaint);
        g2d.fillRect(0, 0, vHorizontalCenter - vOffset, vSize.height);
        vPaint = new GradientPaint(vSize.width, 0, vGradientStartColor, vHorizontalCenter + vOffset, 0, vGradientEndColor, false);
        g2d.setPaint(vPaint);
        g2d.fillRect(vHorizontalCenter + vOffset, 0, vHorizontalCenter - vOffset, vSize.height);
        g2d.setComposite(vPreviousComposite);
    }
}

