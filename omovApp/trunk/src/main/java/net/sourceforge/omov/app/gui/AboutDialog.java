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

package net.sourceforge.omov.app.gui;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.net.MalformedURLException;

import javax.swing.BorderFactory;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.ImageFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.action.OpenBrowserAction;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class AboutDialog extends JDialog {

    private static final Log LOG = LogFactory.getLog(AboutDialog.class);
    private static final long serialVersionUID = -6058616320816195022L;
    
    
    public AboutDialog(JFrame owner) {
        super(owner, "About", true);
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);
        panel.setBorder(BorderFactory.createEmptyBorder(20, 80, 40, 80)); // top, left, bottom, right


//        final JButton logo = new JButton(ImageFactory.getInstance().getAboutLogo());
//        logo.setBorderPainted(false);
//        GuiUtil.enableHandCursor(logo);
//        try {
//            logo.addActionListener(new OpenBrowserAction(Constants.OMOV_WEBSITE_URL));
//            logo.setToolTipText("Visit: " + Constants.OMOV_WEBSITE_URL);
//        } catch (MalformedURLException e) {
//            LOG.error("OpenBrowserAction failed.", e);
//        }
        final JLabel logo = new JLabel(ImageFactory.getInstance().getAboutLogo(), JLabel.CENTER);
        try {
            final OpenBrowserAction openBrowser = new OpenBrowserAction(Constants.getWebUrl());
            logo.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    openBrowser.actionPerformed(null);
                }
            });
            GuiUtil.enableHandCursor(logo);
        } catch (MalformedURLException e) {
            LOG.error("OpenBrowserAction failed.", e);
        }
        
        final JLabel title = new JLabel("OurMovies", JLabel.CENTER);
        title.setFont(new Font("default", Font.BOLD, 13));
        final JLabel versionLabel = new JLabel("Version " + BeanFactory.getInstance().getCurrentApplicationVersion().getVersionString(), JLabel.CENTER);
        versionLabel.setFont(new Font("default", Font.PLAIN, 11));
        
        c.anchor = GridBagConstraints.CENTER;
        c.gridx = 0;

        c.insets = new Insets(0, 0, 20, 0); // top, left, bottom, right
        c.gridy = 0;
        panel.add(logo, c);

        c.insets = new Insets(0, 0, 6, 0); // top, left, bottom, right
        c.gridy = 1;
        panel.add(title, c);

        c.insets = new Insets(0, 0, 0, 0); // top, left, bottom, right
        c.gridy = 2;
        panel.add(versionLabel, c);
        
        
        return panel;
    }
    
    
    public static void main(String[] args) {
        new AboutDialog(null).setVisible(true);
    }
}
