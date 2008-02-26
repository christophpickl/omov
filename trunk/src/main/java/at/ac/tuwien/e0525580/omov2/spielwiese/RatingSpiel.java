package at.ac.tuwien.e0525580.omov2.spielwiese;

import java.awt.Color;

import javax.swing.JFrame;

import at.ac.tuwien.e0525580.omov2.gui.comp.rating.RatingField;

public class RatingSpiel {
    public static void main(String[] args) {
        
        JFrame f = new JFrame();
        
        RatingField r = new RatingField(1, Color.RED);
        
//        JSlider r = new JSlider(0, 5);
//        ShapeSliderUI ui = new ShapeSliderUI();
//        ui.setPrimaryColor(Color.RED);
//        r.setUI(ui);
        
        f.getContentPane().add(r);
        
        f.pack();
        f.setVisible(true);
    }
}
