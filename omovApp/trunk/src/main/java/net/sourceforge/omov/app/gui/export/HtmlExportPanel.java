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
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;
import javax.swing.JCheckBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import net.sourceforge.omov.app.gui.comp.generic.LabeledComponent;
import net.sourceforge.omov.core.tools.export.HtmlColumn;

/**
 * 
 * @author christoph_pickl@users.sourceforge.net
 */
class HtmlExportPanel extends JPanel {

    private static final long serialVersionUID = 1782626577628167819L;

    private final JList htmlColumnsList = new JList(); 
    private final HtmlColumnModel htmlColumnsModel = new HtmlColumnModel();

    private final JCheckBox inpCoverColumn = new JCheckBox("Export Covers");
    
    
    public HtmlExportPanel() {
        this.setOpaque(false);
        this.htmlColumnsList.setModel(this.htmlColumnsModel);
        this.htmlColumnsList.setOpaque(false);
        
        this.initComponents();
    }

    private void initComponents() {
        this.setLayout(new BorderLayout());

        this.add(this.getCoversPanel(), BorderLayout.WEST);

//        this.htmlColumnsList.setVisibleRowCount((int) Math.floor(HtmlColumn.getAllColumns().size() / 2.0));
        this.htmlColumnsList.setVisibleRowCount(4);
        
        final JScrollPane scrollPane = new JScrollPane(this.htmlColumnsList);
        this.add(new LabeledComponent(scrollPane, "Columns"), BorderLayout.CENTER);
    }

    private JPanel getCoversPanel() {
        final JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        panel.setOpaque(false);
        
        this.inpCoverColumn.setOpaque(false);
        
        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(-25, 0, 6, 18);
        panel.add(this.inpCoverColumn, c);
        
        c.gridy = 1;
        c.insets = new Insets(0, 5, 0, 18);
        final JLabel infoText = new JLabel("<html>By selecting this checkbox,<br>a directory will be created<br>containing the cover images.</html>");
        infoText.setFont(new Font("default", Font.PLAIN, 11));
        panel.add(infoText, c);
        
        return panel;
    }
    
    List<HtmlColumn> getHtmlColumns() {
        final int[] indices = this.htmlColumnsList.getSelectedIndices();
        final List<HtmlColumn> columns = new ArrayList<HtmlColumn>(indices.length);
        
        if(this.inpCoverColumn.isSelected()) {
            columns.add(HtmlColumn.COLUMN_COVER);
        }
        
        for (int i : indices) {
            columns.add(this.htmlColumnsModel.getSelectedItem(i));
        }
        return columns;
    }
    
    

    
    private static class HtmlColumnModel extends AbstractListModel {
        private static final long serialVersionUID = 3606847676232298260L;
        
        private static final List<HtmlColumn> DATA;
        static {
            DATA = HtmlColumn.getAllColumns();
        }
        public HtmlColumn getSelectedItem(int index) {
            return DATA.get(index);
        }
        
        public Object getElementAt(int index) {
            return DATA.get(index).getLabel();
        }

        public int getSize() {
            return DATA.size();
        }
    }
}
