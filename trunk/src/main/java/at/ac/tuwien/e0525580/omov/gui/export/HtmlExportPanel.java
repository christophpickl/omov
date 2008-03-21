package at.ac.tuwien.e0525580.omov.gui.export;

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

import at.ac.tuwien.e0525580.omov.gui.comp.generic.LabeledComponent;
import at.ac.tuwien.e0525580.omov.tools.export.HtmlColumn;

class HtmlExportPanel extends JPanel {

    private static final long serialVersionUID = 1782626577628167819L;

    private final JList htmlColumnsList = new JList(); 
    private final HtmlColumnModel htmlColumnsModel = new HtmlColumnModel();

    private final JCheckBox inpCoverColumn = new JCheckBox("Export Covers");
    
    
    public HtmlExportPanel() {

        this.htmlColumnsList.setModel(this.htmlColumnsModel);
        
        this.initComponents();
    }

    private void initComponents() {
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(this, c);
        this.setLayout(layout);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        c.gridx = 0;
        c.gridy = 0;
        this.add(this.getCoversPanel(), c);

        c.gridx = 1;
        this.htmlColumnsList.setVisibleRowCount((int) Math.floor(HtmlColumn.getAllColumns().size() / 2.0));
        final JScrollPane scrollPane = new JScrollPane(this.htmlColumnsList);
//        scrollPane.setPreferredSize(new Dimension(width, height));
        this.add(new LabeledComponent(scrollPane, "Columns"), c);
    }

    private JPanel getCoversPanel() {
        final JPanel panel = new JPanel();
        GridBagLayout layout = new GridBagLayout();
        GridBagConstraints c = new GridBagConstraints();
        layout.setConstraints(panel, c);
        panel.setLayout(layout);

        c.anchor = GridBagConstraints.FIRST_LINE_START;
        
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(0, 0, 6, 18);
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
