package net.sourceforge.omov.core.spielwiese;

import java.awt.Color;

import javax.swing.JFrame;

import net.sourceforge.omov.core.gui.comp.rating.RatingSlider;

public class RatingSpiel {
    public static void main(String[] args) {
        
        JFrame f = new JFrame();
        
        RatingSlider r = new RatingSlider(1, Color.RED);
        
//        JSlider r = new JSlider(0, 5);
//        ShapeSliderUI ui = new ShapeSliderUI();
//        ui.setPrimaryColor(Color.RED);
//        r.setUI(ui);
        
        f.getContentPane().add(r);
        
        f.pack();
        f.setVisible(true);
    }
}
