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

package net.sourceforge.omov.app.gui.export;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

import net.sourceforge.jpotpourri.gui.EscapeDisposer;
import net.sourceforge.jpotpourri.gui.IEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.gui.chooser.ButtonPosition;
import net.sourceforge.jpotpourri.gui.chooser.FileChooser;
import net.sourceforge.jpotpourri.gui.chooser.IFileDirectoryChooserListener;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.PreferencesDao;
import net.sourceforge.omov.core.tools.export.ImportExportConstants;
import net.sourceforge.omov.core.util.FileUtil;
import net.sourceforge.omov.core.util.GuiAction;
import net.sourceforge.omov.core.util.OmovGuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class BackupImportDialog extends JDialog implements ActionListener, IEscapeDisposeReceiver {

    private static final Log LOG = LogFactory.getLog(BackupImportDialog.class);
    private static final long serialVersionUID = 2471103972559399413L;

    private static final String CMD_IMPORT = "CMD_IMPORT";
    private static final String CMD_CLOSE = "CMD_CLOSE";
    // MANTIS [26] backup import: display list of movies which are stored in choosen backup file and let user select (checkbox) which movies should be imported
    
    private final BackupImportController controller;

    private final FileChooser inpFileChooser = new FileChooser("Open", "Choose BackupFile", null, ButtonPosition.LEFT, ImportExportConstants.BACKUP_FILE_EXTENSION);
    private final JProgressBar progressBar = new JProgressBar();
    private final JButton btnImport = new JButton("Import");
    private final JButton btnClose = new JButton("Close");
    
    public BackupImportDialog(JFrame owner) {
        super(owner, "Import Backup", true);
        
        EscapeDisposer.enableEscape(this.getRootPane(), this);

        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });
        
        this.controller = new BackupImportController(this);

        this.inpFileChooser.setDefaultPath(new File(PreferencesDao.getInstance().getRecentBackupImportPath()));
        this.inpFileChooser.addChooserListener(new IFileDirectoryChooserListener() {
            public void doChoosen(final File backupFile) {
                new GuiAction() {
                    @Override
                    protected void _action() {

                        PreferencesDao.getInstance().setRecentBackupImportPath(backupFile.getParentFile().getAbsolutePath());
                        btnImport.setEnabled(true);
                    }
                }.doAction();
            }
        });
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        OmovGuiUtil.setCenterLocation(this);
    }
    
    public void setZipFile(File backupFile) {
        assert(FileUtil.extractExtension(backupFile).equalsIgnoreCase(ImportExportConstants.BACKUP_FILE_EXTENSION)); // assert *.omo extension
        
        this.inpFileChooser.setFile(backupFile);
        this.btnImport.setEnabled(true);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel();
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        panel.setBackground(Constants.getColorWindowBackground());
        
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        c.insets = new Insets(0, 0, 0, 0);
        c.gridx = 0;
        c.gridy = 0;
        panel.add(this.northPanel(), c);
        
        c.insets = new Insets(8, 0, 0, 0);
        c.gridy = 1;
        panel.add(this.southPanel(), c);

        return panel;
    }

    private JPanel northPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setOpaque(false);
        
        panel.add(this.inpFileChooser, BorderLayout.CENTER);

        return panel;
    }
    
    private JPanel southPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        
        panel.setOpaque(false);
        this.progressBar.setOpaque(false);
        this.btnImport.setOpaque(false);
        this.btnClose.setOpaque(false);
        
        this.btnImport.addActionListener(this);
        this.btnImport.setActionCommand(CMD_IMPORT);
        this.btnImport.setEnabled(false);
        
        this.btnClose.addActionListener(this);
        this.btnClose.setActionCommand(CMD_CLOSE);
        
        panel.add(this.btnImport, BorderLayout.WEST);
        panel.add(this.progressBar, BorderLayout.CENTER);
        panel.add(this.btnClose, BorderLayout.EAST);

        return panel;
    }
    
    void enableInterface(final boolean enabled) {
        this.inpFileChooser.setEnabled(enabled);
        this.btnImport.setEnabled(enabled);
        this.btnClose.setEnabled(enabled);
        this.progressBar.setIndeterminate(!enabled);
        this.setDefaultCloseOperation(enabled ? JFrame.DISPOSE_ON_CLOSE : JFrame.DO_NOTHING_ON_CLOSE);
    }

    public void actionPerformed(final ActionEvent event) {
        new GuiAction() {
            @Override
            protected void _action() {
                final String cmd = event.getActionCommand();
                LOG.debug("Action performed with command '"+cmd+"'.");
                
                if(cmd.equals(CMD_IMPORT)) {
                    controller.doImportBackup(inpFileChooser.getSelectedFile());
                } else if(cmd.equals(CMD_CLOSE)) {
                    BackupImportDialog.this.dispose();
                } else {
                    throw new FatalException("Unhandled action command '"+cmd+"'!");
                }
            }
        }.doAction();
    }

    private void doCancel() {
    	this.dispose();
    }
    
	public void doEscape() {
		this.doCancel();
	}
    
}
