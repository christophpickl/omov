package at.ac.tuwien.e0525580.omov.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.omov.gui.comp.generic.brushed.BrushedMetalPanel;

/**
 * 
 * @author Christoph Pickl - e0525580@student.tuwien.ac.at
 */
public final class GuiUtil {
    

    private static final Log LOG = LogFactory.getLog(GuiUtil.class);


    private static final Cursor DEFAULT_CURSOR = new Cursor(Cursor.DEFAULT_CURSOR);
    private static final Cursor HAND_CURSOR = new Cursor(Cursor.HAND_CURSOR);
    
    private GuiUtil() {
        // no instantiation
    }
    
    public static void setCenterLocation(final Component component) {
        GuiUtil.setCenterLocation(component, 0, 0);
    }

    /**
     * sets the position of given component to the center of the screen
     * 
     * @param component
     *            which should be placed in the center of screen
     */
    public static void setCenterLocation(final Component component, int xOffset, int yOffset) {
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

        int x = (screenSize.width - component.getWidth()) / 2;
        int y = (screenSize.height - component.getHeight()) / 2;

        component.setLocation(x + xOffset, y + yOffset);
    }
    
    public static void showNotyetImplemented(Component component) {
        JOptionPane.showMessageDialog(component, "Not yet implemented - sorry.", "Ups", JOptionPane.INFORMATION_MESSAGE);
    }


    private static void lockMinimumWindowSize(final Window window, final int width, final int height,
            final boolean lockWidth, final boolean lockHeight) {
        assert(lockWidth || lockHeight);
        
        window.addComponentListener(new ComponentAdapter() {
            public void componentResized(ComponentEvent event) {
                window.setSize((window.getWidth() < width && lockWidth)  ? width : window.getWidth(),
                             (window.getHeight() < height && lockHeight) ? height : window.getHeight());
            }
        });
    }
    
    /**
     * invoke me after you have invoked pack()
     * @param window
     */
    public static void lockOriginalSizeAsMinimum(final Window window) {
        lockMinimumWindowSize(window, window.getSize().width, window.getSize().height, true, true);
    }

    /**
     * invoke me after you have invoked pack()
     * @param window
     */
    public static void lockOriginalWidthAsMinimum(final Window window) {
        lockMinimumWindowSize(window, window.getSize().width, 0, true, false);
    }

    /**
     * invoke me after you have invoked pack()
     * @param window
     */
    public static void lockOriginalWidthAsMinimum(final Window window, int minimumHeight) {
        lockMinimumWindowSize(window, window.getSize().width, minimumHeight, true, true);
    }

    /**
     * invoke me after you have invoked pack()
     * @param window
     */
    public static void lockOriginalHeightAsMinimum(final Window window) {
        lockMinimumWindowSize(window, 0, window.getSize().height, false, true);
    }
    
    public static JMenuItem createMenuItem(final JMenu menu, final String itemName, ActionListener listener) {
        return createMenuItem(menu, itemName, listener, -1);
    }
    
    /**
     * creates a new <code>JMenuItem</code>, initializes it, adds it to the given menu and returns it
     * @param menu where item should be added
     * @param itemName message bundle label of menuitem
     * @param keyCode numeric representation of key to set accelerator (ignored if keyCode == -1)
     * @return ready to use menuitem
     */
    public static JMenuItem createMenuItem(final JMenu menu, final String label, ActionListener listener, final int keyCode) {
        LOG.debug("creating new menu item: label='"+label+"', listener='"+listener+"', keyCode='"+keyCode+"'");
        
        JMenuItem item = new JMenuItem(label);
        if(listener != null) item.addActionListener(listener);
        item.setMnemonic(item.getText().charAt(0));

        if (keyCode != -1) {
            int mask = InputEvent.CTRL_MASK;
//            if(label.equals(CMD_EXIT)) {
//                mask |= InputEvent.SHIFT_MASK;
//            }
            if(UserSniffer.getOS() == UserSniffer.OS.MAC) {
                mask = InputEvent.META_DOWN_MASK;
            }
            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, mask));
        }
        
        menu.add(item);
        return item;
    }
    

    public static void info(String title, String message) {
        info(null, title, message);
    }
    public static void info(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.INFORMATION_MESSAGE);
    }
    
    public static void warning(String title, String message) {
        warning(null, title, message);
    }
    public static void warning(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.WARNING_MESSAGE);
    }

    public static void error(String title, String message) {
        error(null, title, message);
    }
    public static void error(Component parent, String title, String message) {
        JOptionPane.showMessageDialog(parent, message, title, JOptionPane.ERROR_MESSAGE);
    }
    

//    public static JScrollPane wrapScroll(Component view) {
//        final JScrollPane scrollPane = new JScrollPane(view);
//        scrollPane.setWheelScrollingEnabled(true);
//        return scrollPane;
//    }
    
    public static JScrollPane wrapScroll(Component view, int width, int height) {
        final JScrollPane scrollPane = new JScrollPane(view);
        scrollPane.setPreferredSize(new Dimension(width, height));
        scrollPane.setWheelScrollingEnabled(true);
        return scrollPane;
    }
    
    
    public static boolean getYesNoAnswer(Component owner, String title, String message) {
        return JOptionPane.showConfirmDialog(owner, message, title, JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION;
    }
    
    /**
     * @return null if user aborted;
     */
    public static File getDirectory(Component owner, String initialPath) {
        LOG.debug("showing file chooser with default path '"+(initialPath == null?"null":initialPath)+"'...");
        JFileChooser chooser = new JFileChooser(initialPath);
        
        chooser.setMultiSelectionEnabled(false);
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setDialogTitle("Choose Directory");
        
        
        int returnVal = chooser.showOpenDialog(owner);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            final File selectedFile = chooser.getSelectedFile();
            return selectedFile;
        }
        LOG.debug("User aborted getting file directory; returning null.");
        return null;
    }
    
    public static File getDirectory() {
        return getDirectory(null, null);
    }

    public static JLabel newLabelBold(final String text) {
        return newLabelBold(text, 12);
    }

    public static JLabel newLabelBold(final String text, int size) {
        JLabel label = new JLabel(text);
        label.setFont(new Font("sans", Font.BOLD, size));
        return label;
    }
    
    public static void enableHandCursor(final Component component) {
        component.addMouseListener(new MouseListener() {
            public void mouseEntered(MouseEvent event) {
                component.setCursor(GuiUtil.HAND_CURSOR);
            }
            public void mouseExited(MouseEvent event) {
                component.setCursor(GuiUtil.DEFAULT_CURSOR);
            }
            public void mouseClicked(MouseEvent event) { }
            public void mousePressed(MouseEvent event) { }
            public void mouseReleased(MouseEvent event) { }
        });
    }
    
    public static JPanel wrapBrushedMetalPanel(final Component component) {
//        final JPanel panel = new BrushedMetalPanel();
//        final GridBagLayout layout = new GridBagLayout();
//        final GridBagConstraints c = new GridBagConstraints();
//        layout.setConstraints(panel, c);
//        panel.setLayout(layout);
//
//        c.gridx = 0;
//        c.gridy = 0;
//        c.insets = new Insets(0, 0, 0, 0);
//        c.fill = GridBagConstraints.BOTH;
        
//        panel.add(component, c);
        JPanel panel = new BrushedMetalPanel();
        panel.setLayout(new BorderLayout());
        panel.add(component, BorderLayout.CENTER);
        return panel;
    }
}
