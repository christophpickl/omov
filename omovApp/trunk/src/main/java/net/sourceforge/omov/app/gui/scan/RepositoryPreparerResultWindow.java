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

package net.sourceforge.omov.app.gui.scan;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.WindowConstants;
import javax.swing.table.AbstractTableModel;

import net.sourceforge.jpotpourri.jpotface.PtEscapeDisposer;
import net.sourceforge.jpotpourri.jpotface.IPtEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.jpotpourri.util.PtCollectionUtil;
import net.sourceforge.omov.app.gui.MacLikeTable;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.tools.scan.PreparerHint;
import net.sourceforge.omov.core.tools.scan.RepositoryPreparer.PreparerResult;
import net.sourceforge.omov.gui.GuiActionListener;
import net.sourceforge.omov.gui.OmovGuiUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class RepositoryPreparerResultWindow extends JDialog implements IPtEscapeDisposeReceiver {

    private static final long serialVersionUID = 967471175757339990L;

    private final PreparerResult result;
    
    public RepositoryPreparerResultWindow(JDialog owner, PreparerResult result) {
        super(owner, "Repository Preparer Result", true);
        this.result = result;
        
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        
        PtEscapeDisposer.enableEscape(this.getRootPane(), this);

        OmovGuiUtil.macSmallWindow(this.getRootPane());
        
        this.setBackground(Constants.getColorWindowBackground());
        this.getContentPane().add(this.initComponents());
        this.pack();
        this.setResizable(true);
        PtGuiUtil.setCenterLocation(this);
        OmovGuiUtil.lockOriginalSizeAsMinimum(this);
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); // top, left, bottom, right

        panel.add(this.initNorth(), BorderLayout.NORTH);
        panel.add(this.initCenter(), BorderLayout.CENTER);
        panel.add(this.initSouth(), BorderLayout.SOUTH);
        
        return panel;
    }
    
    private JPanel initNorth() {
        final JPanel wrapPanel = new JPanel(new BorderLayout());
        wrapPanel.setBorder(BorderFactory.createEmptyBorder(0, 0, 6, 0));
        
        final GridBagLayout layout = new GridBagLayout();
        final GridBagConstraints c = new GridBagConstraints();
        final JPanel panel = new JPanel(layout);
        layout.setConstraints(panel, c);
        
        c.anchor = GridBagConstraints.LINE_START;
        c.gridx = 0;
        c.gridy = 0;
        panel.add(OmovGuiUtil.labelBold("Moved Files"), c);
        c.gridx = 1;
        panel.add(OmovGuiUtil.labelBold(String.valueOf(this.result.getCntMovedFiles())), c);
        
        c.gridx = 0;
        c.gridy = 1;
        panel.add(new JLabel("Ignored Files "), c);
        c.gridx = 1;
        panel.add(new JLabel("" + this.result.getCntIgnoredFiles()), c);

        c.gridx = 0;
        c.gridy = 2;
        panel.add(new JLabel("Ignored Directories "), c);
        c.gridx = 1;
        panel.add(new JLabel("" + this.result.getCntIgnoredDirs()), c);

        wrapPanel.add(panel, BorderLayout.WEST);
        return wrapPanel;
    }
    
    private JPanel initCenter() {
        final JPanel panel = new JPanel(new BorderLayout());

        final JTable table = new MacLikeTable(new PreparerResultTableModel(this.result.getHints()));
        table.setAutoResizeMode(JTable.AUTO_RESIZE_LAST_COLUMN);
        table.getColumnModel().getColumn(0).setMaxWidth(80);
        table.getColumnModel().getColumn(0).setMinWidth(80);

        PtEscapeDisposer.enableEscape(table, this);
        
        panel.add(OmovGuiUtil.wrapScroll(table, 500, 180), BorderLayout.CENTER);
        return panel;
    }
    
    private JPanel initSouth() {
        final JPanel panel = new JPanel(new BorderLayout());

        final JButton btnClose = new JButton("Close");
        this.getRootPane().setDefaultButton(btnClose);
        btnClose.addActionListener(new GuiActionListener() {
        	@Override
			public void action(ActionEvent e) {
            doClose();
        }});
        panel.add(btnClose, BorderLayout.EAST);
        
        return panel;
    }
    
    private static class PreparerResultTableModel extends AbstractTableModel {

        private static final long serialVersionUID = -3147258693975754931L;
        private static final List<String> ALL_COLUMN_NAMES = PtCollectionUtil.immutableList("Severity", "Message");
        private final List<PreparerHint> hints;

        private PreparerResultTableModel(final List<PreparerHint> hints) {
            this.hints = new ArrayList<PreparerHint>(hints);
            Collections.sort(this.hints);
            this.fireTableDataChanged();
        }
        public int getColumnCount() {
            return ALL_COLUMN_NAMES.size();
        }

        public int getRowCount() {
            return this.hints.size();
        }

        public Object getValueAt(int row, int col) {
            final PreparerHint hint = this.hints.get(row);
            if(col == 0) {
                return hint.getSeverity();
            } else if(col == 1) {
                return hint.getMsg();
            } else {
                throw new IllegalArgumentException("unhandled column index: " + col);
            }
        }
        @Override
		public String getColumnName(final int col) {
            return ALL_COLUMN_NAMES.get(col);
        }


        @Override
		public Class<?> getColumnClass(int col) {
            return String.class;
        }

        @Override
		public boolean isCellEditable(int rowIndex, int columnIndex) {
            return false;
        }
    }
    
    private void doClose() {
    	this.dispose();
    }

	public void doEscape() {
		this.doClose();
	}
}
