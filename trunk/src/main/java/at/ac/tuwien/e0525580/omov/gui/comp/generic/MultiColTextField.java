package at.ac.tuwien.e0525580.omov.gui.comp.generic;

import javax.swing.BorderFactory;
import javax.swing.JLabel;

public class MultiColTextField extends JLabel {

    private static final long serialVersionUID = 2860809976529219917L;
    
    private final int visibleTextColumns;
    
    public MultiColTextField(int columns) {
        this("", columns);
    }
    
    public MultiColTextField(final String text, int columns) {
//    	this.setColumns(columns);
    	this.visibleTextColumns = columns;
//        this.setPreferredSize(new Dimension(columns * 2, (int) this.getPreferredSize().getHeight()));
        
    	this.setHorizontalAlignment(JLabel.LEFT);
//        this.setOpaque(true); this.setBackground(Color.RED);
    	this.setOpaque(false);
//    	this.setBackground(null);
        this.setBorder(BorderFactory.createEmptyBorder());
//        this.setEditable(false);
        
        this.setText(text);
    }
    
    @Override
    public void setText(String text) {

        final String limitedText;
        if(text.length() > this.visibleTextColumns) {
        	limitedText = text.substring(0, this.visibleTextColumns) + "...";
        } else {
        	limitedText = text;
        }
        
        super.setText(limitedText);
        this.setToolTipText(text);
//        this.setCaretPosition(0);
    }
}
