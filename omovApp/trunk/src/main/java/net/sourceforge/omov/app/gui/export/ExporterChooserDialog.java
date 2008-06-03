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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.CompoundBorder;

import net.sourceforge.omov.app.gui.export.ComboMovieSelection.MovieSelectionMode;
import net.sourceforge.omov.app.gui.main.MainWindowController;
import net.sourceforge.omov.app.util.GuiUtil;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.FatalException;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.gui.EscapeDisposer;
import net.sourceforge.omov.gui.GuiActionListener;
import net.sourceforge.omov.gui.EscapeDisposer.IEscapeDisposeReceiver;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class ExporterChooserDialog extends JDialog implements IEscapeDisposeReceiver {

    private static final long serialVersionUID = -683746227786551236L;
    
    private final ExporterChooserController controller = new ExporterChooserController(this);

    private final JRadioButton btnHtml = new JRadioButton("HTML");
    private final HtmlExportPanel panelHtml = new HtmlExportPanel();
    private final JRadioButton btnBackup = new JRadioButton("BACKUP");
    private final BackupExportPanel panelBackup = new BackupExportPanel();
    
    private final ComboMovieSelection inpMovieSelection = new ComboMovieSelection();

    private final JPanel contentPanel = new JPanel(new BorderLayout());
    private final MainWindowController mainController;
    
    public ExporterChooserDialog(JFrame owner, MainWindowController mainController) {
        super(owner);
        this.mainController = mainController;
        this.setTitle("Export");
        this.setModal(true);
        
        EscapeDisposer.enableEscape(this.getRootPane(), this);
        
        this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            public void windowClosing(final WindowEvent event) {
                doCancel();
            }
        });

        final ButtonGroup group = new ButtonGroup();
        group.add(this.btnHtml);
        group.add(this.btnBackup);
        
        this.btnHtml.setSelected(true);
        this.doButtonSelected(this.btnHtml);
        
        this.btnHtml.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
            doButtonSelected(btnHtml);
        }});
        this.btnBackup.addActionListener(new GuiActionListener() { public void action(ActionEvent e) {
           doButtonSelected(btnBackup);
        }});
        
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(false);
        GuiUtil.setCenterLocation(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());
        this.panelHtml.setOpaque(false);
        this.panelBackup.setOpaque(false);

        panel.add(this.newNorthPanel(), BorderLayout.NORTH);
        panel.add(this.newContentPanel(), BorderLayout.CENTER);
        panel.add(this.newCommandPanel(), BorderLayout.SOUTH);
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 0, 10)); // top, left, bottom, right
        return panel;
    }
    
    private JPanel newNorthPanel() {
        final JPanel panel = new JPanel();
        panel.setOpaque(false);
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        this.btnHtml.setOpaque(false);
        this.btnBackup.setOpaque(false);
        
        c.anchor = GridBagConstraints.LINE_START;
        
        c.gridx = 0;
        c.gridy = 0;
        panel.add(new JLabel("Export "), c);
        c.gridx++;
        panel.add(this.inpMovieSelection, c);
        c.gridx++;
        panel.add(new JLabel(" Movies as "), c);
        c.gridx++;
        panel.add(this.btnHtml, c);
        c.gridx++;
        panel.add(this.btnBackup, c);

        CompoundBorder border = new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, Color.GRAY),
                BorderFactory.createEmptyBorder(0, 0, 6, 0)
        );
        panel.setBorder(border);
        
        return panel;
    }

    private JPanel newContentPanel() {
        this.contentPanel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0)); // top, left, bottom, right
        this.contentPanel.setOpaque(false);
        return this.contentPanel;
    }
    
    private void doButtonSelected(JRadioButton button) {
        
        final JPanel panel;
        if(button == this.btnHtml) {
            panel = this.panelHtml;
        } else {
            panel = this.panelBackup;
        }

        this.contentPanel.removeAll();
        this.contentPanel.add(panel, BorderLayout.CENTER);
        this.pack();
        this.contentPanel.revalidate();
    }
    
    
    private JPanel newCommandPanel() {
        final JButton btnConfirm = new JButton("Export");
        final JButton btnCancel = new JButton("Cancel");
        btnConfirm.setOpaque(false);
        btnCancel.setOpaque(false);

        this.getRootPane().setDefaultButton(btnConfirm);

        btnConfirm.addActionListener(new GuiActionListener() { public void action(ActionEvent event) {
            doConfirm();
        }});
        btnCancel.addActionListener(new GuiActionListener() { public void action(ActionEvent event) {
            doCancel();
        }});
        
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panel.setOpaque(false);
        panel.add(btnCancel);
        panel.add(btnConfirm);
        return panel;
    }
    
    private void doConfirm() {
        final List<Movie> movies = this.getMoviesToExport();
        
        if(movies.size() == 0) { // [mantis 0000060]
        	GuiUtil.warning(this, "Movie Export", "Sorry, but there are no movies to export.");
        	return;
        }
        
        
        if(this.btnHtml.isSelected()) {
            this.controller.doExportHtml(this.panelHtml.getHtmlColumns(), movies);
        } else if(this.btnBackup.isSelected()) {
            this.controller.doExportBackup(movies);
        } else {
            throw new FatalException("Unkown export format selected.");
        }
        this.dispose();
        
    }
    
    private List<Movie> getMoviesToExport() {
        final MovieSelectionMode mode = this.inpMovieSelection.getMovieSelectionMode();
        if(mode == MovieSelectionMode.ALL) {
            try {
                return BeanFactory.getInstance().getMovieDao().getMoviesSorted();
            } catch (BusinessException e) {
                throw new FatalException("Could not get movies from dao!", e);
            }
        } else if(mode == MovieSelectionMode.VISIBLE) {
            return this.mainController.getVisibleTableMovies();
        } else if(mode == MovieSelectionMode.SELECTED) {
            return this.mainController.getSelectedTableMovies();
        } else {
            throw new FatalException("Unhandled movie selection mode '"+mode+"'!");
        }
    }
    
    private void doCancel() {
        this.dispose();
    }

	public void doEscape() {
		this.doCancel();
	}

}
