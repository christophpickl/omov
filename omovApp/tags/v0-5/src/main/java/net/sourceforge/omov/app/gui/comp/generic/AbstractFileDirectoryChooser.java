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

package net.sourceforge.omov.app.gui.comp.generic;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.filechooser.FileFilter;

import net.sourceforge.omov.core.util.GuiAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
abstract class AbstractFileDirectoryChooser extends JPanel implements ActionListener {

    private static final Log LOG = LogFactory.getLog(AbstractFileDirectoryChooser.class);

    static final String DEFAULT_BUTTON_LABEL = "Set";
    
    static final ButtonPosition DEFAULT_BUTTON_POSITION = ButtonPosition.RIGHT;
    
    
    private File fileOrDir;
    
    private JTextField directoryPath = new JTextField(15);

    /** can be null */
    private File defaultPath;

    private final String dialogTitle;
    
    private final JButton button;

    private final Set<IChooserListener> listeners = new HashSet<IChooserListener>();
    
    
    
    public AbstractFileDirectoryChooser(String dialogTitle) {
        this(dialogTitle, null, DEFAULT_BUTTON_POSITION, DEFAULT_BUTTON_LABEL);
    }

    public AbstractFileDirectoryChooser(String dialogTitle, ButtonPosition position) {
        this(dialogTitle, null, position, DEFAULT_BUTTON_LABEL);
    }

    public AbstractFileDirectoryChooser(String dialogTitle, File defaultPath, ButtonPosition position) {
        this(dialogTitle, defaultPath, position, DEFAULT_BUTTON_LABEL);
    }
    
    public AbstractFileDirectoryChooser(String dialogTitle, File defaultPath, ButtonPosition position, String buttonLabel) {
        LOG.info("Constructing new directory chooser instance (buttonLabel="+buttonLabel+"; defaultPath="+(defaultPath == null ? "null" : defaultPath.getAbsolutePath())+").");
        assert(buttonLabel.length() > 0);
        
        this.defaultPath = defaultPath;
        this.directoryPath.setEditable(false);
        this.dialogTitle = dialogTitle;
        
        this.button = new JButton(buttonLabel);
        this.button.setOpaque(false);
        this.button.addActionListener(this);
        
        this.initComponents(position);
    }
    
    
    
    private void initComponents(final ButtonPosition btnPosition) {
        this.setOpaque(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(this, c);
        this.setLayout(layout);
        c.gridy = 0;

        final int buttonTextGap = 6;
        
        c.insets = new Insets(0, 0, 0, (btnPosition == ButtonPosition.LEFT) ? buttonTextGap : 0); // adjust right margin
        c.gridx = 0;
        this.add((btnPosition == ButtonPosition.LEFT) ? this.button : this.directoryPath, c);

        c.insets = new Insets(0, (btnPosition == ButtonPosition.LEFT) ? 0 : buttonTextGap, 0, 0); // adjust left margin
        c.gridx++;
        this.add((btnPosition == ButtonPosition.LEFT) ? this.directoryPath : this.button, c);
    }
    
    final void setFileOrDir(final File fileOrDir) {
        this.fileOrDir = fileOrDir;
        
        if(this.fileOrDir == null) {
            this.clearYourself();
        } else {
            if(this.fileOrDir.exists() == false || this.getIsRightFileOrDir(fileOrDir) == false) {
                JOptionPane.showMessageDialog(this, "The file at '"+fileOrDir.getAbsolutePath()+"' is invalid!", "", JOptionPane.WARNING_MESSAGE);
                this.clearYourself();
            } else {
                this.directoryPath.setText(this.fileOrDir.getAbsolutePath());
            }
        }
    }

    public final void __unchecked_setFileOrDir(final File directory) {
        this.fileOrDir = directory;
        this.directoryPath.setText(this.fileOrDir.getAbsolutePath());
    }
    
    // kann in zukunft zb auch folder-icon resetten
    private void clearYourself() {
        this.directoryPath.setText("");
    }
    
    /**
     * @return can be null
     */
    final File getFileOrDir() {
        return this.fileOrDir;
    }
    
    abstract boolean getIsRightFileOrDir(final File file);
    
    public final void setDefaultPath(File defaultPath) {
    	LOG.debug("Setting default path to '"+(defaultPath == null ? "null" : defaultPath.getAbsolutePath())+"'.");
        this.defaultPath = defaultPath;
    }

    abstract int getSelectionMode();
    abstract FileFilter getFileFilter();
    
    public final void actionPerformed(ActionEvent event) {
        new GuiAction() { protected void _action() {
            LOG.debug("showing file chooser with default path '"+(defaultPath == null?"null":defaultPath.getAbsolutePath())+"'...");
            
            final JFileChooser chooser = new JFileChooser(defaultPath);
            chooser.setDialogTitle(dialogTitle);
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(getSelectionMode());
            
            if(getFileFilter() != null) {
                chooser.setFileFilter(getFileFilter());
            }
            
            int returnVal = chooser.showOpenDialog(AbstractFileDirectoryChooser.this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                final File selectedFileOrDir = chooser.getSelectedFile();
                assert(selectedFileOrDir != null);
                setFileOrDir(selectedFileOrDir);
                
                for (final IChooserListener listener : listeners) {
                    listener.doChoosen(selectedFileOrDir);
                }
            } else {
                LOG.debug("User canceled action.");
            }
        }}.doAction();
    }
    
    
    public final void addChooserListener(IChooserListener listener) {
        this.listeners.add(listener);
    }
    public final void removeDirectoryChooserListener(IChooserListener listener) {
        this.listeners.remove(listener);
    }
    
    @Override
    public final void setEnabled(boolean enabled) {
        this.button.setEnabled(enabled);
    }

    
}
