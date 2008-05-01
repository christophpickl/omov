package net.sourceforge.omov.core.spielwiese;

import net.sourceforge.omov.core.FatalException;

import org.jdesktop.swingx.JXErrorPane;
import org.jdesktop.swingx.error.ErrorInfo;
import org.jdesktop.swingx.error.ErrorLevel;

public class SwingXDialog {
    public static void main(String[] args) {
        // DO NOT use this one; its ugly! better write your own extended dialog box (displaying the stack trace)
        JXErrorPane.showDialog(null, new ErrorInfo("s1: Bold title", "s2: message", null, "s4", new FatalException("blua"), ErrorLevel.WARNING, null));
    }
}
