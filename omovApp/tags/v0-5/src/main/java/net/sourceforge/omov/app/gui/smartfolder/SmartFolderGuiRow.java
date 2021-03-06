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

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Arrays;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import net.sourceforge.omov.app.gui.smartfolder.fields.AbstractCriterionField;
import net.sourceforge.omov.app.gui.smartfolder.fields.CriterionFieldFactory;
import net.sourceforge.omov.core.Constants;
import net.sourceforge.omov.core.bo.Movie.MovieField;
import net.sourceforge.omov.core.smartfolder.AbstractColumnCriterion;
import net.sourceforge.omov.core.smartfolder.SmartFolderUtil;
import net.sourceforge.omov.core.smartfolder.TextMatch;
import net.sourceforge.omov.core.util.GuiAction;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
public class SmartFolderGuiRow  extends JPanel {
    
    private static final long serialVersionUID = -1092069124704329741L;
    private static final Log LOG = LogFactory.getLog(SmartFolderGuiRow.class);

    private final JComboBox comboColumn = new JComboBox(SmartFolderUtil.getAllColumnLabels().toArray());
    
    private final JComboBox comboMatches;
    private final ComboMatchesModel comboMatchesModel;
    
    private final JPanel fieldWrapper = new JPanel();
    private AbstractCriterionField field;

    private final JButton btnAdd = new JButton("+");
    private final JButton btnDelete = new JButton("-");

    private String selectedColumnLabel;
    private String selectedMatchLabel;
    
    private final ISmartFolderGuiRowListener listener;
    
    
    public SmartFolderGuiRow(final ISmartFolderGuiRowListener listener, String preselectedColumnLabel, String preselectedMatchLabel, Object[] values) {
        this.setBackground(Constants.getColorWindowBackground());
        
        this.setLayout(new FlowLayout(FlowLayout.LEFT));
        this.listener = listener;
        this.selectedColumnLabel = preselectedColumnLabel;
        this.selectedMatchLabel = preselectedMatchLabel;
        
        this.comboColumn.setSelectedItem(preselectedColumnLabel);
        
        this.comboMatchesModel = new ComboMatchesModel(SmartFolderUtil.getMatchesLabelByColumnLabel(preselectedColumnLabel));
        this.comboMatches = new JComboBox(this.comboMatchesModel);
        this.comboMatches.setSelectedItem(preselectedMatchLabel);
        
        final Dimension dimension = new Dimension(160, (int) this.comboColumn.getPreferredSize().getHeight());
        this.comboColumn.setPreferredSize(dimension);
        this.comboMatches.setPreferredSize(dimension);
        
        this.field = CriterionFieldFactory.newField(preselectedColumnLabel, preselectedMatchLabel, values);
        this.fieldWrapper.add(this.field);

        final Dimension btnDimension = new Dimension(60, (int) this.btnAdd.getPreferredSize().getHeight());
        this.btnAdd.setPreferredSize(btnDimension);
        this.btnDelete.setPreferredSize(btnDimension);
        this.btnAdd.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            listener.doAddRow();
        }});
        this.btnDelete.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            listener.doDeleteRow(SmartFolderGuiRow.this);
        }});
        
        this.add(this.btnAdd);
        this.add(this.btnDelete);
        this.add(this.comboColumn);
        this.add(this.comboMatches);
        this.add(this.fieldWrapper);

        this.comboColumn.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            new GuiAction() { protected void _action() {
                doChangeColumn();
            }}.doAction();
        }});
        this.comboMatches.addActionListener(new ActionListener() { public void actionPerformed(ActionEvent e) {
            new GuiAction() { protected void _action() {
                doChangeMatch();
            }}.doAction();
        }});
        
        this.btnAdd.setOpaque(false);
        this.btnDelete.setOpaque(false);
        this.comboColumn.setOpaque(false);
        this.comboMatches.setOpaque(false);
        this.fieldWrapper.setOpaque(false);
    }
    
    public void setDeleteButtonEnabled(final boolean enabled) {
        this.btnDelete.setEnabled(enabled);
    }

    private void doChangeColumn() {
        final String newColumn = (String) this.comboColumn.getSelectedItem();
        LOG.debug("changed to column '"+newColumn+"'.");
        
        this.comboMatchesModel.setData(SmartFolderUtil.getMatchesLabelByColumnLabel(newColumn));
        
        final String newMatch = (String) this.comboMatchesModel.getSelectedItem();
        this.changeField(newColumn, newMatch);
    }

    private void doChangeMatch() {
        final String newMatch = (String) this.comboMatches.getSelectedItem();
        LOG.debug("changed to match '"+newMatch+"'.");
        this.changeField((String) this.comboColumn.getSelectedItem(), newMatch);
    }
    
    private void changeField(String columnLabel, String matchLabel) {
        if(this.selectedColumnLabel.equals(columnLabel) && this.selectedMatchLabel.equals(matchLabel)) {
            LOG.debug("ignoring fieldchange to column '"+columnLabel+"' and match '"+matchLabel+"' because its already selected");
            return;
        }
        
        this.selectedColumnLabel = columnLabel;
        this.selectedMatchLabel = matchLabel;
        LOG.debug("changing field for column '"+columnLabel+"' and match '"+matchLabel+"'.");
        this.field = CriterionFieldFactory.newField(columnLabel, matchLabel, null);
        this.fieldWrapper.removeAll();
        this.fieldWrapper.add(this.field);
        this.fieldWrapper.revalidate();
        
        this.listener.doPack();
    }
    
    public static SmartFolderGuiRow newDefaultRow(ISmartFolderGuiRowListener listener) {
        return new SmartFolderGuiRow(listener, MovieField.TITLE.label(), TextMatch.LABEL_EQUALS, new String[] { "" } );
//        return new SmartFolderGuiRow(NumberCriterion.LABEL_YEAR, NumberMatch.LABEL_EQUALS, new Integer[] { DateUtil.getCurrentYear() } );
//        return new SmartFolderGuiRow(listener, MovieField.DATE_ADDED.label(), DateMatch.LABEL_EQUALS, new Date[] { new Date() } );
    }
    
    /**
     * @return null if user entered invalid input (see DateField)
     */
    public AbstractColumnCriterion<?> createCriterion() {
        final Object[] values = this.field.getValues();
        if(values == null) return null;
        return CriterionFieldFactory.newCriterion(this.selectedColumnLabel, this.selectedMatchLabel, values);
    }
    
    public String toString() {
        final String fieldValues = this.field.getValues() == null ? "null" : Arrays.toString(this.field.getValues());
        return "SmartFolderGuiRow[column="+this.comboColumn.getSelectedItem()+";match"+this.comboMatches.getSelectedItem()+";field.values="+fieldValues+"]";
    }
    
    
    private static class ComboMatchesModel extends DefaultComboBoxModel { 

        private static final long serialVersionUID = -8678406240667259762L;
        private List<String> data;
        
        public ComboMatchesModel(List<String> data) {
            this.data = data;
        }
        
        public void setData(List<String> data) {
            this.data = data;
            this.setSelectedItem(this.data.get(0));
            this.fireContentsChanged(this, 0, this.data.size());
        }

        public Object getElementAt(int index) {
            return this.data.get(index);
        }

        public int getSize() {
            return this.data.size();
        }
    }
}
