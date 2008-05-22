/*
 * OurMovies - Yet another movie manager
 * Copyright (C) 2008 Christoph Pickl (christoph_pickl@users.sourceforge.net)
 * 
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

package net.sourceforge.omov.gui;

import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.util.SimpleGuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class NumberField extends JTextField {
    
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
        }});
        f.add(new NumberField(0, 0, 5, 2));
        f.pack();
        SimpleGuiUtil.setCenterLocation(f);
        f.setVisible(true);
    }

    private static final long serialVersionUID = 1642701240001000902L;
    private static boolean DEBUG = false;
    
    private final long minValue;
    private final long maxValue;
//    private final int maxColSize;
    private final boolean numberFieldConstructorFinished;

    private boolean focusSelectionEnabled = false;
    

    public NumberField(long initVal, long minVal, long maxVal) {
        this(initVal, minVal, maxVal, (""+maxVal).length() + 1);
    }
    
    /**
     * trailing zeros ignored -> length is not limited
     * 
     * @param initValue
     * @param minValue
     * @param maxValue
     * @param size ... visible column size
     */
    public NumberField(long initValue, long minValue, long maxValue, int size) {
        super("" + initValue, size);
        if((initValue >= minValue && initValue <= maxValue) == false) {
            throw new FatalException("Initial value '"+initValue+"' is not within valid range ("+minValue+" to "+maxValue+")!");
        }
        this.setHorizontalAlignment(JTextField.RIGHT);
        
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numberFieldConstructorFinished = true;
        
//        this.maxColSize = (""+maxVal).length();
//        if(DEBUG) System.out.println("DEBUG: maxColSize set to " + maxColSize);
        
//        this.addKeyListener(new KeyAdapter(){ public void keyReleased(KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
//            doBackspace();
//        }}});

        this.addFocusListener(new FocusListener() {
            public void focusGained(FocusEvent e) {
                doFocusGained();
            }
            public void focusLost(FocusEvent e) {
                // nothing to do
            }
        });
    }
    
    public void setFocusSelection(final boolean focusSelectionEnabled) {
        this.focusSelectionEnabled = focusSelectionEnabled;
    }
    
    private void doFocusGained() {
        if(this.focusSelectionEnabled == true) {
            this.selectAll();
        }
    }
    
    public void setNumber(int number) {
        if(number < this.minValue || number > this.maxValue) {
            throw new IllegalArgumentException("Number '"+number+"' is not within valid range ("+this.minValue+"-"+this.maxValue+")!");
        }
        this.setText(String.valueOf(number));
    }
    
//    private void doBackspace() {
//        if(DEBUG) System.out.println("DEBUG: doBbackspace() says text is '"+this.getText()+"'");
//        if(this.getText().length() == 0) {
//            this.setText(String.valueOf(this.minVal));
//        }
//    }

    
    protected Document createDefaultModel() {
        return new IntTextDocument();
    }

//    private boolean isValidValue(final String newString) {
//        try {
////            final int value = this.getValue();
//            final int value = Integer.parseInt(newString);
//            if(DEBUG) System.out.println("DEBUG: value="+value+"; minValue="+minVal+"; maxValue="+maxVal+"; (colSize="+newString.length()+"; maxColSize="+maxColSize+")");
//            return newString.length() <= this.maxColSize && (value >= this.minVal && value <= this.maxVal);
//        } catch (NumberFormatException e) {
//            return false;
//        }
//    }

    public long getNumber() {
        if(this.getText().length() == 0) {
            if(DEBUG) System.out.println("User did not enter any number, returning 0.");
            return 0;
        }
        try {
            return Long.parseLong(getText());
        } catch (NumberFormatException e) {
            throw new FatalException("Invalid user input '"+getText()+"'! Should never happen...", e);
        }
    }
    

    private class IntTextDocument extends PlainDocument {
        private static final long serialVersionUID = 2505716035235158421L;

        
        public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
            if (str == null) return;
            
            if(numberFieldConstructorFinished == false) { // super constructor invocation; we can trust him
                super.insertString(offs, str, a);
                return;
            }
            
            final String oldString = getText(0, getLength());
            final String newString = oldString.substring(0, offs) + str + oldString.substring(offs);
            if(DEBUG) System.out.println("DEBUG: oldString='"+oldString+"'; newString='"+newString+"'");
            try {
                Long.parseLong(newString + "0"); // ???
                
                final long value = Long.parseLong(newString);
                final boolean isValidValue = (value >= minValue && value <= maxValue);
                if(DEBUG) System.out.println("DEBUG: is valid value " + isValidValue + " (value="+value+"; minVal="+minValue+"; maxVal="+maxValue+")");
                if(isValidValue == true) {
                    super.insertString(offs, str, a);
                } else {
                    Toolkit.getDefaultToolkit().beep();
                }
            } catch (NumberFormatException e) {
                Toolkit.getDefaultToolkit().beep();
                if(DEBUG) System.out.println("DEBUG: could not parse newString");
            }
        }
    }

}

/*
public class TextFieldWithLimit extends TextField 
   implements KeyListener {
   private int  maxLength;
   public TextFieldWithLimit 
          (String initialStr,int col,int maxLength) {
     super(initialStr,col);
     this.maxLength = maxLength;
     addKeyListener(this);
     }
   public TextFieldWithLimit (int col,int maxLength) {
     this("",col,maxLength);
     }

   public void keyPressed(KeyEvent e) { 
    char c = e.getKeyChar();
    int len = getText().length();
    if (len < maxLength) {
      return;
      }
    else {
      if((c==KeyEvent.VK_BACK_SPACE)||
        (c==KeyEvent.VK_DELETE) ||
        (c==KeyEvent.VK_ENTER)|| 
        (c==KeyEvent.VK_TAB)||
         e.isActionKey())
         return;
      else {
         e.consume(); 
         }
      }
   }
   public void keyReleased(KeyEvent e) { }
   public void keyTyped(KeyEvent e) { }
   }
*/
