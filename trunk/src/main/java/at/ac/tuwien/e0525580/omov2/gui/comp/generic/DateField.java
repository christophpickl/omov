package at.ac.tuwien.e0525580.omov2.gui.comp.generic;

import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov2.util.GuiUtil;

public class DateField extends JTextField {

    private static final Log LOG = LogFactory.getLog(DateField.class);
    private static final long serialVersionUID = 8776501786825325958L;
    private static boolean DEBUG = false;

    private static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy/MM/dd");
    private static final int DEFAULT_COLUMN_SIZE = 6;
    
    private final SimpleDateFormat format; // use to parse back user input
    private final boolean numberFieldConstructorFinished;
    
    public DateField(Date date, SimpleDateFormat format, int columnSize) {
        super(format.format(date), columnSize);
        this.setToolTipText("Format: " + format.toPattern().toUpperCase());
        this.format = format;
        
        this.numberFieldConstructorFinished = true;
    }
    
    public DateField(Date date, int columnSize) {
        this(date, DEFAULT_DATE_FORMAT, columnSize);
    }
    
    public DateField(Date date) {
        this(date, DEFAULT_DATE_FORMAT, DEFAULT_COLUMN_SIZE);
    }
    
    public Date getDate() {
        try {
            return this.format.parse(this.getText());
        } catch (ParseException e) {
            LOG.warn("Could not parse datestring '"+this.getText()+"'!");
            return null;
        }
    }
    

    protected Document createDefaultModel() {
        return new DateTextDocument();
    }
    
    private static boolean isValid(String text) {
        final int n = text.length();
        if(DEBUG) System.out.println("DEBUG: isValid("+text+") length="+n);
        
        if(n > 10) return false;
        
        if(n == 0) {
            return true;
        } else if(n > 0 && n <= 4) {
            return isNumber(text);
        } else if(n == 5) {
            return isNumber(text.substring(0, 4)) && text.charAt(4) == '/';
        } else if(n > 5 && n <= 7) {
            return isNumber(text.substring(0, 4)) && text.charAt(4) == '/'
                && isNumber(text.substring(5));
        } else if(n == 8) {
            return isNumber(text.substring(0, 4)) && text.charAt(4) == '/'
                && isNumber(text.substring(5, 7)) && text.charAt(7) == '/';
        } else if(n > 8 && n <= 10) {
            return isNumber(text.substring(0, 4)) && text.charAt(4) == '/'
                && isNumber(text.substring(5, 7)) && text.charAt(7) == '/'
                && isNumber(text.substring(8));
        }
        throw new RuntimeException("unhandled text '"+text+"'!"); // should never occur
    }
    
    private static boolean isNumber(String text) {
        if(DEBUG) System.out.println("DEBUG: isNumber("+text+")");
        try {
            Integer.parseInt(text);
            return true;
        } catch(NumberFormatException e) {
            return false;
        }
    }

    private class DateTextDocument extends PlainDocument {
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
            
            final boolean isValidValue = isValid(newString); 
                
            if(isValidValue == true) {
                super.insertString(offs, str, a);
            } else {
                Toolkit.getDefaultToolkit().beep();
            }
        }
    }
    

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.addWindowListener(new WindowAdapter() {
          public void windowClosing(WindowEvent e) {
            System.exit(0);
        }});
        f.add(new DateField(new Date()));
        f.pack();
        GuiUtil.setCenterLocation(f);
        f.setVisible(true);
    }
}
