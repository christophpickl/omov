package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;
import java.net.URL;

import javax.swing.JLabel;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.action.OpenBrowserAction;

public class HyperlinkLabel extends JLabel {

    private static final Log LOG = LogFactory.getLog(HyperlinkLabel.class);
    private static final long serialVersionUID = -2856402859433242420L;

    public HyperlinkLabel(String url) {
        this(url, url);
    }

    public HyperlinkLabel(final String text, final String _url) {
        super(text);
        
        try {
            final URL url = new URL(_url);
            final Color oldForeground = this.getForeground();
            this.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) {
                    setForeground(Color.BLUE);
                    setText("<html><u>" + text + "</u></html>");
                }

                public void mouseExited(MouseEvent e) {
                    setForeground(oldForeground);
                    setText(text);
                }

                public void mouseClicked(MouseEvent e) {
                    new OpenBrowserAction(url).actionPerformed(null);
                }
            });
        } catch (MalformedURLException e) {
            LOG.error("Invalid url '"+_url+"'!", e);
        }
        
        
    }
}
