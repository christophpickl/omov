package at.ac.tuwien.e0525580.omov.spielwiese;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

import at.ac.tuwien.e0525580.omov.FatalException;

public class SwingXDialog {
    public static void main(String[] args) {
        // DO NOT use this one; its ugly! better write your own extended dialog box (displaying the stack trace)
        JXErrorPane.showDialog(null, new ErrorInfo("s1: Bold title", "s2: message", null, "s4", new FatalException("blua"), ErrorLevel.WARNING, null));
    }
}
