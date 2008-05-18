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

package net.sourceforge.omov.app.util;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.InputEvent;
import java.io.File;

import javax.swing.Icon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.omov.app.gui.comp.generic.brushed.BrushedMetalPanel;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.util.SimpleGuiUtil;
import net.sourceforge.omov.core.util.UserSniffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.decorator.HighlighterFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class GuiUtil extends SimpleGuiUtil {


    private static final Log LOG = LogFactory.getLog(GuiUtil.class);

    private static final int META_MASK = (UserSniffer.isMacOSX() ? InputEvent.META_DOWN_MASK : InputEvent.CTRL_MASK );

    protected GuiUtil() {
        // no instantiation
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

    public static void lockWidthAndHeightAsMinimum(final Window window, int minimumWidth, int minimumHeight) {
        lockMinimumWindowSize(window, minimumWidth, minimumHeight, true, true);
    }

    /**
     * invoke me after you have invoked pack()
     * @param window
     */
    public static void lockOriginalHeightAsMinimum(final Window window) {
        lockMinimumWindowSize(window, 0, window.getSize().height, false, true);
    }

    public static JMenuItem createMenuItem(final JMenu menu, final char mnemonicChar, final String label, final String actionCmd, ActionListener listener) {
        return createMenuItem(menu, mnemonicChar, label, actionCmd, listener, -1, null);
    }

    public static JMenuItem createMenuItem(final JMenu menu, final char mnemonicChar, final String label, final String actionCmd, ActionListener listener, final int keyCode) {
        return createMenuItem(menu, mnemonicChar, label, actionCmd, listener, keyCode, null);
    }


    public static JMenuItem createMenuItem(final JMenu menu, final char mnemonicChar, final String label, final String actionCmd, ActionListener listener, final int keyCode, final Icon icon) {
        return createMenuItem(menu, mnemonicChar, label, actionCmd, listener, keyCode, icon, META_MASK);
    }


    /**
     * creates a new <code>JMenuItem</code>, initializes it, adds it to the given menu and returns it
     * @param menu where item should be added
     * @param itemName message bundle label of menuitem
     * @param keyCode numeric representation of key to set accelerator (ignored if keyCode == -1)
     * @return ready to use menuitem
     */
    public static JMenuItem createMenuItem(final JMenu menu, final char mnemonicChar, final String label, final String actionCmd, ActionListener listener, final int keyCode, final Icon icon, final int mask) {
        LOG.debug("creating new menu item: label='"+label+"', actionCmd='"+actionCmd+"', listener='"+listener+"', keyCode='"+keyCode+"'");
        if(label == null) throw new NullPointerException("label");
        if(actionCmd == null) throw new NullPointerException("actionCmd");

        final JMenuItem item = (icon != null) ? new JMenuItem(label, icon) : new JMenuItem(label);
        item.setActionCommand(actionCmd);
        if(listener != null) item.addActionListener(listener);
        item.setMnemonic(mnemonicChar);

        if (keyCode != -1) {

//            if(label.equals(CMD_EXIT)) {
//                mask |= InputEvent.SHIFT_MASK;
//            }

            item.setAccelerator(KeyStroke.getKeyStroke(keyCode, mask));
        }

        if(menu != null) {
            menu.add(item);
        }
        return item;
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

    public static File getFile() {
        return GuiUtil.getFile(null);
    }

    public static File getFile(FileFilter filter) {
        JFileChooser chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
        if(filter != null) {
            chooser.setFileFilter(filter);
        }
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if(chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
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

    /**
     * @deprecated extend MacLikeTable instead
     */
    public static void setAlternatingBgColor(JXTable table) {
        table.setHighlighters(HighlighterFactory.createAlternateStriping(Constants.getColorRowBackgroundEven(), Constants.getColorRowBackgroundOdd()));
    }

    
    
    public static void macSmallWindow(JRootPane rootPane) {
        if(UserSniffer.isMacOSX()) {
        	rootPane.putClientProperty("Window.style", "small");
        }
    }
}
