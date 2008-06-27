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

package net.sourceforge.omov.app.gui.smartfolder;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.EtchedBorder;

import net.sourceforge.omov.app.gui.movie.AbstractAddEditDialog;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.smartfolder.AbstractColumnCriterion;
import net.sourceforge.omov.core.smartfolder.SmartFolder;
import net.sourceforge.omov.core.util.OmovGuiUtil;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import at.ac.tuwien.e0525580.jlib.util.CollectionUtil;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class AddEditSmartFolderDialog extends AbstractAddEditDialog<SmartFolder> implements ISmartFolderGuiRowListener {

    private static final Log LOG = LogFactory.getLog(AddEditSmartFolderDialog.class);
    private static final long serialVersionUID = -3926633241922397590L;

    private final JTextField inpTitle = new JTextField(15);

    private final JComboBox inpAllAny = new JComboBox(CollectionUtil.asArray("all", "any"));

    private final JPanel rowWrapPanel = new JPanel();
    private final List<SmartFolderGuiRow> guiRows = new LinkedList<SmartFolderGuiRow>();
    
    
    public AddEditSmartFolderDialog(JFrame owner, SmartFolder smartFolder) {
        super(owner, smartFolder);
        
        this.setTitle( this.isAddMode() ? "Add new Smartfolder" : smartFolder.getName());
        this.getContentPane().add(this.initComponents());
        this.pack();
        OmovGuiUtil.setCenterLocation(this);
    }

    private JPanel initComponents() {
        final JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Constants.getColorWindowBackground());
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        
        panel.add(this.newTopPanel(), BorderLayout.NORTH);
        panel.add(this.newCenterPanel(), BorderLayout.CENTER);
        panel.add(this.newCommandPanel(), BorderLayout.SOUTH);
        
        return panel;
    }

    private static final String LBL_MATCH_CRITERIA = "of the following criteria for folder ";
    private static final String LBL_MATCH_CRITERION = "the following criterion for folder ";
    
    private final JLabel lblMatchCriteria = new JLabel();
    
    private JPanel newTopPanel() {
        final JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panel.setOpaque(false);
        this.inpAllAny.setOpaque(false);
        
//        this.inpAllAny.setBorder(BorderFactory.createEmptyBorder(arg0, arg1, arg2, arg3));
        
        final String lblMatchCriteriaText;
        if(this.isAddMode() == false) {
            this.inpTitle.setText(this.getEditItem().getName());
            this.inpAllAny.setSelectedIndex(this.getEditItem().isMatchAll() ? 0 : 1);
            lblMatchCriteriaText = (this.getEditItem().getCriteria().size() == 1) ? LBL_MATCH_CRITERION : LBL_MATCH_CRITERIA;
        } else {
        	lblMatchCriteriaText = LBL_MATCH_CRITERION;
        }
        
        this.lblMatchCriteria.setText(lblMatchCriteriaText);
        
        panel.add(new JLabel("Match"));
        panel.add(this.inpAllAny);
        panel.add(this.lblMatchCriteria);
        panel.add(this.inpTitle);

        return panel;
    }

    private JScrollPane newCenterPanel() {
        rowWrapPanel.setLayout(new BoxLayout(rowWrapPanel, BoxLayout.Y_AXIS));
        this.rowWrapPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED)); // @see http://java.sun.com/docs/books/tutorial/uiswing/components/border.html

        if(this.isAddMode()) {
            this.addGuiRow(SmartFolderGuiRow.newDefaultRow(this));
        } else {
            for (AbstractColumnCriterion<?> criterion: this.getEditItem().getCriteria()) {
                final String column = criterion.getColumnLabel();
                final String match = criterion.getMatchLabel();
                final Object[] values = criterion.getValues();
                this.addGuiRow(new SmartFolderGuiRow(this, column, match, values));
            }
        }
        
        final JScrollPane scrollPane = new JScrollPane(this.rowWrapPanel);
        scrollPane.setWheelScrollingEnabled(true);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        return scrollPane;
    }
    
    private void addGuiRow(SmartFolderGuiRow row) {
        this.guiRows.add(row);
        this.rowWrapPanel.add(row);
        this.rowWrapPanel.revalidate();
        
        if(this.guiRows.size() == 1) {
            this.guiRows.get(0).setDeleteButtonEnabled(false); // very first time
            this.inpAllAny.setVisible(false);
        } else {
        	assert(this.guiRows.size() > 1);
            this.guiRows.get(0).setDeleteButtonEnabled(true);
            this.inpAllAny.setVisible(true);
            this.lblMatchCriteria.setText(LBL_MATCH_CRITERIA);
        }
        this.pack();
    }
    
    private void deleteGuiRow(SmartFolderGuiRow row) {
        final boolean wasDeleted = this.guiRows.remove(row);
        assert(wasDeleted);
        
        this.rowWrapPanel.remove(row);
        this.rowWrapPanel.revalidate();

        if(this.guiRows.size() == 1) {
            this.guiRows.get(0).setDeleteButtonEnabled(false);
            this.inpAllAny.setVisible(false);
            this.lblMatchCriteria.setText(LBL_MATCH_CRITERION);
        }
        this.pack();
    }
    
    public void doPack() {
        this.pack();
    }
    
    @Override
    protected SmartFolder _getConfirmedObject() {
        final long id = (this.isAddMode()) ? -1 : this.getEditItem().getId();
        final String name = (this.inpTitle.getText().length() == 0) ? "N/A" : this.inpTitle.getText();
        final boolean matchAll = this.inpAllAny.getSelectedIndex() == 0;
        
        final List<AbstractColumnCriterion<?>> criteria = new ArrayList<AbstractColumnCriterion<?>>(this.guiRows.size());
        for(final SmartFolderGuiRow row : this.guiRows) {
            final AbstractColumnCriterion<?> criterion = row.createCriterion();
            if(criteria == null) {
                LOG.info("Skipped row " + row);
            } else {
                criteria.add(criterion);
            }
        }
        
        return new SmartFolder(id, name, matchAll, criteria);
    }

    public void doAddRow() {
        this.addGuiRow(SmartFolderGuiRow.newDefaultRow(this));
    }

    public void doDeleteRow(SmartFolderGuiRow row) {
        this.deleteGuiRow(row);
    }
    
    
    public static void main(String[] args) {
        AddEditSmartFolderDialog addWindow = new AddEditSmartFolderDialog(null, null);
        addWindow.setVisible(true);
        if(addWindow.isActionConfirmed()) {
            addWindow.getConfirmedObject();
        }
    }
}
