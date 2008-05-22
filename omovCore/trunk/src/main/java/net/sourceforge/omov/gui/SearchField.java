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

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import net.sourceforge.omov.core.util.UserSniffer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * A text field for search/filter interfaces. The extra functionality includes
 * a placeholder string (when the user hasn't yet typed anything), and a button
 * to clear the currently-entered text.
 * 
 * @author Elliott Hughes
 * copyright: {@link http://elliotth.blogspot.com/2004/09/cocoa-like-search-field-for-java.html}
 */
public class SearchField extends JTextField {
	
    private static final Log LOG = LogFactory.getLog(SearchField.class);
    
    private static final long serialVersionUID = -4973040283694201458L;
    
    // some todos:
    // - add a menu of recent searches.
    // - make recent searches persistent.
    // - use rounded corners, at least on Mac OS X.
    //
    private static final Border CANCEL_BORDER = new CancelBorder();
    private boolean sendsNotificationForEachKeystroke = false;
    private boolean showingPlaceholderText = false;
    private boolean armed = false;
    
    public static interface ISearchFieldListener {
        void didResetSearch(); // by releasing escape or hitting the "X"-button
    }
    
    private final Set<ISearchFieldListener> listeners = new HashSet<ISearchFieldListener>();
    public void addISearchFieldListener(ISearchFieldListener listener) {
        this.listeners.add(listener);
    }
    public void removeISearchFieldListener(ISearchFieldListener listener) {
        this.listeners.remove(listener);
    }
    private void notifiyListenersDidResetSearch() {
        for (ISearchFieldListener listener : this.listeners) {
            listener.didResetSearch();
        }
    }
    public SearchField(String placeholderText) {
        super(15);
        addFocusListener(new PlaceholderText(placeholderText));
        initBorder();
        initKeyListener();

        if(UserSniffer.isMacOSX()) {
        	LOG.debug("activating macosx search field.");
        	
            this.putClientProperty("JTextField.variant", "search");
            this.putClientProperty("JTextField.Search.CancelAction", new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					cancel();
				}
            });
        }
    }
    
    public SearchField() {
        this("Search");
    }
    
    private void initBorder() {
        setBorder(new CompoundBorder(getBorder(), CANCEL_BORDER));
        MouseInputListener mouseInputListener = new CancelListener();
        addMouseListener(mouseInputListener);
        addMouseMotionListener(mouseInputListener);
    }
    
    private void initKeyListener() {
        addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
                    cancel();
                } else if (sendsNotificationForEachKeystroke) {
                    maybeNotify();
                }
            }
        });
    }
    
    private void cancel() {
    	LOG.debug("cancel() invoked; notifying listeners.");
        setText("");
        this.notifiyListenersDidResetSearch();
        postActionEvent();
    }
    
    private void maybeNotify() {
        if (showingPlaceholderText) {
            return;
        }
        postActionEvent();
    }
    
    public void setSendsNotificationForEachKeystroke(boolean eachKeystroke) {
        this.sendsNotificationForEachKeystroke = eachKeystroke;
    }
    
    
    
    
    /**
     * Draws the cancel button as a gray circle with a white cross inside.
     */
    private static class CancelBorder extends EmptyBorder {
        private static final long serialVersionUID = -2767979848421481905L;
        private static final Color GRAY = new Color(0.7f, 0.7f, 0.7f);
        CancelBorder() {
            super(0, 0, 0, 15);
        }
        public void paintBorder(Component c, Graphics oldGraphics, int x, int y, int width, int height) {
            SearchField field = (SearchField) c;
            if (field.showingPlaceholderText || field.getText().length() == 0) {
                return;
            }
            Graphics2D g = (Graphics2D) oldGraphics;
            g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            final int circleL = 14;
            final int circleX = x + width - circleL;
            final int circleY = y + (height - 1 - circleL) / 2;
            g.setColor(field.armed ? Color.GRAY : GRAY);
            g.fillOval(circleX, circleY, circleL, circleL);
            final int lineL = circleL - 8;
            final int lineX = circleX + 4;
            final int lineY = circleY + 4;
            g.setColor(Color.WHITE);
            g.drawLine(lineX, lineY, lineX + lineL, lineY + lineL);
            g.drawLine(lineX, lineY + lineL, lineX + lineL, lineY);
        }
    }
    
    /**
     * Handles a click on the cancel button by clearing the text and notifying
     * any ActionListeners.
     */
    private class CancelListener extends MouseInputAdapter {
        private boolean isOverButton(MouseEvent e) {
            // If the button is down, we might be outside the component
            // without having had mouseExited invoked.
            if (contains(e.getPoint()) == false) {
                return false;
            }
            // In lieu of proper hit-testing for the circle, check that
            // the mouse is somewhere in the border.
            Rectangle innerArea = SwingUtilities.calculateInnerArea(SearchField.this, null);
            return (innerArea.contains(e.getPoint()) == false);
        }
        public void mouseDragged(MouseEvent e) {
            arm(e);
        }
        public void mouseEntered(MouseEvent e) {
            arm(e);
        }
        public void mouseExited(MouseEvent e) {
            disarm();
        }
        public void mousePressed(MouseEvent e) {
            arm(e);
        }
        public void mouseReleased(MouseEvent e) {
        	LOG.debug("Mouse released; armed="+armed);
            if (armed) {
                cancel();
            }
            disarm();
        }
        private void arm(MouseEvent e) {
            armed = (isOverButton(e) && SwingUtilities.isLeftMouseButton(e));
            repaint();
        }
        private void disarm() {
            armed = false;
            repaint();
        }
    }
    
    
    /**
     * Replaces the entered text with a gray placeholder string when the
     * search field doesn't have the focus. The entered text returns when
     * we get the focus back.
     */
    private class PlaceholderText implements FocusListener {
        private String placeholderText;
        private String previousText = "";
        private Color previousColor;
        PlaceholderText(String placeholderText) {
            this.placeholderText = placeholderText;
            focusLost(null);
        }
        public void focusGained(FocusEvent e) {
            setForeground(previousColor);
            setText(previousText);
            showingPlaceholderText = false;
        }
        public void focusLost(FocusEvent e) {
            previousText = getText();
            previousColor = getForeground();
            if (previousText.length() == 0) {
                showingPlaceholderText = true;
                setForeground(Color.GRAY);
                setText(placeholderText);
            }
        }
    }
}
