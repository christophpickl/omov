package at.ac.tuwien.e0525580.omov2.gui.comp.generic;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import at.ac.tuwien.e0525580.omov2.FatalException;
import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class NumberField extends JTextField {
    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
        }});
        f.add(new NumberField(0, 0, 5, 2));
        f.pack();
        GuiUtil.setCenterLocation(f);
        f.setVisible(true);
    }

    private static final long serialVersionUID = 1642701240001000902L;
    private static boolean DEBUG = false;
    
    private final int minValue;
    private final int maxValue;
//    private final int maxColSize;
    private final boolean numberFieldConstructorFinished;

    public NumberField(int initVal, int minVal, int maxVal) {
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
    public NumberField(int initValue, int minValue, int maxValue, int size) {
        super("" + initValue, size);
        if((initValue >= minValue && initValue <= maxValue) == false) throw new FatalException("Initial value '"+initValue+"' is not within valid range ("+minValue+" to "+maxValue+")!");
        
        this.minValue = minValue;
        this.maxValue = maxValue;
        this.numberFieldConstructorFinished = true;
        
//        this.maxColSize = (""+maxVal).length();
//        if(DEBUG) System.out.println("DEBUG: maxColSize set to " + maxColSize);
        
//        this.addKeyListener(new KeyAdapter(){ public void keyReleased(KeyEvent event) {
//        if(event.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
//            doBackspace();
//        }}});
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

    public int getNumber() {
        if(this.getText().length() == 0) {
            if(DEBUG) System.out.println("User did not enter any number, returning 0.");
            return 0;
        }
        try {
            return Integer.parseInt(getText());
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
                Integer.parseInt(newString + "0"); // ???
                
                final int value = Integer.parseInt(newString);
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
