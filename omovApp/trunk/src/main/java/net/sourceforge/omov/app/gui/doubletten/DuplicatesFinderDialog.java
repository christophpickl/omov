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

package net.sourceforge.omov.app.gui.doubletten;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.WindowConstants;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableCellRenderer;

import net.sourceforge.jpotpourri.jpotface.IPtEscapeDisposeReceiver;
import net.sourceforge.jpotpourri.jpotface.PtEscapeDisposer;
import net.sourceforge.jpotpourri.jpotface.util.PtGuiUtil;
import net.sourceforge.omov.app.gui.MacLikeTable;
import net.sourceforge.omov.app.gui.doubletten.DuplicatesTableModel.DuplicatesColumn;
import net.sourceforge.omov.core.BeanFactory;
import net.sourceforge.omov.core.BusinessException;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.Movie;
import net.sourceforge.omov.guicore.GuiActionListener;
import net.sourceforge.omov.guicore.OmovGuiUtil;
import net.sourceforge.omov.logic.tools.doubletten.DuplicatesFinder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdesktop.swingx.JXTable;
import org.jdesktop.swingx.table.TableColumnExt;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class DuplicatesFinderDialog extends JDialog implements IPtEscapeDisposeReceiver {

    private static final Log LOG = LogFactory.getLog(DuplicatesFinderDialog.class);
    private static final long serialVersionUID = 747517539473473198L;

    private final DuplicatesFinder finder;

    private int[] similarMovieIndices = new int[0];
    private final DuplicatesTableModel tableModel;
    private final JXTable table;
    private final JButton btnDelete = new JButton("Delete Movie");
    
    public DuplicatesFinderDialog(final JFrame owner, final DuplicatesFinder finder) {
        super(owner, "Duplicate Finder Result", true);
        this.finder = finder;
        
        this.tableModel = new DuplicatesTableModel(this.finder);
        this.table = new MacLikeTable(this.tableModel) {
            private static final long serialVersionUID = -7772086018064365835L;
            @Override
			public Component prepareRenderer(TableCellRenderer renderer, int row, int column ) {
                final Component c = super.prepareRenderer(renderer, row, column);

                if(this.getSelectedRow() == row) {
                    c.setBackground(Constants.getColorSelectedBackground());
                    c.setForeground(Constants.getColorSelectedForeground());
                } else {
                    for (int i : DuplicatesFinderDialog.this.similarMovieIndices) {
                        if(row == i) {
                            c.setBackground(Constants.getColorLightGray());
                            break;
                        }
                    }
                }
                
                return c;
            }
        };
        

        
        this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
        this.addWindowListener(new WindowAdapter() {
            @Override
			public void windowClosing(final WindowEvent event) {
                doClose();
            }
        });
        PtEscapeDisposer.enableEscape(this.getRootPane(), this);
        PtEscapeDisposer.enableEscape(this.table, this);
        
        
        for(DuplicatesColumn movieColumn : DuplicatesTableModel.getColumns()) {
            final TableColumnExt column = this.table.getColumnExt(movieColumn.getLabel());
            column.setPreferredWidth(movieColumn.getPrefWidth());
            if(movieColumn.getMaxWidth() != -1) column.setMaxWidth(movieColumn.getMaxWidth());
            if(movieColumn.getMinWidth() != -1) column.setMinWidth(movieColumn.getMinWidth());
        }
        
        this.table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.table.setAutoResizeMode(JTable.AUTO_RESIZE_NEXT_COLUMN);
        this.table.packAll();
        
        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
            public void valueChanged(ListSelectionEvent event) {
                if(event.getValueIsAdjusting() == true) {
                    return;
                }
                doSelectionChanged();
                
            }
        });
        
        
        this.getContentPane().add(this.initComponents());
        this.pack();
        PtGuiUtil.setCenterLocation(this);
        OmovGuiUtil.lockWidthAndHeightAsMinimum(this, 300, 160);
    }
    
    private void doSelectionChanged() {
        final int tableRow = this.table.getSelectedRow();
        this.btnDelete.setEnabled(tableRow != -1);
        
        if(tableRow == -1) {
            this.similarMovieIndices = new int[0];
        } else {
            final int modelRow = this.table.convertRowIndexToModel(tableRow);
            final int[] modelIndices = this.tableModel.selectionChanged(modelRow);
            final int[] rowIndices = new int[modelIndices.length];
            for (int i = 0; i < modelIndices.length; i++) {
                rowIndices[i] = this.table.convertRowIndexToView(modelIndices[i]);
            }
            this.similarMovieIndices = rowIndices;
            
            this.table.repaint();
        }
    }
    
    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(12, 10, 10, 10));

        final JScrollPane tableScroll = new JScrollPane(this.table);
        tableScroll.setPreferredSize(new Dimension(600, 180));
        
        panel.add(tableScroll, BorderLayout.CENTER);
        panel.add(this.southPanel(), BorderLayout.SOUTH);

        return panel;
    }
    
    private JPanel southPanel() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        panel.setBackground(Constants.getColorWindowBackground());
        
        this.btnDelete.setEnabled(false);
        this.btnDelete.setOpaque(false);
        // if in need of another ActionListener -> write actionPerformed() method and outsource GuiAction instantiation
        this.btnDelete.addActionListener(new GuiActionListener() {
        	@Override
			public void action(final ActionEvent event) {
        		doDelete();
        }});

        final JButton btnClose = new JButton("Close");
        btnClose.setOpaque(false);
        btnClose.addActionListener(new GuiActionListener() {@Override
		public void action(ActionEvent e) {
            DuplicatesFinderDialog.this.doClose();
        }});
        this.getRootPane().setDefaultButton(btnClose);
        
        panel.add(this.btnDelete, BorderLayout.WEST);
        panel.add(btnClose, BorderLayout.EAST);

        return panel;
    }
    
    private void doDelete() {
        final int selectedRow = this.table.getSelectedRow();
        LOG.debug("Deleting duplicate; selectedRow="+selectedRow);
        
        if(selectedRow == -1) {
            LOG.debug("ignoring delete button because no row was selected.");
            return;
        }
        final int modelRow = this.table.convertRowIndexToModel(selectedRow);
        final Movie duplicate = this.tableModel.getMovieAtRow(modelRow);
        
        final boolean confirmed = OmovGuiUtil.getYesNoAnswer(this, "Delete Duplicate Movie", "Do you really want to delete the\nduplicate Movie '"+duplicate.getTitle()+"' (ID="+duplicate.getId()+")?");
        if(confirmed == false) {
            LOG.debug("Deleting duplicate aborted by user.");
            return;
        }
        
        this.tableModel.deleteMovie(modelRow, duplicate);
        try {
            BeanFactory.getInstance().getMovieDao().deleteMovie(duplicate);
        } catch (BusinessException e) {
            LOG.error("Could not delete duplicate movie", e);
            OmovGuiUtil.error("Delete Duplicate Movie", "The movie '"+duplicate.getTitle()+"' could not be deleted!\nAlthough it now had disappeared from duplicates.");
        }
    }

    private void doClose() {
    	this.dispose();
    }
    
	public void doEscape() {
		this.doClose();
	}
    
    /*
    private class SimilarHighlighterRenderer extends DefaultTableCellRenderer {

        private static final long serialVersionUID = -4727591028580436195L;
        
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            
            if(column == 0) {
                System.out.println("rendering row " + row + ": similar ints are " + Arrays.toString(similarMovieIndices) + " ... ");
            }
            this.setOpaque(true);
            for (int i : similarMovieIndices) {
                if(row == i && isSelected == false) {
                    if(column == 0) System.out.println("green");
                    this.setBackground(Color.GREEN);
                    this.setForeground(Color.GREEN);
                    break;
                }
            }
            // if ((row % 2) == 0)
            if(isSelected && column == 0) {
                if(column == 0) System.out.println("selected");
                this.setBackground(MovieTableX.COLOR_SELECTED_BG);
                this.setForeground(MovieTableX.COLOR_SELECTED_FG);
            }
            
            this.setText(String.valueOf(value));
            return this;
        }
    }
     */
}
